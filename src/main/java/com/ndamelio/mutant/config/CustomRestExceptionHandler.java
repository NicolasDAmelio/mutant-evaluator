package com.ndamelio.mutant.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";

    @ExceptionHandler(MutantForbiddenException.class)
    public ResponseEntity<Object> handleMutantForbiddenException(MutantForbiddenException ex){

        MutantCustomError mutantCustomError =
            new MutantCustomError(HttpStatus.FORBIDDEN, ex.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(mutantCustomError, new HttpHeaders(), mutantCustomError.getStatus());
    }

    @ExceptionHandler(MutantInvalidDNAException.class)
    public ResponseEntity<Object> handleMutantInvalidDNAException(MutantInvalidDNAException ex){

        MutantCustomError mutantCustomError =
            new MutantCustomError(HttpStatus.FORBIDDEN, ex.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(mutantCustomError, new HttpHeaders(), mutantCustomError.getStatus());
    }
}