package com.example.orchestration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkStock() {
        // Simulasi cek stok sparepart di database
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "Inventory");
        payload.put("status", "SUCCESS");
        payload.put("message", "stok masih tersedia dan siap dikirimkan ke tujuan");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }

    // Endpoint Saga (Rollback) jika transaksi batal
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> releaseStock() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("service", "Inventory");
        payload.put("status", "ROLLED_BACK");
        payload.put("message", "stok sparepart dikembalikan ke rak");
        payload.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(payload);
    }
}