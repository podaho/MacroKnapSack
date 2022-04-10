package com.pogrammerlin.macroknapsack.integrationTest;

import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchResponse;
import com.pogrammerlin.macroknapsack.controller.DefaultController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
public class DefaultControllerIT {
    @Autowired
    private DefaultController controller;

    @Test
    public void integrationTestSampleSearchResult() {
        // Test Setup
        stubFor(get(urlEqualTo("/rest/server.api?search_expression=banana&method=foods.search&page_number=0&max_results=5&format=json"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")
                        .withBody(buildFatSecretResponseJson())));

        // Action
        ResponseEntity<FatSecretSearchResponse> response = controller.sampleSearchFood("banana");

        // Validation
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private static String buildFatSecretResponseJson() {
        try {
            InputStream inputStream = getSystemResourceAsStream("jsonResponseContract/fatsecret-search-result-success.json");
            return IOUtils.toString(inputStream);
        } catch(Exception e) {
            log.error("Error reading test far secret response success JSON");
            assertTrue(false);
            return null;
        }
    }
}
