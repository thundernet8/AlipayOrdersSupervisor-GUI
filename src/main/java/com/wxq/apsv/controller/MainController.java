package com.wxq.apsv.controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.interfaces.TabController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxq.apsv.interfaces.Observer;
import com.wxq.apsv.model.*;
import com.wxq.apsv.view.*;
import com.wxq.apsv.utils.*;
import com.wxq.apsv.enums.*;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    private MainController() {
        this.InitView();
        this.InitListeners();
    }

    public static void Launch() {
        JFrame frame = new JFrame(MainController.class.toString());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int) (screenSize.width * 0.1), (int) (screenSize.height * 0.08), (int) (screenSize.width * 0.8), (int) (screenSize.height * 0.8));
        Dimension preferSize = new Dimension((int) (screenSize.width * 0.8), (int) (screenSize.height * 0.8));
        frame.setPreferredSize(preferSize);
        frame.setContentPane(new MainController().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setTitle(Constants.APP_NAME);
        frame.pack();
        frame.setVisible(true);
    }

    private void InitView() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));

        // Add Tabs
        final JPanel configTaskPanel = new ConfigTaskController();
        configTaskPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("配置任务", configTaskPanel);

        final JPanel taskStatusPanel = new TaskStatusController();
        configTaskPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        taskStatusPanel.setBackground(new Color(255,255,255));
        tabbedPane.addTab("任务状态", taskStatusPanel);

        final JPanel settingPanel = new SettingController();
        settingPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        settingPanel.setBackground(Color.red);
        tabbedPane.addTab("设置", settingPanel);

        final JPanel aboutPanel = new AboutController();
        aboutPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        aboutPanel.setBackground(new Color(255,255,255));
        tabbedPane.addTab("关于", aboutPanel);

        tabbedPane.setSelectedIndex(Settings.getInstance().getCurrentTab().ordinal());
    }

    private void InitListeners() {
        // Tab 切换
        tabbedPane.addChangeListener((ChangeEvent e) -> {
            logger.debug(Integer.toString(tabbedPane.getSelectedIndex()));
            TabController tabController = (TabController)tabbedPane.getSelectedComponent();
            Settings.getInstance().setCurrentTab(tabController.getTabType());
        });
    }
}
