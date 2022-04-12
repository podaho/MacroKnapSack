package com.pogrammerlin.macroknapsack.dto.generic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanRequestObject {
    private long userId;
    private long macroId;
}
