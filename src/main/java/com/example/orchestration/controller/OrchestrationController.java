package com.example.orchestration.controller;

import com.example.orchestration.model.StepResponse;
import com.example.orchestration.service.OrchestrationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    private final OrchestrationService orchestrationService;

    public OrchestrationController(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    // endpoint testing simulateErrorny, kalau mau di ubah gas aja Anggota 5
    @PostMapping("/saga")
    public ResponseEntity<StepResponse> runSaga(
            @RequestParam(value = "simulateError", defaultValue = "false") boolean simulateError) {
        StepResponse res = orchestrationService.executeSagaWorkFlow(simulateError);
        if ("FAILED".equals(res.getStatus())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("/step1")
    public ResponseEntity<StepResponse> runStep1() {
        StepResponse response = orchestrationService.executeStep1();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/step2")
    public ResponseEntity<StepResponse> runStep2() {
        StepResponse response = orchestrationService.executeStep2();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/step3")
    public ResponseEntity<StepResponse> runStep3() {
        StepResponse response = orchestrationService.executeStep3();
        return ResponseEntity.ok(response);
    }
}
