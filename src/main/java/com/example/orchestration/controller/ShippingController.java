package com.example.orchestration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createShipping(
            @RequestParam(value = "simulateError", defaultValue = "false") boolean simulateError) {
        
        // Sengaja digagalkan untuk testing sistem Saga / Compensating Transaction
        if (simulateError) {
            Map<String, Object> errorPayload = new LinkedHashMap<>();
            errorPayload.put("service", "Shipping");
            errorPayload.put("status", "FAILED");
            errorPayload.put("message", "kurir pingsan di jalan!!");
            errorPayload.put("timestamp", Instant.now().toString());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorPayload);
        }

        // Jika berhasil
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "Shipping");
        payload.put("status", "SUCCESS");
        payload.put("message", "resi diterbitkan: JNT-998877");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }
}