package com.pogrammerlin.macroknapsack.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.pogrammerlin.macroknapsack.dto.AddFoodItemizedDetails;
import com.pogrammerlin.macroknapsack.dto.AddMacroDetails;
import com.pogrammerlin.macroknapsack.dto.MacroNutrientSpecifications;
import com.pogrammerlin.macroknapsack.dto.request.AddMacroRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddFoodItemResponse;
import com.pogrammerlin.macroknapsack.dto.response.AddMacroResponse;
import com.pogrammerlin.macroknapsack.dto.response.MacrosDetailResponse;
import com.pogrammerlin.macroknapsack.mapper.MacroKnapSackDataMapper;
import com.pogrammerlin.macroknapsack.model.Macro;
import com.pogrammerlin.macroknapsack.model.User;
import com.pogrammerlin.macroknapsack.repository.MacroRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.pogrammerlin.macroknapsack.utility.AsyncHelper.handleFutureError;
import static com.pogrammerlin.macroknapsack.utility.DataValidator.validateUserid;

@Slf4j
@Component
@AllArgsConstructor
public class MacroService {
    private MacroRepository macroRepository;
    private MacroKnapSackDataMapper mapper;
    private UserService userService;

    public MacrosDetailResponse getAllMacroGoalsByUserId(String userId) {
        if(!validateUserid(userId)) {
            //TODO: throw exception if validation fails
            return null;
        }
        User requestedUser = userService.getUserByUserId(Long.valueOf(userId));
        return mapper.mapMacrosDetailResponse(macroRepository.findAllByUser(requestedUser));
    }

    public AddMacroResponse createNewMacroGoals(AddMacroRequest addMacroRequest) {
        User requestUser = userService.getUserByUserId(addMacroRequest.getUserId());
        //TODO: null check here

        List<AddMacroDetails> addMacroDetails = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        List<CompletableFuture<AddMacroDetails>> completableFutureList = getAddMacroDetailsCompletableFutures(addMacroRequest.getAddMacroDetailsList(), requestUser);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));
        CompletableFuture<List<AddMacroDetails>> completableFutureResponses = allFutures
                .thenApply(future -> completableFutureList
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                ).toCompletableFuture();

        try {
            addMacroDetails = completableFutureResponses.get();
        } catch(Exception e) {
            log.error("Unexpected Async Failure; cause: {}", e.getMessage());
            //TODO: handle exception here
        }

        return AddMacroResponse.builder()
                .userId(requestUser.getId())
                .addMacroDetailsList(addMacroDetails)
                .build();
    }

    private List<CompletableFuture<AddMacroDetails>> getAddMacroDetailsCompletableFutures(List<MacroNutrientSpecifications> addMacroDetailsList, User requestUser) {
        List<CompletableFuture<AddMacroDetails>> completableFutureList = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for(MacroNutrientSpecifications macroSpecs : addMacroDetailsList) {
            completableFutureList.add(getAddMacroDetails(macroSpecs, requestUser, errors));
        }
        return completableFutureList;
    }

    private CompletableFuture<AddMacroDetails> getAddMacroDetails(MacroNutrientSpecifications macroSpecs, User requestUser, List<String> errors) {
        return CompletableFuture.supplyAsync(() -> {
                    List<Macro> macroQueryResult
                            = macroRepository.findAllByKcalGoalAndAndCarbGoalAndAndFatGoalAndProteinGoalAndFiberGoalAndUser(
                                                macroSpecs.getKcalGoal(),
                                                macroSpecs.getCarbGoal(),
                                                macroSpecs.getFatGoal(),
                                                macroSpecs.getProteinGoal(),
                                                macroSpecs.getFiberGoal(),
                                                requestUser);

                    if(CollectionUtils.isEmpty(macroQueryResult)) {
                        Macro newMacro = mapper.mapMacro(macroSpecs, requestUser);
                        newMacro = macroRepository.save(newMacro);
                        return mapper.mapAddMacroDetails(newMacro, true);
                    } else {
                        return mapper.mapAddMacroDetails(macroQueryResult.stream().findFirst().get(), false);
                    }
                })
                .exceptionally(e -> handleFutureError(e, errors));
    }
}
