package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class ExportSinglePdf {
    public static final String HOME_DIR = "single-pdf/";
    public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);
    public static void main(String[] args) throws Exception {
        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("chartConfig", Resource.resolveResource(HOME_DIR+"single.json"));
        config.set("type", "pdf");

        em.export(config,OUT_DIR,false);

        Test.done("ExportSinglePdf");
    }
}

