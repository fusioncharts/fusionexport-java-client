
package com.fusioncharts.fusionexport.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExportConfig{

    private Map<String,DataValue> configAttributes = null;
    private JsonObject requestJSON = null;
    private final String CHARTCONFIG = "chartConfig";
    private final String INPUTSVG = "inputSVG";
    private final String CALLBACKS = "callbacks";
    private final String DASHBOARDLOGO = "dashboardLogo";
    private final String OUTPUTFILEDEFINITION = "outputFileDefinition";
    private final String CLIENTNAME = "clientName";
    private final String TEMPLATE = "templateFilePath";
    private final String RESOURCES = "resourceFilePath";
    private final String CLIENTNAME_VALUE = "JAVA";

    public ExportConfig() throws Exception {
        configAttributes = new HashMap<>();
        requestJSON = new JsonObject();
        ConfigValidator.readMetadata();
    }

    public ExportConfig set(String configName, String value) throws ExportException {
        if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("string")){
            configAttributes.put(configName,new DataValue<String>(value));
        }
        else if(ConfigValidator.getConfigMetaDataConvertor(configName).equalsIgnoreCase("BooleanConverter")){
            set(configName,Boolean.parseBoolean(value));
        }
        else if(ConfigValidator.getConfigMetaDataConvertor(configName).equalsIgnoreCase("NumberConverter")){
            set(configName,Integer.parseInt(value));
        }
        else{
            throw new ExportException("Type of"+configName+" is not valid");
        }
        return this;
    }

    public ExportConfig set(String configName, boolean value) throws ExportException {
        if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("boolean")){
            configAttributes.put(configName,new ExportConfig.DataValue<Boolean>(value));
        }
        else{
            throw new ExportException("Type of"+configName+" is not valid");
        }
        return this;
    }

    public ExportConfig set(String configName, Integer value) throws ExportException {
        if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("integer")){
            configAttributes.put(configName,new DataValue<Integer>(value));
        }
        else{
            throw new ExportException("Type of"+configName+" is not valid");
        }
        return this;
    }

    class DataValue<T>{
        T data;

        DataValue(T data){
            this.data = data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public void createRequest() throws Exception {

        //set Client Name
        requestJSON.addProperty(CLIENTNAME,CLIENTNAME_VALUE);

        //set Chart Config
        if(configAttributes.containsKey(CHARTCONFIG)){
            String chartConfig = (String) configAttributes.get(CHARTCONFIG).getData();
            if(!chartConfig.isEmpty() && chartConfig.contains(".json")){
                chartConfig = new JsonParser().parse(Utils.getFileContentAsString(Utils.resolvePath(chartConfig))).toString();
            }
            requestJSON.addProperty(CHARTCONFIG,chartConfig);
        }

        //set SVG Config
        if(configAttributes.containsKey(INPUTSVG)){
            String svg= (String) configAttributes.get(INPUTSVG).getData();
            if(!svg.isEmpty()){
                svg = Utils.getFileContentAsString(Utils.resolvePath(svg));
                requestJSON.addProperty(INPUTSVG,Utils.getBase64EncodedString(svg));
            }

        }

        //set callback
        if(configAttributes.containsKey(CALLBACKS)){
            String callbacks= (String) configAttributes.get(CALLBACKS).getData();
            if(!callbacks.isEmpty()){
                callbacks = Utils.getFileContentAsString(Utils.resolvePath(callbacks));
                requestJSON.addProperty(CALLBACKS,Utils.getBase64EncodedString(callbacks));
            }

        }

        //set DASHBOARDLOGO
        if(configAttributes.containsKey(DASHBOARDLOGO)){
            String dashboardlogo= (String) configAttributes.get(DASHBOARDLOGO).getData();
            if(!dashboardlogo.isEmpty()){
                dashboardlogo = Utils.resolvePath(dashboardlogo);
                requestJSON.addProperty(DASHBOARDLOGO,Utils.getBase64ForZip(dashboardlogo));
            }
        }

        //set OUTPUTFILEDEFINITION
        if(configAttributes.containsKey(OUTPUTFILEDEFINITION)){
            String filedef= (String) configAttributes.get(OUTPUTFILEDEFINITION).getData();
            if(!filedef.isEmpty()){
                filedef = (Utils.resolvePath(filedef));
                requestJSON.addProperty(OUTPUTFILEDEFINITION,Utils.getBase64ForZip(filedef));
            }

        }

        //set template
        if(configAttributes.containsKey(TEMPLATE)){
            String templateFile = (String) configAttributes.get(TEMPLATE).getData();
            String resourceFile =null;
            ArrayList<String> templateFileRef = null;
            if(configAttributes.containsKey(RESOURCES) && !configAttributes.get(RESOURCES).toString().isEmpty()){
                resourceFile = (String) configAttributes.get(RESOURCES).getData();
            }
            templateFileRef = getTemplate(templateFile);
            ResourceReader resourceReader = new ResourceReader(resourceFile,templateFileRef);
            String base64Zip = resourceReader.processForZip();
            String relativeTempPath = resourceReader.getRelativeTemplatePath(Utils.resolvePath(templateFile));

            requestJSON.addProperty(TEMPLATE,relativeTempPath);
            if(!resourceFile.isEmpty())
                requestJSON.addProperty(RESOURCES,base64Zip);

        }

        for(String configName : configAttributes.keySet()){
            if(!requestJSON.has(configName)){
                requestJSON.addProperty(configName,configAttributes.get(configName).getData().toString());
            }
        }
    }

    private ArrayList<String> getTemplate(String path) throws Exception {
        ArrayList<String> extractedPaths = new ArrayList<>();
        String templateAbsolutePath = Utils.resolvePath(path);
        extractedPaths.add(templateAbsolutePath);
        Document doc = null;
        try{
            File file =Utils.getFile(templateAbsolutePath);
            doc = Jsoup.parse(file, "UTF-8");
            //Utils.writeTempFile(file);
        }catch (IOException e){
            //TODO generate ExportException
        }

        Elements links = doc.select("link[href]");
        Elements media = doc.select("[src]");


        for(Element elm : links){
            String attrPath = elm.attr("href");
            if(Utils.isValidPath(attrPath)) {
                extractedPaths.add(Utils.resolvePath(attrPath,templateAbsolutePath) );
            }
        }

        for(Element elm : media){
            String attrPath = elm.attr("src");
            if(Utils.isValidPath(attrPath)) {
                extractedPaths.add(Utils.resolvePath(attrPath,templateAbsolutePath) );
            }
        }
        return extractedPaths;

    }

    public String getFormattedExportConfigs(){ return requestJSON.toString();
    }

}















