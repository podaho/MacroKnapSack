package com.pogrammerlin.macroknapsack.dto;

import com.pogrammerlin.macroknapsack.constant.NutritionPlanFoodItemActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanFoodItemActionRequest {
    private Long foodItemId;
    private String foodItemExternalId;
    private NutritionPlanFoodItemActionType actionType;
}
