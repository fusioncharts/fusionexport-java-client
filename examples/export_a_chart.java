import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        String configPath = "fullPath/resources/static2/resources/multiple.json";
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", configPath);


        ExportManager manager = new ExportManager(config);
        manager.export(new ExportDoneListener() {
                           @Override
                           public void exportDone(ExportDoneData result, ExportException error) {
                               if (error != null) {
                                   System.out.println(error.getMessage());
                               } else {
                                   ExportManager.saveExportedFiles("fullPath", result);
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

