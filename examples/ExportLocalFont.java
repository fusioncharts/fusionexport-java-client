import com.fusioncharts.fusionexport.client.ExportConfig;
import com.fusioncharts.fusionexport.client.ExportManager;

public class ExportLocalFont {
    public static void main(String[] args) throws Exception {

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", "./resources/export_local_font/chart-config-file.json");
        config.set("templateFilePath", "./resources/export_local_font/dashboard-template.html");

        String[] files = em.export(config, ".", true);

        /*
        HashMap<String, ByteArrayOutputStream> files = em.exportAsStream(config);
        for (String key : files.keySet()) {
            String fileName = key;
            ByteArrayOutputStream baos = files.get(key);

            File newFile = new File("./" + fileName);
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }
        */

        System.out.println("Done.");
    }
}
