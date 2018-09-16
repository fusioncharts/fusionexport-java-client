import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();
        config.set("inputSVG", "fullpath_of_svg_vector.svg");
        em.export(config,"outPath",false);
    }
}

