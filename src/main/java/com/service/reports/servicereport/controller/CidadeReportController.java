package com.service.reports.servicereport.controller;

import com.service.reports.servicereport.config.ApiPath;
import com.service.reports.servicereport.services.RelatorioService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(ApiPath.REPORT_SERVICE)
public class CidadeReportController {

    @Autowired
    private RelatorioService reportService;

    @PostMapping(value = "/listAll", produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateReport(@RequestBody String jsonData) {
        try {
            // Nome do relatório (pode ser dinâmico com base nos dados se necessário)
            String reportName = "allCidades";

            byte[] reportBytes = reportService.generateReport(reportName, jsonData);
            return ResponseEntity.ok().body(reportBytes);
        } catch (IOException | JRException e) {
            e.printStackTrace(); // Trate a exceção adequadamente em uma aplicação real
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateReportV2(@RequestParam String reportName, @RequestBody String jsonData) {
        try {
            //String reportName = "allCidades";
            byte[] reportBytes = reportService.generateReport(reportName, jsonData);
            return ResponseEntity.ok().body(reportBytes);
        } catch (IOException | JRException e) {
            e.printStackTrace(); // Trate a exceção adequadamente em uma aplicação real
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping(value = "/parameters", produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateReportWithParams(@RequestParam String reportName, @RequestBody String jsonData) {
        try {
            //String reportName = "allCidades";
            byte[] reportBytes = reportService.generateReportWithParams(reportName, jsonData);
            return ResponseEntity.ok().body(reportBytes);
        } catch (Exception e) {
            e.printStackTrace(); // Trate a exceção adequadamente em uma aplicação real
            return ResponseEntity.status(500).build();
        }
    }
}
