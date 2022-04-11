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
public class AddFoodItemizedDetails extends DataActionResponseObject {
    private String externalId;
    private String name;
    private double kcal;
    private double carb;
    private double fat;
    private double protein;
    private double servingSize;
    private short rating;
}
