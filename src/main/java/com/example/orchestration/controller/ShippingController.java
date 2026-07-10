package com.example.orchestration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @PostMapping("/create")
    public ResponseEntity<String> createShipping(
            @RequestParam(value = "simulateError", defaultValue = "false") boolean simulateError) {
        
        // Sengaja digagalkan untuk testing sistem Saga / Compensating Transaction
        if (simulateError) {
            String errorResponse = "{\"service\": \"Shipping\", \"status\": \"FAILED\", \"message\": \"kurir pingsan di jalan!!\"}";
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        // Jika berhasil
        String jsonResponse = "{\"service\": \"Shipping\", \"status\": \"SUCCESS\", \"message\": \"resi diterbitkan: JNT-998877\"}";
        return ResponseEntity.ok(jsonResponse);
    }
}