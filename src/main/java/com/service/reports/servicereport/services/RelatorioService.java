package com.service.reports.servicereport.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioService {
    private final ResourceLoader resourceLoader;


    public RelatorioService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public byte[] generateReport(String reportName, String jsonData) throws JRException, IOException {
        // Caminho do relatório no diretório "resources/reports"
        String reportPath = "classpath:reports/" + reportName + ".jrxml";

        // Carrega o arquivo do relatório usando a classe Resource
        Resource resource = resourceLoader.getResource(reportPath);

        // Verifica se o recurso existe
        if (resource.exists()) {
            // Compila o relatório JRXML
            InputStream inputStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // Converte a string JSON em um InputStream
            InputStream jsonInputStream = new ByteArrayInputStream(jsonData.getBytes());

            // Cria um dataSource a partir do JSON
            JsonDataSource jsonDataSource = new JsonDataSource(jsonInputStream);

            // Parâmetros do relatório (se necessário)
            Map<String, Object> parameters = new HashMap<>();

            // Gera o relatório em formato PDF
            byte[] reportBytes = JasperRunManager.runReportToPdf(jasperReport, parameters, jsonDataSource);

            // Fecha os inputStreams
            inputStream.close();
            jsonInputStream.close();

            return reportBytes;
        } else {
            throw new IllegalArgumentException("O relatório não foi encontrado: " + reportName);
        }
    }

}
