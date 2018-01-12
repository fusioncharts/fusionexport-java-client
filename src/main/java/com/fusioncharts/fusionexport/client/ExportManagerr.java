package com.fusioncharts.fusionexport.client;

public class ExportManagerr {

    private ExportConfigg config;
    private ExportDoneListener exportDoneListener = null;
    private ExportStateChangedListener exportStateChangedListener=null;
    private String host=null;
    private int port=Integer.MIN_VALUE;

    public static class Config{

        private ExportConfigg chartConfig;
        private ExportDoneListener exportDoneListener = null;
        private ExportStateChangedListener exportStateChangedListener = null;
        private String host="";
        private int port = Integer.MIN_VALUE;

        public Config(ExportConfigg config){
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
}
