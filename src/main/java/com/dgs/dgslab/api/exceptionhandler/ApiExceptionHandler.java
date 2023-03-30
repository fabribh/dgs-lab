package com.dgs.dgslab.api.exceptionhandler;

import com.dgs.dgslab.domain.exceptions.BusinessException;
import com.dgs.dgslab.domain.exceptions.Problem;
import com.dgs.dgslab.domain.exceptions.ProblemType;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_GENERIC_ERROR = "Occurred an internal error, try again in a moment." +
            " If the error repeat, please contact us!";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleNoSuchMessageException(BusinessException ex, WebRequest request) {

        var status = HttpStatus.NOT_FOUND;
        var issueType = ProblemType.ERROR_BUSINESS;
        var detail = ex.getMessage();

        var issue = createProblemBuilder(status, issueType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, issue, new HttpHeaders(), status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType,
                                                        String detail) {
        return Problem.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}
