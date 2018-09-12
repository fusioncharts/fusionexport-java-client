package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.gson.*;
import org.apache.tools.ant.DirectoryScanner;

public class ResourceReader {
    private final Map<String,String> payloadFiles;
    private String resourcePath =null;
    private String basePath = null;
    private String relativeTemplatePath=null;
    private String templatePath = null;
    String [] includedFilePath;
    ArrayList<String> extractedTemplatePath =null;

    public ResourceReader(String resourcePath,String templatePath ,ArrayList<String> extractedTemplatePath,Map<String,String> payloadFiles){
        this.resourcePath = resourcePath == null ? "" : resourcePath;
        this.templatePath = templatePath;
        this.extractedTemplatePath = extractedTemplatePath;
        this.payloadFiles = payloadFiles;
    }

    private void parseResourceJSON()throws Exception {
        ResourceJSon resourceJSon = null;
        Gson gson = new Gson();
        String jsonContents = Utils.getFileContentAsString(resourcePath);
        resourceJSon  = gson.fromJson(jsonContents,ResourceJSon.class);
        if(resourceJSon != null) {
        scanDirectory(resourceJSon);
        basePath = resourceJSon.basePath !=null && !resourceJSon.basePath.isEmpty() ? resourceJSon.basePath : null;
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

    private String prepareDataForZip() throws ExportException {
        ArrayList<String> allPaths = new ArrayList<>();
        ArrayList<String> allRelativePaths = new ArrayList<>();
        allPaths.addAll(extractedTemplatePath);
        if(includedFilePath !=null) {
            for (String path : includedFilePath) {
                allPaths.add(path);
            }
        }
        basePath = basePath !=null && !basePath.isEmpty() ? basePath : getLongestCommonPrefix(allPaths.toArray(new String[0]));
        if(!Utils.isAbsolute(basePath)) {
            try {
                URI a = new URI(resourcePath);
                URI b = new URI(basePath);
                basePath = a.resolve(b).toString();
            }catch (Exception e){
                throw new ExportException("Resource Path or BasePath is not valid");
            }
        }

        if(!basePath.isEmpty()) {
            ArrayList<String> temp = new ArrayList<>();

            int listSize =allPaths.size();
            for (int i = 0; i < listSize; i++) {
                if (Utils.pathWithinBasePath(allPaths.get(i), basePath)) {
                    temp.add(allPaths.get(i));
                }
            }
            for (int i = 0; i < allPaths.size(); i++) {
                if (allPaths.get(i).equalsIgnoreCase(basePath)) {
                    allRelativePaths.add(basePath);
                } else {
                    if (Utils.pathWithinBasePath(allPaths.get(i), basePath))
                        allRelativePaths.add(allPaths.get(i).substring(basePath.length(), allPaths.get(i).length()));
                }
            }
            if(temp.isEmpty()) {
                allRelativePaths.clear();
                temp.add(this.templatePath);
                relativeTemplatePath = Utils.getFile(this.templatePath).getName();
                allRelativePaths.add(Utils.getFile(this.templatePath).getName());
            }

            return generateBase64ZIP(temp,allRelativePaths);
        }

        return null;

    }



    private String generateBase64ZIP(ArrayList<String> allpath,ArrayList<String> allpath2) throws ExportException {
        try {
            Map<String,Boolean> processedPaths = new HashMap<>();
            String tempPath = Constants.TEMP_REQUEST_PAYLOAD;
            FileOutputStream fout = new FileOutputStream(tempPath);
            ZipOutputStream zout = new ZipOutputStream(fout);
            //put all non-template files

            for (Map.Entry<String,String> fileEntry : payloadFiles.entrySet()){
                String fileName = fileEntry.getKey();
                File file = Utils.getFile(fileEntry.getValue());
                if(fileName.equalsIgnoreCase(Constants.INPUTSVG)){
                    fileName = Constants.INPUTSVG.concat(".svg");
                }else if(fileName.equalsIgnoreCase(Constants.DASHBOARDLOGO)){
                    fileName = Constants.DASHBOARDLOGO.concat(Utils.getFileExtension(file));
                }else if(fileName.equalsIgnoreCase(Constants.CALLBACKS)){
                    fileName = Constants.DEFAULT_CALLBACKFILE_NAME;
                }else if(fileName.equalsIgnoreCase(Constants.OUTPUTFILEDEFINITION)){
                    fileName = Constants.DEFAULT_OUTPUTDEF_NAME;
                }
                addToZipFile(file.toPath(),fileName, zout);
            }
            // put all template dependant files
            for(int i=0 ;i<allpath.size();i++)
            {
                File f = Utils.getFile(allpath.get(i));
                if(!processedPaths.containsKey(allpath.get(i))) {

                    String relativePath = allpath2.get(i).substring(0, allpath2.get(i).length() - f.getName().length()) + f.getName();
                    if (relativePath.equalsIgnoreCase(basePath)) {
                        addToZipFile(f.toPath(), Constants.DEFAULT_TEMPLATE_FOLDER.concat(f.getName()), zout);
                    } else {
                        addToZipFile(f.toPath(), Constants.DEFAULT_TEMPLATE_FOLDER.concat(relativePath), zout);
                    }
                    processedPaths.put(allpath.get(i), true);
                }
            }
            zout.close();
            fout.close();

            String payLoadZipPath = Utils.resolvePath(tempPath);
            //new File(tempPath).delete();
            return payLoadZipPath;
        }catch (Exception e){
            throw new ExportException(e);
        }

    }

    public String getRelativeTemplatePath(String templatePath){
        if(relativeTemplatePath == null){
            relativeTemplatePath = templatePath.substring(basePath.length(), templatePath.length());
            if (relativeTemplatePath.length() == 0) {
                relativeTemplatePath = Paths.get(templatePath).getFileName().toString();
            }
        }
        return relativeTemplatePath;
    }

    public String processForZip() throws Exception {
        if(!resourcePath.isEmpty()){
            parseResourceJSON();
        }
        return prepareDataForZip();

    }

    private void addToZipFile(Path file, String relativePath,ZipOutputStream zipStream) {
        String inputFileName = file.toFile().getPath();
        try (FileInputStream inputStream = new FileInputStream(inputFileName)) {
            ZipEntry entry = new ZipEntry(relativePath);
            zipStream.putNextEntry(entry);
            byte[] readBuffer = new byte[2048];
            int amountRead;
            int written = 0;

            while ((amountRead = inputStream.read(readBuffer)) > 0) {
                zipStream.write(readBuffer, 0, amountRead);
                written += amountRead;
            }
        }
        catch(IOException e) {

        }
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
