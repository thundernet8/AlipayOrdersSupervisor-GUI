package com.wxq.apsv.controller;

import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class AboutController extends JPanel implements TabController {
    private final static Logger logger = LoggerFactory.getLogger(TaskStatusController.class);

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
        this.linkLabel = new JLabel("test");
        linkLabel.setText("abc");
        linkLabel.setPreferredSize(new Dimension(100, 300));
        linkLabel.setMinimumSize(new Dimension(100, 300));
        add(linkLabel, BorderLayout.SOUTH);
    }

    private void InitListeners() {

    }
}
