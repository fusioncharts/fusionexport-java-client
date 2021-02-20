import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportUsingDefaultTemplate {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "src/test/resources/static2/resources/default_template.json");
        config.set("templateFormat", "A4");
        config.set("header", "My Header");
        config.set("subheader", "My Subheader");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}
