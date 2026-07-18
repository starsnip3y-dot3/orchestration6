package com.example.orchestration.controller;

import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MockController {

    // @GetMapping("/mock1")
    // public ResponseEntity<Map<String, Object>> mockOne() {
    //     Map<String, Object> payload = new LinkedHashMap<>();
    //     payload.put("service", "mock1");
    //     payload.put("status", "SUCCESS");
    //     payload.put("message", "step 1 payload prepared");
    //     payload.put("timestamp", Instant.now().toString());
    //     return ResponseEntity.ok(payload);
    // }

    // @GetMapping("/mock2")
    // public ResponseEntity<Map<String, Object>> mockTwo() {
    //     Map<String, Object> payload = new LinkedHashMap<>();
    //     payload.put("service", "mock2");
    //     payload.put("status", "SUCCESS");
    //     payload.put("message", "step 2 payload prepared");
    //     payload.put("timestamp", Instant.now().toString());
    //     return ResponseEntity.ok(payload);
    // }

    @PostMapping("/mock3")
    public ResponseEntity<Map<String, Object>> mockThree() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "mock3");
        payload.put("status", "SUCCESS");
        payload.put("message", "step 3 payload prepared");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }
}
