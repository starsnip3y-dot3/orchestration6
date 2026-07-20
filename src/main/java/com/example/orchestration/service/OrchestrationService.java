package com.example.orchestration.service;

import com.example.orchestration.model.StepResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OrchestrationService {

    private static final Logger log = LoggerFactory.getLogger(OrchestrationService.class);

    private final WebClient webClient;

    private StepResponse lastStepOneResult;
    private StepResponse lastStepTwoResult;

    private StepResponse lastStepThreeResult;

    public OrchestrationService(WebClient.Builder webClientBuilder,
            @Value("${mock.base-url:http://localhost:8080}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StepResponse executeStep1() {
        log.info("Step 1 started: Calling /mock1 endpoint");

        Object payload = webClient.post()
                .uri("/mock1")
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        StepResponse response = new StepResponse();
        response.setStep("step1");
        response.setStatus("SUCCESS");
        response.setMessage("Step 1 completed and mock1 response was stored");
        response.setPayload(payload);

        this.lastStepOneResult = response;
        log.info("Step 1 completed successfully. Stored response from /mock1: {}", payload);
        return response;
    }

    public StepResponse executeStep2() {
        log.info("Step 2 started: Calling /mock2 endpoint");

        Object payload = webClient.post()
                .uri("/mock2")
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        StepResponse response = new StepResponse();
        response.setStep("step2");
        response.setStatus("SUCCESS");
        response.setMessage("Step 2 completed and mock2 response was stored");
        response.setPayload(payload);

        this.lastStepTwoResult = response;
        log.info("Step 2 completed successfully. Stored response from /mock2: {}", payload);
        return response;
    }

    public StepResponse getLastStepOneResult() {
        return lastStepOneResult;
    }

    public StepResponse getLastStepTwoResult() {
        return lastStepTwoResult;
    }

    public StepResponse executeStep3() {
        log.info("Step 3 started: calling mock3 endpoint");

        Object payload = webClient.post()
                .uri("/mock3")
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        StepResponse response = new StepResponse();
        response.setStep("step3");
        response.setStatus("SUCCESS");
        response.setMessage("step 3 completed and the mock3 response was stored");
        response.setPayload(payload);

        this.lastStepThreeResult = response;
        log.info("Step 3 completed. Stored response from mock3: {}", payload);
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
            log.info("SAGA Step 1: req /mock1");
            Object res1 = webClient.post()
                    .uri("/mock1").retrieve().bodyToMono(Object.class).block();
            log.info("SAGA Step 1: Berhasil, response: {}", res1);
            step1Success = true;
            log.info("SAGA Step 2: req /mock2");
            Object res2 = webClient.post()
                    .uri("/mock2").retrieve().bodyToMono(Object.class).block();
            log.info("SAGA Step 2: Berhasil, response: {}", res2);
            step2Success = true;

            log.info("SAGA Step 3: req /mock3 (simulateError={})", simulateError);
            Object res3 = webClient.post()
                    .uri(
                            uriBuilder -> uriBuilder.path("/mock3").queryParam("simulateError", simulateError)
                                    .build())

                    .retrieve().bodyToMono(Object.class).block();
            log.info("SAGA Step 3: Berhasil, response: {}", res3);
            finalPayload = res3;

            StepResponse res = new StepResponse();
            res.setStep("SAGA_FLOW");
            res.setStatus("SUCCESS");
            res.setMessage("FLOW SAGA BERHASIL DIEKSEKUSI SEMUANYA (HAPPY FLOW)");
            res.setPayload(finalPayload);
            return res;
        } catch (Exception e) {
            log.error("SAGA ERROR: Error during execution: {}", e.getMessage());
            if (step2Success) { // s1 ss compensate
                try {
                    log.info("SAGA ROLLBACK Step 2: Revert /mock2");
                    Object rollbackRes2 = webClient.post()
                            .uri("/mock2/rollback")
                            .retrieve()
                            .bodyToMono(Object.class)
                            .block();
                    log.info("SAGA ROLLBACK STEP 2 BERHASIL: {}", rollbackRes2);
                } catch (Exception ex) {
                    log.error("SAGA ROLLBACK Step 2 FAILED: {}", ex.getMessage());
                }
                if (step1Success) { // s1 ss compensate
                    try {
                        log.info("SAGA ROLLBACK Step 1: Revert /mock1");
                        Object rollbackRes1 = webClient.post()
                                .uri("/mock1/rollback")
                                .retrieve()
                                .bodyToMono(Object.class)
                                .block();
                        log.info("SAGA ROLLBACK STEP 1 BERHASIL: {}", rollbackRes1);
                    } catch (Exception ex) {
                        log.error("SAGA ROLLBACK Step 1 FAILED: {}", ex.getMessage());
                    }
                }
            }

            StepResponse ErrRes = new StepResponse();
            ErrRes.setStep("SAGA_FLOW");
            ErrRes.setStatus("FAILED");
            ErrRes.setMessage("TRANSAKSI GAGAL DAN SUDAH DI ROLLBACK (Compensated), ALASAN: " + e.getMessage());
            return ErrRes;
        }
    }
}
