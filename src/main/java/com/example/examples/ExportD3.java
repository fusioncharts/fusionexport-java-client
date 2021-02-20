package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class ExportD3 {

	public static final String HOME_DIR = "export-d3/";
	public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);

    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", Resource.resolveResource(HOME_DIR+"template_d3.html"));
        config.set("type", "jpg");
        config.set("asyncCapture", true);

        em.export(config,OUT_DIR,false);
    }
}
