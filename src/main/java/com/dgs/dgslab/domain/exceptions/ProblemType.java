package com.dgs.dgslab.domain.exceptions;

import lombok.Getter;

@Getter
public enum ProblemType {

    ERROR_BUSINESS("/error-business", "Business Rules Violation");

    private String title;
    private String uri;

    ProblemType(String title, String uri) {
        this.title = "https://dgs-lab.com" + title;
        this.uri = uri;
    }
}
