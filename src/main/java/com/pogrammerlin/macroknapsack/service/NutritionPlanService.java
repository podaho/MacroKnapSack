package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.constant.NutrientType;
import com.pogrammerlin.macroknapsack.dto.request.AddNutritionPlanRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddNutritionPlanResponse;
import com.pogrammerlin.macroknapsack.dto.response.NutritionPlansResponse;
import com.pogrammerlin.macroknapsack.mapper.MacroKnapSackDataMapper;
import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.Macro;
import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.model.NutritionPlanFoodItemCount;
import com.pogrammerlin.macroknapsack.model.User;
import com.pogrammerlin.macroknapsack.repository.NutritionPlanRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pogrammerlin.macroknapsack.utility.DataValidator.validateUserid;

@Slf4j
@Component
@AllArgsConstructor
public class NutritionPlanService {
    private NutritionPlanRepository nutritionPlanRepository;
    private FoodItemRatingService foodItemRatingService;
    private UserService userService;
    private MacroService macroService;
    private NutritionPlanFoodItemCountService nutritionPlanFoodItemCountService;
    private MacroKnapSackDataMapper mapper;

    public NutritionPlansResponse getAllNutritionPlansByUserId(String userId) {
        if(!validateUserid(userId)) {
            return null;
        }
        User requestUser = userService.getUserByUserId(Long.valueOf(userId));
        List<NutritionPlan> nutritionPlansQueryResult = nutritionPlanRepository.findAllByUser(requestUser);
        Set<FoodItem> involvedFoodItems = nutritionPlansQueryResult.stream()
                                                                    .flatMap(nutritionPlan -> nutritionPlan.getFoodItemCounts()
                                                                            .stream()
                                                                            .map(NutritionPlanFoodItemCount::getFoodItemRating))
                                                                            .map(FoodItemRating::getFoodItem)
                                                                    .collect(Collectors.toSet());
        List<FoodItemRating> foodItemRatings = foodItemRatingService.getFoodItemRatingsByUserAndFoodItems(involvedFoodItems, requestUser);
        Map<Long, FoodItemRating> foodItemIdToFoodItemRatingMap
                = foodItemRatings.stream().collect(Collectors.toMap(fir -> fir.getFoodItem().getId(), Function.identity()));
        return mapper.mapNutritionPlansResponse(nutritionPlansQueryResult, foodItemIdToFoodItemRatingMap);
    }

    public AddNutritionPlanResponse createNewNutritionPlan(AddNutritionPlanRequest addNutritionPlanRequest, String method) {
        switch(method) {
            case "SOMETHING_NEW":
                log.error("SOMETHING_NEW Not Yet Implemented!");
                return createSomethingNew(addNutritionPlanRequest);
            case "STANDARD":
            default:
                return createStandard(addNutritionPlanRequest);
        }
    }

    public AddNutritionPlanResponse createStandard(AddNutritionPlanRequest addNutritionPlanRequest) {
        User requestUser = userService.getUserByUserId(addNutritionPlanRequest.getUserId());

        //select food item set
        Macro macro = macroService.getMacroById(addNutritionPlanRequest.getMacroId());
        List<FoodItemRating> selectedFoodItemRatingList = selectFoodItemsForNutritionPlan(addNutritionPlanRequest, requestUser, macro);

        //initializing treemaps
        TreeMap<Double, FoodItem> kcalTM = new TreeMap<>(), carbTM = new TreeMap<>(), fatTM = new TreeMap<>(), protTM = new TreeMap<>(), fiberTM = new TreeMap<>();
        //outlining our target caloric intake
        double kcalGoal = macro.getKcalGoal();

        //creating priorityQueue to determine most deficient nutrition given the macro criteria
        PriorityQueue<NutrientStruct> nutrientStructPQ = getNutrientStructs(macro);

        //this map will hold the food item id and how many servings of each we need to consume
        Map<Long, Double> foodItemIdToCountMap = new HashMap<>();
        for(FoodItemRating foodItemRating : selectedFoodItemRatingList) {
            //start by adding all nutritional values into treemap for access later 
            kcalTM.put(foodItemRating.getFoodItem().getKcal(), foodItemRating.getFoodItem());
            carbTM.put(foodItemRating.getFoodItem().getCarb(), foodItemRating.getFoodItem());
            fatTM.put(foodItemRating.getFoodItem().getFat(), foodItemRating.getFoodItem());
            protTM.put(foodItemRating.getFoodItem().getProtein(), foodItemRating.getFoodItem());
            fiberTM.put(foodItemRating.getFoodItem().getFiber(), foodItemRating.getFoodItem());
            //updating all nutritional goals based on one serving of this food item
            processNutrientUpdate(nutrientStructPQ, foodItemRating.getFoodItem(), new ArrayList<>());
            //adding one of each selected food item to count map
            foodItemIdToCountMap.put(foodItemRating.getFoodItem().getId(), 1.0);
        }

        //while we have leftover calories that need to be filled we attempt to fill the goal while assessing which food item we should use
        while(kcalGoal > 0.0) {
            kcalGoal = fillKCalGoal(carbTM, fatTM, protTM, fiberTM, kcalGoal, nutrientStructPQ, foodItemIdToCountMap);
        }

        //if we went over on our caloric intake, we will find the most consumed food item in this routine, 
        //  and scale down the serving to meet the caloric intake goal
        if(kcalGoal < 0.0) {
            giveBackKCal(selectedFoodItemRatingList, kcalGoal, nutrientStructPQ, foodItemIdToCountMap);
        }
        
        Map<NutrientType, Double> nutrientTypeToRemainingAmount = new HashMap<>();
        while(!nutrientStructPQ.isEmpty()) {
            NutrientStruct nutrientStruct = nutrientStructPQ.poll();
            nutrientTypeToRemainingAmount.put(nutrientStruct.getNutrientType(), nutrientStruct.getValue());
        }
        
        NutritionPlan newNutritionPlan = mapper.buildNewNutritionPlan(macro, addNutritionPlanRequest, requestUser);
        newNutritionPlan = nutritionPlanRepository.saveAndFlush(newNutritionPlan);
        List<NutritionPlanFoodItemCount> newNutritionPlanFoodItemCounts = nutritionPlanFoodItemCountService.saveAllNutritionPlanFoodItemCountDefinitions(
                                                                            newNutritionPlan, 
                                                                            selectedFoodItemRatingList,
                                                                            foodItemIdToCountMap);

        return mapper.mapAddNutritionPlanResponse(newNutritionPlan, requestUser, macro, kcalGoal, newNutritionPlanFoodItemCounts, nutrientTypeToRemainingAmount);
    }

    private PriorityQueue<NutrientStruct> getNutrientStructs(Macro macro) {
        PriorityQueue<NutrientStruct> nutrientStructPQ = new PriorityQueue<>(Comparator.comparingDouble(NutrientStruct::getValue).reversed());
        nutrientStructPQ.add(new NutrientStruct(NutrientType.CARB, macro.getCarbGoal()));
        nutrientStructPQ.add(new NutrientStruct(NutrientType.FAT, macro.getFatGoal()));
        nutrientStructPQ.add(new NutrientStruct(NutrientType.PROTEIN, macro.getProteinGoal()));
        nutrientStructPQ.add(new NutrientStruct(NutrientType.FIBER, macro.getFiberGoal()));
        return nutrientStructPQ;
    }

    private void giveBackKCal(List<FoodItemRating> selectedFoodItemRatingList, double kcalGoal, PriorityQueue<NutrientStruct> nutrientStructPQ, Map<Long, Double> foodItemIdToCountMap) {
        TreeSet<FoodItemRating> highestFreqencyAndLeastRatedTS = new TreeSet<>(new Comparator<FoodItemRating>() {
            @Override
            public int compare(FoodItemRating o1, FoodItemRating o2) {
                double res = foodItemIdToCountMap.get(o1.getFoodItem().getId()) - foodItemIdToCountMap.get(o2.getFoodItem().getId());
                if(res != 0.0) {
                    if(res > 0.0) return 1;
                    return -1;
                }
                return o2.getRating() - o1.getRating();
            }
        });
        highestFreqencyAndLeastRatedTS.addAll(selectedFoodItemRatingList);

        //setting a placeholder for the best candidate to scale down
        FoodItemRating foodItemRatingToScaleDown = highestFreqencyAndLeastRatedTS.last();
        //we will keep discounting food item plans in the routing until we meet our caloric goal
        while(kcalGoal < 0.0) {
            if(Objects.isNull(foodItemRatingToScaleDown)) {
                break;
            }
            FoodItem foodItemToScaleDown = foodItemRatingToScaleDown.getFoodItem();
            //seeing how much of the most logical scale down food item we should scale down to meet our kcal goal
            double discountRatio = kcalGoal /highestFreqencyAndLeastRatedTS.last().getFoodItem().getKcal();
            //we set a threshold to not scale down past 1 serving of the food item, this is to maintain at least some of each food item in the routine
            if(foodItemIdToCountMap.get(foodItemToScaleDown.getId()) - discountRatio < 1.0) {
                //in the case that scaling down all the way would bring the food item serving count to less than zero, 
                //  we scale down what we can and replace the discount ratio to preserve at least 1 serving
                discountRatio = foodItemIdToCountMap.get(foodItemToScaleDown.getId()) - 1.0;
            }
            //we update the food item count based on the discount
            foodItemIdToCountMap.put(foodItemToScaleDown.getId(), foodItemIdToCountMap.get(foodItemToScaleDown.getId()) - discountRatio);
            //we update the kcal goal by the discount kcal amount per food item serving
            kcalGoal += foodItemToScaleDown.getKcal()*discountRatio;
            //we add back the nutritional values we subtracted previously as we are giving some of that food item back
            revertNutrientUpdate(nutrientStructPQ, foodItemToScaleDown, new ArrayList<>(), discountRatio);
            //In the case that we are still over in caloric intake, we set the next ideal food item to discount for the next loop iteration
            foodItemRatingToScaleDown = highestFreqencyAndLeastRatedTS.lower(foodItemRatingToScaleDown);
        }
    }

    private double fillKCalGoal(TreeMap<Double, FoodItem> carbTM, TreeMap<Double, FoodItem> fatTM, TreeMap<Double, FoodItem> protTM, TreeMap<Double, FoodItem> fiberTM, double kcalGoal, PriorityQueue<NutrientStruct> nutrientStructPQ, Map<Long, Double> foodItemIdToCountMap) {
        //a list to hold the processed nutrients so the later while loops don't continue to process nutrients that have already been processed
        List<NutrientStruct> processedNutrients = new ArrayList<>();
        //given the priorityQueue, this poll should return the nutrient that has the greatest deficit
        NutrientStruct mostLacking = nutrientStructPQ.poll();
        //placeholder for the food item that best fits this nutritional goal
        FoodItem foodItem;
        //switch statement to determine what the most lacking nutrient type is and retrieving the most logical food item to fit that criteria, 
        //  making adjustments to that nutritional goal values per serving consumption of the food item, then adding that nutrient to the processed array
        switch(mostLacking.getNutrientType()) {
            case CARB:
                foodItem = getBestFoodItem(carbTM, mostLacking);
                processedNutrients.add(mostLacking.subtract(foodItem.getCarb()));
                break;
            case FAT:
                foodItem = getBestFoodItem(fatTM, mostLacking);
                processedNutrients.add(mostLacking.subtract(foodItem.getFat()));
                break;
            case PROTEIN:
                foodItem = getBestFoodItem(protTM, mostLacking);
                processedNutrients.add(mostLacking.subtract(foodItem.getProtein()));
                break;
            case FIBER:
            default:
                foodItem = getBestFoodItem(fiberTM, mostLacking);
                processedNutrients.add(mostLacking.subtract(foodItem.getFiber()));
                break;
        }
        //making caloric intake adjustments per serving of food item
        kcalGoal -= foodItem.getKcal();
        //process the remaining nutrients per serving of the most logically selected food item
        processNutrientUpdate(nutrientStructPQ, foodItem, processedNutrients);
        //incrementing the number of servings of that food item by one
        foodItemIdToCountMap.put(foodItem.getId(), foodItemIdToCountMap.get(foodItem.getId()) + 1.0);
        return kcalGoal;
    }

    private List<FoodItemRating> selectFoodItemsForNutritionPlan(AddNutritionPlanRequest addNutritionPlanRequest, User requestUser, Macro macro) {
        Set<FoodItem> macroGoalNutritionPlansInvolvedFoodItems = macro.getNutritionPlans()
                                                                    .stream()
                                                                    .flatMap(nutritionPlan -> nutritionPlan.getFoodItemCounts()
                                                                                                            .stream()
                                                                                                            .map(NutritionPlanFoodItemCount::getFoodItemRating))
                                                                                                            .map(FoodItemRating::getFoodItem)
                                                                    .collect(Collectors.toSet());
        
        List<FoodItemRating> selectedFoodItemRatingList
                = foodItemRatingService.getTopNFoodItemRatingsByUserExcludingSet(addNutritionPlanRequest.getNumberOfItems(), requestUser, macroGoalNutritionPlansInvolvedFoodItems);
        if(selectedFoodItemRatingList.size() < addNutritionPlanRequest.getNumberOfItems()) {
            selectedFoodItemRatingList.addAll(
                    foodItemRatingService.getTopRatedNFoodItemRatingsByUser(
                            addNutritionPlanRequest.getNumberOfItems() - selectedFoodItemRatingList.size(),
                            requestUser));
        }
        return selectedFoodItemRatingList;
    }

    public void processNutrientUpdate(PriorityQueue<NutrientStruct> nutrientStructPQ, FoodItem foodItem, List<NutrientStruct> processedNutrients) {
        while(!nutrientStructPQ.isEmpty()) {
            NutrientStruct nutrient = nutrientStructPQ.poll();
            switch(nutrient.getNutrientType()) {
                case CARB:
                    processedNutrients.add(nutrient.subtract(foodItem.getCarb()));
                    break;
                case FAT:
                    processedNutrients.add(nutrient.subtract(foodItem.getFat()));
                    break;
                case PROTEIN:
                    processedNutrients.add(nutrient.subtract(foodItem.getProtein()));
                    break;
                case FIBER:
                default:
                    processedNutrients.add(nutrient.subtract(foodItem.getFiber()));
                    break;
            }
        }
        nutrientStructPQ.addAll(processedNutrients);
    }

    public void revertNutrientUpdate(PriorityQueue<NutrientStruct> nutrientStructPQ, FoodItem foodItem, List<NutrientStruct> processedNutrients, double discountRatio) {
        while(!nutrientStructPQ.isEmpty()) {
            NutrientStruct nutrient = nutrientStructPQ.poll();
            switch(nutrient.getNutrientType()) {
                case CARB:
                    processedNutrients.add(nutrient.add(foodItem.getCarb()*discountRatio));
                    break;
                case FAT:
                    processedNutrients.add(nutrient.add(foodItem.getFat()*discountRatio));
                    break;
                case PROTEIN:
                    processedNutrients.add(nutrient.add(foodItem.getProtein()*discountRatio));
                    break;
                case FIBER:
                default:
                    processedNutrients.add(nutrient.add(foodItem.getFiber()*discountRatio));
                    break;
            }
        }
        nutrientStructPQ.addAll(processedNutrients);
    }

    public FoodItem getBestFoodItem(TreeMap<Double, FoodItem> nutrientTM, NutrientStruct mostLacking) {
        Map.Entry<Double, FoodItem> foodItemEntry = nutrientTM.floorEntry(mostLacking.getValue());
        if(Objects.isNull(foodItemEntry)) {
            foodItemEntry = nutrientTM.higherEntry(mostLacking.getValue());
        }
        return foodItemEntry.getValue();
        //TODO: throw exception if null
    }

    public AddNutritionPlanResponse createSomethingNew(AddNutritionPlanRequest addNutritionPlanRequest) {
        return null;
    }

    @Data
    @AllArgsConstructor
    private class NutrientStruct {
        private NutrientType nutrientType;
        private double value;

        public NutrientStruct subtract(double x) {
            this.value -= x;
            return this;
        }

        public NutrientStruct add(double x) {
            this.value += x;
            return this;
        }
    }
}
