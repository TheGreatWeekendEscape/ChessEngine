package com.chess.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
        RespuestaErrorHttp respuestaErrorHttp = new RespuestaErrorHttp(ex.getMessage());
        return handleExceptionInternal(ex, respuestaErrorHttp, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    protected ResponseEntity<?> handleRuntimeExceptions(Exception ex, WebRequest request) {
        RespuestaErrorHttp respuestaErrorHttp = new RespuestaErrorHttp(ex.getMessage());
        return handleExceptionInternal(ex, respuestaErrorHttp, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PresentationException.class)
    protected ResponseEntity<?> handlePresentationExceptions(PresentationException ex, WebRequest request) {
        RespuestaErrorHttp respuestaErrorHttp = new RespuestaErrorHttp(ex.getMessage());
        return handleExceptionInternal(ex, respuestaErrorHttp, new HttpHeaders(), ex.getHttpStatus(), request);

    }
}
