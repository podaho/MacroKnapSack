package com.pogrammerlin.macroknapsack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MacroNutrientSpecifications {
    private double kcalGoal;
    private double carbGoal;
    private double fatGoal;
    private double fiberGoal;
    private double proteinGoal;
}
