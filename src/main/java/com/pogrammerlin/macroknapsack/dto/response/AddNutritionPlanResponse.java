package com.pogrammerlin.macroknapsack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddNutritionPlanResponse extends NutritionPlanResponse {
    private boolean isNew;
}
