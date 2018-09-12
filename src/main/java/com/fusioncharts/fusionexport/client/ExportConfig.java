
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

import static com.fusioncharts.fusionexport.client.Constants.*;

public class ExportConfig{

    private Map<String,DataValue> configAttributes = null;
    private Map<String,String> payloadEntries = null;
    private ArrayList<String> templateFileRef = null;
    private Map<String,String> requestParams = null;


    public ExportConfig()throws Exception{
        configAttributes = new HashMap<>();
        requestParams = new HashMap<>();
        payloadEntries = new HashMap<>();
        ConfigValidator.readMetadata();
    }

    public ExportConfig set(String configName, String value) throws ExportException {

            try{
                if (ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("string")) {
                    configAttributes.put(configName, new DataValue<String>(value));
                } else if (ConfigValidator.getConfigMetaDataConvertor(configName).equalsIgnoreCase("BooleanConverter")) {
                    set(configName, Boolean.parseBoolean(value));
                } else if (ConfigValidator.getConfigMetaDataConvertor(configName).equalsIgnoreCase("NumberConverter")) {
                    set(configName, Integer.parseInt(value));
                } else {
                    throw new ExportException("Type of" + configName + " is not valid");
                }
            }catch (Exception e){
                if(e.getMessage() == null){
                    throw new ExportException("Config:"+configName+" not valid");
                }
                else {
                    throw new ExportException(e);
                }
            }
        return this;
    }

    public ExportConfig set(String configName, boolean value)throws ExportException{
        try {
            if (ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("boolean")) {
                configAttributes.put(configName, new ExportConfig.DataValue<Boolean>(value));
            } else {
                throw new ExportException("Type of" + configName + " is not valid");
            }
        }catch (Exception e){
            if(e.getMessage() == null){
                throw new ExportException("Config:"+configName+" not valid");
            }
            else {
                throw new ExportException(e);
            }
        }
        return this;
    }

    public ExportConfig set(String configName, Integer value)throws ExportException{
        try {
            if (ConfigValidator.getConfigMetaDataType(configName).equalsIgnoreCase("integer")) {
                configAttributes.put(configName, new DataValue<Integer>(value));
            } else {
                throw new ExportException("Type of" + configName + " is not valid");
            }
        }
        catch (Exception e){
            if(e.getMessage() == null){
                throw new ExportException("Config:"+configName+" not valid");
            }
            else {
                throw new ExportException(e);
            }
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
        requestParams.put(CLIENTNAME,CLIENTNAME_VALUE);

        requestParams.put(PLATFORM,System.getProperty("os.name"));

        //set Chart Config
        if(configAttributes.containsKey(CHARTCONFIG)){
            String chartConfig = (String) configAttributes.get(CHARTCONFIG).getData();
            if(!chartConfig.isEmpty() && chartConfig.contains(".json")){
                chartConfig = new JsonParser().parse(Utils.getFileContentAsString(Utils.resolvePath(chartConfig))).toString();
            }
            requestParams.put(CHARTCONFIG,chartConfig);
        }

        //set SVG Config
        if(configAttributes.containsKey(INPUTSVG)){
            String svg= (String) configAttributes.get(INPUTSVG).getData();
            if(!svg.isEmpty()){
                payloadEntries.put(INPUTSVG,Utils.resolvePath(svg));
                requestParams.put(INPUTSVG,INPUTSVG.concat(".svg"));

            }

        }

        //set callback
        if(configAttributes.containsKey(CALLBACKS)){
            String callbacks= (String) configAttributes.get(CALLBACKS).getData();
            if(!callbacks.isEmpty()){
                payloadEntries.put(CALLBACKS,Utils.resolvePath(callbacks));
                requestParams.put(CALLBACKS,DEFAULT_CALLBACKFILE_NAME);

            }

        }

        //set DASHBOARDLOGO
        if(configAttributes.containsKey(DASHBOARDLOGO)){
            String dashboardlogo= (String) configAttributes.get(DASHBOARDLOGO).getData();
            if(!dashboardlogo.isEmpty()){
                payloadEntries.put(DASHBOARDLOGO,Utils.resolvePath(dashboardlogo));
                dashboardlogo = Utils.getFileExtension(Utils.getFile(Utils.resolvePath(dashboardlogo)));
                requestParams.put(DASHBOARDLOGO,DASHBOARDLOGO.concat(dashboardlogo));
            }
        }

        //set OUTPUTFILEDEFINITION
        if(configAttributes.containsKey(OUTPUTFILEDEFINITION)){
            String filedef= (String) configAttributes.get(OUTPUTFILEDEFINITION).getData();
            if(!filedef.isEmpty()){
                filedef = (Utils.resolvePath(filedef));
                requestParams.put(OUTPUTFILEDEFINITION,DEFAULT_OUTPUTDEF_NAME);
                payloadEntries.put(OUTPUTFILEDEFINITION,filedef);
            }

        }

        //set template
        String templateFile = null;
        String resourceFile =null;
        if(configAttributes.containsKey(TEMPLATE)){
            templateFile = (String) configAttributes.get(TEMPLATE).getData();
            resourceFile =null;

            if(configAttributes.containsKey(RESOURCES) && !configAttributes.get(RESOURCES).toString().isEmpty()){
                resourceFile = (String) configAttributes.get(RESOURCES).getData();
            }
            templateFileRef = getTemplate(templateFile);
        }

        for(String configName : configAttributes.keySet()){
            if(!(requestParams.containsKey(configName) || RESOURCES.equalsIgnoreCase(configName))){
                requestParams.put(configName,configAttributes.get(configName).getData().toString());
            }
        }

        //prepare payload
        ResourceReader resourceReader = new ResourceReader(resourceFile,templateFile,templateFileRef,payloadEntries);
        String payloadZipPath = resourceReader.processForZip();
        if(configAttributes.containsKey(TEMPLATE)){
            String relativeTempPath = resourceReader.getRelativeTemplatePath(Utils.resolvePath(templateFile));
            requestParams.put(TEMPLATE,Constants.DEFAULT_TEMPLATE_FOLDER.concat(relativeTempPath));
        }
        requestParams.put(PAYLOAD,payloadZipPath);

    }

    private ArrayList<String> getTemplate(String path) throws Exception {
        ArrayList<String> extractedPaths = new ArrayList<>();
        String templateAbsolutePath = Utils.resolvePath(path);
        extractedPaths.add(templateAbsolutePath);
        Document doc = null;
        try{
            File file =Utils.getFile(templateAbsolutePath);
            doc = Jsoup.parse(file, "UTF-8");
        }catch (IOException e){
            throw new ExportException(e);
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

    public Map getRequestParams(){
        return requestParams;
    }

}















