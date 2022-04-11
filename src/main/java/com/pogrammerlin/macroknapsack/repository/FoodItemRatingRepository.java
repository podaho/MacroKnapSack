package com.pogrammerlin.macroknapsack.repository;

import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRatingRepository extends JpaRepository<FoodItemRating, Long> {
    FoodItemRating findFoodItemRatingByFoodItemAndUser(FoodItem foodItem, User user);
}
