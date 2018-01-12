import com.fusioncharts.fusionexport.client.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {
    public static void main(String[] args) throws Exception{



        String chartConfigFile = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/chart-config.json";
        String svgFile = "static/sample.svg";
        String resourcesFile = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/static/resources.json";
        String templateFile =
                "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/static/html/template.html";

        ExportConfigg exportConfigg = new ExportConfigg.ConfigBuilder()
                                        .addConfig("templateFilePath",templateFile)
                                        .addConfig("resourceFilePath",resourcesFile)
                                        .build();



        ExportManagerr err = new ExportManagerr.Config(exportConfigg).addExportDoneListener(new ExportDoneListener() {
            @Override
            public void exportDone(String result, ExportException error) {
                if (error != null) {
                    System.out.println(error.getMessage());
                } else {
                    System.out.println("DONE: " + result);
                }
            }
        }).addExportStateChangedListener(new ExportStateChangedListener() {
            @Override
            public void exportStateChanged(String state) {
                System.out.println("STATE: " + state);
            }
        }).export();

        // Instantiate the ExportConfig class and add the required configurations
        //ConfigValidator.readMetadata();
        /*ExportConfig config = new ExportConfig();
        config.set("chartConfig", readResourceFile("chart-config.json"));

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Call the export() method with the export config and the respective callbacks
        em.export(config, new ExportDoneListener() {
            public void exportDone(String result, ExportException error) {
                if (error != null) {
                    System.out.println(error.getMessage());
                } else {
                    System.out.println("DONE: " + result);
                }
            }
        }, new ExportChart());


        ExportManagerr err = new ExportManagerr.Config(config).addExportDoneListener(new ExportDoneListener() {
            @Override
            public void exportDone(String result, ExportException error) {

            }
        }).addExportStateChangedListener(new ExportStateChangedListener() {
            @Override
            public void exportStateChanged(String state) {

            }
        }).export();*/

        String path = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/static/html/template.html";
        new ExportConfigg.ConfigBuilder().getTemplate(path);


    }

    // Called when export is done
    public void exportDone(String result, ExportException error) {

    }

    // Called on each export state change
    public void exportStateChanged(String state) {
        System.out.println("STATE: " + state);
    }

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
        }
    }


}

