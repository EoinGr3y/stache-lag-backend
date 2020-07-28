package com.rockseven.test.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ InvalidDataException.class })
    public final ResponseEntity handleException(Exception ex, WebRequest request) {
        log.error("Handling exception");
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof InvalidDataException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            InvalidDataException invalidDataException = (InvalidDataException) ex;
            return ResponseEntity
                    .status(status)
                    .body(
                            invalidDataException.getMessage()
                    );
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity
                    .status(status)
                    .body(
                            ex.getMessage()
                    );
        }
    }
}
