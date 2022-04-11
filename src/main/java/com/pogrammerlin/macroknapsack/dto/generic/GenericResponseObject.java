package com.pogrammerlin.macroknapsack.dto.generic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> errors;
}
