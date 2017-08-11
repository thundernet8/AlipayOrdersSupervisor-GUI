package com.wxq.apsv.controller;

import javax.swing.*;

import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;

public class SettingController extends JPanel implements TabController {
    @Override
    public WinTab getTabType() {
        return WinTab.SETTINGS;
    }

    public SettingController() {

    }
}
