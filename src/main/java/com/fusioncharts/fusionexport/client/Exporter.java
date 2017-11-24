package com.fusioncharts.fusionexport.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exporter {

    private ExportDoneListener exportDoneListener;
    private ExportStateChangedListener exportStateChangedListener;
    private ExportConfig exportConfig;
    private String exportServerHost = Constants.DEFAULT_HOST;
    private int exportServerPort = Constants.DEFAULT_PORT;
    private Socket socket;
    private Thread socketConnectionThread;

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

    public void start() {
        this.socketConnectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Exporter.this.handleSocketConnection();
            }
        });
        this.socketConnectionThread.start();
    }

    public void cancel() {
        try {
            if(this.socket != null) {
                this.socket.close();
            }
        } catch(Exception exp) {
            exp.printStackTrace();
        }
    }

    private void handleSocketConnection() {
        try {
            this.socket = new Socket(this.exportServerHost, this.exportServerPort);
            OutputStream os = this.socket.getOutputStream();
            InputStream is = this.socket.getInputStream();

            byte[] writeBuffer = this.getFormattedExportConfigs().getBytes("UTF-8");
            os.write(writeBuffer, 0, writeBuffer.length);
            os.flush();

            byte[] readBuffer = new byte[1024 * 16];
            String dataReceived = "";
            int read = 0;
            while((read = is.read(readBuffer, 0, readBuffer.length)) > -1) {
                dataReceived += new String(readBuffer, 0, read, "UTF-8");
                dataReceived = this.processDataReceived(dataReceived);
            }
        } catch (Exception exp) {
            this.onExportDone(null, new ExportException(exp.getMessage()));
        } finally {
            if(this.socket != null) {
                try {
                    this.socket.close();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
    }

    private String processDataReceived(String data) {
        String[] parts = data.split(Pattern.quote(Constants.UNIQUE_BORDER), -1);
        for(int i = 0; i<parts.length - 1; i++) {
            String part = parts[i];
            if(part.startsWith(Constants.EXPORT_EVENT)) {
                this.processExportStateChangedData(part);
            } else if(part.startsWith(Constants.EXPORT_DATA)) {
                this.processExportDoneData(part);
            }
        }
        return parts[parts.length - 1];
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
        this.exportStateChangedListener.exportStateChanged(state);
    }

    private void onExportDone(String result, ExportException error) {
        this.exportDoneListener.exportDone(result, error);
    }

    private String getFormattedExportConfigs() {
        return String.format("%s.%s<=:=>%s", "ExportManager", "export", this.exportConfig.getFormattedConfigs());
    }

}








