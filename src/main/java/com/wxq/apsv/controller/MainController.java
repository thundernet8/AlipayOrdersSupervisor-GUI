package com.wxq.apsv.controller;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.interfaces.TabController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxq.apsv.model.*;
import com.wxq.apsv.utils.*;

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
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 0, 0, 0), -1, -1));

        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, 0, 3, 1|2, 1|2, null, new Dimension(200, 200), null, 0, false));

        // Add Tabs
        final JPanel configTaskPanel = new ConfigTaskController();
        final JPanel taskStatusPanel = new TaskDetailController();
        final JPanel settingPanel = new SettingController();
        final JPanel aboutPanel = new AboutController();
        tabbedPane.addTab("配置任务", configTaskPanel);
        tabbedPane.addTab("任务详细", taskStatusPanel);
        tabbedPane.addTab("设置", settingPanel);
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
