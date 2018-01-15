package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Utils {

    public static String getResourcePath(String resourceName){
        return ClassLoader.getSystemClassLoader().getResource(resourceName).getPath();
    }

    public static String getFileContentAsString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static String getBase64EncodedString(String data){
        return Base64.getEncoder().encode(data.getBytes()).toString();
    }

    public static String getBase64ForZip(String path){
        File originalFile = new File(path);
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            byte[] bytes = new byte[(int)originalFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = new String(Base64.getEncoder().encode(bytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  encodedBase64;
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
        return Paths.get(basePath).getParent().resolve(path).normalize().toString();
    }

    public static String getDirectory(String path){
        return Paths.get(path).getParent().toString();
    }

    public static File getFile(String absolutePath) {


        File file = new File(absolutePath);
        return file;
    }

    public static void writeTempFile(File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.flush();
        fw.close();
    }

    public static boolean isValidPath(String path){
        return !path.trim().matches("^http(s)?:\\\\/");
    }

    public static boolean pathWithinBasePath(String path,String basePath){
        return Paths.get(path).startsWith(Paths.get(basePath).getParent());
    }
    public static String getRelativePath(String filepath,String basePath){
        return Paths.get(basePath).relativize(Paths.get(filepath)).toString();
    }
    public static boolean isAbsolutePath(String path){
        return Paths.get(path).isAbsolute();
    }

    public static void getAndSaveDecodedFile(String path,String base64string) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(Utils.resolvePath(path));
        byte[] result = Base64.getDecoder().decode(base64string);
        fileOutputStream.write(result);
        fileOutputStream.close();

    }
}
