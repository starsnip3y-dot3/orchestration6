package com.example.orchestration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepResponse {
    private String step;
    private String status;
    private String message;
    private Object payload;
}