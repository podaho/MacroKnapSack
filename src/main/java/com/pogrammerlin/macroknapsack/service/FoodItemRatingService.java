package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.User;
import com.pogrammerlin.macroknapsack.repository.FoodItemRatingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class FoodItemRatingService {
    private FoodItemRatingRepository foodItemRatingRepository;

    public FoodItemRating getFoodItemRatingByFoodItemAndUser(FoodItem foodItem, User user) {
        return foodItemRatingRepository.findFoodItemRatingByFoodItemAndUser(foodItem, user);
    }

    public FoodItemRating upsertFoodItemRating(FoodItemRating foodItemRating) {
        return foodItemRatingRepository.save(foodItemRating);
    }

    public List<FoodItemRating> getFoodItemRatingsByUserAndFoodItems(Set<FoodItem> foodItems, User user) {
        return foodItemRatingRepository.findFoodItemRatingByFoodItemInAndUser(foodItems, user);
    }

    public List<FoodItemRating> getTopNFoodItemRatingsByUserExcludingSet(int limit, User user, Set<FoodItem> foodItems) {
        return foodItemRatingRepository.findFoodItemRatingByUserAndFoodItemIsNotInOrderByRatingDesc(user, foodItems, PageRequest.of(0, limit));
    }

    public List<FoodItemRating> getTopRatedNFoodItemRatingsByUser(int limit, User user) {
        return foodItemRatingRepository.findFoodItemRatingByUserOrderByRatingDesc(user, PageRequest.of(0, limit));
    }
}
