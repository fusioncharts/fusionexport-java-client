package com.fusioncharts.fusionexport.client;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exporter {

    private ExportConfig exportConfig;
    private String exportServerHost;
    private int exportServerPort;
    private String exportServerProtocol;
    
    private HttpConnectionManager connectionManager;

    public Exporter(ExportConfig exportConfig) {
        this.exportConfig = exportConfig;
    }

    public void setExportConnectionConfig(String exportProtocol, String exportServerHost, int exportServerPort) {
        this.exportServerHost = exportServerHost;
        this.exportServerPort = exportServerPort;
        this.exportServerProtocol = exportProtocol;
    }

    public ExportConfig getExportConfig() {
        return this.exportConfig;
    }

    public String getExportServerProtocol() {
        return this.exportServerProtocol;
    }
    
    public String getExportServerHost() {
        return this.exportServerHost;
    }

    public int getExportServerPort() {
        return this.exportServerPort;
    }

    public byte[] start() throws ExportException {
        return handleConnection();
    }

    private byte[] handleConnection() throws ExportException {
        byte[] result;
        try {
            connectionManager = new HttpConnectionManager();
            connectionManager.setExportConnectionConfig(getExportServerProtocol(), getExportServerHost(), getExportServerPort());
            updateRequestParams();
            result = connectionManager.executeRequest();
        } catch (ExportException e) {
            throw new ExportException(e);
        }
        return result;

    }

    private void updateRequestParams() throws ExportException {
        Map<String, String> requestParams = exportConfig.getRequestParams();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            if (param.getKey().equalsIgnoreCase(Constants.PAYLOAD)) {
                try {
                    connectionManager.addZipFile(param.getKey(), new FileInputStream(param.getValue()));
                } catch (FileNotFoundException e) {
                    throw new ExportException("Payload Zip not found, Error: " + e);
                }
            } else {
                connectionManager.addReqParam(param.getKey(), param.getValue());
            }
        }
    }

}








