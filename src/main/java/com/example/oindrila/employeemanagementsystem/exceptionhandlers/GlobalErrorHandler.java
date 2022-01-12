package com.example.oindrila.employeemanagementsystem.exceptionhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<String> handleDuplicateKeyExceptions(final SQLIntegrityConstraintViolationException e) {
        log.error("Handling Duplicate Key Error:\n", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolationExceptions(final ConstraintViolationException e) {
        log.error("Handling Constraint Violation Exception : \n", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
