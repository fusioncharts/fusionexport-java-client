package com.fusioncharts.fusionexport.client;

public interface ExportDataProcessor {

    void processDataReceived(String data) ;
    String getExportServerHost();
    int getExportServerPort();
}
