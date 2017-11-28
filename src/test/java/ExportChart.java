import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fusioncharts.fusionexport.client.*;

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {

    public static void main(String[] args) {
        ExportChart ec = new ExportChart();

        String chartConfigFile = "fullpath/of/scrollchart.json";
        String exportServerIP = "127.0.0.1"; // The IP address of export server
        int exportServerPort = 1337; // The Port of export server

        // The export configurations used by export server
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", ExportChart.readResourceFile("chart-config.json"));
        config.set("exportAsZip", "false");
        System.out.println(config.getFormattedConfigs());

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

    private static String readResourceFile(String resourceName) {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
            ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024 * 8];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                outBuffer.write(data, 0, nRead);
            }
            outBuffer.flush();
            return new String(outBuffer.toByteArray(), "UTF-8");
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
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