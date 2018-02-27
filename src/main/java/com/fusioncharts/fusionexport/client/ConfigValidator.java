package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.*;

public class ConfigValidator {

    private static Map<String,Map<String,String>> attributeMap;
    private static String META_DATA_TYPINGS ="fusionexport-typings.json";
    private static final String META_DATA_TYPE ="type";
    private static final String META_DATA_CONVERTOR ="converter";

    public static void readMetadata() throws Exception {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsontree = null;
        try {
            String jsonContents = Utils.getFileContentAsString(Utils.getResourcePath(META_DATA_TYPINGS));
            jsontree = jsonParser.parse(jsonContents);

        }
        catch(Exception e){
            throw new ExportException("Metadata file not found");
        }
        if(jsontree!=null){
            attributeMap = new HashMap<>();
            for(Map.Entry<String,JsonElement> entry: ((JsonObject) jsontree).entrySet()){
                String configName = entry.getKey();
                Map<String,String> configValue = new HashMap<>();
                for(Map.Entry<String,JsonElement> subEntry: ((JsonObject) entry.getValue()).entrySet()){
                    configValue.put(subEntry.getKey(),subEntry.getValue().getAsString());
                }
                attributeMap.put(configName,configValue);
            }
        }
    }

    public static String getConfigMetaDataType(String configName){
        return attributeMap.get(configName).get(META_DATA_TYPE);
    }

    public static String getConfigMetaDataConvertor(String configName){
        return attributeMap.get(configName).get(META_DATA_CONVERTOR);
    }

}
