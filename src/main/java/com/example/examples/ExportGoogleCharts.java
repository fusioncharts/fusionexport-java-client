package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class ExportGoogleCharts {
    public static final String HOME_DIR = "google-charts/";
    public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);

    public static void main(String[] args) throws Exception {
	
        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("templateFilePath",Resource.resolveResource(HOME_DIR+"template_googlecharts.html"));
        config.set("type", "jpg");
        config.set("asyncCapture", true);
		
        String[] files = em.export(config, OUT_DIR, true);
        
        Test.done("ExportGoogleCharts");
    }
}

