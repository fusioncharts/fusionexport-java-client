package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class ExportLocalFont {
    public static final String HOME_DIR = "local-font/";
    public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);

    public static void main(String[] args) throws Exception {
	
        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("templateFilePath", Resource.resolveResource(HOME_DIR+"dashboard-template.html"));
        config.set("chartConfig", Resource.resolveResource(HOME_DIR+"chart-config-file.json"));
        config.set("type", "pdf");
		
        String[] files = em.export(config, OUT_DIR, true);
        
        Test.done("ExportLocalFont");
    }
}

