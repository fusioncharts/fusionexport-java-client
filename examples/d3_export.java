import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();

        config.set("templateFilePath", "filePath_of_template_d3.html");
        config.set("type", "jpg");
        config.set("asyncCapture", true);

        em.export(config,"output_file_path",false);
    }
}
