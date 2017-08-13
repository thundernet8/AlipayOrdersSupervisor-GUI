package com.wxq.apsv.controller;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.enums.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class TaskDetailController extends JPanel implements TabController {
    private final static Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

    private JButton button1;

    public TaskDetailController() {
        this.InitViews();
        this.InitListeners();
    }

    @Override
    public WinTab getTabType() {
        return WinTab.TASKSTATUS;
    }

    private void InitViews() {
        this.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
    }

    private void InitListeners() {
        // Test
//        JPanel taskListPane = new TaskListPane();
//        TaskListModel taskListModel = new TaskListModel();
//        taskListModel.RegisterObserver((Observer) taskListPane);

        button1 = new JButton();
        button1.setText("Button000");
        add(button1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 30), new Dimension(100, 30), new Dimension(100, 30), 1, false));

//        button1.addActionListener((ActionEvent e) -> {
//            logger.debug("Button1 clicked");
//            taskListModel.AddTask(new ApsvTask((int)Math.floor(Math.random() * 100)));
//        });
    }
}
