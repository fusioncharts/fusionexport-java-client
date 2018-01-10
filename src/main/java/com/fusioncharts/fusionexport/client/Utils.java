package com.fusioncharts.fusionexport.client;

import java.io.File;
import java.io.IOException;
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

    public static String resolvePath(String path){
        if(Paths.get(path).isAbsolute()){
            return path;
        }
        else{
            return Paths.get(path).toAbsolutePath().toString();
        }
    }

    public static String resolvePath(String path,String basePath){
        return Paths.get(path).getParent().resolve(basePath).normalize().toString();
    }

    public static String getDirectory(String path){
        return Paths.get(path).getParent().toString();
    }

    public static File getFile(String absolutePath){
        return new File(absolutePath);
    }

    public static boolean isValidPath(String path){
        return !path.trim().matches("^http(s)?:\\\\/");
    }
}
