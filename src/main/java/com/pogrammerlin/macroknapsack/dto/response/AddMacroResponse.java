package com.pogrammerlin.macroknapsack.dto.response;

import com.pogrammerlin.macroknapsack.dto.AddMacroDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddMacroResponse {
    private long userId;
    List<AddMacroDetails> addMacroDetailsList;
}
