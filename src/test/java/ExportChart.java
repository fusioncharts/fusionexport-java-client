import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

//        String rootPath = System.getProperty("user.dir") + java.io.File.separator;
//        String configPath = rootPath + "examples" + java.io.File.separator + "chart-config-file.json";
//        ExportConfig config = new ExportConfig();
//        config.set("chartConfig", configPath);
//        config.set("exportAsZip", "true");
//
        String chartConfigFile = "/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test/resources/static2/resources/multiple.json";

        String resourcesFile = "/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test/resources/static2/resources/resource.json";
        String templateFile =
                "/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test/resources/static2/resources/template.html";

        ExportConfig config = new ExportConfig();
        config.set("chartConfig",chartConfigFile)
                .set("templateFilePath",templateFile)
                .set("resourceFilePath",resourcesFile)
                .set("dashboardLogo", "/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test/resources/static2/resources/logo.jpg")
                .set("callbackFilePath", "/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test/resources/static2/resources/callback.js");

        ExportManager manager = new ExportManager();
        manager.export(config,"/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test",false);

    }
}

