package com.pogrammerlin.macroknapsack.controller;

import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchResponse;
import com.pogrammerlin.macroknapsack.dto.request.AddFoodItemRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddFoodItemResponse;
import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.service.FoodItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/food-item")
public class FoodItemActionController {
    private FoodItemService foodItemService;

    @GetMapping("/all")
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        return ResponseEntity.ok(foodItemService.getAllFoodItems());
    }

    @PostMapping("/create")
    public ResponseEntity<AddFoodItemResponse> addNewFoodItem(@RequestBody AddFoodItemRequest addFoodItemRequest) {
        return ResponseEntity.accepted().body(foodItemService.createFoodItem(addFoodItemRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<FatSecretSearchResponse> sampleSearchFood(@RequestParam("term") String searchTerm) {
        return ResponseEntity.ok(foodItemService.getFoodSearchResult(searchTerm));
    }
}
