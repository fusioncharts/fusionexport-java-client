import com.fusioncharts.fusionexport.client.*; // import sdk


public class ExportChart {
    public static void main(String[] args) throws Exception {

        String chartConfig = "fullPath/resources/static/scrollchart.json";
        String localJS = "fullPath/resources/static/expand_scroll.js";

        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", chartConfig);
        config.set("callbackFilePath", localJS);
        config.set("asyncCapture", "true");

        // Instantiate the ExportManager class
        ExportManager manager = new ExportManager(config);
        // Call the export() method with the export config and the respective callbacks
        manager.export(new ExportDoneListener() {
                           @Override
                           public void exportDone(ExportDoneData result, ExportException error) {
                               if (error != null) {
                                   System.out.println(error.getMessage());
                               } else {
                                   ExportManager.saveExportedFiles("fullpath", result);
                               }
                           }
                       },
                new ExportStateChangedListener() {
                    @Override
                    public void exportStateChanged(ExportState state) {
                        System.out.println("STATE: " + state.reporter);
                    }
                });
    }
}

