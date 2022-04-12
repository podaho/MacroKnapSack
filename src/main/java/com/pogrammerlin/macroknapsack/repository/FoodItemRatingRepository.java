package com.pogrammerlin.macroknapsack.repository;

import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface FoodItemRatingRepository extends PagingAndSortingRepository<FoodItemRating, Integer> {
    FoodItemRating findFoodItemRatingByFoodItemAndUser(FoodItem foodItem, User user);

    List<FoodItemRating> findFoodItemRatingByFoodItemInAndUser(Set<FoodItem> foodItems, User user);

    List<FoodItemRating> findFoodItemRatingByUserOrderByRatingDesc(User user, Pageable pageable);

    List<FoodItemRating> findFoodItemRatingByUserAndFoodItemIsNotInOrderByRatingDesc(User user, Set<FoodItem> foodItems, Pageable pageable);
}
