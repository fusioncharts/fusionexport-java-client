/*
package com.fusioncharts.fusionexport.client;

import java.io.IOException;
import java.util.ArrayList;

public class ExportManagerr {

    private ExportConfig config;
    private ExportDoneListener exportDoneListener = null;
    private ExportStateChangedListener exportStateChangedListener=null;
    private String host=null;
    private int port=Integer.MIN_VALUE;

    public static class Config{

        private ExportConfig chartConfig;
        private ExportDoneListener exportDoneListener = null;
        private ExportStateChangedListener exportStateChangedListener = null;
        private String host="";
        private int port = Integer.MIN_VALUE;

        public Config(ExportConfig config){
            this.chartConfig = config;
        }
        public Config addHostAndPort(String host,int port){
            this.host = host;
            this.port = port;
            return  this;
        }

        public Config addExportDoneListener(ExportDoneListener exportDoneListener){
            this.exportDoneListener = exportDoneListener;
            return this;
        }

        public Config addExportStateChangedListener(ExportStateChangedListener exportStateChangedListener){
            this.exportStateChangedListener = exportStateChangedListener;
            return this;
        }

        public ExportManagerr export(){
            return new ExportManagerr(this);
        }
    }

    private ExportManagerr(Config config){
        this.config = config.chartConfig;
        this.exportDoneListener = config.exportDoneListener;
        this.exportStateChangedListener = config.exportStateChangedListener;
        this.host = !config.host.isEmpty() ? config.host : Constants.DEFAULT_HOST;
        this.port =  config.port!=Integer.MIN_VALUE ? config.port : Constants.DEFAULT_PORT;
        this.exportFusionCharts();
    }

    public void exportFusionCharts(){
        Exporter exporter=null;
        if(exportStateChangedListener ==null && exportDoneListener==null)
            exporter = new Exporter(config);
        else if(exportStateChangedListener !=null && exportDoneListener==null)
            exporter = new Exporter(config,exportStateChangedListener);
        else if(exportStateChangedListener ==null && exportDoneListener!=null)
            exporter = new Exporter(config,exportDoneListener);
        else
            exporter = new Exporter(config,exportDoneListener,exportStateChangedListener);

        if(exporter!=null) {
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
    public static ArrayList<String>  getExportedFileNames(ExportDoneData exportedOutput) {
        ArrayList<String> fileNames = new ArrayList<>();
        if(exportedOutput.data.length != 0) {
            for (ExportData data : exportedOutput.data) {
                fileNames.add(data.realName);
            }
        }
        return  fileNames;
    }


}
*/
