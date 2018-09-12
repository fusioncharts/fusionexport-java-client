package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    public static String getResourcePath(String resourceName) throws IOException {
        return ClassLoader.getSystemClassLoader().getResource(resourceName).getPath();
    }

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

    public static String  getBase64EncodedString(String data){
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String getBase64ForZip(String path) throws ExportException {
        File originalFile = new File(path);
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            byte[] bytes = new byte[(int)originalFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = new String(Base64.getEncoder().encode(bytes));
        } catch (FileNotFoundException e) {
            throw new ExportException(e);
        } catch (IOException e) {
            throw new ExportException(e);
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
        return !path.trim().matches("^http(s)?:\\\\/");
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

    public static void getAndSaveDecodedFile(String path,String base64string) throws ExportException {
        try{
            Path pathToFile = Paths.get(path);
            if(!Files.exists(pathToFile)) {
                Files.createDirectories(pathToFile.getParent());
                Files.createFile(pathToFile);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(pathToFile.toString());
            byte[] result = Base64.getDecoder().decode(base64string);
            fileOutputStream.write(result);
            fileOutputStream.close();
        }catch (IOException e){
            throw new ExportException(e);
        }

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
}
