
import com.fusioncharts.fusionexport.client.*;

public class defaulttemplate {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "src/test/java/test/multiple-chart-config-file.json");
        config.set("header", "src/test/java/test/dashboard-template.html");
        config.set("subheader","-template.html");
//        config.set("chartConfig",chartConf.toString());
//        config.set("type", "pdf");

        String[] files = em.export(config, ".",true,true);



        System.out.println("Done");
    }
}
