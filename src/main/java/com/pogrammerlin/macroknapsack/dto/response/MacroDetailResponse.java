package com.pogrammerlin.macroknapsack.dto.response;

import com.pogrammerlin.macroknapsack.dto.generic.GenericResponseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MacroDetailResponse extends GenericResponseObject {
    private long id;
    private double kcalGoal;
    private double carbGoal;
    private double fatGoal;
    private double proteinGoal;
    private double fiberGoal;
}
