package com.wxq.apsv.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;

public class Initialize {
    private static final Logger logger = LoggerFactory.getLogger(Initialize.class);

    public static Settings settings = Settings.getInstance();

    /**
     * 初始化主题配置
     */
    public static void InitTheme() {
        try {
            switch (settings.getTheme()) {
                case "BeautyEye":
                    BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    UIManager.put("RootPane.setupButtonVisible", false);
                    break;
                default:
                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void InitTabs() {
        // TODO
    }

    public static void InitShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("App is shutting down!");
            // TODO more dispose
            settings.Save();
        }));
    }
}
