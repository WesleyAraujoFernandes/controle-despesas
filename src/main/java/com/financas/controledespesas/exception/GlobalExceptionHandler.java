package com.financas.controledespesas.exception;

import java.net.http.HttpHeaders;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorResponse error = buildError(
                "https://api.financas.com/errors/not-found",
                "Recurso não encontrado",
                status,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiErrorResponse error = buildError("https://api.financas.com/errors/business-rule", "Regra de negócio violada",
                status, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        List<ApiErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> ApiErrorResponse.FieldError.builder()
                        .field(field.getField())
                        .message(field.getDefaultMessage())
                        .build())
                .toList();
        ApiErrorResponse error = ApiErrorResponse.builder()
                .type("https://api.financas.com/errors/invalid-params")
                .title("Um ou mais campos estão inválidos")
                .status(status.value())
                .detail("Faça o preenchimento correto e tente novamente.")
                .instance(path)
                .timestamp(OffsetDateTime.now())
                .fields(fieldErrors)
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponse error = buildError(
            "https://api.financas.com/errors/internal-server-error", 
            "Erro interno do servidor", status, "Ocorreu um erro interno inesperado no sistema. Tente novamente mais tarde.", request.getRequestURI());
            return ResponseEntity.status(status).body(error);
    }

    private ApiErrorResponse buildError(String type, String title, HttpStatus status, String detail, String path) {
        return ApiErrorResponse.builder()
                .type(type)
                .title(title)
                .status(status.value())
                .detail(detail)
                .instance(path)
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
