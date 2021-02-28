package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class ExportMinify {
	public static final String HOME_DIR = "minify-resources/";
	public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);

    public static void main(String[] args) throws Exception {
    	//Add true as first parameter to ExportManager if you want to minify your files
        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();
        em.setMinifyResources(true);
        config.set("templateFilePath", Resource.resolveResource(HOME_DIR+"dashboard-template-minify.html"));
        config.set("chartConfig", Resource.resolveResource(HOME_DIR+"chart-config-file.json"));

        em.export(config,OUT_DIR,true);

        Test.done("ExportMinify");
    }
}

