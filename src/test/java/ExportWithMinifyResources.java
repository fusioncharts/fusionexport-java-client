import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportWithMinifyResources {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        em.setMinifyResources(true);
        config.set("chartConfig", "src/test/resources/static2/resources/chart-config-file.json");
        config.set("templateFilePath", "src/test/resources/static2/resources/dashboard-template-minify.html");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}
