import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportLocalFontFromCssOnly {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "src/test/resources/static2/resources/chart-config-file.json");
        config.set("templateFilePath", "src/test/resources/static2/resources/localfont-template.html");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}