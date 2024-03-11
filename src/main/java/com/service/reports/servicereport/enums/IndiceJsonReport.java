package com.service.reports.servicereport.enums;

public enum IndiceJsonReport {
    PARAMS("params"),
    DADOS_REPORT("dadosReport");

    private final String valor;

    IndiceJsonReport(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

}
