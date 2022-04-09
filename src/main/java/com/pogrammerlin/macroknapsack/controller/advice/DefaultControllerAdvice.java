package com.pogrammerlin.macroknapsack.controller.advice;

import com.pogrammerlin.macroknapsack.controller.DefaultController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = DefaultController.class)
public class DefaultControllerAdvice {
}
