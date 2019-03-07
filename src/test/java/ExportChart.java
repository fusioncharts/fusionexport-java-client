import com.fusioncharts.fusionexport.client.*; // import sdk

public class ExportChart {
    public static void main(String[] args) throws Exception {
	
		StringBuilder chartConf = new StringBuilder();
		chartConf.append("[");
		chartConf.append("  {");
		chartConf.append("    \"type\": \"column2d\",");
		chartConf.append("    \"renderAt\": \"chart-container\",");
		chartConf.append("    \"width\": \"600\",");
		chartConf.append("    \"height\": \"200\",");
		chartConf.append("    \"dataFormat\": \"json\",");
		chartConf.append("    \"dataSource\": {");
		chartConf.append("      \"chart\": {");
		chartConf.append("        \"caption\": \"Number of visitors last week\",");
		chartConf.append("        \"subCaption\": \"Bakersfield Central vs Los Angeles Topanga\"");
		chartConf.append("      },");
		chartConf.append("      \"data\": [");
		chartConf.append("        {");
		chartConf.append("          \"label\": \"Mon\",");
		chartConf.append("          \"value\": \"15123\"");
		chartConf.append("        },");
		chartConf.append("        {");
		chartConf.append("          \"label\": \"Tue\",");
		chartConf.append("          \"value\": \"14233\"");
		chartConf.append("        },");
		chartConf.append("        {");
		chartConf.append("          \"label\": \"Wed\",");
		chartConf.append("          \"value\": \"25507\"");
		chartConf.append("        }");
		chartConf.append("      ]");
		chartConf.append("    }");
		chartConf.append("  }");
		chartConf.append("]");		
		
		
        // Instantiate the ExportManager class
		ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", chartConf.toString());
        config.set("type", "png");
		
        String[] files = em.export(config,".",true);
        
        for(String f : files) {
        	System.out.println(f);
        }
    }
}

