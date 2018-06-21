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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

public class ExportManager {

    private ExportConfig chartConfig;
    private ExportDoneListener exportDoneListener = null;
    private ExportStateChangedListener exportStateChangedListener = null;
    private String host = "";
    private int port = Integer.MIN_VALUE;

    public ExportManager(ExportConfig config) throws ExportException {
        this.chartConfig = config;
        try {
            this.chartConfig.createRequest();
        } catch (Exception e) {

            throw new ExportException("Error in Config" + "\n");
        }
    }

    public static void saveExportedFiles(String dirPath, ExportDoneData exportedOutput) {
        try {
            if (exportedOutput.data.length != 0) {
                for (ExportData data : exportedOutput.data) {
                    String filePath = dirPath + "/" + data.realName;
                    Utils.getAndSaveDecodedFile(filePath, data.fileContent);
                }
            }
        } catch (ExportException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<String> getExportedFileNames(ExportDoneData exportedOutput) {
        ArrayList<String> fileNames = new ArrayList<>();
        if (exportedOutput.data.length != 0) {
            for (ExportData data : exportedOutput.data) {
                fileNames.add(data.realName);
            }
        }
        return fileNames;
    }

    private static String GetBucketRegion(String bucketName, String accessKey, String secretAccessKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        // create client with a default region. in our case it is "USEAST1"
        AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        GetBucketLocationRequest request = new GetBucketLocationRequest(bucketName);
        String region = client.getBucketLocation(request);
        //Region.valueOf(region);
        return region;
    }

    // Amazon S3 Upload
    public static void amazon_s3_upload(ExportDoneData exportDoneData, String bucketName, String accessKey, String secretAccessKey) throws IOException {
        String region;
        try {
            region = GetBucketRegion(bucketName, accessKey, secretAccessKey);
        } catch (Exception ex) {
            throw ex;
        }

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        for (ExportData data : exportDoneData.data) {
            byte[] bytes = Base64.getDecoder().decode(data.fileContent);
            ByteArrayInputStream data_stream = new ByteArrayInputStream(bytes);
            data_stream.close();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            //PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, data.realName,data_stream,objectMetadata);
            s3client.putObject(bucketName, data.realName, data_stream, objectMetadata);
        }
    }

    public static void UploadFileToFTPServer(ExportDoneData exportDoneData, String userName, String password, String host, String remoteDirectory) throws IOException {
        UploadFileToFTPServer(exportDoneData, userName, password, host, 21, remoteDirectory);
    }

    public static void UploadFileToFTPServer(ExportDoneData exportDoneData, String userName, String password, String host, int port, String remoteDirectory) throws IOException {
        if (port == 0) port = 21;

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        ftpClient.login(userName, password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Adds trailing slash
        remoteDirectory = remoteDirectory.endsWith("/") ? remoteDirectory : remoteDirectory + "/";

        for (ExportData data : exportDoneData.data) {
            byte[] bytes = Base64.getDecoder().decode(data.fileContent);
            ByteArrayInputStream data_stream = new ByteArrayInputStream(bytes);
            data_stream.close();
            System.out.print("Uploading file to " + remoteDirectory + data.realName + "...");
            boolean done = ftpClient.storeFile(remoteDirectory + data.realName, data_stream);
            if (done) {
                System.out.println("done.");
            }
        }
    }

    public void setHostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void export() {
        this.exportDoneListener = exportDoneListener;
        exportChart();
    }

    public void export(ExportDoneListener exportDoneListener) {
        this.exportDoneListener = exportDoneListener;
        exportChart();
    }

    public void export(ExportStateChangedListener exportStateChangedListener) {
        this.exportStateChangedListener = exportStateChangedListener;
        exportChart();
    }

    public void export(ExportDoneListener exportDoneListener, ExportStateChangedListener exportStateChangedListener) {
        this.exportStateChangedListener = exportStateChangedListener;
        this.exportDoneListener = exportDoneListener;
        exportChart();
    }

    private void exportChart() {
        Exporter exporter = null;
        if (exportStateChangedListener == null && exportDoneListener == null)
            exporter = new Exporter(chartConfig);
        else if (exportStateChangedListener != null && exportDoneListener == null)
            exporter = new Exporter(chartConfig, exportStateChangedListener);
        else if (exportStateChangedListener == null && exportDoneListener != null)
            exporter = new Exporter(chartConfig, exportDoneListener);
        else
            exporter = new Exporter(chartConfig, exportDoneListener, exportStateChangedListener);

        if (exporter != null) {
            this.host = !this.host.isEmpty() ? this.host : Constants.DEFAULT_HOST;
            this.port = this.port != Integer.MIN_VALUE ? this.port : Constants.DEFAULT_PORT;
            exporter.setExportConnectionConfig(this.host, this.port);
            try {
                exporter.start();
            } catch (ExportException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
