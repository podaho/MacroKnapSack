package com.pogrammerlin.macroknapsack.repository;


import com.pogrammerlin.macroknapsack.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    Set<FoodItem> findByExternalIdIn(Set<String> externalIds);
}
