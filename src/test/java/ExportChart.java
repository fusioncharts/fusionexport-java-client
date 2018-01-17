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
        String logo ="/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-java-client/src/test/resources/static2/resources/kdhfhjf.jpg";

        ExportConfig config = new ExportConfig();
        config.set("chartConfig",chartConfigFile)
                .set("templateFilePath",templateFile)
                .set("resourceFilePath",resourcesFile)
                .set("dashboardLogo",logo)
                .set("dashboardHeading","")
                .set("dashboardSubHeading","")
                .createRequest();


        ExportManager manager = new ExportManager(config);
        manager.export(new ExportDoneListener() {
            @Override
            public void exportDone(ExportDoneData result, ExportException error) {
                if (error != null) {
                    System.out.println(error.getMessage());
                } else {
                    try {
                        ExportManager.saveExportedFiles("/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/fusionexport-java-client", result);
                    }catch (IOException e){
                        System.out.print("Path not valid");
                    }
                }
            }
        },
                new ExportStateChangedListener() {
                    @Override
                    public void exportStateChanged(ExportState state) {
                        System.out.println("STATE: " + state.reporter);
                    }
                });


        /*ExportConfigg exportConfigg = new ExportConfigg.ConfigBuilder()
                .addConfig("chartConfig",chartConfigFile)
                                       .addConfig("templateFilePath",templateFile)
                                        .addConfig("resourceFilePath",resourcesFile)
                //.addConfig("type","jpg")
                                        .build();



        ExportManagerr err = new ExportManagerr.Config(exportConfigg).addExportDoneListener(new ExportDoneListener() {
            @Override
            public void exportDone(ExportDoneData result, ExportException error) {
                if (error != null) {
                    System.out.println(error.getMessage());
                } else {
                    try {
                        ExportManagerr.saveExportedFiles("/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK", result);
                    }catch (IOException e){
                        System.out.print("Path not valid");
                    }
                }
            }
        }).addExportStateChangedListener(new ExportStateChangedListener() {
            @Override
            public void exportStateChanged(ExportState state) {
                System.out.println("STATE: " + state.reporter);
            }
        }).export();


    // Called when export is done

    private static String readResourceFile(String resourceName) {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
            ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024 * 8];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                outBuffer.write(data, 0, nRead);
            }
            outBuffer.flush();
            return new String(outBuffer.toByteArray(), "UTF-8");
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }*/
    }


}

