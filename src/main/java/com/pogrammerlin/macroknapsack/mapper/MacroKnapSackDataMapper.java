package com.pogrammerlin.macroknapsack.mapper;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchByIdResponse;
import com.pogrammerlin.macroknapsack.client.FatSecret.dto.Serving;
import com.pogrammerlin.macroknapsack.dto.AddFoodItemizedDetails;
import com.pogrammerlin.macroknapsack.dto.AddMacroDetails;
import com.pogrammerlin.macroknapsack.dto.MacroNutrientSpecifications;
import com.pogrammerlin.macroknapsack.dto.response.MacroDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.MacrosDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.UserDetailResponse;
import com.pogrammerlin.macroknapsack.dto.response.UsersDetailResponse;
import com.pogrammerlin.macroknapsack.model.FoodItem;
import com.pogrammerlin.macroknapsack.model.FoodItemRating;
import com.pogrammerlin.macroknapsack.model.Macro;
import com.pogrammerlin.macroknapsack.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pogrammerlin.macroknapsack.constant.Constants.METRIC_SERVING_UNIT;

@Slf4j
@Service
@AllArgsConstructor
public class MacroKnapSackDataMapper {
    public AddMacroDetails mapAddMacroDetails(Macro macro, boolean isNew) {
        return AddMacroDetails.builder()
                .kcalGoal(macro.getKcalGoal())
                .carbGoal(macro.getCarbGoal())
                .fatGoal(macro.getFatGoal())
                .proteinGoal(macro.getProteinGoal())
                .fiberGoal(macro.getFiberGoal())
                .isNew(isNew)
                .build();
    }

    public Macro mapMacro(MacroNutrientSpecifications macroSpecs, User user) {
        return Macro.builder()
                .kcalGoal(macroSpecs.getKcalGoal())
                .carbGoal(macroSpecs.getCarbGoal())
                .fatGoal(macroSpecs.getFatGoal())
                .proteinGoal(macroSpecs.getProteinGoal())
                .fiberGoal(macroSpecs.getFiberGoal())
                .user(user)
                .build();
    }

    public MacrosDetailResponse mapMacrosDetailResponse(List<Macro> macroList) {
        return MacrosDetailResponse.builder()
                .macroDetailResponses(macroList.stream()
                                                .map(this::mapMacroDetailResponse)
                                                .collect(Collectors.toList()))
                .build();
    }

    public MacroDetailResponse mapMacroDetailResponse(Macro macro) {
        return MacroDetailResponse.builder()
                .id(macro.getId())
                .kcalGoal(macro.getKcalGoal())
                .carbGoal(macro.getCarbGoal())
                .fatGoal(macro.getFatGoal())
                .proteinGoal(macro.getProteinGoal())
                .fiberGoal(macro.getFiberGoal())
                .build();
    }

    public UsersDetailResponse mapUsersDetailResponse(List<User> users) {
        return UsersDetailResponse.builder()
                .userDetailResponses(users.stream()
                                            .map(this::mapUserDetailResponse)
                                            .collect(Collectors.toList()))
                .build();
    }

    public UserDetailResponse mapUserDetailResponse(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .weight(user.getWeight())
                .build();
    }

    public AddFoodItemizedDetails mapAddFoodItemizedDetails(FoodItem foodItem, FoodItemRating foodItemRating, boolean isNew) {
        return AddFoodItemizedDetails.builder()
                .externalId(foodItem.getExternalId())
                .name(foodItem.getName())
                .kcal(foodItem.getKcal())
                .carb(foodItem.getCarb())
                .fat(foodItem.getFat())
                .protein(foodItem.getProtein())
                .servingSize(foodItem.getServingSize())
                .rating(foodItemRating.getRating())
                .isNew(isNew)
                .build();
    }

    public FoodItem mapFoodItem(FatSecretSearchByIdResponse response) {
        if(Objects.isNull(response)
                || Objects.isNull(response.getFood())
                || Objects.isNull(response.getFood().getServings())
                || CollectionUtils.isEmpty(response.getFood().getServings().getServings())) {
            return null;
        }
        Serving servingDetails = findClosestToHundredGramServings(response.getFood().getServings().getServings());

        if(Objects.isNull(servingDetails)) {
            return null;
        }

        return FoodItem.builder()
                .externalId(response.getFood().getExternalId())
                .name(response.getFood().getName())
                .kcal(Double.valueOf(servingDetails.getCalories()))
                .carb(Double.valueOf(servingDetails.getCarbohydrate()))
                .fat(Double.valueOf(servingDetails.getFat()))
                .protein(Double.valueOf(servingDetails.getProtein()))
                .fiber(Double.valueOf(servingDetails.getFiber()))
                .servingSize(Double.valueOf(servingDetails.getMetricServingAmount()))
                .build();
    }

    private Serving findClosestToHundredGramServings(List<Serving> servings) {
        return servings.stream()
                .filter(serving -> !StringUtils.isBlank(serving.getMetricServingUnit())
                        && serving.getMetricServingUnit().equals(METRIC_SERVING_UNIT)
                        && !StringUtils.isBlank(serving.getMetricServingAmount()))
                .min((s1, s2) -> {
                    return Double.compare(
                            Math.abs(100.0 - Double.valueOf(s1.getMetricServingAmount())),
                            Math.abs(100.0 - Double.valueOf(s2.getMetricServingAmount())));
                })
                .orElse(null);
    }
}
