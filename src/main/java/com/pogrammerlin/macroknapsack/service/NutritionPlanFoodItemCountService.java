package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.mapper.MacroKnapSackDataMapper;
import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.model.NutritionPlanFoodItemCount;
import com.pogrammerlin.macroknapsack.repository.NutritionPlanFoodItemCountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class NutritionPlanFoodItemCountService {
    NutritionPlanFoodItemCountRepository nutritionPlanFoodItemCountRepository;
    MacroKnapSackDataMapper mapper;

    public List<NutritionPlanFoodItemCount> saveAllNutritionPlanFoodItemCountDefinitions(NutritionPlan nutritionPlan, List<FoodItemRating> foodItemRatings, Map<Long, Double> foodItemIdToCountMap) {
        return nutritionPlanFoodItemCountRepository.saveAllAndFlush(mapper.mapNutritionPlanFoodItemCount(nutritionPlan, foodItemRatings, foodItemIdToCountMap));
    }
}
