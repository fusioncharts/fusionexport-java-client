package com.fusioncharts.fusionexport.client;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExportConfig implements Cloneable {

    private ConcurrentHashMap<String, String> configs;

    public ExportConfig() {
        this.configs = new ConcurrentHashMap<String, String>();
    }

    public void set(String configName, String configValue) {
        this.configs.put(configName, configValue);
    }

    public String get(String configName) {
        return this.configs.get(configName);
    }

    public boolean remove(String configName) {
        return this.configs.remove(configName, this.configs.get(configName));
    }

    public boolean has(String configName) {
        return this.configs.containsKey(configName);
    }

    public void clear() {
        this.configs.clear();
    }

    public int count() {
        return this.configs.size();
    }

    public String[] configNames() {
        List<String> configNames = new ArrayList<String>();
        Enumeration<String> iterator = this.configs.keys();
        while (iterator.hasMoreElements()) configNames.add(iterator.nextElement());
        return configNames.toArray(new String[configNames.size()]);
    }

    public String[] configValues() {
        List<String> configValues = new ArrayList<String>();
        Enumeration<String> iterator = this.configs.elements();
        while (iterator.hasMoreElements()) configValues.add(iterator.nextElement());
        return configValues.toArray(new String[configValues.size()]);
    }

    @Override
    public ExportConfig clone() {
        ExportConfig newExportConfig = null;
        try {
            newExportConfig = (ExportConfig) super.clone();
            newExportConfig.configs = new ConcurrentHashMap<String, String>();
        } catch (CloneNotSupportedException exp){
            newExportConfig = new ExportConfig();
        }
        Set<Map.Entry<String, String>> configs = this.configs.entrySet();
        for (Map.Entry<String, String> config : configs) {
            newExportConfig.set(config.getKey(), config.getValue());
        }
        return newExportConfig;
    }

    public String getFormattedConfigs() {
        StringBuilder configsAsJSON = new StringBuilder();
        Set<Map.Entry<String, String>> configs = this.configs.entrySet();
        for (Map.Entry<String, String> config : configs) {
            String formattedConfigValue = this.getFormattedConfigValue(config.getKey(), config.getValue());
            String keyValuePair = String.format("\"%s\": %s, ", config.getKey(), formattedConfigValue);
            configsAsJSON.append(keyValuePair);
        }
        if(configsAsJSON.length() >= 2) {
            configsAsJSON.delete(configsAsJSON.length() - 2, configsAsJSON.length());
        }
        configsAsJSON.insert(0,"{ ");
        configsAsJSON.append(" }");
        return configsAsJSON.toString();
    }

    private String getFormattedConfigValue(String configName, String configValue) {
        if(configName.equals("chartConfig")) {
            return configValue;
        } else if(configName.equals("maxWaitForCaptureExit")) {
            return configValue;
        } else if(configName.equals("asyncCapture")) {
            return configValue.toLowerCase();
        } else if(configName.equals("exportAsZip")) {
            return configValue.toLowerCase();
        } else {
            return String.format("\"%s\"", configValue);
        }
    }

    @Override
    public String toString() {
        return this.getFormattedConfigs();
    }
}














