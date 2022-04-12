package com.pogrammerlin.macroknapsack.controller;

import com.pogrammerlin.macroknapsack.dto.request.AddNutritionPlanRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddNutritionPlanResponse;
import com.pogrammerlin.macroknapsack.dto.response.NutritionPlansResponse;
import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.service.NutritionPlanService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/nutrition-plan")
public class NutritionPlanActionController {
    private NutritionPlanService nutritionPlanService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<NutritionPlansResponse> getAllNutritionPlan(@PathVariable(value = "userId") String userId) {
        return ResponseEntity.ok(nutritionPlanService.getAllNutritionPlansByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<AddNutritionPlanResponse> createNewNutritionPlan(@Parameter(schema = @Schema(allowableValues = {"STANDARD", "SOMETHING_NEW"})) @RequestParam("method") String method,
                                                                            @RequestBody AddNutritionPlanRequest addNutritionPlanRequest) {
        return ResponseEntity.accepted().body(nutritionPlanService.createNewNutritionPlan(addNutritionPlanRequest, method));
    }
}
