import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportLocalFontWithMinify {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        em.setMinifyResources(true);
        config.set("chartConfig", "./resources/export_local_font/chart-config-file.json");
        config.set("templateFilePath", "./resources/export_local_font/dashboard-template.html");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}
