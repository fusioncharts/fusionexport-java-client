package com.fusioncharts.fusionexport.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class HttpConnectionManager {
    private CloseableHttpClient client;
    private MultipartEntityBuilder builder;

    public HttpConnectionManager() {
        client = HttpClients.createDefault();
        builder = MultipartEntityBuilder.create();
    }

    public HttpPost createPostReq(String url){
        return new HttpPost(url);
    }

    public void addReqParam(String key, String value){
        builder.addTextBody(key, value);
    }

    public  void addZipFile(String key , InputStream file){
        builder.addBinaryBody(key, file, ContentType.create("application/zip"), Constants.DEFAULT_PAYLOAD_NAME);
    }

    public HttpEntity generatePostRequest(String url){
        HttpEntity request = builder.build();
        createPostReq(url).setEntity(request);
        return request;
    }

    public CloseableHttpResponse getResponse(HttpPost request) throws ExportException {
        CloseableHttpResponse response ;
        try
        {
          response  = client.execute(request);
          if(response.getStatusLine().getStatusCode() == 200){
            return response;
          }
          else {
              throw new ExportException("Error in connection with code"+response.getStatusLine().getStatusCode());
          }
        }catch (IOException |ExportException e){
            throw new ExportException(e);
        }
        finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new ExportException(e.getMessage());
            }
        }
    }

}
