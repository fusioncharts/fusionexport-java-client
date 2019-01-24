package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Base64;

public class Utils {

    public  static String getResourceContentAsString(String resourceName) throws IOException {
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(resourceName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        isr.close();
        is.close();
        return sb.toString();
    }

    public static String getFileContentAsString(String path) throws ExportException{
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        }catch (Exception e){
            throw new ExportException(e);
        }
    }

    public static String resolvePath(String path){
        if(Paths.get(path).isAbsolute()){
            return path;
        }
        else{
            return Paths.get(path).toAbsolutePath().toString();
        }
    }

    public static String resolvePath(String path,String basePath){
        if (path.startsWith("http://") || path.startsWith("https://") || path.startsWith("file://")) {
            return path;
        }
        return Paths.get(basePath).getParent().resolve(path).normalize().toString();
    }

    public static String getDirectory(String path){
        return Paths.get(path).getParent().toString();
    }

    public static File getFile(String absolutePath) {


        File file = new File(absolutePath);
        return file;
    }


    public static boolean isValidPath(String path){
        return !path.trim().matches("^http(s)?:\\/\\/.*");
    }

    public static boolean isAbsolute(String path){
        return Paths.get(path).isAbsolute();
    }

    public static boolean pathWithinBasePath(String path,String basePath){
        if(Files.isDirectory(Paths.get(basePath)))
            return Paths.get(path).startsWith(Paths.get(basePath));
        else
            return Paths.get(path).startsWith(Paths.get(basePath).getParent());
    }

    public static String getFileExtension(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }

    public static ArrayList<String> unzip(ByteArrayInputStream zipFileStream, String destDir) throws ExportException {
        File dir = new File(destDir);
        ArrayList<String> filePaths = new ArrayList<>();
        if(!dir.exists()) dir.mkdirs();

        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(zipFileStream);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                filePaths.add(newFile.getAbsolutePath());
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
           throw new ExportException(e);
        }
    return filePaths;
    }
    
    public static String fileToBase64(String fileName) throws IOException {
    	byte[] fileContents = Files.readAllBytes(new File(fileName).toPath());
        //File file = new File(fileName);
        byte[] encoded = Base64.encodeBase64(fileContents);
        return new String(encoded, StandardCharsets.US_ASCII);
    }
    
}
