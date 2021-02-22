import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportWithMinifyResources {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "src/test/resources/static2/resources/chart-config-file-for-big.json");
        config.set("templateFilePath", "src/test/resources/static2/resources/dashboard-big-template.html");
        //Optional
        config.set("type", "pdf");
        config.set("quality", "best");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}
