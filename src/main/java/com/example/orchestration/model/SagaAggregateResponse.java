package com.example.orchestration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SagaAggregateResponse {
    private String sagaId;
    private String overallStatus;
    private String summaryMessage;
    private Instant startedAt;
    private Instant finishedAt;
    private List<StepResult> steps;
}
