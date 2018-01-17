import com.fusioncharts.fusionexport.client.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExportChart {
    public static void main(String[] args) throws Exception{



        String chartConfigFile = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-java-client/src/test/resources/static2/resources/multiple.json";
        String svgFile = "static/sample.svg";
        String resourcesFile = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-java-client/src/test/resources/static2/resources/resource.json";
        String templateFile =
                "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-java-client/src/test/resources/static2/resources/template.html";
        String logo ="/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-java-client/src/test/resources/static2/resources/sdffds.jpg";

        ExportConfig config = new ExportConfig();
        config//.set("chartConfig",chartConfigFile)
                .set("templateFilePath",templateFile)
                .set("resourceFilePath",resourcesFile);
                //.set("dashboardHeading","Ujjal")
                //.set("dashboardSubheading","Dutta");


        ExportManager manager = new ExportManager(config);
        manager.export(new ExportDoneListener() {
            @Override
            public void exportDone(ExportDoneData result, ExportException error) {
                if (error != null) {
                    System.out.println(error.getMessage());
                } else {
                    ExportManager.saveExportedFiles("/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-php-client/example/kk", result);
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

