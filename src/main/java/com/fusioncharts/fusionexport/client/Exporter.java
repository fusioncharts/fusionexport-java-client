package com.fusioncharts.fusionexport.client;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exporter implements ExportDataProcessor{

    private ExportDoneListener exportDoneListener;
    private ExportStateChangedListener exportStateChangedListener;
    private ExportConfig exportConfig;
    private String exportServerHost = Constants.DEFAULT_HOST;
    private int exportServerPort = Constants.DEFAULT_PORT;
    private Socket socket;
    private WebSocketManager socketManager;

    public Exporter(ExportConfig exportConfig) {
        this.exportConfig = exportConfig;
    }

    public Exporter(ExportConfig exportConfig, ExportDoneListener exportDoneListener) {
        this.exportConfig = exportConfig;
        this.exportDoneListener = exportDoneListener;
    }

    public Exporter(ExportConfig exportConfig, ExportStateChangedListener exportStateChangedListener) {
        this.exportConfig = exportConfig;
        this.exportStateChangedListener = exportStateChangedListener;
    }

    public Exporter(ExportConfig exportConfig, ExportDoneListener exportDoneListener, ExportStateChangedListener exportStateChangedListener) {
        this.exportConfig = exportConfig;
        this.exportDoneListener = exportDoneListener;
        this.exportStateChangedListener = exportStateChangedListener;
    }

    public void setExportConnectionConfig(String exportServerHost, int exportServerPort) {
        this.exportServerHost = exportServerHost;
        this.exportServerPort = exportServerPort;
    }

    public ExportConfig getExportConfig() {
        return this.exportConfig;
    }

    public ExportDoneListener getExportDoneListener() {
        return this.exportDoneListener;
    }

    public ExportStateChangedListener getExportStateChangedListener() {
        return this.exportStateChangedListener;
    }

    public String getExportServerHost() {
        return this.exportServerHost;
    }

    public int getExportServerPort() {
        return this.exportServerPort;
    }

    public void start() { Exporter.this.handleSocketConnection(); }

    public void cancel() {
        try {
            if(this.socketManager != null) {
                this.socketManager.close();
            }
        } catch(Exception exp) {
            exp.printStackTrace();
        }
    }

    private void handleSocketConnection()
    {
       try
       {
            this.socketManager = new WebSocketManager(this.exportServerHost, this.exportServerPort,this);
            String writeBuffer = this.getFormattedExportConfigs();
            socketManager.sendMessage(writeBuffer);
            System.out.println("Done");
       }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void processDataReceived(String data) {
        if(data.startsWith(Constants.EXPORT_EVENT)) {
            this.processExportStateChangedData(data);
        } else if(data.startsWith(Constants.EXPORT_DATA)) {
            this.processExportDoneData(data);
        }
    }

    private void processExportStateChangedData(String data) {
        String state = data.substring(Constants.EXPORT_EVENT.length(), data.length());
        String exportError = this.checkExportError(state);
        if (exportError == null) {
            this.onExportSateChanged(state);
        } else {
            this.onExportDone(null, new ExportException(exportError));
        }
    }

    private void processExportDoneData(String data) {
        String exportResult = data.substring(Constants.EXPORT_DATA.length(), data.length());
        this.onExportDone(exportResult, null);
    }

    private String checkExportError(String state) {
        String errorPattern = "^\\{\\s*\"error\"\\s*:\\s*\"(.+)\"\\s*}$";
        Pattern pattern = Pattern.compile(errorPattern);
        Matcher matcher = pattern.matcher(state.trim());
        if(matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private void onExportSateChanged(String state) {
        if (this.exportStateChangedListener != null) {
            this.exportStateChangedListener.exportStateChanged(state);
        }
    }

    private void onExportDone(String result, ExportException error) {
        if (this.exportDoneListener != null) {
            this.exportDoneListener.exportDone(result, error);
        }
    }

    private String getFormattedExportConfigs() {
        return String.format("%s.%s<=:=>%s", "ExportManager", "export", this.exportConfig.getFormattedConfigs());
    }

}








