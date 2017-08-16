package com.wxq.apsv.view;

import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.ArrayList;

public final class TaskListTable extends JTable implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(TaskListTable.class);

    private String[] columns = new String[]{"序号", "名称", "备注", "Push地址", "状态", "操作"};

    public String[] getColumns() {
        return columns;
    }

    protected EventListenerList listenerList = new EventListenerList();

    public void addColumnActionListener(TaskColumnActionListener l) {
        listenerList.add(TaskColumnActionListener.class, l);
    }

    public void removeColumnActionListener(TaskColumnActionListener l) {
        listenerList.remove(TaskColumnActionListener.class, l);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == columns.length - 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isRowSelected(int row) {
        return false;
    }

    @Override
    public void Update(ObservableSubject s) {
        if (s instanceof TaskListModel) {
            ArrayList<ApsvTask> tasks = ((TaskListModel)s).getTasks();
            Object[][] cellData = new String[tasks.size()][columns.length];

            for (int i=0; i<tasks.size(); i++) {
                ApsvTask task = tasks.get(i);
                cellData[i] = new String[]{Integer.toString(task.id), task.name, task.note, task.pushApi, ApsvTaskStatus.toString(task.status)};
            }

            TableModel tableModel = new DefaultTableModel(cellData, columns);
            this.setModel(tableModel);

            this.UpdateOthers();
        }
    }

    public void UpdateOthers() {
        TableColumnModel columnModel = this.getColumnModel();
        // 行高列宽
        this.setRowHeight(40);
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setMaxWidth(60);
        columnModel.getColumn(1).setPreferredWidth(160);
        columnModel.getColumn(1).setMaxWidth(160);
//        columnModel.getColumn(2).setPreferredWidth(200);
//        columnModel.getColumn(2).setMaxWidth(200);
        columnModel.getColumn(3).setPreferredWidth(250);
        columnModel.getColumn(3).setMaxWidth(250);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(4).setMaxWidth(120);
        columnModel.getColumn(5).setPreferredWidth(180);
        columnModel.getColumn(5).setMinWidth(180);
        columnModel.getColumn(5).setMaxWidth(180);

        // Actions columns
        new TaskActionsColumn(this, columns.length - 1, (ActionEvent e, int row, int column, TaskAction action) -> {
            logger.info("row {} col {} action {}", row, column, action);
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2) {
                if (listeners[i] == TaskColumnActionListener.class) {
                    ((TaskColumnActionListener)listeners[i+1]).actionPerformed(new ActionEvent(this, 0, ""), row, column, action);
                }
            }
        });

        this.setDragEnabled(false);
    }
}
