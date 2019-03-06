import com.fusioncharts.fusionexport.client.*; // import sdk

public class ExportChart {
    public static void main(String[] args) throws Exception {

	
        String configPath = "fullpath/resources/static2/resources/multiple.json";
		configPath = "D:\\_Work@FusionCharts\\FusionExport_Code\\fusionexport-java-client\\examples\\resources\\multiple.json";
        String templatePath ="fullpath/resources/static2/resources/template.html";
		templatePath = "D:\\_Work@FusionCharts\\FusionExport_Code\\fusionexport-java-client\\examples\\resources\\template.html";

        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
		
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", configPath);
        config.set("templateFilePath", templatePath);
        config.set("type", "pdf");
        config.set("templateFormat", "A4");
		
        String[] files = em.export(config,".",true);
        
        for(String f : files) {
        	System.out.println(f);
        }
    }
}

