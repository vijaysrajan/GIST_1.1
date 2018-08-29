package com.fratics.precis.util;

import com.fratics.precis.fis.base.PrecisBase;

import java.io.*;
import java.util.Properties;

public class ConfigObject extends PrecisBase {

    public static String configFile = "./conf/precisconfig.properties";
    private Properties properties = new Properties();

    public ConfigObject() {
    }

    public static String getConfigFile() {
        return configFile;
    }

    public static void setConfigFile(String configFile) {
        ConfigObject.configFile = configFile;
    }

    public static void main(String[] args) throws Exception {
        ConfigObject c = new ConfigObject();
        c.initialize();
        c.getProperties().setProperty("test", Character.toString('\001'));
        c.store("./data/tmpStore.txt");
        c.dump();
    }

    public boolean initialize() throws FileNotFoundException, IOException {
        properties.load(new FileInputStream(configFile));
        return true;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void store(String fileName) throws IOException {
        this.properties.store(new FileOutputStream(fileName), "dumping precis properties");
    }

    public void dump() throws IOException {
        PrintWriter writer = new PrintWriter(System.err);
        properties.list(writer);
        writer.flush();
    }
}
