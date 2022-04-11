package com.pogrammerlin.macroknapsack.dto;

import com.pogrammerlin.macroknapsack.dto.generic.DataActionResponseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddMacroDetails extends DataActionResponseObject {
    private double kcalGoal;
    private double carbGoal;
    private double fatGoal;
    private double fiberGoal;
    private double proteinGoal;
}
