package com.example.orchestration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @PostMapping("/check")
    public ResponseEntity<String> checkStock() {
        // Simulasi cek stok sparepart di database
        String jsonResponse = "{\"service\": \"Inventory\", \"status\": \"SUCCESS\", \"message\": \"stok masih tersedia dan siap dikirimkan ke tujuan\"}";
        return ResponseEntity.ok(jsonResponse);
    }

    // Endpoint Saga (Rollback) jika transaksi batal
    @PostMapping("/cancel")
    public ResponseEntity<String> releaseStock() {
        String jsonResponse = "{\"service\": \"Inventory\", \"status\": \"ROLLED_BACK\", \"message\": \"stok sparepart dikembalikan ke rak\"}";
        return ResponseEntity.ok(jsonResponse);
    }
}