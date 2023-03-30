package com.dgs.dgslab.api.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorObject {
    private String userMessage;
    private String devMessage;
}
