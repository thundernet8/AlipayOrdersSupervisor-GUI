package com.wxq.apsv.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.enums.TaskStatus;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.model.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskDetailPane extends JPanel implements Observer {
    private RunTasksModel runTasksModel;

    private JComboBox taskSelector;

    private JButton runBtn;

    @Override
    public void Update(ObservableSubject s) {
        ArrayList<String> options = new ArrayList<>();
        if (s instanceof TaskListModel) {
            TaskListModel model = (TaskListModel)s;
            options = model.getTasks().stream().map(t -> t.name).collect(Collectors.toCollection(ArrayList::new));
        } else if (s instanceof  RunTasksModel) {
            RunTasksModel model = (RunTasksModel)s;
            if (model.getCurrentSelectTask().status == TaskStatus.STOPPED) {
                runBtn.setText("启动");
            } else {
                runBtn.setText("停止");
            }
            options = runTasksModel.getTasks().stream().map(t -> t.name).collect(Collectors.toCollection(ArrayList::new));
        }

        UpdateSelector(options);
    }

    private void UpdateSelector(ArrayList<String> options) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(options.toArray());
        taskSelector.setModel(model);
        ApsvTask currentTask = runTasksModel.getCurrentSelectTask();
        if (currentTask == null || !(options.stream().anyMatch(o -> StringUtils.equals(o, currentTask.name)))) {
            taskSelector.setSelectedIndex(-1);
            runTasksModel.SelectTask("");
        } else {
            taskSelector.setSelectedItem(currentTask.name);
        }
    }

    public TaskDetailPane(RunTasksModel model) {
        runTasksModel = model;
        InitComponents();
    }

    private void InitComponents() {
        this.setLayout(new GridLayoutManager(5, 12, new Insets(5, 10, 20, 5), -1, 5));

        // Task selector
        // TODO selector item id and display text
        add(new JLabel("选择任务"), new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, new Dimension(80, 36), new Dimension(-1, 36), 0, false));

        taskSelector = new JComboBox();
        add(taskSelector, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, new Dimension(100, -1), null, null, 0, false));

        runBtn = new JButton("启动");
        add(runBtn, new GridConstraints(0, 2, 1, 1, 8, 0, 0, 0, null, null, new Dimension(80, -1), 0, false));

        add(new JLabel(""), new GridConstraints(0, 3, 1, 1, 8, 0, 1|2, 0, null, null, null, 0, false));

        add(new JLabel(""), new GridConstraints(4, 0, 1, 12, 0, 0, 1|2, 1|2, null, null, null, 0, false));
    }

    public JButton getRunBtn() {
        return runBtn;
    }

    public JComboBox getTaskSelector() {
        return taskSelector;
    }
}
