package com.example.orchestration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/charge")
    public ResponseEntity<String> processPayment() {
        // Simulasi potong saldo dari e-wallet user
        String jsonResponse = "{\"service\": \"Payment\", \"status\": \"SUCCESS\", \"message\": \"saldo berhasil dipotong sebesar Rp 1.000\"}";
        return ResponseEntity.ok(jsonResponse);
    }

    // Endpoint untuk Saga (Rollback)
    @PostMapping("/refund")
    public ResponseEntity<String> refundPayment() {
        String jsonResponse = "{\"service\": \"Payment\", \"status\": \"ROLLED_BACK\", \"message\": \"uang Rp 1.000 berhasil dikembalikan ke saldo\"}";
        return ResponseEntity.ok(jsonResponse);
    }
}