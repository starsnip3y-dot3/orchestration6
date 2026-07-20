package com.example.orchestration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StepResult {
    private String stepName;
    private String type;
    private String status;
    private Object rawResponse;
}
