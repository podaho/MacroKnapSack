package com.pogrammerlin.macroknapsack.client.FatSecret;

import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchResponse;
import com.pogrammerlin.macroknapsack.config.OAuthFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fatSecretClient",
            url = "${client.fatSecret.url}",
            configuration = OAuthFeignClientConfiguration.class)
public interface FatSecretClient {
    @GetMapping
    FatSecretSearchResponse searchFoods(@RequestParam(value = "search_expression") String searchExpression,
                                        @RequestParam(value = "method", required = false, defaultValue = "foods.search") String method,
                                        @RequestParam(value = "page_number", required = false, defaultValue = "5") String pageNumber,
                                        @RequestParam(value = "max_results", required = false, defaultValue = "5") String maxResults,
                                        @RequestParam(value = "format", required = false, defaultValue = "json") String format);
}
