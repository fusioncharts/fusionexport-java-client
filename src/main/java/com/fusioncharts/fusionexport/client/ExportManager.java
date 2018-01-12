/*
package com.fusioncharts.fusionexport.client;

public class ExportManager {

    private String host;
    private int port;

    public ExportManager() {
        this.host = Constants.DEFAULT_HOST;
        this.port = Constants.DEFAULT_PORT;
    }

    public ExportManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public Exporter export(ExportConfig exportConfig) {
        Exporter exporter = new Exporter(exportConfig);
        exporter.setExportConnectionConfig(this.host, this.port);
        exporter.start();
        return exporter;
    }

    public Exporter export(ExportConfig exportConfig, ExportDoneListener exportDoneListener) {
        Exporter exporter = new Exporter(exportConfig, exportDoneListener);
        exporter.setExportConnectionConfig(this.host, this.port);
        exporter.start();
        return exporter;
    }

    public Exporter export(ExportConfig exportConfig, ExportStateChangedListener exportStateChangedListener) {
        Exporter exporter = new Exporter(exportConfig, exportStateChangedListener);
        exporter.setExportConnectionConfig(this.host, this.port);
        exporter.start();
        return exporter;
    }

    public Exporter export(ExportConfig exportConfig, ExportDoneListener exportDoneListener, ExportStateChangedListener exportStateChangedListener) {
        Exporter exporter = new Exporter(exportConfig, exportDoneListener, exportStateChangedListener);
        exporter.setExportConnectionConfig(this.host, this.port);
        exporter.start();
        return exporter;
    }
}
*/
