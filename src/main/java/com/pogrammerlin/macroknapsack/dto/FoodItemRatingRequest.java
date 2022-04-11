package com.pogrammerlin.macroknapsack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemRatingRequest {
    private String externalId;
    private short rating;
}
