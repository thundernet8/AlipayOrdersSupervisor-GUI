package com.wxq.apsv.view;

import com.wxq.apsv.interfaces.ObservableSubject;
import com.wxq.apsv.interfaces.Observer;
import com.wxq.apsv.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TaskListTable extends JTable implements Observer {
    public String[] columns;

    @Override
    public void Update(ObservableSubject s) {
        if (s instanceof TaskListModel) {
            ArrayList<ApsvTask> tasks = ((TaskListModel)s).getTasks();
            Object[][] cellData = new String[tasks.size()][columns.length];
            for (int i=0; i<tasks.size(); i++) {
                ApsvTask task = tasks.get(i);
                cellData[i] = new String[]{Integer.toString(task.id), task.name, task.note, task.pushApi};
            }

            DefaultTableModel tableModel = new DefaultTableModel(cellData, columns);
            this.setModel(tableModel);
        }
    }
}
