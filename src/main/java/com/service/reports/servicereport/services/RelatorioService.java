package com.service.reports.servicereport.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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

    public byte[] generateReportWithParams(String reportName, String jsonData) throws Exception {

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
            JsonNode apenasDadosDoReport = getDadosReport(jsonData);
            InputStream jsonInputStream = new ByteArrayInputStream(apenasDadosDoReport.toString().getBytes());

            // Cria um dataSource a partir do JSON
            JsonDataSource jsonDataSource = new JsonDataSource(jsonInputStream);

            // Parâmetros do relatório (se necessário)
            Map<String, Object> parameters = getAtributosFromJson(jsonData);

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

    public Map<String, Object> getAtributosFromJson(String jsonString) throws Exception {
        // Criar um objeto ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Converter o JSON em um nó JsonNode
        JsonNode jsonNode = getParamnsReport(jsonString);

        // Verificar se o nó é um objeto
        if (jsonNode.isObject()) {
            // Criar um mapa para armazenar os nomes dos atributos e seus valores
            Map<String, Object> atributos = new HashMap<>();

            // Iterar sobre os campos do objeto JSON
            Iterator<Map.Entry<String, JsonNode>> campos = jsonNode.fields();
            while (campos.hasNext()) {
                Map.Entry<String, JsonNode> campo = campos.next();
                String nomeAtributo = campo.getKey();
                JsonNode valorAtributo = campo.getValue();
                atributos.put(nomeAtributo, valorAtributo.asText()); // Armazenar o valor como texto (pode ser ajustado conforme necessário)
            }

            return atributos;
        }

        // Se o nó não for um objeto, pode retornar um mapa vazio ou lançar uma exceção, dependendo do seu requisito.
        return Collections.emptyMap();
    }

    public static JsonNode getDadosReport(String jsonString) throws Exception {
        // Criar um objeto ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Converter o JSON em um nó JsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Obter o nó com a chave "dadosReport"
        JsonNode dadosReportNode = jsonNode.path("dadosReport");

        return dadosReportNode;
    }

    public static JsonNode getParamnsReport(String jsonString) throws Exception {
        // Criar um objeto ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Converter o JSON em um nó JsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Obter o nó com a chave "dadosReport"
        JsonNode dadosReportNode = jsonNode.path("params");

        return dadosReportNode;
    }
}
