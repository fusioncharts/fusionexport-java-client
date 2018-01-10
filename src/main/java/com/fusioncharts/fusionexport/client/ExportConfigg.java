package com.fusioncharts.fusionexport.client;

import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.shape.SVGPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportConfigg {

    //TODO Read Config

    public static class ConfigBuilder{

        private Map<String,DataValue> configAttributes = null;
        private JsonObject requestJSON = null;
        private final String CHARTCONFIG = "chartConfig";
        private final String INPUTSVG = "inputSVG";
        private final String CALLBACKS = "callbacks";
        private final String DASHBOARDLOGO = "dashboardlogo";
        private final String OUTPUTFILEDEFINITION = "outputFileDefinition";
        private final String CLIENTNAME = "clientName";
        private final String TEMPLATE = "template";
        private final String RESOURCES = "resources";
        private final String CLIENTNAME_VALUE = "JAVA";

        public ConfigBuilder(){
            configAttributes = new HashMap<>();
            requestJSON = new JsonObject();
        }

        public ConfigBuilder addConfig(String configName,String value) throws ExportException {
            if(ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("string")){
                configAttributes.put(configName,new DataValue<String>(value));
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

        public ExportConfig build(){
            return new ExportConfig(); //TODO return this
        }

        public void createRequest() throws IOException{

            //set Client Name
            requestJSON.addProperty(CLIENTNAME,CLIENTNAME_VALUE);

            //set Chart Config
            if(configAttributes.containsKey(CHARTCONFIG)){
                String chartConfig = (String) configAttributes.get(CHARTCONFIG).getData();
                if(!chartConfig.isEmpty() && chartConfig.contains(".json")){
                    chartConfig = Utils.getFileContentAsString(Utils.getResourcePath(chartConfig));
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
                if(configAttributes.containsKey(RESOURCES) && !configAttributes.get(RESOURCES).toString().isEmpty()){
                    resourceFile = (String) configAttributes.get(RESOURCES).getData();
                }

                templateFile = Utils.getFileContentAsString(Utils.getResourcePath(templateFile));
                resourceFile = Utils.getFileContentAsString(Utils.getResourcePath(resourceFile));
            }
        }

        public void getTemplate(String path){
            List<String> extratedPaths = new ArrayList<>();
            List<String>extractedResourcePath = new ArrayList<>();
            String templateAbsolutePath = Utils.resolvePath(path);
            Document doc = null;
            try{
                doc = Jsoup.parse(Utils.getFile(templateAbsolutePath), "UTF-8");
            }catch (IOException e){
                //TODO generate ExportException
            }

            Elements links = doc.select("link[href]");
            Elements media = doc.select("[src]");


            for(Element elm : links){
                String attrPath = elm.attr("href");
                if(Utils.isValidPath(attrPath)) {
                    extratedPaths.add(Utils.resolvePath(attrPath,templateAbsolutePath) );
                }
            }

            for(Element elm : media){
                String attrPath = elm.attr("src");
                if(Utils.isValidPath(attrPath)) {
                    extratedPaths.add(Utils.resolvePath(attrPath,templateAbsolutePath) );
                }
            }



        }

    }

    //TODO Create Request



}
