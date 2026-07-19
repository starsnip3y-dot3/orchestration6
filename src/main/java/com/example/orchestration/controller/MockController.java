package com.example.orchestration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class MockController {

    // @GetMapping("/mock1")
    // public ResponseEntity<Map<String, Object>> mockOne() {
    // Map<String, Object> payload = new LinkedHashMap<>();
    // payload.put("service", "mock1");
    // payload.put("status", "SUCCESS");
    // payload.put("message", "step 1 payload prepared");
    // payload.put("timestamp", Instant.now().toString());
    // return ResponseEntity.ok(payload);
    // }

    //kebutuhan anggota 4
    @PostMapping("/mock1")
    public ResponseEntity<Map<String, Object>> mockOne() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "mock1");
        payload.put("status", "SUCCESS");
        payload.put("message", "step 1 payload prepared");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/mock1/rollback")
    public ResponseEntity<Map<String, Object>> mockOneRollBack() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "mock1");
        payload.put("status", "ROLLED_BACK");
        payload.put("message", "step 1 changes reverted");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }

    // @GetMapping("/mock2")
    // public ResponseEntity<Map<String, Object>> mockTwo() {
    // Map<String, Object> payload = new LinkedHashMap<>();
    // payload.put("service", "mock2");
    // payload.put("status", "SUCCESS");
    // payload.put("message", "step 2 payload prepared");
    // payload.put("timestamp", Instant.now().toString());
    // return ResponseEntity.ok(payload);
    // }

    //kebutuhan anggota 4
    @PostMapping("/mock2")
    public ResponseEntity<Map<String, Object>> mockTwo() {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put("service", "mock2");
    payload.put("status", "SUCCESS");
    payload.put("message", "step 2 payload prepared");
    payload.put("timestamp", Instant.now().toString());
    return ResponseEntity.ok(payload);
    }

    @PostMapping("/mock2/rollback")
    public ResponseEntity<Map<String, Object>> mockTwoRollBack() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "mock2");
        payload.put("status", "ROLLED_BACK");
        payload.put("message", "step 2 changes reverted");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }

    
    @PostMapping("/mock3")
    public ResponseEntity<Map<String, Object>> mockThree(
        //kebutuhan anggota 4
            @RequestParam(value = "simulateError", defaultValue = "false") boolean simulateError) {
        if (simulateError) {
            Map<String, Object> errorPayload = new LinkedHashMap<>();
            errorPayload.put("service", "mock3");
            errorPayload.put("status", "FAILED");
            errorPayload.put("message", "Simulated error occurred at step 3!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorPayload);
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "mock3");
        payload.put("status", "SUCCESS");
        payload.put("message", "step 3 payload prepared");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }
}
