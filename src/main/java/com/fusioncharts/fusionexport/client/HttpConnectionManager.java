package com.fusioncharts.fusionexport.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpConnectionManager {
    private CloseableHttpClient client;
    private MultipartEntityBuilder builder;
    private String url;
    
    private String exportServerHost;
    private int exportServerPort;
    private boolean exportServerIsSecure;
    private String exportServerProtocol;

    public void setExportConnectionConfig(String exportProtocol, String exportServerHost, int exportServerPort) {
        this.exportServerHost = exportServerHost;
        this.exportServerPort = exportServerPort;
        this.exportServerIsSecure = !exportProtocol.isEmpty() && exportProtocol == Constants.SECURED_PROTOCOL? true : false;
        if (this.exportServerIsSecure)
            setExportServerProtocol(Constants.SECURED_PROTOCOL);
        else
            setExportServerProtocol(Constants.UNSECURED_PROTOCOL);
    }

    public void setExportConnectionConfig(String exportServerHost, int exportServerPort, boolean exportServerIsSecure) {
        this.exportServerHost = exportServerHost;
        this.exportServerPort = exportServerPort;
        this.exportServerIsSecure = exportServerIsSecure;
        if (exportServerIsSecure)
            setExportServerProtocol(Constants.SECURED_PROTOCOL);
        else
            setExportServerProtocol(Constants.UNSECURED_PROTOCOL);
    }

    public String getExportServerProtocol() {
        return this.exportServerProtocol;
    }
    public void setExportServerProtocol(String exportServerProtocol) {
        this.exportServerProtocol = exportServerProtocol;
    }
    
    public String getExportServerHost() {
        return this.exportServerHost;
    }

    public int getExportServerPort() {
        return this.exportServerPort;
    }
    
    public HttpConnectionManager() {
        initConnectionManager();
    }

    private void initConnectionManager(){
        if(client == null) {
            try {
                client = HttpClients.custom()
                        .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(builder == null) {
            builder = MultipartEntityBuilder.create();
        }
    }

    private HttpGet createGetReq() throws ExportException{
        this.url = createURL();
        return new HttpGet(url);
    }

    private HttpPost createPostReq() throws ExportException{
    	this.url = createURL();
    	return new HttpPost(url);
    }

    private String createURL() throws ExportException {
        URL url;

        try {
                url = new URL(getExportServerProtocol(), getExportServerHost(),
                    getExportServerPort(), Constants.DEFAULT_EXPORT_API);
	            return url.toString();
        } catch (MalformedURLException e) {
            throw new ExportException("URL params not correct");
        }
    }
    
    public void addReqParam(String key, String value){
        builder.addTextBody(key, value);
    }

    public  void addZipFile(String key , InputStream file){
        builder.addBinaryBody(key, file, ContentType.create("application/zip"), Constants.DEFAULT_PAYLOAD_NAME);
    }

    private HttpPost generatePostRequest() throws ExportException{
        HttpEntity requestParams = builder.build();
        HttpPost request;
        if(getExportServerProtocol() == Constants.SECURED_PROTOCOL){
            HttpGet getReq = createGetReq();
            try {
                client.execute(getReq);
            }
            catch (Exception e) {
                String msg = "HTTPS server not found, overriding requests to an HTTP server";

                Logger logger = Logger.getLogger(HttpConnectionManager.class.getName());
                logger.setLevel(Level.WARNING);
                logger.warning(msg);

                setExportServerProtocol(Constants.UNSECURED_PROTOCOL);
            }
        }
        request =  createPostReq();
        request.setEntity(requestParams);

        return request;
    }

    private byte[] getResponse(HttpPost request) throws ExportException {
        CloseableHttpResponse response ;
        try
        {
          response  = client.execute(request);
          if(response.getStatusLine().getStatusCode() == 200){
              HttpEntity entity = response.getEntity();
             return EntityUtils.toByteArray(entity);

          }
          else {
        	  String responseString = new BasicResponseHandler().handleEntity(response.getEntity());
        	  String msg = new JsonParser().parse(responseString).getAsJsonObject().get("error").toString();

              throw new ExportException("Server Error - " + msg);
          }
        }
        catch (HttpHostConnectException | UnknownHostException e) {
        	throw new ExportException("Connection Refused: Unable to connect to FusionExport server. Make sure that your server is running on "+ this.getExportServerProtocol() + "://" + this.getExportServerHost() + ":" + this.getExportServerPort());
        }
        catch (IOException | ExportException e){
           if(e.getClass() == ClientProtocolException.class){
               throw new ExportException("Connection Refused: Unable to connect to FusionExport server. Make sure that your server is running on "+ this.getExportServerProtocol() + "://" + this.getExportServerHost() + ":" + this.getExportServerPort());
           }
            throw new ExportException(e);
        }
        finally {
            try {
                client.close();
                client = null;
                builder = null;
            } catch (IOException e) {

                throw new ExportException(e.getMessage());
            }
        }
    }

    public byte[] executeRequest() throws ExportException {
    	initConnectionManager();
        return getResponse(generatePostRequest());
    }

}
