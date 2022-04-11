package com.pogrammerlin.macroknapsack.dto.request;

import com.pogrammerlin.macroknapsack.dto.MacroNutrientSpecifications;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddMacroRequest {
    private long userId;
    List<MacroNutrientSpecifications> addMacroDetailsList;
}
