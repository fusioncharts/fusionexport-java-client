package com.fusioncharts.fusionexport.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class HttpConnectionManager {
    private CloseableHttpClient client;
    private MultipartEntityBuilder builder;
    private String url;
    
    public HttpConnectionManager() {
        initConnectionManager();
    }

    private void initConnectionManager(){
        if(client == null) {
            client = HttpClients.createDefault();
        }
        if(builder == null) {
            builder = MultipartEntityBuilder.create();
        }
    }

    private HttpPost createPostReq(String url){
    	this.url = url;
    	return new HttpPost(url);
    }

    public void addReqParam(String key, String value){
        builder.addTextBody(key, value);
    }

    public  void addZipFile(String key , InputStream file){
        builder.addBinaryBody(key, file, ContentType.create("application/zip"), Constants.DEFAULT_PAYLOAD_NAME);
    }

    private HttpPost generatePostRequest(String url){
        HttpEntity requestParams = builder.build();
        HttpPost request =  createPostReq(url);
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
        catch (HttpHostConnectException e) {
        	throw new ExportException("Connection Refused: Unable to connect to FusionExport server. Make sure that your server is running on " + this.url);
        }
        catch (IOException | ExportException e){
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

    public byte[] executeRequest(String url) throws ExportException {
    	this.url = url;
    	initConnectionManager();
        return getResponse(generatePostRequest(url));
    }

}
