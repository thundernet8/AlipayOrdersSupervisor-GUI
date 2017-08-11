package com.wxq.apsv;

import com.wxq.apsv.controller.MainController;
import com.wxq.apsv.utils.Initialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("App main start");

        Initialize.InitTheme();

        Initialize.InitFont();

        Initialize.InitShutdownHook();

        MainController.Launch();
    }
}
