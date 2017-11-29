import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fusioncharts.fusionexport.client.*; // import sdk

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {

    public static void main(String[] args) {

        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", readResourceFile("chart-config.json"));
        config.set("exportAsZip", "false");

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Call the export() method with the export config and the respective callbacks
        Exporter exporter = em.export(config, new ExportChart(), new ExportChart());
    }

    @Override // Called when export is done
    public void exportDone(String result, ExportException error) {
        if (error != null) {
            System.out.println(error.getMessage());
        } else {
            System.out.println("DONE: " + result);
        }
    }

    @Override // Called on each export state change
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
}