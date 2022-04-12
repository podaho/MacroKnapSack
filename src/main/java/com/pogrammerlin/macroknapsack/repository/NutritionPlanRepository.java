package com.pogrammerlin.macroknapsack.repository;

import com.pogrammerlin.macroknapsack.model.NutritionPlan;
import com.pogrammerlin.macroknapsack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    List<NutritionPlan> findAllByUser(User user);
}
