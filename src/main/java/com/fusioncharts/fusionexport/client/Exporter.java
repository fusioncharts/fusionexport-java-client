package com.fusioncharts.fusionexport.client;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exporter{

    private ExportConfig exportConfig;
    private String exportServerHost = Constants.DEFAULT_HOST;
    private int exportServerPort = Constants.DEFAULT_PORT;
    private HttpConnectionManager connectionManager;

    public Exporter(ExportConfig exportConfig) {
        this.exportConfig = exportConfig;
    }



    public void setExportConnectionConfig(String exportServerHost, int exportServerPort) {
        this.exportServerHost = exportServerHost;
        this.exportServerPort = exportServerPort;
    }

    public ExportConfig getExportConfig() {
        return this.exportConfig;
    }


    public String getExportServerHost() {
        return this.exportServerHost;
    }

    public int getExportServerPort() {
        return this.exportServerPort;
    }

    public void start() throws ExportException { this.handleConnection(); }

    public void cancel() throws ExportException{

    }

    private void handleConnection() throws  ExportException
    {
       try
       {
           connectionManager = new HttpConnectionManager();
           updateRequestParams();
           connectionManager.executeRequest(createURL());

            System.out.println("Done");
       }catch (Exception e){
           throw new ExportException("Cannot establish connection to "+getExportServerHost()+" "+getExportServerPort());
        }

    }

    private String createURL() throws ExportException {
        URL url ;
        try {
            url =  new URL(Constants.DEFAULT_PROTOCAL,
                                Constants.DEFAULT_HOST,
                                Constants.DEFAULT_PORT,
                                Constants.DEFAULT_EXPORT_API);
            return url.toString();
        } catch (MalformedURLException e) {
            throw new ExportException("URL params not correct");
        }
    }

    private String checkExportError(String state) {
        String errorPattern = "^\\{\\s*\"error\"\\s*:\\s*\"(.+)\"\\s*}$";
        Pattern pattern = Pattern.compile(errorPattern);
        Matcher matcher = pattern.matcher(state.trim());
        if(matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private void updateRequestParams() throws ExportException {
        Map<String,String > requestParams =  exportConfig.getRequestParams();
        for(Map.Entry<String,String> param : requestParams.entrySet()){
            if(param.getKey().equalsIgnoreCase(Constants.PAYLOAD)){
                try {
                    connectionManager.addZipFile(param.getKey(),new FileInputStream(param.getValue()));
                } catch (FileNotFoundException e) {
                   throw new ExportException("Payload Zip not found, Error: "+e);
                }
            }else{
                connectionManager.addReqParam(param.getKey(),param.getValue());
            }
        }
    }

}








