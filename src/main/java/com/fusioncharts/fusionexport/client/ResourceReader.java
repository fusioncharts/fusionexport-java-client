package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.gson.*;
import org.apache.tools.ant.DirectoryScanner;

public class ResourceReader {
    private String resourcePath =null;
    private String basePath = null;
    private String relativeTemplatePath=null;
    String [] includedFilePath;
    ArrayList<String> extractedTemplatePath =null;

    public ResourceReader(String path,ArrayList<String> extractedTemplatePath){
        this.resourcePath = path;
        this.extractedTemplatePath = extractedTemplatePath;
    }

    private void parseResourceJSON()throws Exception {
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
            scanDirectory(resourceJSon);
            basePath = resourceJSon.basePath;
        }
    }

    private void scanDirectory(ResourceJSon resourceJSon){
        String[] tempFilename;
        int i=0;
        String baseDirectory = Utils.getDirectory(resourcePath);
        DirectoryScanner ds = new DirectoryScanner();
        if(resourceJSon.getIncludes().length == 0)
            return;
        ds.setIncludes(resourceJSon.getIncludes());
        if(resourceJSon.getExcludes().length !=0)
            ds.setExcludes(resourceJSon.getExcludes());
        ds.setBasedir(new File(baseDirectory));
        ds.setCaseSensitive(true);
        ds.scan();
        includedFilePath = ds.getIncludedFiles();
        tempFilename = new String[includedFilePath.length];
        for(String path:includedFilePath ){
            tempFilename[i++] = baseDirectory +"/"+ path;
        }
        includedFilePath = null;
        includedFilePath = tempFilename;
    }

    private String getLongestCommonPrefix(String[] s)
    {
        int k = s[0].length();
        for (int i = 1; i < s.length; i++)
        {
            k = Math.min(k, s[i].length());
            for (int j = 0; j < k; j++)
                if (s[i].charAt(j) != s[0].charAt(j))
                {
                    k = j;
                    break;
                }
        }
        return s[0].substring(0, k);
    }

    private String prepareDataForZip() throws IOException {
        ArrayList<String> allPaths = new ArrayList<>();
        ArrayList<String> allPaths2 = new ArrayList<>();
        allPaths.addAll(extractedTemplatePath);
        if(includedFilePath !=null) {
            for (String path : includedFilePath) {
                allPaths.add(path);
            }
        }
        basePath = basePath !=null ? basePath : getLongestCommonPrefix(allPaths.toArray(new String[0]));

        if(basePath==null){
            //TODO throw Export Array
        }

        for(int i=0;i<allPaths.size();i++){
            if(!Utils.pathWithinBasePath(allPaths.get(i),basePath)){
                allPaths.remove(i);
            }
        }
        for(int i=0;i<allPaths.size();i++){
            allPaths2.add(allPaths.get(i).substring(basePath.length(),allPaths.get(i).length()));
        }


        return generateBase64ZIP(allPaths,allPaths2);
    }



    private String generateBase64ZIP(ArrayList<String> allpath,ArrayList<String> allpath2) throws IOException {
        Map<String,Boolean> processedPaths = new HashMap<>();
        String tempPath = "temp.zip";
        FileOutputStream fout = new FileOutputStream(tempPath);
        ZipOutputStream zout = new ZipOutputStream(fout);
        for(int i=0 ;i<allpath.size();i++)
        {
            File f = Utils.getFile(allpath.get(i));
            if(!processedPaths.containsKey(allpath.get(i))) {
                ZipEntry ze = new ZipEntry(allpath2.get(i).substring(0,allpath2.get(i).length() - f.getName().length())+f.getName());
                zout.putNextEntry(ze);
                zout.closeEntry();
                processedPaths.put(allpath.get(i), true);
            }
            //Utils.writeTempFile(f);
        }
        zout.close();
        fout.close();

        String base64Zip = Utils.getBase64ForZip(tempPath);
        //new File(tempPath).delete();
        return base64Zip;
    }

    public String getRelativeTemplatePath(String templatePath){
        return relativeTemplatePath =templatePath.substring(basePath.length(),templatePath.length());
    }

    public String processForZip() throws Exception {
        if(!resourcePath.isEmpty()){
            parseResourceJSON();
        }
        return prepareDataForZip();

    }


    class ResourceJSon{
        public String basePath;
        public ArrayList<String> include = null;
        public ArrayList<String> exclude =null;

        public String[] getIncludes(){
            if(include != null)
                return include.toArray(new String[0]);
            else
                return new String[0];
        }

        public String[] getExcludes(){
            if(exclude != null)
                return exclude.toArray(new String[0]);
            else
                return new String[0];
        }
    }
}
