package com.fratics.precis.fpg.config;


import java.io.*;
import java.util.Properties;

public class FPGConfigObject extends FPGBase {

    public static String configFile = "./conf/fpgconfig.properties";
    private Properties properties = new Properties();

    public FPGConfigObject() {
    }

    public static String getConfigFile() {
        return configFile;
    }

    public static void setConfigFile(String configFile) {
    		FPGConfigObject.configFile = configFile;
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
    
    public static void main(String[] args) throws Exception {
		FPGConfigObject c = new FPGConfigObject();
		c.initialize();
		c.getProperties().setProperty("test", Character.toString('\001'));
		c.store("./data/tmpStore.txt");
		c.dump();
    }
}
