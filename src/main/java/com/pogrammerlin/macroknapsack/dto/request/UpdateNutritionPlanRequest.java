package com.pogrammerlin.macroknapsack.dto.request;

import com.pogrammerlin.macroknapsack.dto.NutritionPlanFoodItemActionRequest;
import com.pogrammerlin.macroknapsack.dto.generic.NutritionPlanRequestObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNutritionPlanRequest extends NutritionPlanRequestObject {
    private long nutritionPlanId;
    private List<NutritionPlanFoodItemActionRequest> foodItemActionRequests;
}
