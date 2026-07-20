# Topik 3 — Orchestration & Saga (Kelompok 6)

## Anggota & Tugas

| Anggota   | Tugas                                                   |
| --------- | ------------------------------------------------------- |
| Anggota 1 | Membuat 3 mock service (Inventory, Payment, Shipping)   |
| Anggota 2 | Logic Saga step 1 & 2 (Inventory Check, Payment Charge) |
| Anggota 3 | Logic Saga step 3 (Shipping Create) + error handling    |
| Anggota 4 | Logic rollback / compensating transactions              |
| Anggota 5 | Agregasi response + dokumentasi                         |

---

## Arsitektur Singkat

Aplikasi ini mengimplementasikan **Saga Orchestration Pattern** dengan 3 mock service yang berjalan dalam satu aplikasi Spring Boot:

1. **Inventory** — cek & reserve stok sparepart
2. **Payment** — potong saldo user
3. **Shipping** — buat resi pengiriman

Ketiga mock service dipanggil secara berurutan oleh `OrchestrationService` via HTTP `WebClient`. Jika salah satu step gagal, sistem menjalankan **compensating transaction** (rollback) untuk step-step yang sudah berhasil.

---

## Endpoint Agregasi (Tugas Anggota 5)

### `POST /orchestration/saga/aggregate?simulateError={true|false}`

**Fungsi:** Menggabungkan hasil Step 1 (Inventory), Step 2 (Payment), Step 3 (Shipping), termasuk rollback, menjadi satu JSON response utuh.

### Contoh Response — Happy Flow

```json
{
    "sagaId": "be76214d-0c3e-48d6-9251-b6e696beb3de",
    "overallStatus": "SUCCESS",
    "summaryMessage": "Semua step (Inventory, Payment, Shipping) berhasil dieksekusi tanpa rollback.",
    "startedAt": "2026-07-20T21:51:54.814093800Z",
    "finishedAt": "2026-07-20T21:51:54.834091500Z",
    "steps": [
        {
            "stepName": "Inventory-Check",
            "type": "EXECUTION",
            "status": "SUCCESS",
            "rawResponse": {
                "service": "Inventory",
                "status": "SUCCESS",
                "message": "stok masih tersedia dan siap dikirimkan ke tujuan",
                "timestamp": "2026-07-20T21:51:54.819096300Z"
            }
        },
        {
            "stepName": "Payment-Charge",
            "type": "EXECUTION",
            "status": "SUCCESS",
            "rawResponse": {
                "service": "Payment",
                "status": "SUCCESS",
                "message": "saldo berhasil dipotong sebesar Rp 1.000",
                "timestamp": "2026-07-20T21:51:54.825559100Z"
            }
        },
        {
            "stepName": "Shipping-Create",
            "type": "EXECUTION",
            "status": "SUCCESS",
            "rawResponse": {
                "service": "Shipping",
                "status": "SUCCESS",
                "message": "resi diterbitkan: JNT-998877",
                "timestamp": "2026-07-20T21:51:54.831079300Z"
            }
        }
    ]
}
```

### Contoh Response — Error & Rollback Flow

```json
{
    "sagaId": "9d9fa8ad-c34e-44b0-b105-b3a64613ed20",
    "overallStatus": "FAILED_ROLLED_BACK",
    "summaryMessage": "Step Shipping gagal. Payment dan Inventory sudah di-rollback.",
    "startedAt": "2026-07-20T21:41:14.814015100Z",
    "finishedAt": "2026-07-20T21:41:14.843597200Z",
    "steps": [
        {
            "stepName": "Inventory-Check",
            "type": "EXECUTION",
            "status": "SUCCESS",
            "rawResponse": {
                "service": "Inventory",
                "status": "SUCCESS",
                "message": "stok masih tersedia dan siap dikirimkan ke tujuan",
                "timestamp": "2026-07-20T21:41:14.818085600Z"
            }
        },
        {
            "stepName": "Payment-Charge",
            "type": "EXECUTION",
            "status": "SUCCESS",
            "rawResponse": {
                "service": "Payment",
                "status": "SUCCESS",
                "message": "saldo berhasil dipotong sebesar Rp 1.000",
                "timestamp": "2026-07-20T21:41:14.823604600Z"
            }
        },
        {
            "stepName": "Shipping-Create",
            "type": "EXECUTION",
            "status": "FAILED",
            "rawResponse": {
                "message": "kurir pingsan di jalan!!"
            }
        },
        {
            "stepName": "Payment-Refund",
            "type": "COMPENSATION",
            "status": "ROLLED_BACK",
            "rawResponse": {
                "service": "Payment",
                "status": "ROLLED_BACK",
                "message": "uang Rp 1.000 berhasil dikembalikan ke saldo",
                "timestamp": "2026-07-20T21:41:14.836472400Z"
            }
        },
        {
            "stepName": "Inventory-Cancel",
            "type": "COMPENSATION",
            "status": "ROLLED_BACK",
            "rawResponse": {
                "service": "Inventory",
                "status": "ROLLED_BACK",
                "message": "stok sparepart dikembalikan ke rak",
                "timestamp": "2026-07-20T21:41:14.841595400Z"
            }
        }
    ]
}
```

---

## Screenshot Pengujian

> **Screenshot akan diisi setelah testing manual.**

### 1. Happy Flow (Berhasil semua step, tanpa rollback)

   ![Testing HappyFlow_1.1](<Docs/Testing%20HappyFlow_1.1.png>)
   ![Testing HappyFlow_1.2](<Docs/Testing%20HappyFlow_1.2.png>)
   Postman — Happy flow (200 OK, `overallStatus: "SUCCESS"`)

### 2. Error Flow (Gagal di salah satu step, dengan rollback)

   ![Testing Error + Rollback Flow_1.1](<Docs/Testing%20Error%20+%20Rollback%20Flow_1.1.png>)
   ![Testing Error + Rollback Flow_1.2](<Docs/Testing%20Error%20+%20Rollback%20Flow_1.2.png>)
   ![Testing Error + Rollback Flow_1.3](<Docs/Testing%20Error%20+%20Rollback%20Flow_1.3.png>)
    Postman — Error flow (`overallStatus: "FAILED_ROLLED_BACK"`)

### 3. Log Aplikasi (Menunjukkan urutan step + rollback)

   ![Log Console.png](<Docs/Log%20Console.png>)
    Log aplikasi menunjukkan urutan step + rollback

### Cara Testing

```bash
# Happy flow
curl -X POST "http://localhost:8080/orchestration/saga/aggregate"

# Error + rollback flow
curl -X POST "http://localhost:8080/orchestration/saga/aggregate?simulateError=true"

atau pakai Postman dengan method POST ke endpoint yang sama.
```

### Cara Menjalankan Aplikasi

```bash
# Pastikan JDK 25 terinstall, lalu:
./mvnw spring-boot:run
```

---

## Kesimpulan

Alur end-to-end dari Step 1 (Inventory Check), Step 2 (Payment Charge), hingga Step 3 (Shipping Create) berjalan sesuai desain Saga Orchestration Pattern. Jika terjadi kegagalan di salah satu step, sistem secara otomatis menjalankan compensating transaction untuk mengembalikan state ke kondisi awal. Response agregasi memberikan visibility penuh terhadap seluruh riwayat eksekusi dalam satu JSON utuh.
