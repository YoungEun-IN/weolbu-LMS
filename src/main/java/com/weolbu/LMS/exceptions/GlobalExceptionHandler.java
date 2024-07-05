package com.weolbu.LMS.exceptions;

import com.weolbu.LMS.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;

import static com.weolbu.LMS.utils.ApiUtils.error;


@Slf4j
@ControllerAdvice   // 전역 설정을 위한 annotaion
public class GlobalExceptionHandler {
    private ResponseEntity<ApiUtils.ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        return newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<ApiUtils.ApiResult<?>> newResponse(String message, HttpStatus status) {
        return ResponseEntity
            .status(status)
            .header("Content-Type", "application/json")
            .body(error(message, status));
    }

    @ExceptionHandler({
        HttpClientErrorException.class,
        IllegalArgumentException.class,
        IllegalStateException.class,
        ConstraintViolationException.class,
        HttpMessageNotReadableException.class,
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(
        MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(
            ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowedException(Exception e) {
        return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
