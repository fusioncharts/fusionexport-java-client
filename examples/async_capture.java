import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("chartConfig", "filePath_Of_Config_Json");
        config.set("callbackFilePath","filePath_Of_callbackJS");
        config.set("asyncCapture", true);

        em.export(config,"output_file_path",false);
    }
}
