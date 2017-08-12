package com.wxq.apsv.controller;

import javax.swing.*;
import javax.swing.border.*;
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

    private JPanel wrapperPannel;
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
        // Layout设置要尽可能早
        this.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));

        this.wrapperPannel = new JPanel();
        this.wrapperPannel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 0, 0));
        this.add(wrapperPannel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

        // Border
        EtchedBorder line = new EtchedBorder();
        Border border = BorderFactory.createTitledBorder(line, "任务编辑列表");
        this.wrapperPannel.setBorder(border);

        this.InitTaskListTable();
    }

    private void InitTaskListTable() {
        // 添加任务列表Table
        this.taskListTable = new TaskListTable();
        this.taskListTable.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        JScrollPane scrollPane = new JScrollPane(taskListTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40, 0));
        this.wrapperPannel.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(600, 50), null, 0, false));

//        this.taskListTable.setAutoCreateColumnsFromModel(true);

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

        //DefaultTableModel tableModel = new DefaultTableModel(cellData, columns);
        DefaultTableModel tableModel = new DefaultTableModel(new String[][]{{"1", "name", "notes", ""}}, columns);
        this.taskListTable.setModel(tableModel);

        // 行高列宽
        this.taskListTable.setRowHeight(40);
        this.taskListTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        this.taskListTable.getColumnModel().getColumn(0).setMaxWidth(150);
        this.taskListTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        this.taskListTable.getColumnModel().getColumn(1).setMaxWidth(200);
        this.taskListTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        this.taskListTable.getColumnModel().getColumn(2).setMaxWidth(250);

        this.updateUI();
    }
}
