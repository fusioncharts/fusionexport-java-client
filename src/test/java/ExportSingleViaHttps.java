import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportSingleViaHttps {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        em.setHostAndPort("127.0.0.1", 1337, true);
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "src/test/resources/static2/resources/single.json");

        String[] files = em.export(config, ".", true);

        System.out.println("Done");
    }
}
