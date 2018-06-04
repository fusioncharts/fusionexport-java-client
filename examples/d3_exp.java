import com.fusioncharts.fusionexport.client.*;

public class d3_exp {
    public static void main(String[] args) throws Exception {

        String rootPath = System.getProperty("user.dir") + java.io.File.separator;
        String templateRelativePath = "src\\test\\resources\\static2\\resources\\template_d3.html".replace("\\", java.io.File.separator);
        String templateAbsolutePath = rootPath + templateRelativePath;
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", templateAbsolutePath);
        config.set("type", "pdf");
        config.set("asyncCapture", true);

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


