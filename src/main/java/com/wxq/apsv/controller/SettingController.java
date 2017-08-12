package com.wxq.apsv.controller;

import javax.swing.*;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;

import java.awt.*;

public class SettingController extends JPanel implements TabController {
    @Override
    public WinTab getTabType() {
        return WinTab.SETTINGS;
    }

    public SettingController() {
        this.InitViews();
    }

    public void InitViews() {
        this.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
    }
}
