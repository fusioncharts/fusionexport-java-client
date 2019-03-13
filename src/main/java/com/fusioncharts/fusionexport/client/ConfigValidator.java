package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.*;

public class ConfigValidator {

    private static Map<String,Map<String,Object>> attributeMap;
    private static String META_DATA_TYPINGS ="fusionexport-typings.json";
    private static final String META_DATA_TYPE ="type";
    private static final String META_DATA_CONVERTOR ="converter";
    private static final String META_DATA_SUPPORTEDTYPE ="supportedTypes";
    private static final String META_DATA_DATASET ="dataset";

    public static void readMetadata() throws Exception {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsontree = null;
        try {
            String jsonContents = Utils.getResourceContentAsString(META_DATA_TYPINGS);
            jsontree = jsonParser.parse(jsonContents);
        }
        catch(Exception e){
            throw new ExportException("Metadata file not found");
        }
        
        if(jsontree!=null){
			Gson gson = new Gson();
            attributeMap = new HashMap<>();
            for(Map.Entry<String,JsonElement> entry: ((JsonObject) jsontree).entrySet()){
                String configName = entry.getKey();
                Map<String,Object> configValue = new HashMap<>();
                for(Map.Entry<String,JsonElement> subEntry: ((JsonObject) entry.getValue()).entrySet()){
                	Object value = null;
                	if (subEntry.getValue().getClass().getName().equals("com.google.gson.JsonArray")) {
                		JsonArray jsonArray = (JsonArray)subEntry.getValue();
                		@SuppressWarnings("rawtypes")
						ArrayList arrayList = gson.fromJson(jsonArray, ArrayList.class);
                		value = arrayList;
                	} else {
                		value = subEntry.getValue();
                	}
                	configValue.put(subEntry.getKey(),value);
                }
                attributeMap.put(configName,configValue);
            }
        }	
    }

    public static String getConfigMetaDataType(String configName){
        return attributeMap.get(configName).get(META_DATA_TYPE).toString();
    }

    public static String getConfigMetaDataConvertor(String configName){
        return attributeMap.get(configName).get(META_DATA_CONVERTOR).toString().replace("\"", "");
    }

    @SuppressWarnings("unchecked")
	public static ArrayList<String> getConfigMetaDataSupportedTypes(String configName){
        return (ArrayList<String>)attributeMap.get(configName).get(META_DATA_SUPPORTEDTYPE);
    }
    
    @SuppressWarnings("unchecked")
	public static ArrayList<String> getConfigMetaDataDataSet(String configName){
    	return (ArrayList<String>)attributeMap.get(configName).get(META_DATA_DATASET);
    }
}
