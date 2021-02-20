
package com.fusioncharts.fusionexport.client;
import java.awt.font.NumericShaper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;

import com.google.gson.JsonParser;

import org.apache.http.util.TextUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fusioncharts.fusionexport.client.Constants.*;
import com.fusioncharts.fusionexport.client.Minifier;


public class ExportConfig {

    private Map<String, DataValue> configAttributes = null;
    private Map<String, String> payloadEntries = null;
    private ArrayList<String> templateFileRef = null;
    private Map<String, String> requestParams = null;


    public ExportConfig() throws Exception {
        configAttributes = new HashMap<>();
        requestParams = new HashMap<>();
        payloadEntries = new HashMap<>();
        ConfigValidator.readMetadata();
    }

    public ExportConfig set(String configName, Object value) throws ExportException {

        try {

            ArrayList<String> supportedTypes = ConfigValidator.getConfigMetaDataSupportedTypes(configName);

            if (supportedTypes.size() > 1 || supportedTypes.contains("enum") || supportedTypes.contains("file")) {
                String converterName = ConfigValidator.getConfigMetaDataConvertor(configName).toLowerCase();
            		/*
            		if (converterName.equalsIgnoreCase("booleanconverter")) {
            			booleanConverter(configName, value);            			
            		} else if (converterName.equalsIgnoreCase("numberconverter")) {
            			numberConverter(configName, value);
            		} else if (converterName.equalsIgnoreCase("chartconfigconverter")) {
            			chartConfigConverter(configName, value);
            		} else if (converterName.equalsIgnoreCase("enumconverter")) {
            			ArrayList<String> dataset = ConfigValidator.getConfigMetaDataDataSet(configName);
            			enumConverter(configName, value, dataset);
            		}
            		*/
                switch (converterName) {
                    case "booleanconverter":
                        booleanConverter(configName, value);
                        break;
                    case "numberconverter":
                        numberConverter(configName, value);
                        break;
                    case "chartconfigconverter":
                        chartConfigConverter(configName, value);
                        break;
                    case "fileconverter":
                        fileConverter(configName, value);
                        break;
                    case "enumconverter":
                        ArrayList<String> dataset = ConfigValidator.getConfigMetaDataDataSet(configName);
                        enumConverter(configName, value, dataset);
                        break;
                }
            } else {
                showTemplateWarning(configName);
                configAttributes.put(configName, new DataValue<String>(value.toString()));
            }

        } catch (Exception e) {
            if (e.getMessage() == null) {
                throw new ExportException("Config:" + configName + " not valid");
            } else {
                throw new ExportException(e);
            }
        }
        return this;
    }

    public void booleanConverter(String configName, Object configValue) throws ExportException {
        if (configValue.getClass().getSimpleName().equals(Boolean.class.getSimpleName())) {
            configAttributes.put(configName, new ExportConfig.DataValue<Boolean>((Boolean) configValue));
        } else if (configValue.getClass().getSimpleName().equals(String.class.getSimpleName())) {
            configAttributes.put(configName, new ExportConfig.DataValue<Boolean>(Boolean.parseBoolean(configValue.toString())));
        } else {
            throw new ExportException(configName + ": Data should be a String or Boolean");
        }
    }

    public void numberConverter(String configName, Object configValue) throws ExportException {
        if (configValue.getClass().getSimpleName().equals(Integer.class.getSimpleName())) {
            configAttributes.put(configName, new ExportConfig.DataValue<Integer>((Integer) configValue));
        } else if (configValue.getClass().getSimpleName().equals(String.class.getSimpleName())) {
            configAttributes.put(configName, new ExportConfig.DataValue<Integer>(Integer.parseInt(configValue.toString())));
        } else {
            throw new ExportException(configName + ": Data should be a String or Integer");
        }
    }

    public void chartConfigConverter(String configName, Object configValue) throws ExportException, IOException {

        if (!configValue.getClass().getSimpleName().equals(String.class.getSimpleName())) {
            throw new ExportException(configName + ": Data should be a String");
        }

        String valueStr = configValue.toString();

        if (valueStr.toLowerCase().endsWith(".json")) {
            if (Files.notExists(Paths.get(valueStr))) {
                throw new ExportException(configName + ": File not found. Please provide an appropriate path.");
            }
            // Read the file content and convert to string
            valueStr = new String(Files.readAllBytes(Paths.get(valueStr)));
        }

        if (!isValidJson(valueStr)) {
            throw new ExportException(configName + ": JSON structure is invalid. Please check your JSON data.");
        }

        configAttributes.put(configName, new ExportConfig.DataValue<String>(valueStr));
    }

    public void fileConverter(String configName, Object configValue) throws ExportException, IOException {

        if (!configValue.getClass().getSimpleName().equals(String.class.getSimpleName())) {
            throw new ExportException(configName + ": Data should be a String");
        }

        String valueStr = configValue.toString();

        if (Files.notExists(Paths.get(valueStr))) {
            throw new ExportException(configName + ": File not found. Please provide an appropriate path.");
        }

        showTemplateWarning(configName);
        configAttributes.put(configName, new DataValue<String>(configValue.toString()));
    }

    public void enumConverter(String configName, Object configValue, ArrayList<String> dataset) throws ExportException {

        if (!configValue.getClass().getSimpleName().equals(String.class.getSimpleName())) {
            throw new ExportException(configName + ": Data should be a String");
        }

        String valueStr = configValue.toString();
        Boolean dataExists = false;

        for (String ds : dataset) {
            if (ds.toLowerCase().equals(valueStr.toLowerCase())) {
                dataExists = true;
                break;
            }
        }

        if (!dataExists) {
            String supportParams = org.apache.commons.lang3.StringUtils.join(", ", dataset);
            String errMsg = String.format("Invalid argument value in parameter '%s'\nSupported parameters are: %s", configName.toString(), supportParams);
            throw new ExportException(errMsg);
        }

        configAttributes.put(configName, new ExportConfig.DataValue<String>(valueStr));
    }


    public Object get(String configName) {
        if (configAttributes.containsKey(configName)) {
            return configAttributes.get(configName).data;
        }
        return null;
    }

    public Boolean has(String configName) {
        return configAttributes.containsKey(configName);
    }

    public Boolean remove(String configName) {
        if (configAttributes.containsKey(configName)) {
            configAttributes.remove(configName);
            return true;
        }
        return false;
    }

    public void clear() {
        configAttributes.clear();
    }

    private void showTemplateWarning(String configName) {
        if (configName.equals("template") || configName.equals("templateFilePath")) {
            if (configAttributes.containsKey("templateFilePath") || configAttributes.containsKey("template")) {
                System.out.println("WARNING: Both 'templateFilePath' and 'template' is provided. 'templateFilePath' will be ignored.");
            }
        }
    }

    class DataValue<T> {
        T data;

        DataValue(T data) {
            this.data = data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    private static Boolean isValidJson(String jsonString) {
        jsonString = jsonString.trim();
        if ((jsonString.startsWith("{") && jsonString.endsWith("}")) || //For object
                (jsonString.startsWith("[") && jsonString.endsWith("]"))) //For array
        {
            try {
                new JsonParser().parse(jsonString);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public void createRequest(boolean exportBulk, Boolean minifyResources) throws Exception {

        //set Client Name
        requestParams.put(CLIENTNAME, CLIENTNAME_VALUE);

        requestParams.put(PLATFORM, System.getProperty("os.name"));
        if (exportBulk)
        {
            requestParams.put("exportBulk","true");
        }
        else {
            requestParams.put("exportBulk","false");
        }
        //set Chart Config
        if (configAttributes.containsKey(CHARTCONFIG)) {
            String chartConfig = (String) configAttributes.get(CHARTCONFIG).getData();
            if(!chartConfig.isEmpty() && chartConfig.endsWith(".json")){
                chartConfig = new JsonParser().parse(Utils.getFileContentAsString(Utils.resolvePath(chartConfig))).toString();
            }
            requestParams.put(CHARTCONFIG, chartConfig);
        }

        //set SVG Config
        if (configAttributes.containsKey(INPUTSVG)) {
            String svg = (String) configAttributes.get(INPUTSVG).getData();
            if (!svg.isEmpty()) {
                payloadEntries.put(INPUTSVG, Utils.resolvePath(svg));
                requestParams.put(INPUTSVG, INPUTSVG.concat(".svg"));

            }

        }

        //set callback
        if (configAttributes.containsKey(CALLBACKS)) {
            String callbacks = (String) configAttributes.get(CALLBACKS).getData();
            if (!callbacks.isEmpty()) {
                payloadEntries.put(CALLBACKS, Utils.resolvePath(callbacks));
                requestParams.put(CALLBACKS, DEFAULT_CALLBACKFILE_NAME);

            }

        }

        //set DASHBOARDLOGO
        if (configAttributes.containsKey(DASHBOARDLOGO)) {
            String dashboardlogo = (String) configAttributes.get(DASHBOARDLOGO).getData();
            if (!dashboardlogo.isEmpty()) {
                payloadEntries.put(DASHBOARDLOGO, Utils.resolvePath(dashboardlogo));
                dashboardlogo = Utils.getFileExtension(Utils.getFile(Utils.resolvePath(dashboardlogo)));
                requestParams.put(DASHBOARDLOGO, DASHBOARDLOGO.concat(dashboardlogo));
            }
        }

        //set OUTPUTFILEDEFINITION
        if (configAttributes.containsKey(OUTPUTFILEDEFINITION)) {
            String filedef = (String) configAttributes.get(OUTPUTFILEDEFINITION).getData();
            if (!filedef.isEmpty()) {
                filedef = (Utils.resolvePath(filedef));
                requestParams.put(OUTPUTFILEDEFINITION, DEFAULT_OUTPUTDEF_NAME);
                payloadEntries.put(OUTPUTFILEDEFINITION, filedef);
            }

        }

        //set template
        String templateFile = null;
        String tempTemplate = null;
        String resourceFile =null;
        if(configAttributes.containsKey(TEMPLATE)){
            templateFile = (String) configAttributes.get(TEMPLATE).getData();
            tempTemplate = templateFile;

            // Checks if the content is raw html or it's just file name 
            if (templateFile.startsWith("<")) {
                File tempFile = File.createTempFile("fc-export", ".tmp");
                PrintWriter out = new PrintWriter(tempFile.getAbsolutePath());
                out.println(templateFile);
                out.close();
                templateFile = tempFile.getAbsolutePath();
            }

            //If minifyResources option is enabled
            if (minifyResources) {
                templateFile = Minifier.minify("html", templateFile);
            }

            resourceFile = null;

            if (configAttributes.containsKey(RESOURCES) && !configAttributes.get(RESOURCES).toString().isEmpty()) {
                resourceFile = (String) configAttributes.get(RESOURCES).getData();
            }

            templateFileRef = getTemplate(templateFile, tempTemplate, minifyResources);
        }

        for (String configName : configAttributes.keySet()) {
            if (!(requestParams.containsKey(configName) || RESOURCES.equalsIgnoreCase(configName))) {
                requestParams.put(configName, configAttributes.get(configName).getData().toString());
            }
        }

        //prepare payload
        ResourceReader resourceReader = new ResourceReader(resourceFile, templateFile, templateFileRef, payloadEntries);
        String payloadZipPath = resourceReader.processForZip();
        if (configAttributes.containsKey(TEMPLATE)) {
            String relativeTempPath = resourceReader.getRelativeTemplatePath(Utils.resolvePath(templateFile));
            requestParams.put(TEMPLATE, Constants.DEFAULT_TEMPLATE_FOLDER.concat(relativeTempPath));
        }
        requestParams.put(PAYLOAD, payloadZipPath);

    }

    private ArrayList<String> getTemplate(String minifiedPath, String originalPath, Boolean minifyResources) throws Exception {
        ArrayList<String> extractedPaths = new ArrayList<>();
        String templateAbsolutePath = Utils.resolvePath(originalPath);
        extractedPaths.add(minifiedPath);
        Document doc = null;
        try {
            File file = Utils.getFile(templateAbsolutePath);
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            throw new ExportException(e);
        }

        Elements links = doc.select("link[href]");
        Elements media = doc.select("[src]");
        Elements res = doc.select("style");

        for (Element elt : res) {
            Matcher cssMatcher = Pattern.compile("url\\((.*?)\\)").matcher(elt.data());
            while (cssMatcher.find()) {
                String templateFontsPath = cssMatcher.group(1).replaceAll("^[\"']+|[\"']+$", "");
                if (Utils.isValidPath(templateFontsPath)) {
                    extractedPaths.add(Utils.resolvePath(templateFontsPath, templateAbsolutePath));
                }
            }
        }

        for (Element elm : links) {
            String attrPath = elm.attr("href");
            if (Utils.isValidPath(attrPath)) {
                String hrefAbsolutePath = Utils.resolvePath(attrPath, templateAbsolutePath);
                extractedPaths.add((minifyResources && Minifier.validToMinify(hrefAbsolutePath)) ?Minifier.minify("resource", hrefAbsolutePath) :hrefAbsolutePath );

                String fileName = new File(hrefAbsolutePath).getName();
                int dotIndex = fileName.lastIndexOf('.');
                String fileExtension = fileName.substring(dotIndex + 1);
                if (dotIndex > -1 && fileExtension.equals("css")){
                    FileInputStream fstream = new FileInputStream(hrefAbsolutePath);
                    FileChannel fc = fstream.getChannel();
                    ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
                    Charset cs = Charset.forName("8859_1");
                    CharsetDecoder cd = cs.newDecoder();
                    CharBuffer cb = cd.decode(bb);
                    Pattern p = Pattern.compile("url\\((.*?)\\)");
                    Matcher cssMatcher = p.matcher(cb);
                    while (cssMatcher.find()) {
                        String cssFontsPath = cssMatcher.group(1).replaceAll("^[\"']+|[\"']+$", "");
                        if (Utils.isValidPath(cssFontsPath)) {
                            extractedPaths.add(Utils.resolvePath(cssFontsPath, hrefAbsolutePath));
                        }
                    }
                }
            }
        }

        for (Element elm : media) {
            String attrPath = elm.attr("src");
            if(Utils.isValidPath(attrPath)) {
                String resolvedPath = Utils.resolvePath(attrPath,templateAbsolutePath);
                extractedPaths.add((minifyResources && Minifier.validToMinify(resolvedPath)) ?Minifier.minify("resource", resolvedPath) :resolvedPath );
            }
        }
        return extractedPaths;

    }

    public void deleteTempFiles () {
        for (String tempPath : templateFileRef) {
            if (tempPath.contains(".min.fusionexport.")) {
                File tempFile = new File(tempPath);
                tempFile.delete();
            }
        }
    }

    public Map getRequestParams() {
        return requestParams;
    }

}















