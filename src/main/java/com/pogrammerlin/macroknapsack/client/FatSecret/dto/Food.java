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
public class Food {
    @JsonProperty("food_description")
    private String foodDescription;

    @JsonProperty("food_id")
    private String foodId;

    @JsonProperty("food_name")
    private String foodName;

    @JsonProperty("food_type")
    private String foodType;

    @JsonProperty("food_url")
    private String foodURL;
}
