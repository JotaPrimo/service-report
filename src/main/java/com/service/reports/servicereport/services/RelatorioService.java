package com.service.reports.servicereport.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.reports.servicereport.config.Constantes;
import com.service.reports.servicereport.enums.IndiceJsonReport;
import com.service.reports.servicereport.web.exceptions.ReportNameBlank;
import com.service.reports.servicereport.web.exceptions.ReportNotFoundException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class RelatorioService {
    private final ResourceLoader resourceLoader;

    public RelatorioService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public byte[] generateReport(String reportName, String jsonData) throws JRException, IOException {

        String reportPath = returnReportPathJrxml(reportName);

        Resource resource = resourceLoader.getResource(reportPath);

        if (resource.exists()) {
            InputStream inputStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            InputStream jsonInputStream = new ByteArrayInputStream(jsonData.getBytes());

            JsonDataSource jsonDataSource = new JsonDataSource(jsonInputStream);

            Map<String, Object> parameters = new HashMap<>();

            byte[] reportBytes = JasperRunManager.runReportToPdf(jasperReport, parameters, jsonDataSource);

            inputStream.close();
            jsonInputStream.close();

            return reportBytes;
        } else {
            throw new ReportNotFoundException("O relatório não foi encontrado: ".concat(reportName));
        }
    }

    public byte[] generateReportWithParams(String reportName, String jsonData) throws Exception {

        String reportPath = returnReportPathJrxml(reportName);

        Resource resource = resourceLoader.getResource(reportPath);

        if (resource.exists()) {

            InputStream inputStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            JsonNode apenasDadosDoReport = getDadosPorIndiceReport(jsonData, IndiceJsonReport.DADOS_REPORT.getValor());
            InputStream jsonInputStream = new ByteArrayInputStream(apenasDadosDoReport.toString().getBytes());

            JsonDataSource jsonDataSource = new JsonDataSource(jsonInputStream);

            Map<String, Object> parameters = getAtributosFromJson(jsonData);

            byte[] reportBytes = JasperRunManager.runReportToPdf(jasperReport, parameters, jsonDataSource);

            inputStream.close();
            jsonInputStream.close();

            return reportBytes;
        } else {
            throw new ReportNotFoundException("O relatório não foi encontrado: ".concat(reportName));
        }
    }

    public Map<String, Object> getAtributosFromJson(String jsonString) throws Exception {

        JsonNode jsonNode = getDadosPorIndiceReport(jsonString, IndiceJsonReport.PARAMS.getValor());

        if (jsonNode.isObject()) {
            return passarParametrosParaMap(jsonNode.fields());
        }

        return Collections.emptyMap();
    }

    public static Map<String, Object> passarParametrosParaMap(Iterator<Map.Entry<String, JsonNode>> campos) {
        Map<String, Object> atributos = new HashMap<>();
        while (campos.hasNext()) {
            Map.Entry<String, JsonNode> campo = campos.next();
            atributos.put(campo.getKey(), campo.getValue().asText());
        }
        return atributos;
    }

    public static JsonNode getDadosPorIndiceReport(String jsonString, String indice) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        return jsonNode.path(indice);
    }

    public static String returnReportPathJrxml(String reportName) {

        if (reportName.isBlank()) {
            throw new ReportNameBlank("O nome do relatório não pode ser nulo");
        }

        StringBuilder pathBuilder = new StringBuilder(Constantes.REPORTS_DIRECTORY);

        return pathBuilder.append(reportName).append(Constantes.EXT_JRXML).toString();
    }

}
