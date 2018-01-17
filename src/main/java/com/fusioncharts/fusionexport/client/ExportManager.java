package com.fusioncharts.fusionexport.client;

import java.io.IOException;
import java.util.ArrayList;

public class ExportManager {

    private ExportConfig chartConfig;
    private ExportDoneListener exportDoneListener = null;
    private ExportStateChangedListener exportStateChangedListener = null;
    private String host="";
    private int port = Integer.MIN_VALUE;

    public ExportManager(ExportConfig config) throws ExportException {
        this.chartConfig = config;
        try{
            this.chartConfig.createRequest();
        }catch(Exception e){
            throw new ExportException("Error in Config"+"\n"+e.getMessage());
        }
    }

    public void setHostAndPort(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void export (){
        this.exportDoneListener = exportDoneListener;
        exportChart();
    }
    public void export (ExportDoneListener exportDoneListener){
        this.exportDoneListener = exportDoneListener;
        exportChart();
        }

    public void export(ExportStateChangedListener exportStateChangedListener){
        this.exportStateChangedListener = exportStateChangedListener;
        exportChart();
    }

    public void export (ExportDoneListener exportDoneListener,ExportStateChangedListener exportStateChangedListener){
        this.exportStateChangedListener = exportStateChangedListener;
        this.exportDoneListener = exportDoneListener;
        exportChart();
    }

    private void exportChart(){
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
            try {
                exporter.start();
            }catch (ExportException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void  saveExportedFiles(String dirPath, ExportDoneData exportedOutput)  {
        try {
            if (exportedOutput.data.length != 0) {
                for (ExportData data : exportedOutput.data) {
                    String filePath = dirPath + "/" + data.realName;
                    Utils.getAndSaveDecodedFile(filePath, data.fileContent);
                }
            }
        }
        catch (ExportException e){
            System.out.println(e.getMessage());
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
