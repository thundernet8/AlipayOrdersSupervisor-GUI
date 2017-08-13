package com.wxq.apsv.controller;

import javax.swing.*;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class AboutController extends JPanel implements TabController {
    private final static Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

    private JLabel logoLabel;

    private JLabel titleLabel;

    private JLabel copyrightLabel;

    private JLabel versionLabel;

    private JLabel linkLabel;

    @Override
    public WinTab getTabType() {
        return WinTab.ABOUT;
    }

    public AboutController() {
        this.InitViews();
        this.InitListeners();
    }

    private void InitViews() {
        this.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
    }

    private void InitListeners() {

    }
}
