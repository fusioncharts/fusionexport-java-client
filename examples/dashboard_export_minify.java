import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {
    	//Add true as first parameter to ExportManager if you want to minify your files
        ExportManager em = new ExportManager(true);
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", "filepath_template.html");
        config.set("chartConfig", "filepath_multiple.json");

        em.export(config,"outPath",false);
    }
}

