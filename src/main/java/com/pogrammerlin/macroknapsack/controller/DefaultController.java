package com.pogrammerlin.macroknapsack.controller;

import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchResponse;
import com.pogrammerlin.macroknapsack.property.GlobalProperties;
import com.pogrammerlin.macroknapsack.service.DefaultService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/default")
public class DefaultController {
    private GlobalProperties props;
    private DefaultService defaultService;

    @GetMapping("/helloWorld")
    public ResponseEntity helloWord(@RequestParam("say") String someString) {
        return ResponseEntity.ok("Hello Word! My name is " + props.getMyName() + ", and to you I say "+someString);
    }

    @GetMapping("/sampleSearch")
    public ResponseEntity<FatSecretSearchResponse> sampleSearchFood(@RequestParam("term") String searchTerm) {
        return ResponseEntity.ok(defaultService.getFoodSearchResult(searchTerm));
    }
}
