package com.pogrammerlin.macroknapsack.dto.request;

import com.pogrammerlin.macroknapsack.dto.generic.NutritionPlanRequestObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddNutritionPlanRequest extends NutritionPlanRequestObject {
    private int numberOfItems;
    private String name;
}
