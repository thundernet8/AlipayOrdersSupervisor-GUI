package com.wxq.apsv.controller;

import javax.swing.*;

import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;

public class AboutController extends JPanel implements TabController {
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

    }

    private void InitListeners() {

    }
}
