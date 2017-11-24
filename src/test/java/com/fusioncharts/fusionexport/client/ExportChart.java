package com.fusioncharts.fusionexport.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {

    public static void main(String[] args) {
        ExportChart ec = new ExportChart();

        ExportConfig config = new ExportConfig();
        config.set("chartConfig", ExportChart.readResourceFile("chart-config.json"));

        ExportManager em = new ExportManager();
        Exporter exporter = em.export(config, ec, ec);
    }

    @Override
    public void exportDone(String result, ExportException error) {
        if(error != null) {
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
}