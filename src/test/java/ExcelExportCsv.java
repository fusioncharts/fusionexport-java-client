import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExcelExportCsv {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "src/test/resources/static2/resources/single.json");
        config.set("type", "csv");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}

