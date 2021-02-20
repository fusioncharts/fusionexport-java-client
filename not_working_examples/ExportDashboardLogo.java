package com.example.examples;

import com.example.utils.Resource;
import com.example.utils.Test;

import com.fusioncharts.fusionexport.client.*;

public class ExportDashboardLogo {
    public static final String HOME_DIR = "dashboard-logo/";
    public static final String OUT_DIR = Resource.exampleOutput(HOME_DIR);
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", Resource.resolveResource(HOME_DIR+"template.html"));
        config.set("chartConfig", Resource.resolveResource(HOME_DIR+"multiple.json"));
        config.set("callbackFilePath", Resource.resolveResource(HOME_DIR+"callback.js"));
        config.set("dashboardLogo", Resource.resolveResource(HOME_DIR+"logo.jpg"));
        config.set("dashboardHeading", "FusionCharts");
        config.set("dashboardSubheading", "The best charting library in the world");

        em.export(config,OUT_DIR,false);

        Test.done("ExportDashboardLogo");
    }
}

