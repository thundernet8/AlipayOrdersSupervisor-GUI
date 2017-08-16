package com.wxq.apsv.controller;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.enums.*;

import com.wxq.apsv.model.RunTasksModel;
import com.wxq.apsv.model.TaskListModel;
import com.wxq.apsv.view.OrderListTable;
import com.wxq.apsv.view.OrderActionsColumn;
import com.wxq.apsv.view.TaskDetailPane;
import com.wxq.apsv.worker.ApsvRepushTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class TaskDetailController extends JPanel implements TabController {
    private final static Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

    private TaskListModel taskListModel;

    private RunTasksModel runTasksModel;

    private TaskDetailPane taskDetailPane;

    private OrderListTable orderListTable;

    public TaskDetailController() {
        this.InitModels();
        this.InitViews();
        this.InitListeners();
    }

    @Override
    public WinTab getTabType() {
        return WinTab.TASKSTATUS;
    }

    private void InitModels() {
        taskListModel = TaskListModel.getInstance();
        runTasksModel = RunTasksModel.getInstance();
    }

    private void InitViews() {
        setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));

        taskDetailPane = new TaskDetailPane(runTasksModel);
        add(taskDetailPane, new GridConstraints(0, 0, 1, 1, 0, 3, 1|2, 1|2, null, null, null, 0, false));

        InitOrderListTable();
    }

    private void InitOrderListTable() {
        // 订单列表Table
        this.orderListTable = new OrderListTable();
        JScrollPane scrollPane = new JScrollPane(orderListTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskDetailPane.getBotWrapperPanel().add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(600, 50), null, 0, false));

        this.orderListTable.setAutoCreateColumnsFromModel(true);

        // Data Rows
        orderListTable.setModel(new DefaultTableModel(null, orderListTable.getColumns()));

        // Actions
        new OrderActionsColumn(orderListTable, orderListTable.getColumns().length - 1, (ActionEvent e, int row, int column, OrderAction action) -> {
            logger.info("row {} col {} action {}", row, column, action);
        });

        // 注册Observer
        runTasksModel.RegisterObserver(this.orderListTable);
    }

    private void InitListeners() {
        taskListModel.RegisterObserver(taskDetailPane);

        runTasksModel.RegisterObserver(taskDetailPane);

        taskDetailPane.getRunBtn().addActionListener((ActionEvent e) -> {
            Toolkit.getDefaultToolkit().beep();
            runTasksModel.SwitchTaskStatus();
        });

        taskDetailPane.getTaskSelector().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                runTasksModel.SelectTask(e.getItem().toString());
            }
        });

        // order repush action
        this.orderListTable.addColumnActionListener((ActionEvent e, int row, int column, OrderAction action) -> {
            logger.info("row {} col {} action {}", row, column, action);
            if (column != orderListTable.getColumns().length - 1) {
                return;
            }
            if (action == OrderAction.REPUSH) {
                // repush
                new java.util.Timer().schedule(new ApsvRepushTask(runTasksModel.getCurrentSelectTask(), runTasksModel.getOrders().get(row)), 1000);
            }
        });
    }
}
