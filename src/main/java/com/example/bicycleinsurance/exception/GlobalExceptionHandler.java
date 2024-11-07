package com.example.bicycleinsurance.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(CustomValidationException ex) {
        return new ErrorResponse("VALIDATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(ScriptExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleScriptException(ScriptExecutionException ex) {
        return new ErrorResponse("SCRIPT_EXECUTION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorResponse("CONSTRAINT_VIOLATION", ex.getMessage());
    }

    public record ErrorResponse(String code, String message) {
    }
}
