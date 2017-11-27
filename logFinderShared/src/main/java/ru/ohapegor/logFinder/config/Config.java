package ru.ohapegor.logFinder.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Config {

    private static ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder;

    //init block
    static {
        Parameters params = new Parameters();
        File propertiesFile = new File("G:\\Egor\\Work\\Projects\\LogReader\\spring\\logFinder\\config\\properties.properties");

        builder = new ReloadingFileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(params.fileBased().setFile(propertiesFile));
        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),
                null, 5, TimeUnit.SECONDS);
        trigger.start();
    }

    public static String getString(String key) {
        try {
            return builder.getConfiguration().getString(key);
        } catch (ConfigurationException e) {
            throw new ConfigException(e);
        }
    }

    public static long getLong(String key) {
        try {
            return builder.getConfiguration().getLong(key);
        } catch (ConfigurationException e) {
            throw new ConfigException(e);
        }
    }

    public static int getInt(String key) {
        try {
            return builder.getConfiguration().getInt(key);
        } catch (ConfigurationException e) {
            throw new ConfigException(e);
        }
    }
}
