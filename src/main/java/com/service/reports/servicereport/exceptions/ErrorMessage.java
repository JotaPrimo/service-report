package com.service.reports.servicereport.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.service.reports.servicereport.utils.DataUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {
    // path url qual rescurso que deu erro
    private String path;

    // qual methodo http gerou erro
    private String method;

    // status de erro no formato de numero
    private int status;

    // status de erro no formato de palavra
    private String statusText;

    // titulo do erro
    private String title;

    // message de erro simplficada
    private String message;

    private String when;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorMessage() {
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String title, String message) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.title = title;
        this.message = message;
        this.when = DataUtils.getDataAtualDMYHMS();
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String title, String message, BindingResult result) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        this.title = title;
        this.when = DataUtils.getDataAtualDMYHMS();
        addErrors(result);
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", status=" + status +
                ", statusText='" + statusText + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", when=" + when +
                ", errors=" + errors +
                '}';
    }
}