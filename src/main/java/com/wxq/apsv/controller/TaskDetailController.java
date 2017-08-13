package com.wxq.apsv.controller;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.enums.*;

import com.wxq.apsv.model.TaskListModel;
import com.wxq.apsv.view.TaskDetailPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class TaskDetailController extends JPanel implements TabController {
    private final static Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

    private TaskListModel taskListModel;

    private TaskDetailPane taskDetailPane;

    public TaskDetailController() {
        this.InitViews();
        this.InitListeners();
    }

    @Override
    public WinTab getTabType() {
        return WinTab.TASKSTATUS;
    }

    private void InitViews() {
        setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));

        taskDetailPane = new TaskDetailPane();
        add(taskDetailPane, new GridConstraints(0, 0, 1, 1, 0, 3, 1|2, 1|2, null, null, null, 0, false));

        taskListModel = TaskListModel.getInstance();
        taskListModel.RegisterObserver(taskDetailPane);
    }

    private void InitListeners() {

    }
}
