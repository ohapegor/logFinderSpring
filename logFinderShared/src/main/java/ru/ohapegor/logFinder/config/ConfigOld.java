package ru.ohapegor.logFinder.config;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;


//apache configuration v1
 class ConfigOld {


    private static PropertiesConfiguration propertiesConfiguration;

    private static String propertiesLocation = "g:\\Egor\\git\\EO-Test-Spring\\config\\properties.properties";

    private static void init(){
        try {
            propertiesConfiguration = new PropertiesConfiguration(propertiesLocation);
            propertiesConfiguration.setEncoding("UTF-8");
            FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
            fileChangedReloadingStrategy.setRefreshDelay(1000);
            propertiesConfiguration.setReloadingStrategy(fileChangedReloadingStrategy);
        } catch (ConfigurationException e) {
            throw new ConfigException(e);
        }
    }

    public static Configuration getProperties(){
        if (propertiesConfiguration == null) init();
        return propertiesConfiguration;
    }

    public static String getString(String key){
        return propertiesConfiguration.getString(key);
    }

    public static int getInt(String key){
        return propertiesConfiguration.getInt(key);
    }


}
