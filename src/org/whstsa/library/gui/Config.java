package org.whstsa.library.gui;

import javafx.scene.control.Alert;
import org.apache.commons.lang3.SystemUtils;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.gui.factories.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private Properties properties;
    private File configFile;
    private boolean notified;

    public Config(File configFile) {
        this.properties = new Properties();
        this.configFile = configFile;
        this.notified = false;
        try {
            properties.load(new FileReader(this.configFile));
        } catch (IOException ex) {
            this.setProperty("initialDirectory", "null");
            this.setProperty("tooltips", "true");
            this.setProperty("autosave", "true");
            this.setProperty("autosaveInterval", "10");
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
            if (notified) {
                notified = false;
            }
        } catch (IOException ex) {
            if (!notified) {
                DialogUtils.createDialog("Couldn't Save Config.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                notified = true;
            }
            ex.printStackTrace();
        }
    }

    public static String determineOptimalFileLocation() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return System.getProperty("user.home") + "/AppData/Local/LibraryDB/config.properties";
        } else if (SystemUtils.IS_OS_MAC) {
            return System.getProperty("user.home") + "/Library/Application\\ Support/LibraryDB/config.properties";
        } else {
            return System.getProperty("user.home") + "/.librarydb/config.properties";
        }
    }

}
