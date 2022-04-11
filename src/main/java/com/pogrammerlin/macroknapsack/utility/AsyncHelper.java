package com.pogrammerlin.macroknapsack.utility;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AsyncHelper {
    public static <T> T handleFutureError(Throwable e, List<String> errors){
        errors.add(e.getMessage());
        return null;
    }
}
