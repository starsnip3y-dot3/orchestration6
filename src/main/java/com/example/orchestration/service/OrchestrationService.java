package com.example.orchestration.service;

import com.example.orchestration.model.StepResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class OrchestrationService {

    private static final Logger log = LoggerFactory.getLogger(OrchestrationService.class);

    private final WebClient webClient;
    private StepResponse lastStepThreeResult;

    public OrchestrationService(WebClient.Builder webClientBuilder,
            @Value("${mock.base-url:http://localhost:8080}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StepResponse executeStep3() {
        log.info("Step 3 started: calling shipping endpoint");

        Object payload = webClient.post()
                .uri("/api/shipping/create")
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        StepResponse response = new StepResponse();
        response.setStep("step3");
        response.setStatus("SUCCESS");
        response.setMessage("Step 3 (Shipping) selesai, info resi sudah tersimpan (contohnya).");
        response.setPayload(payload);

        this.lastStepThreeResult = response;
        log.info("Step 3 completed. Stored response from shipping: {}", payload);
        return response;
    }

    public StepResponse getLastStepThreeResult() {
        return lastStepThreeResult;
    }

    public StepResponse executeSagaWorkFlow(boolean simulateError) {
        log.info("SAGA: Mulai flow transaksi");
        boolean step1Success = false;
        boolean step2Success = false;
        Object finalPayload = null;
        try {
            log.info("SAGA Step 1: req /api/inventory/check");
            Object res1 = webClient.post()
                    .uri("/api/inventory/check").retrieve().bodyToMono(Object.class).block();
            log.info("SAGA Step 1: Berhasil, response: {}", res1);
            step1Success = true;
            log.info("SAGA Step 2: req /api/payment/charge");
            Object res2 = webClient.post()
                    .uri("/api/payment/charge").retrieve().bodyToMono(Object.class).block();
            log.info("SAGA Step 2: Berhasil, response: {}", res2);
            step2Success = true;

            log.info("SAGA Step 3: req /api/shipping/create (simulateError={})", simulateError);
            Object res3 = webClient.post()
                    .uri(
                            uriBuilder -> uriBuilder.path("/api/shipping/create")
                                    .queryParam("simulateError", simulateError)
                                    .build())
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            response -> response.bodyToMono(Map.class).map(body -> {
                                String reason = body != null && body.get("message") != null
                                        ? (String) body.get("message")
                                        : "Shipping gagal dengan status " + response.statusCode();
                                return new RuntimeException(reason);
                            })
                    )
                    .bodyToMono(Object.class).block();
            log.info("SAGA Step 3: Berhasil, response: {}", res3);
            finalPayload = res3;

            StepResponse res = new StepResponse();
            res.setStep("SAGA_FLOW");
            res.setStatus("SUCCESS");
            res.setMessage("Transaksi Sudah aman, dari stok, pembayaran dan pengiriman");
            res.setPayload(finalPayload);
            return res;
        } catch (Exception e) {
            log.error("SAGA ERROR: Error during execution: {}", e.getMessage());
            if (step2Success) {
                try {
                    log.info("SAGA ROLLBACK Step 2: Revert /api/payment");
                    Object rollbackRes2 = webClient.post()
                            .uri("/api/payment/refund")
                            .retrieve()
                            .bodyToMono(Object.class)
                            .block();
                    log.info("SAGA ROLLBACK STEP 2 BERHASIL: {}", rollbackRes2);
                } catch (Exception ex) {
                    log.error("SAGA ROLLBACK Step 2 FAILED: {}", ex.getMessage());
                }
            }
            if (step1Success) {
                try {
                    log.info("SAGA ROLLBACK Step 1: Revert /api/inventory");
                    Object rollbackRes1 = webClient.post()
                            .uri("/api/inventory/cancel")
                            .retrieve()
                            .bodyToMono(Object.class)
                            .block();
                    log.info("SAGA ROLLBACK STEP 1 BERHASIL: {}", rollbackRes1);
                } catch (Exception ex) {
                    log.error("SAGA ROLLBACK Step 1 FAILED: {}", ex.getMessage());
                }
            }

            StepResponse ErrRes = new StepResponse();
            ErrRes.setStep("SAGA_FLOW");
            ErrRes.setStatus("FAILED");
            ErrRes.setMessage(
                    "Ada error saat pengiriman. transaksi otomatis dibatalkan uang/stok sudah di-rollback dan aman. alasan: "
                            + e.getMessage());
            return ErrRes;
        }
    }
}
