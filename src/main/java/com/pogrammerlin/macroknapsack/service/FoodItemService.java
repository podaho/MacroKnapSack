package com.pogrammerlin.macroknapsack.service;

import com.pogrammerlin.macroknapsack.client.FatSecret.FatSecretClient;
import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchByIdResponse;
import com.pogrammerlin.macroknapsack.dto.AddFoodItemizedDetails;
import com.pogrammerlin.macroknapsack.dto.FoodItemRatingRequest;
import com.pogrammerlin.macroknapsack.dto.request.AddFoodItemRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddFoodItemResponse;
import com.pogrammerlin.macroknapsack.mapper.MacroKnapSackDataMapper;
import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.User;
import com.pogrammerlin.macroknapsack.repository.FoodItemRatingRepository;
import com.pogrammerlin.macroknapsack.repository.FoodItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.pogrammerlin.macroknapsack.constant.Constants.SEARCH_FOODS_BY_ID_QUERY_METHOD;
import static com.pogrammerlin.macroknapsack.constant.Constants.SEARCH_FOODS_QUERY_FORMAT;
import static com.pogrammerlin.macroknapsack.utility.AsyncHelper.handleFutureError;

@Slf4j
@Component
@AllArgsConstructor
public class FoodItemService {
    private FoodItemRepository foodItemRepository;
    private FoodItemRatingRepository foodItemRatingRepository;
    private FatSecretClient fatSecretClient;
    private UserService userService;
    private MacroKnapSackDataMapper mapper;

    @Qualifier("asyncExecutor")
    private Executor executor;

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public AddFoodItemResponse createFoodItem(AddFoodItemRequest addFoodItemRequest) {
        User requestUser = userService.getUserByUserId(addFoodItemRequest.getUserId());
        //TODO: null check here
        Map<String, Short> foodItemExternalIdToRatingMap = addFoodItemRequest
                                            .getFoodItemRatingRequests()
                                            .stream()
                                            .collect(Collectors.toMap(FoodItemRatingRequest::getExternalId, FoodItemRatingRequest::getRating));

        Set<FoodItem> foodItemQueryResult = foodItemRepository
                                            .findByExternalIdIn(foodItemExternalIdToRatingMap.keySet());


        List<AddFoodItemizedDetails> addFoodItemizedDetails = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        List<CompletableFuture<AddFoodItemizedDetails>> completableFutureList = getFoodItemizedDetailsCompletableFutures(foodItemExternalIdToRatingMap, foodItemQueryResult, requestUser, errorList);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        CompletableFuture<List<AddFoodItemizedDetails>> completableFutureResponses = allFutures
                .thenApply(future -> completableFutureList
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                ).toCompletableFuture();

        try {
            addFoodItemizedDetails = completableFutureResponses.get();
        } catch(Exception e) {
            log.error("Unexpected Async Failure; cause: {}", e.getMessage());
            //TODO: handle exception here
        }

        return AddFoodItemResponse.builder()
                .userId(requestUser.getId())
                .addFoodItemizedDetails(addFoodItemizedDetails)
                .errors(errorList)
                .build();
    }

    private List<CompletableFuture<AddFoodItemizedDetails>> getFoodItemizedDetailsCompletableFutures(Map<String, Short> foodItemExternalIdToRatingMap, Set<FoodItem> foodItemQueryResult, User requestUser, List<String> errors) {
        List<CompletableFuture<AddFoodItemizedDetails>> completableFutureList = new ArrayList<>();
        for(Map.Entry<String, Short> mapEntry : foodItemExternalIdToRatingMap.entrySet()) {
            foodItemQueryResult
                    .stream()
                    .filter(dbFoodItem -> dbFoodItem.getExternalId().equals(mapEntry.getKey()))
                    .findFirst()
                    .ifPresentOrElse(
                            foodItem -> {
                                CompletableFuture<AddFoodItemizedDetails> addFoodItemizedDetailsCompletableFuture
                                        = CompletableFuture.supplyAsync(() -> updateExistingFoodItemRating(foodItem, requestUser, mapEntry.getValue(), false), executor)
                                                            .exceptionally(e -> handleFutureError(e, errors));
                                completableFutureList.add(addFoodItemizedDetailsCompletableFuture);
                            },
                            () -> {
                                CompletableFuture<AddFoodItemizedDetails> addFoodItemizedDetailsCompletableFuture
                                        = CompletableFuture.supplyAsync(() -> addNewFoodItemAndRating(mapEntry.getKey(), requestUser, mapEntry.getValue(), true), executor)
                                                            .exceptionally(e -> handleFutureError(e, errors));
                                completableFutureList.add(addFoodItemizedDetailsCompletableFuture);
                            }
                    );
        }
        return completableFutureList;
    }

    private AddFoodItemizedDetails updateExistingFoodItemRating(FoodItem foodItem, User user, Short rating, boolean isNew) {
        FoodItemRating foodItemRating = foodItemRatingRepository.findFoodItemRatingByFoodItemAndUser(foodItem, user);
        if(Objects.isNull(foodItemRating)) {
            foodItemRating = FoodItemRating.builder()
                    .foodItem(foodItem)
                    .user(user)
                    .build();
        }
        foodItemRating.setRating(rating);
        foodItemRating = foodItemRatingRepository.saveAndFlush(foodItemRating);
        return mapper.mapAddFoodItemizedDetails(foodItem, foodItemRating, isNew);
    }

    private AddFoodItemizedDetails addNewFoodItemAndRating(String externalId, User user, Short rating, boolean isNew) {
        FatSecretSearchByIdResponse foodItemResponse = retrieveFatSecretFoodItemDataByExternalId(externalId);
        FoodItem newFoodItem = mapper.mapFoodItem(foodItemResponse);
        //TODO: null check here and throw exception
        newFoodItem = foodItemRepository.saveAndFlush(newFoodItem);
        return updateExistingFoodItemRating(newFoodItem, user, rating, isNew);
    }

    private FatSecretSearchByIdResponse retrieveFatSecretFoodItemDataByExternalId(String externalId) {
        return fatSecretClient.getFoodById(externalId,
                SEARCH_FOODS_BY_ID_QUERY_METHOD,
                SEARCH_FOODS_QUERY_FORMAT);
    }
}
