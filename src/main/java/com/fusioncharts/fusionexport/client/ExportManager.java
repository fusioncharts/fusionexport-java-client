package com.fusioncharts.fusionexport.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

public class ExportManager {

    private ExportConfig chartConfig;
    private String host = "";
    private int port = Integer.MIN_VALUE;
    private String outDir = "";
    private boolean unzip = false;

    public ExportManager() throws ExportException {

    }

    private void createRequest() throws ExportException {
        try {
            this.chartConfig.createRequest();
        } catch (Exception e) {

            throw new ExportException("Error in Config" + "\n");
        }
    }

    public void setHostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String[] export(ExportConfig config, String outDir, boolean unzip) throws ExportException {
        this.chartConfig = config;
        this.outDir = outDir;
        this.unzip = unzip;
        String[] filepaths;
        try {
            createRequest();
            filepaths = exportChart();
        } catch (ExportException e) {
            throw new ExportException(e);
        } finally {
            try {
                Files.delete(Paths.get(Constants.TEMP_REQUEST_PAYLOAD));
            } catch (IOException e) {
                throw new ExportException(e.getMessage());
            }
        }
        return filepaths;
    }

    private String[] exportChart() {
        Exporter exporter = new Exporter(chartConfig);
        String[] filePaths = new String[0];
        if (exporter != null) {
            this.host = !this.host.isEmpty() ? this.host : Constants.DEFAULT_HOST;
            this.port = this.port != Integer.MIN_VALUE ? this.port : Constants.DEFAULT_PORT;
            exporter.setExportConnectionConfig(this.host, this.port);
            try {
                filePaths = saveResponse(exporter.start());
            } catch (ExportException e) {
                System.out.println(e.getMessage());
            }
        }
        return filePaths;
    }

    private String[] saveResponse(byte[] response) throws ExportException {
        ArrayList<String> fileList = new ArrayList<>();
        try {
            if (!unzip) {
                String path = outDir + File.separator + Constants.EXPORT_FILE_NAME;
                Files.write(new File(path).toPath(), response);
                fileList.add(outDir.concat(Constants.EXPORT_FILE_NAME));

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
