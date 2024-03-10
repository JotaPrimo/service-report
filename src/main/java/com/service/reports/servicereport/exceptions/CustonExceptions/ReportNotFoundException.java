package com.service.reports.servicereport.exceptions.CustonExceptions;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(String message) {
        super(message);
    }
}
