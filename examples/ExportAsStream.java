import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.fusioncharts.fusionexport.client.*; // import sdk

public class ExportAsStream {
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
        //config.set("chartConfig", "chart-config.json");
        config.set("chartConfig",chartConf.toString());
        config.set("type", "png");
		
        HashMap<String, ByteArrayOutputStream> files = em.exportAsStream(config);        
        
        for (String key : files.keySet()) {
        	// key contains the file name
            String fileName = key;
            // value has the exported binary data for that particular key/file
            ByteArrayOutputStream baos = files.get(key);

            File newFile = new File("./" + fileName);
    		new File(newFile.getParent()).mkdirs();
    		FileOutputStream fos = new FileOutputStream(newFile);
    		fos.write(baos.toByteArray());
    		fos.flush();
    		fos.close();
        }
		
        System.out.println("Done");
    }
}

