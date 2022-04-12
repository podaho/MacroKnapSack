package com.pogrammerlin.macroknapsack.mapper;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchByIdResponse;
import com.pogrammerlin.macroknapsack.client.FatSecret.dto.Serving;
import com.pogrammerlin.macroknapsack.constant.NutrientType;
import com.pogrammerlin.macroknapsack.dto.FoodItemizedDetails;
import com.pogrammerlin.macroknapsack.dto.AddMacroDetails;
import com.pogrammerlin.macroknapsack.dto.MacroNutrientSpecifications;
import com.pogrammerlin.macroknapsack.dto.request.AddNutritionPlanRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddNutritionPlanResponse;
import com.pogrammerlin.macroknapsack.dto.response.MacroDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.MacrosDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.NutritionPlanResponse;
import com.pogrammerlin.macroknapsack.dto.response.NutritionPlansResponse;
import com.pogrammerlin.macroknapsack.dto.response.UserDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.UsersDetailResponse;
import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.Macro;
import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.model.NutritionPlanFoodItemCount;
import com.pogrammerlin.macroknapsack.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pogrammerlin.macroknapsack.constant.Constants.METRIC_SERVING_UNIT;
import static com.pogrammerlin.macroknapsack.constant.NutrientType.CARB;
import static com.pogrammerlin.macroknapsack.constant.NutrientType.FAT;
import static com.pogrammerlin.macroknapsack.constant.NutrientType.FIBER;
import static com.pogrammerlin.macroknapsack.constant.NutrientType.PROTEIN;

@Slf4j
@Service
@AllArgsConstructor
public class MacroKnapSackDataMapper {
    public AddNutritionPlanResponse mapAddNutritionPlanResponse(NutritionPlan nutritionPlan, User user, Macro macro, double remainingKCal, List<NutritionPlanFoodItemCount> nutritionPlanFoodItemCounts, Map<NutrientType, Double> nutrientTypeToRemainingAmountMap) {
        return AddNutritionPlanResponse.builder()
                .id(nutritionPlan.getId())
                .name(nutritionPlan.getName())
                .owningUserId(user.getId())
                .owningMacroGoalIds(Arrays.asList(macro.getId()))
                .kcalTotal(macro.getKcalGoal() - remainingKCal)
                .carbTotal(macro.getCarbGoal() - nutrientTypeToRemainingAmountMap.getOrDefault(CARB, 0.0))
                .fatTotal(macro.getCarbGoal() - nutrientTypeToRemainingAmountMap.getOrDefault(FAT, 0.0))
                .proteinTotal(macro.getCarbGoal() - nutrientTypeToRemainingAmountMap.getOrDefault(PROTEIN, 0.0))
                .fiberTotal(macro.getCarbGoal() - nutrientTypeToRemainingAmountMap.getOrDefault(FIBER, 0.0))
                .foodItems(mapFoodItemizedDetails(nutritionPlanFoodItemCounts))
                .build();
    }

    public List<NutritionPlanFoodItemCount> mapNutritionPlanFoodItemCount(NutritionPlan nutritionPlan, List<FoodItemRating> foodItemRatings, Map<Long, Double> foodItemIdToCountMap) {
        return foodItemRatings.stream()
                .map(foodItemRating -> {
                    return NutritionPlanFoodItemCount.builder()
                            .foodItemRating(foodItemRating)
                            .numServings(foodItemIdToCountMap.getOrDefault(foodItemRating.getFoodItem().getId(), 0.0))
                            .nutritionPlan(nutritionPlan)
                            .build();
                }).collect(Collectors.toList());
    }

    public NutritionPlanResponse mapNutritionPlanResponse(NutritionPlan nutritionPlan, Map<Long, FoodItemRating> foodItemIdToFoodItemRatingMap) {
        double kcalTotal = 0.0, carbTotal = 0.0, fatTotal = 0.0, proteinTotal = 0.0, fiberTotal = 0.0;

        for(FoodItem foodItem : nutritionPlan.getFoodItemCounts()
                                                .stream()
                                                .map(NutritionPlanFoodItemCount::getFoodItemRating)
                                                .map(FoodItemRating::getFoodItem)
                                                .collect(Collectors.toList())) {
            kcalTotal += foodItem.getKcal();
            carbTotal += foodItem.getCarb();
            fatTotal += foodItem.getFat();
            proteinTotal += foodItem.getProtein();
            fiberTotal += foodItem.getFiber();
        }

        return NutritionPlanResponse.builder()
                .id(nutritionPlan.getId())
                .name(nutritionPlan.getName())
                .owningUserId(nutritionPlan.getUser().getId())
                .owningMacroGoalIds(nutritionPlan.getMacros()
                                                    .stream()
                                                    .map(Macro::getId)
                                                    .collect(Collectors.toList()))
                .kcalTotal(kcalTotal)
                .carbTotal(carbTotal)
                .fatTotal(fatTotal)
                .proteinTotal(proteinTotal)
                .fiberTotal(fiberTotal)
                .foodItems(nutritionPlan.getFoodItemCounts()
                        .stream()
                        .map(NutritionPlanFoodItemCount::getFoodItemRating)
                        .map(this::mapFoodItemizedDetails)
                        .collect(Collectors.toList()))
                .build();
    }

    public NutritionPlansResponse mapNutritionPlansResponse(List<NutritionPlan> nutritionPlans, Map<Long, FoodItemRating> map) {
        return NutritionPlansResponse.builder()
                .nutritionPlanResponses(nutritionPlans.stream()
                                                        .map(nutritionPlan -> mapNutritionPlanResponse(nutritionPlan, map))
                                                        .collect(Collectors.toList()))
                .build();
    }

    public AddMacroDetails mapAddMacroDetails(Macro macro, boolean isNew) {
        return AddMacroDetails.builder()
                .kcalGoal(macro.getKcalGoal())
                .carbGoal(macro.getCarbGoal())
                .fatGoal(macro.getFatGoal())
                .proteinGoal(macro.getProteinGoal())
                .fiberGoal(macro.getFiberGoal())
                .isNew(isNew)
                .build();
    }

    public Macro mapMacro(MacroNutrientSpecifications macroSpecs, User user) {
        return Macro.builder()
                .kcalGoal(macroSpecs.getKcalGoal())
                .carbGoal(macroSpecs.getCarbGoal())
                .fatGoal(macroSpecs.getFatGoal())
                .proteinGoal(macroSpecs.getProteinGoal())
                .fiberGoal(macroSpecs.getFiberGoal())
                .user(user)
                .build();
    }

    public MacrosDetailResponse mapMacrosDetailResponse(List<Macro> macroList) {
        return MacrosDetailResponse.builder()
                .macroDetailResponses(macroList.stream()
                                                .map(this::mapMacroDetailResponse)
                                                .collect(Collectors.toList()))
                .build();
    }

    public MacroDetailResponse mapMacroDetailResponse(Macro macro) {
        return MacroDetailResponse.builder()
                .id(macro.getId())
                .kcalGoal(macro.getKcalGoal())
                .carbGoal(macro.getCarbGoal())
                .fatGoal(macro.getFatGoal())
                .proteinGoal(macro.getProteinGoal())
                .fiberGoal(macro.getFiberGoal())
                .build();
    }

    public UsersDetailResponse mapUsersDetailResponse(List<User> users) {
        return UsersDetailResponse.builder()
                .userDetailResponses(users.stream()
                                            .map(this::mapUserDetailResponse)
                                            .collect(Collectors.toList()))
                .build();
    }

    public UserDetailResponse mapUserDetailResponse(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .weight(user.getWeight())
                .build();
    }

    public FoodItemizedDetails mapAddFoodItemizedDetails(FoodItemRating foodItemRating, boolean isNew) {
        FoodItemizedDetails mappedFoodItemizedDetails = mapFoodItemizedDetails(foodItemRating);
        mappedFoodItemizedDetails.setIsNew(isNew);
        return mappedFoodItemizedDetails;
    }

    public List<FoodItemizedDetails> mapFoodItemizedDetails(List<NutritionPlanFoodItemCount> nutritionPlanFoodItemCounts) {
        return nutritionPlanFoodItemCounts.stream().map(npic -> {
            FoodItemizedDetails res = mapFoodItemizedDetails(npic.getFoodItemRating());
            res.setServingSize(npic.getNumServings()*npic.getFoodItemRating().getFoodItem().getServingSize());
            return res;
        }).collect(Collectors.toList());
    }

    public FoodItemizedDetails mapFoodItemizedDetails(FoodItemRating foodItemRating) {
        if(Objects.isNull(foodItemRating)) {
            return null;
        }
        return FoodItemizedDetails.builder()
                .externalId(foodItemRating.getFoodItem().getExternalId())
                .name(foodItemRating.getFoodItem().getName())
                .kcal(foodItemRating.getFoodItem().getKcal())
                .carb(foodItemRating.getFoodItem().getCarb())
                .fat(foodItemRating.getFoodItem().getFat())
                .protein(foodItemRating.getFoodItem().getProtein())
                .servingSize(foodItemRating.getFoodItem().getServingSize())
                .rating(foodItemRating.getRating())
                .build();
    }

    public FoodItem mapFoodItem(FatSecretSearchByIdResponse response) {
        if(Objects.isNull(response)
                || Objects.isNull(response.getFood())
                || Objects.isNull(response.getFood().getServings())
                || CollectionUtils.isEmpty(response.getFood().getServings().getServings())) {
            return null;
        }
        Serving servingDetails = findClosestToHundredGramServings(response.getFood().getServings().getServings());

        if(Objects.isNull(servingDetails)) {
            return null;
        }

        return FoodItem.builder()
                .externalId(response.getFood().getExternalId())
                .name(response.getFood().getName())
                .kcal(Double.valueOf(servingDetails.getCalories()))
                .carb(Double.valueOf(servingDetails.getCarbohydrate()))
                .fat(Double.valueOf(servingDetails.getFat()))
                .protein(Double.valueOf(servingDetails.getProtein()))
                .fiber(Double.valueOf(servingDetails.getFiber()))
                .servingSize(Double.valueOf(servingDetails.getMetricServingAmount()))
                .build();
    }

    public NutritionPlan buildNewNutritionPlan(Macro macro, AddNutritionPlanRequest addNutritionPlanRequest, User user) {
        return NutritionPlan.builder()
                .macros(new HashSet<>(Arrays.asList(macro)))
                .name(addNutritionPlanRequest.getName())
                .user(user)
                .build();
    }

    private Serving findClosestToHundredGramServings(List<Serving> servings) {
        return servings.stream()
                .filter(serving -> !StringUtils.isBlank(serving.getMetricServingUnit())
                        && serving.getMetricServingUnit().equals(METRIC_SERVING_UNIT)
                        && !StringUtils.isBlank(serving.getMetricServingAmount()))
                .min((s1, s2) -> {
                    return Double.compare(
                            Math.abs(100.0 - Double.valueOf(s1.getMetricServingAmount())),
                            Math.abs(100.0 - Double.valueOf(s2.getMetricServingAmount())));
                })
                .orElse(null);
    }
}
