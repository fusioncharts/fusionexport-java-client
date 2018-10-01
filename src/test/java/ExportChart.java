import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        ExportManager em = new ExportManager();
        ExportConfig config = new ExportConfig();
        config.set("inputSVG", "/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/examples/resources/vector.svg");
        em.export(config,"/Users/ujjal/Documents/fusioncharts/fusionexport/fusionexport-java-client/src/test/resources",false);
    }
}

