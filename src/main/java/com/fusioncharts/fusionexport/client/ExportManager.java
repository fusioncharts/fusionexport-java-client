package com.fusioncharts.fusionexport.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;


public class ExportManager {

    private ExportConfig chartConfig;
    private String host= "";
    private int port = Integer.MIN_VALUE;
    private boolean isSecure = Constants.IS_SECURE;
    private String outDir = "";
    private Boolean unzip = false;
    private boolean minifyFiles = false;

    public ExportManager(Boolean minifyFiles) throws ExportException {
        this.minifyFiles = minifyFiles;
    }

    public ExportManager() throws ExportException {
        this(false);
    }

    private void createRequest() throws ExportException {
        try {
            this.chartConfig.createRequest(this.minifyFiles);
        } catch (Exception e) {

            throw new ExportException("Error in Config" + "\n");
        }
    }

    public void setHostAndPort(String host, int port, boolean isSecure) {
        this.host = host;
        this.port = port;
        this.isSecure = isSecure;
    }

    public String[] convertResultToBase64String(String[] result) throws IOException {
    	ArrayList<String> fileList = new ArrayList<>();
    	
    	for(String file : result) {
    		fileList.add(Utils.fileToBase64(file));
    	}
    	
    	return fileList.toArray(new String[0]);
    }
    
    public String[] export(ExportConfig config, String outDir, boolean unzip) throws ExportException {
        this.chartConfig = config;
        this.outDir = outDir;
        this.unzip = unzip;
        String[] filepaths;
        try {
            createRequest();
            filepaths = (String[])exportChart(false);
        } catch (ExportException e) {
            throw new ExportException(e);
        } finally {
            try {
                Files.delete(Paths.get(Constants.TEMP_REQUEST_PAYLOAD));
            } catch (IOException e) {
                throw new ExportException(e);
            }
        }
        return filepaths;
    }

    @SuppressWarnings("unchecked")
	public HashMap<String, ByteArrayOutputStream> exportAsStream(ExportConfig config) throws ExportException {
        this.chartConfig = config;
        //this.outDir = outDir;
        //this.unzip = unzip;
        HashMap<String, ByteArrayOutputStream> streams = null;
        
        try {
            createRequest();
            streams = (HashMap<String, ByteArrayOutputStream>)exportChart(true);
        } catch (ExportException e) {
            throw new ExportException(e);
        } finally {
            try {
                Files.delete(Paths.get(Constants.TEMP_REQUEST_PAYLOAD));
            } catch (IOException e) {
                throw new ExportException(e);
            }
        }
        return streams;
    }
    
    
    private Object exportChart(boolean exportAsStream) {
        Exporter exporter = new Exporter(chartConfig);
        //String[] filePaths = new String[0];
        Object returnObject = null;
        if (exporter != null) {
            this.host = !this.host.isEmpty() ? this.host : Constants.DEFAULT_HOST;
            this.port = this.port != Integer.MIN_VALUE ? this.port : Constants.DEFAULT_PORT;
            exporter.setExportConnectionConfig(this.host, this.port, this.isSecure);
            try {
            	if (!exportAsStream) {
            		returnObject = saveResponse(exporter.start());
            	} else {
            		returnObject = Utils.unzipToStream(exporter.start());
            	}
            	
            	
            } catch (ExportException e) {
                System.out.println(e.getMessage());
            }
        }
        return returnObject;
    }

    private String[] saveResponse(byte[] response) throws ExportException {
        ArrayList<String> fileList = new ArrayList<>();
        try {
            if (!unzip) {
                String path = outDir + File.separator + Constants.EXPORT_FILE_NAME;
                Files.write(new File(path).toPath(), response);
                fileList.add(path);

            } else {
                fileList = Utils.unzip(new ByteArrayInputStream(response), outDir);
            }

        } catch (ExportException e) {
            throw e;
        } catch (IOException e) {
            throw new ExportException(e.getMessage());
        }
        return fileList.toArray(new String[0]);
    }
}
