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
    private StepResponse lastStepThreeResult;

    public OrchestrationService(WebClient.Builder webClientBuilder,
            @Value("${mock.base-url:http://localhost:8080}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
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
}
