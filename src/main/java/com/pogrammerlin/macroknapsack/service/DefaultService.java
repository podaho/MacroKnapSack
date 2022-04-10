package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.client.FatSecret.FatSecretClient;
import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.pogrammerlin.macroknapsack.constant.Constants.SEARCH_FOODS_QUERY_METHOD;
import static com.pogrammerlin.macroknapsack.constant.Constants.SEARCH_FOODS_QUERY_PAGE_NUMBER;
import static com.pogrammerlin.macroknapsack.constant.Constants.SEARCH_FOODS_QUERY_MAX_RESULTS;
import static com.pogrammerlin.macroknapsack.constant.Constants.SEARCH_FOODS_QUERY_FORMAT;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultService {
    private final FatSecretClient fatSecretClient;

    public FatSecretSearchResponse getFoodSearchResult(String searchTerm) {
        return fatSecretClient.searchFoods(searchTerm,
                SEARCH_FOODS_QUERY_METHOD,
                SEARCH_FOODS_QUERY_PAGE_NUMBER,
                SEARCH_FOODS_QUERY_MAX_RESULTS,
                SEARCH_FOODS_QUERY_FORMAT);
    }
}
