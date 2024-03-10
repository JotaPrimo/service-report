package com.service.reports.servicereport.exceptions;

import com.service.reports.servicereport.exceptions.CustonExceptions.ReportNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(ReportNotFoundException exception,
                                                                        HttpServletRequest request,
                                                                        BindingResult result) {
        log.error("class ApiExceptionHandler - ", exception);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Erro de cadatro", "Campo(s) inválido(s)", result));
    }
}
