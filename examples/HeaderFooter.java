import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Dictionary;
import java.util.HashMap;

import com.fusioncharts.fusionexport.client.*; // import sdk

public class HeaderFooter {
    public static void main(String[] args) throws Exception {
	
		String chartConf = "__FULL_PATH_TO__/dashboard_charts.json"; 	
		String templateFilePath = "__FULL_PATH_TO__/template.html";
		
        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();

        config.set("chartConfig",chartConf);
        config.set("templateFilePath", templateFilePath );
        config.set("templateFormat", "A4");
        config.set("headerEnabled", "true");
        config.set("footerEnabled", "true");
        config.set("headerComponents", "{ \"title\": { \"style\": \"color:blue;\" } }");
        config.set("footerComponents", "{ \"pageNumber\": { \"style\": \"color:green;\" } }");
        
        config.set("type", "pdf");
		
        String[] files = em.export(config, ".", true);
        		
        System.out.println("Done");
    }
}

