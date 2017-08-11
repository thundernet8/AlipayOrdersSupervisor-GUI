package com.wxq.apsv.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.wxq.apsv.enums.*;

public class Settings {
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);

    private static  Settings instance = new Settings();

    private File file;

    private Properties props;

    private String theme;
    private WinTab currentTab;
    private String font;
    private int fontSize;

    public static Settings getInstance(){
        return instance;
    }

    private Settings() {
        file = new File("config/config.properties");
        File dir = new File("config/");
        if (!file.exists()){
            try{
                dir.mkdirs();
                file.createNewFile();
                this.InitDefaults();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }

        try {
            props = new Properties();
            props.load(new FileInputStream(file.getPath()));
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private void InitDefaults() {
        try {
            props = new Properties();
            props.load(new FileInputStream(file.getPath()));

            props.setProperty("settings.appearance.theme", "Darcula");
            props.setProperty("settings.ui.tab", WinTab.CONFIGTASKS.name());

            this.Save();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public void Save() {
        try {
            props.store(new FileOutputStream(file.getPath()), "Copyright (c) 2017");
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public String getProps(String key) {
        return props.getProperty(key);
    }

    public void setProps(String key, String value) {
        props.setProperty(key, value);
    }

    public String getTheme() {
        return props.getProperty("settings.appearance.theme", "Darcula");
    }

    public void setTheme(String theme) {
        props.setProperty("settings.appearance.theme", theme);
    }

    public WinTab getCurrentTab() {
        return WinTab.valueOf(props.getProperty("settings.ui.tab"));
    }

    public void setCurrentTab(WinTab tab) {
        props.setProperty("settings.ui.tab", tab.name());
    }

    public String getFont() {
        return props.getProperty("settings.ui.font");
    }

    public void setFont(String font) {
        props.setProperty("settings.ui.font", font);
    }

    public int getFontSize() {
        return Integer.parseInt(props.getProperty("settings.ui.font.size", "16"));
    }

    public void setFontSize(int size) {
        props.setProperty("settings.ui.font.size", Integer.toString(size));
    }
}
