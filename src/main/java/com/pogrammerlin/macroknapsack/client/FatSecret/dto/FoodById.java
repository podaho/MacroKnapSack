package com.pogrammerlin.macroknapsack.client.FatSecret.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodById {
    @JsonProperty("food_id")
    private String externalId;

    @JsonProperty("food_name")
    private String name;

    @JsonProperty("servings")
    private Servings servings;
}
