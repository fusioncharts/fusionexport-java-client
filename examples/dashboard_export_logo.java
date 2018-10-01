import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", "filepath_template.html");
        config.set("chartConfig", "filepath_multiple.json");
        config.set("callbackFilePath", "filepath_callback.js");
        config.set("dashboardLogo", "filepath_logo.jpg");
        config.set("dashboardHeading", "FusionCharts");
        config.set("dashboardSubheading", "The best charting library in the world");

        em.export(config,"outPath",false);
    }
}

