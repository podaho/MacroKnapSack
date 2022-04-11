package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.repository.NutritionPlanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class NutritionPlanService {
    private NutritionPlanRepository nutritionPlanRepository;

    public List<NutritionPlan> getAllNutritionPlans() {
        return nutritionPlanRepository.findAll();
    }
}
