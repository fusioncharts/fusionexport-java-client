import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", "path_of_template.html");
        config.set("chartConfig", "path_of_multiple.json");
        config.set("callbackFilePath", "path_of_callback.js");

        em.export(config,"output_path",false);
    }
}

