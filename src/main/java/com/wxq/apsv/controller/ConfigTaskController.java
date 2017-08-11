package com.wxq.apsv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.view.*;
import com.wxq.apsv.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;

public class ConfigTaskController extends JPanel implements TabController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTaskController.class);

    private TaskListTable taskListTable;
    private TaskListModel taskListModel;

    @Override
    public WinTab getTabType() {
        return WinTab.CONFIGTASKS;
    }

    public ConfigTaskController() {
        this.InitViews();
    }

    private void InitViews() {
        //this.InitTaskListTable();
        this.add(new JLabel("lable"));
    }

    private void InitTaskListTable() {
        // 添加任务列表Table
        this.taskListTable = new TaskListTable();
        this.taskListTable.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        this.add(taskListTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));

        this.taskListTable.setAutoCreateColumnsFromModel(true);

        // Columns
        String[] columns = {"ID", "名称", "备注", "操作"};

        // Data Rows
        this.taskListModel = new TaskListModel();
        ArrayList<ApsvTask> tasks = taskListModel.getTasks();
        Object[][] cellData = new String[tasks.size()][columns.length];
        for (int i=0; i<tasks.size(); i++) {
            ApsvTask task = tasks.get(i);
            cellData[i] = new String[]{Integer.toString(task.id), task.name, task.note, ""};
        }

        DefaultTableModel tableModel = new DefaultTableModel(cellData, columns);
        this.taskListTable.setModel(tableModel);

        // 行高列宽
        this.taskListTable.setRowHeight(40);
        this.taskListTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        this.taskListTable.getColumnModel().getColumn(0).setMaxWidth(150);

        this.taskListTable.updateUI();
    }
}
