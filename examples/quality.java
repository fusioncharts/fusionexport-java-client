import com.fusioncharts.fusionexport.client.*;

public class quality {
    public static void main(String[] args) throws Exception {

        String rootPath = System.getProperty("user.dir") + java.io.File.separator;
        String configPath = rootPath + "examples" + java.io.File.separator + "chart-config-file.json";
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", configPath);
        config.set("quality", "best");

        ExportManager manager = new ExportManager(config);
        manager.export(new ExportDoneListener() {
                           @Override
                           public void exportDone(ExportDoneData result, ExportException error) {
                               if (error != null) {
                                   System.out.println(error.getMessage());
                               } else {
                                   ExportManager.saveExportedFiles(rootPath + "bin" + java.io.File.separator + "static2" + java.io.File.separator + "resources", result);
                               }
                           }
                       },
                new ExportStateChangedListener() {
                    @Override
                    public void exportStateChanged(ExportState state) {
                        System.out.println("STATE: " + state.customMsg);
                    }
                });

    }
}

