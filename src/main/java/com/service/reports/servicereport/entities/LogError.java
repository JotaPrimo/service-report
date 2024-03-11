package com.service.reports.servicereport.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


public class LogError implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime when;

    private String reportName;

    private String jsonData;

    public LogError(LocalDateTime when, String reportName, String jsonData) {
        this.when = when;
        this.reportName = reportName;
        this.jsonData = jsonData;
    }

}
