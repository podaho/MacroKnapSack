package com.pogrammerlin.macroknapsack.dto.response;

import com.pogrammerlin.macroknapsack.dto.generic.GenericResponseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse extends GenericResponseObject {
    private long id;
    private String firstName;
    private String lastName;
    private short age;
    private int weight;
}
