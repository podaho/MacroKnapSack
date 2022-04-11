package com.pogrammerlin.macroknapsack.repository;

import com.pogrammerlin.macroknapsack.model.Macro;
import com.pogrammerlin.macroknapsack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MacroRepository extends JpaRepository<Macro, Long> {
    List<Macro> findAllByUser(User user);

    List<Macro> findAllByKcalGoalAndAndCarbGoalAndAndFatGoalAndProteinGoalAndFiberGoalAndUser(double kcalGoal, double carbGoal, double fatGoal, double proteinGoal, double fiberGoal, User user);
}
