import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", "filepath_template.html");
        config.set("chartConfig", "filepath_multiple.json");

        em.export(config,"outPath",false);
    }
}

