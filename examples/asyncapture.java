import com.fusioncharts.fusionexport.client.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        String chartConfig = "fullPath/resources/static/scrollchart.json";
        String localJS = "fullPath/resources/static/expand_scroll.js";

        ExportConfig config = new ExportConfig();
        config.set("chartConfig", chartConfig);
        config.set("callbackFilePath", localJS);
        config.set("asyncCapture", "true");

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

