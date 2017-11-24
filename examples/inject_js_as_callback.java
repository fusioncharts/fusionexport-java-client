import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fusioncharts.fusionexport.client.*;

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {

    public static void main(String[] args) {
        ExportChart ec = new ExportChart();

        String chartConfigFile = "fullpath/of/dashboard_charts.json";
        String exportServerIP = "127.0.0.1"; // The IP address of export server
        String exportServerPort = 1337; // The Port of export server

        // The export configurations used by export server
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", readFile(chartConfigFile));
        config.set("templateFilePath", "fullpath/of/template.html");
        config.set("callbackFilePath", "fullpath/of/callback.js");

        ExportManager em = new ExportManager(exportServerIP, exportServerPort);
        Exporter exporter = em.export(config, ec, ec);
    }

    @Override
    public void exportDone(String result, ExportException error) {
        if (error != null) {
            System.out.println(error.getMessage());
        } else {
            System.out.println("DONE: " + result);
        }
    }

    @Override
    public void exportStateChanged(String state) {
        System.out.println("STATE: " + state);
    }

    private static String readFile(String file) {
        String fileContent = "";
        try {
            File f = new File(file);
            FileInputStream inp = new FileInputStream(f);
            byte[] bf = new byte[(int) f.length()];
            inp.read(bf);
            fileContent = new String(bf, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}