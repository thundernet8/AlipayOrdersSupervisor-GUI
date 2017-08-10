package com.wxq.apsv.controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxq.apsv.interfaces.Observer;
import com.wxq.apsv.model.*;
import com.wxq.apsv.view.*;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private JPanel mainPanel;
    private JButton button1;

    private MainController() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        button1 = new JButton();
        button1.setText("Button");
        mainPanel.add(button1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 30), new Dimension(100, 30), new Dimension(100, 30), 1, false));

        // Test
        JPanel taskListPane = new TaskListPane();
        TaskListModel taskListModel = new TaskListModel();
        taskListModel.RegisterObserver((Observer) taskListPane);

        button1.addActionListener((ActionEvent e) -> {
            logger.info("Button1 clicked");
            taskListModel.AddTask(new ApsvTask((int)Math.floor(Math.random() * 100)));
        });
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
}
