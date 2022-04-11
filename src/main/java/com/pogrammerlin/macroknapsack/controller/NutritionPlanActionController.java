package com.pogrammerlin.macroknapsack.controller;

import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.service.NutritionPlanService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/nutrition-plan")
public class NutritionPlanActionController {
    private NutritionPlanService nutritionPlanService;

    @GetMapping("/all")
    public ResponseEntity<List<NutritionPlan>> getAllNutritionPlan() {
        return ResponseEntity.ok(nutritionPlanService.getAllNutritionPlans());
    }
}
