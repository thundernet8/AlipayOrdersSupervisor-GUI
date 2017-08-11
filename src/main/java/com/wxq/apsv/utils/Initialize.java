package com.wxq.apsv.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.util.Enumeration;
import org.apache.commons.lang.StringUtils;

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

    /**
     * 初始化字体
     */
    public static void InitFont() {
        String lowDpiKey = "lowDpiInit";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.width <= 1366 && StringUtils.isEmpty(settings.getProps(lowDpiKey))) {
            settings.setFontSize(15);
            settings.setProps(lowDpiKey, "true");
            settings.Save();
        }

        Font fnt = new Font(settings.getFont(), Font.PLAIN, settings.getFontSize());
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
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
