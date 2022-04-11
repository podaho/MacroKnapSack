package com.pogrammerlin.macroknapsack.controller;

import com.pogrammerlin.macroknapsack.dto.request.AddMacroRequest;
import com.pogrammerlin.macroknapsack.dto.response.AddMacroResponse;
import com.pogrammerlin.macroknapsack.dto.response.MacrosDetailResponse;
import com.pogrammerlin.macroknapsack.service.MacroService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/macro-goal")
public class MacroActionController {
    private MacroService macroService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<MacrosDetailResponse> getAllMacroGoals(@PathVariable(value = "userId") String userId) {
        return ResponseEntity.ok(macroService.getAllMacroGoalsByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<AddMacroResponse> addNewMacroGoal(@RequestBody AddMacroRequest addMacroRequest) {
        return ResponseEntity.accepted().body(macroService.createNewMacroGoals(addMacroRequest));
    }
}
