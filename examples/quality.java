import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("chartConfig", "fullPath_single.json");
        config.set("quality", "best");


        em.export(config,"outPath",false);
    }
}

