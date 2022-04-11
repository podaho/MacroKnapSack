package com.pogrammerlin.macroknapsack.utility;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DataValidator {
    public static boolean validateUserid(String userIdString) {
        return userIdString.matches("\\d+");
    }
}
