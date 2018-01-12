import com.fusioncharts.fusionexport.client.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExportChart {
    public static void main(String[] args) throws Exception{



        String chartConfigFile = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/chart-config.json";
        String svgFile = "static/sample.svg";
        String resourcesFile = "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/static/resources.json";
        String templateFile =
                "/Users/ujjaldutta/Documents/FusionChartsWorks/FusionExport/JavaSDKExport/FusionExportJavaSDK/src/test/resources/static/html/template.html";

        ExportConfigg exportConfigg = new ExportConfigg.ConfigBuilder()
                                        .addConfig("templateFilePath",templateFile)
                                        .addConfig("resourceFilePath",resourcesFile)
                .addConfig("type","jpg")
                                        .build();



        ExportManagerr err = new ExportManagerr.Config(exportConfigg).addExportDoneListener(new ExportDoneListener() {
            @Override
            public void exportDone(ExportDoneData result, ExportException error) {
                if (error != null) {
                    System.out.println(error.getMessage());
                } else {
                    System.out.println("DONE: " + result.data[0].realName);
                }
            }
        }).addExportStateChangedListener(new ExportStateChangedListener() {
            @Override
            public void exportStateChanged(ExportState state) {
                System.out.println("STATE: " + state.reporter);
            }
        }).export();
    }

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
        }
    }


}

