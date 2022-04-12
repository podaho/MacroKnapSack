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
public class AddFoodItemResponse extends GenericResponseObject {
    private long userId;
    private List<FoodItemizedDetails> foodItemizedDetails;
}
