package com.fusioncharts.fusionexport.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.*;
import org.apache.tools.ant.DirectoryScanner;

public class ResourceReader {
    private String resourcePath =null;
    private String basePath = null;
    String [] includedFilePath;

    public ResourceReader(String path){
        this.resourcePath = path;
    }

    public void parseResourceJSON()throws Exception {
        ResourceJSon resourceJSon = null;
        try {
            Gson gson = new Gson();
            String jsonContents = Utils.getFileContentAsString(resourcePath);
            resourceJSon  = gson.fromJson(jsonContents,ResourceJSon.class);

        }
        catch(IOException e){
            throw new Exception("Resource file not found");
        }

        if(resourceJSon != null) {
            basePath = resourceJSon.basePath;
            scanDirectory(resourceJSon);
        }
    }

    public void scanDirectory(ResourceJSon resourceJSon){
        DirectoryScanner ds = new DirectoryScanner();
        ds.setIncludes(resourceJSon.getIncludes());
        ds.setExcludes(resourceJSon.getExcludes());
        ds.setBasedir(new File(Utils.getDirectory(resourcePath)));
        ds.setCaseSensitive(true);
        ds.scan();
        includedFilePath = ds.getIncludedFiles();
    }

    class ResourceJSon{
        public String basePath;
        public ArrayList<String> include;
        public ArrayList<String> exclude;

        public String[] getIncludes(){
            return include.toArray(new String[0]);
        }

        public String[] getExcludes(){
            return exclude.toArray(new String[0]);
        }
    }
}
