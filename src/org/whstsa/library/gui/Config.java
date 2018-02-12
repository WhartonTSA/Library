package org.whstsa.library.gui;

import org.whstsa.library.LibraryDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;

public class Config {

    private Properties properties;
    private File configFile;

    public Config(File configFile) {
        this.properties = new Properties();
        this.configFile = configFile;
        try {
            properties.load(new FileReader(this.configFile));
        } catch (Exception ex){
            LibraryDB.LOGGER.debug("Couldn't load config.");
            ex.printStackTrace();
        }

    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    public void setProperty(String key, String property) {
        this.properties.setProperty(key, property);
        LibraryDB.LOGGER.debug("Property " + property + " was applied to " + key);
    }

    public void save() {
        try {
            this.properties.store(new FileOutputStream(this.configFile), null);
            LibraryDB.LOGGER.debug("Config saved.");
        } catch (Exception ex) {
            LibraryDB.LOGGER.debug("Couldn't save file.");
            ex.printStackTrace();
        }
    }

}
