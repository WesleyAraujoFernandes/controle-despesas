package com.financas.controledespesas.exception;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
    private final OffsetDateTime timestamp;
    private final List<FieldError> fields;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
    }
}
