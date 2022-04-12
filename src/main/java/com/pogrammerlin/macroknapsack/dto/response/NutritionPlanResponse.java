package com.pogrammerlin.macroknapsack.dto.response;

import com.pogrammerlin.macroknapsack.dto.FoodItemizedDetails;
import com.pogrammerlin.macroknapsack.dto.generic.GenericResponseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanResponse extends GenericResponseObject {
    private long id;
    private String name;
    private long owningUserId;
    private List<Long> owningMacroGoalIds;
    private double kcalTotal;
    private double carbTotal;
    private double fatTotal;
    private double proteinTotal;
    private double fiberTotal;
    private List<FoodItemizedDetails> foodItems;
}