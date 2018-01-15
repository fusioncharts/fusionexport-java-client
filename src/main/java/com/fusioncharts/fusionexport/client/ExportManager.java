package com.fusioncharts.fusionexport.client;

import java.io.IOException;
import java.util.ArrayList;

public class ExportManager {

    private ExportConfig chartConfig;
    private ExportDoneListener exportDoneListener = null;
    private ExportStateChangedListener exportStateChangedListener = null;
    private String host="";
    private int port = Integer.MIN_VALUE;

    public ExportManager(ExportConfig config){
        this.chartConfig = config;
    }
    public void setHostAndPort(String host, int port){
        this.host = host;
        this.port = port;

    }

    public ExportManager (ExportConfig config ,ExportDoneListener exportDoneListener){
        this.exportDoneListener = exportDoneListener;
        this.chartConfig = config;

    }

    public ExportManager (ExportConfig config,ExportStateChangedListener exportStateChangedListener){
        this.exportStateChangedListener = exportStateChangedListener;
        this.chartConfig = config;
    }

    public ExportManager (ExportConfig config,ExportDoneListener exportDoneListener,ExportStateChangedListener exportStateChangedListener){
        this.exportStateChangedListener = exportStateChangedListener;
        this.chartConfig = config;
        this.exportDoneListener = exportDoneListener;
    }

    public void export(){
        Exporter exporter=null;
        if(exportStateChangedListener ==null && exportDoneListener==null)
            exporter = new Exporter(chartConfig);
        else if(exportStateChangedListener !=null && exportDoneListener==null)
            exporter = new Exporter(chartConfig,exportStateChangedListener);
        else if(exportStateChangedListener ==null && exportDoneListener!=null)
            exporter = new Exporter(chartConfig,exportDoneListener);
        else
            exporter = new Exporter(chartConfig,exportDoneListener,exportStateChangedListener);

        if(exporter!=null) {
            this.host = !this.host.isEmpty()  ? this.host : Constants.DEFAULT_HOST;
            this.port =  this.port!=Integer.MIN_VALUE ? this.port : Constants.DEFAULT_PORT;
            exporter.setExportConnectionConfig(this.host, this.port);
            exporter.start();
        }
    }

    public static void  saveExportedFiles(String dirPath, ExportDoneData exportedOutput) throws IOException {
        if(exportedOutput.data.length != 0) {
            for (ExportData data : exportedOutput.data) {
                String filePath = dirPath+"/"+data.realName;
                Utils.getAndSaveDecodedFile(filePath,data.fileContent);
            }
        }
    }
    public static ArrayList<String> getExportedFileNames(ExportDoneData exportedOutput) {
        ArrayList<String> fileNames = new ArrayList<>();
        if(exportedOutput.data.length != 0) {
            for (ExportData data : exportedOutput.data) {
                fileNames.add(data.realName);
            }
        }
        return  fileNames;
    }


}
