import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        String chartConfig = "fullpath/resources/static/bulk.json";

        ExportConfig config = new ExportConfig();
        config.set("chartConfig", chartConfig);


        ExportManager manager = new ExportManager(config);
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

