package com.example.orchestration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/charge")
    public ResponseEntity<Map<String, Object>> processPayment() {
        // Simulasi potong saldo dari e-wallet user
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "Payment");
        payload.put("status", "SUCCESS");
        payload.put("message", "saldo berhasil dipotong sebesar Rp 1.000");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }

    // Endpoint untuk Saga (Rollback)
    @PostMapping("/refund")
    public ResponseEntity<Map<String, Object>> refundPayment() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "Payment");
        payload.put("status", "ROLLED_BACK");
        payload.put("message", "uang Rp 1.000 berhasil dikembalikan ke saldo");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }
}