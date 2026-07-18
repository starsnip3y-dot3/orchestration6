package com.example.orchestration.controller;

import com.example.orchestration.model.StepResponse;
import com.example.orchestration.service.OrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    private final OrchestrationService orchestrationService;

    public OrchestrationController(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/step3")
    public ResponseEntity<StepResponse> runStep3() {
        StepResponse response = orchestrationService.executeStep3();
        return ResponseEntity.ok(response);
    }
}
