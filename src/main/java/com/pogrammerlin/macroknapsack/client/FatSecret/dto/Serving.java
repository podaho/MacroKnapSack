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
public class Serving {
    @JsonProperty("calories")
    private String calories;

    @JsonProperty("carbohydrate")
    private String carbohydrate;

    @JsonProperty("fat")
    private String fat;

    @JsonProperty("protein")
    private String protein;

    @JsonProperty("fiber")
    private String fiber;

    @JsonProperty("metric_serving_amount")
    private String metricServingAmount;

    @JsonProperty("metric_serving_unit")
    private String metricServingUnit;
}
