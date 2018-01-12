package com.fusioncharts.fusionexport.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.shape.SVGPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportConfigg {

    //TODO Read Config
    private JsonObject requestJSON = null;

    public static class ConfigBuilder{

        private Map<String,DataValue> configAttributes = null;
        private JsonObject requestJSON = null;
        private final String CHARTCONFIG = "chartConfig";
        private final String INPUTSVG = "inputSVG";
        private final String CALLBACKS = "callbacks";
        private final String DASHBOARDLOGO = "dashboardlogo";
        private final String OUTPUTFILEDEFINITION = "outputFileDefinition";
        private final String CLIENTNAME = "clientName";
        private final String TEMPLATE = "templateFilePath";
        private final String RESOURCES = "resourceFilePath";
        private final String CLIENTNAME_VALUE = "JAVA";

        public ConfigBuilder() throws Exception {
            configAttributes = new HashMap<>();
            requestJSON = new JsonObject();
            ConfigValidator.readMetadata();
        }

        public ConfigBuilder addConfig(String configName,String value) throws ExportException {
            if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("string")){
                configAttributes.put(configName,new DataValue<String>(value));
            }
            else if(ConfigValidator.getConfigMetaDataConvertor(configName).equalsIgnoreCase("BooleanConverter")){
                addConfig(configName,Boolean.parseBoolean(value));
            }
            else if(ConfigValidator.getConfigMetaDataConvertor(configName).equalsIgnoreCase("NumberConverter")){
                addConfig(configName,Integer.parseInt(value));
            }
            else{
                throw new ExportException("Type of"+configName+" is not valid");
            }
            return this;
        }

        public ConfigBuilder addConfig(String configName,boolean value) throws ExportException {
            if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("boolean")){
                configAttributes.put(configName,new DataValue<Boolean>(value));
            }
            else{
                throw new ExportException("Type of"+configName+" is not valid");
            }
            return this;
        }

        public ConfigBuilder addConfig(String configName,Integer value) throws ExportException {
            if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("integer")){
                configAttributes.put(configName,new DataValue<Integer>(value));
            }
            else{
                throw new ExportException("Type of"+configName+" is not valid");
            }
            return this;
        }

        public class DataValue<T>{
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

        public ExportConfigg build() throws Exception {
            createRequest();
            return new ExportConfigg(this);
        }

        public void createRequest() throws Exception {

            //set Client Name
            requestJSON.addProperty(CLIENTNAME,CLIENTNAME_VALUE);

            //set Chart Config
            if(configAttributes.containsKey(CHARTCONFIG)){
                String chartConfig = (String) configAttributes.get(CHARTCONFIG).getData();
                if(!chartConfig.isEmpty() && chartConfig.contains(".json")){
                    chartConfig = new JsonParser().parse(Utils.getFileContentAsString(Utils.getResourcePath(chartConfig))).toString();
                }
                requestJSON.addProperty(CHARTCONFIG,chartConfig);
            }

            //set SVG Config
            if(configAttributes.containsKey(INPUTSVG)){
                String svg= (String) configAttributes.get(INPUTSVG).getData();
                if(!svg.isEmpty()){
                    svg = Utils.getFileContentAsString(Utils.getResourcePath(svg));
                }
                requestJSON.addProperty(INPUTSVG,Utils.getBase64EncodedString(svg));
            }

            //set callback
            if(configAttributes.containsKey(CALLBACKS)){
                String callbacks= (String) configAttributes.get(CALLBACKS).getData();
                if(!callbacks.isEmpty()){
                    callbacks = Utils.getFileContentAsString(Utils.getResourcePath(callbacks));
                }
                requestJSON.addProperty(CALLBACKS,Utils.getBase64EncodedString(callbacks));
            }

            //set DASHBOARDLOGO
            if(configAttributes.containsKey(DASHBOARDLOGO)){
                String dashboardlogo= (String) configAttributes.get(DASHBOARDLOGO).getData();
                if(!dashboardlogo.isEmpty()){
                    dashboardlogo = Utils.getFileContentAsString(Utils.getResourcePath(dashboardlogo));
                }
                requestJSON.addProperty(DASHBOARDLOGO,Utils.getBase64EncodedString(dashboardlogo));
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
        }

        public ArrayList<String> getTemplate(String path) throws Exception {
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

    }

    private ExportConfigg(ConfigBuilder configBuilder){
        this.requestJSON = configBuilder.requestJSON;
    }

    public String getFormattedExportConfigs(){
        return requestJSON.toString();
    }



}
