import com.fusioncharts.fusionexport.client.*; // import sdk

import java.io.IOException;

import static com.fusioncharts.fusionexport.client.ExportManager.amazon_s3_upload;

public class amazons3_upload {
    public static void main(String[] args) throws Exception {

        String configPath = "fullPath/resources/static2/resources/single.json";

        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", configPath);

        // Instantiate the ExportManager class
        ExportManager manager = new ExportManager(config);
        // Call the export() method with the export config and the respective callbacks
        manager.export(new ExportDoneListener() {
                           @Override
                           public void exportDone(ExportDoneData result, ExportException error) throws IOException {
                               if (error != null) {
                                   System.out.println(error.getMessage());
                               } else {
                                   ExportManager.amazon_s3_upload(result,"bucket","accesskey","secretAccessKey");
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

