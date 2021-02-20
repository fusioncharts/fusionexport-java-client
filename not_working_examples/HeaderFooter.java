package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class HeaderFooter {
    public static final String HOME_DIR = "header-footer/";
    public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);

    public static void main(String[] args) throws Exception {
		
        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();

        config.set("chartConfig",Resource.resolveResource(HOME_DIR+"single.json"));
        config.set("templateFilePath", Resource.resolveResource(HOME_DIR+"template.html"));
        config.set("templateFormat", "A4");
        config.set("headerEnabled", "true");
        config.set("footerEnabled", "true");
        config.set("headerComponents", "{ \"title\": { \"style\": \"color:blue;\" } }");
        config.set("footerComponents", "{ \"pageNumber\": { \"style\": \"color:green;\" } }");
        
        config.set("type", "pdf");
		
        String[] files = em.export(config, OUT_DIR, true);
        		
        Test.done("HeaderFooter");
    }
}

