import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("chartConfig", "filepath_multiple.json");
        config.set("outputFile", "export-<%= number(5) %>");


        em.export(config,"outPath",false);
    }
}

