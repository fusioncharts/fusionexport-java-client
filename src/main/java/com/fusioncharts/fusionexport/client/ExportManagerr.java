package com.fusioncharts.fusionexport.client;

public class ExportManagerr {

    private ExportConfig config;
    private ExportDoneListener exportDoneListener;
    private ExportStateChangedListener exportStateChangedListener;

    public static class Config{

        private ExportConfig chartConfig;
        private ExportDoneListener exportDoneListener = null;
        private ExportStateChangedListener exportStateChangedListener = null;

        public Config(ExportConfig config){
            this.chartConfig = config;
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
        this.exportFusionCharts();
    }

    public void exportFusionCharts(){

    }
}
