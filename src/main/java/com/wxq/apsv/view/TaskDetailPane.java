package com.wxq.apsv.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.enums.TaskStatus;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.model.*;
import com.wxq.apsv.worker.ApsvTimerManager;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * 任务选择,启停操作以及状态信息面板
 */
public class TaskDetailPane extends JPanel implements Observer {
    private RunTasksModel runTasksModel;

    private JPanel topWrapperPanel;

    private JPanel botWrapperPanel;

    private JComboBox taskSelector;

    private JLabel orderCountLabel;

    private JLabel orderPushedCountLabel;

    private JButton runBtn;

    private JLabel elapseTimeLabel;

    private java.util.Timer elapseTimeTimer;

    @Override
    synchronized public void Update(ObservableSubject s) {
        ArrayList<ApsvTask> tasks = new ArrayList<>();
        if (s instanceof TaskListModel) {
            TaskListModel model = (TaskListModel)s;
            tasks = model.getTasks();
        } else if (s instanceof  RunTasksModel) {
            RunTasksModel model = (RunTasksModel)s;
            ApsvTask currentTask = model.getCurrentSelectTask();
            if (currentTask == null) {
                runBtn.setVisible(false);
            } else if (currentTask.status == TaskStatus.STOPPED) {
                runBtn.setText("启动");
                runBtn.setVisible(true);
            } else {
                runBtn.setText("停止");
                runBtn.setVisible(true);
            }
            tasks = runTasksModel.getTasks();
        }

        ArrayList<String> options = tasks.stream().map(t -> t.name).collect(Collectors.toCollection(ArrayList::new));

        SwingUtilities.invokeLater(() -> {
            UpdateSelector(options);
            UpdateNums();
            UpdateElapseTime();
        });
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

    private void UpdateNums() {
        ArrayList<ApsvOrder> orders = runTasksModel.getOrders();
        int taskCount = orders.size();
        long pushedTaskCount = orders.stream().filter(o -> o.pushed).count();
        orderCountLabel.setText(Integer.toString(taskCount));
        orderPushedCountLabel.setText(Long.toString(pushedTaskCount));
    }

    private void UpdateElapseTime() {
        ApsvTask currentTask = runTasksModel.getCurrentSelectTask();
        if (elapseTimeTimer != null )
            elapseTimeTimer.cancel();
        if (currentTask != null && currentTask.status != TaskStatus.STOPPED) {
            elapseTimeTimer = new java.util.Timer();
            elapseTimeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String elapse = ApsvTimerManager.GetElapseTime(currentTask.id);
                    elapseTimeLabel.setText("运行时间 " + elapse);
                }
            }, 200, 1000);
        } else {
            elapseTimeLabel.setText("");
        }
    }

    public TaskDetailPane(RunTasksModel model) {
        runTasksModel = model;
        InitComponents();
    }

    private void InitComponents() {
        this.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, 10));

        topWrapperPanel = new JPanel();
        botWrapperPanel = new JPanel();
        // Border
        EtchedBorder line = new EtchedBorder();
        Border border1 = BorderFactory.createTitledBorder(line, "任务状态");
        Border border2 = BorderFactory.createTitledBorder(line, "订单列表");
        topWrapperPanel.setBorder(border1);
        botWrapperPanel.setBorder(border2);
        topWrapperPanel.setLayout(new GridLayoutManager(2, 12, new Insets(5, 5, 20, 5), -1, 5));
        botWrapperPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 5, 10, 5), -1, 5));
        add(topWrapperPanel, new GridConstraints(0, 0, 1, 1, 8, 1, 0, 0, null, new Dimension(-1, 100), null, 0, false));
        add(botWrapperPanel, new GridConstraints(1, 0, 1, 1, 8, 3, 0, 1|2, null, null, null, 0, false));


        // Task selector label (Column 1)
        // TODO selector item id and display text
        topWrapperPanel.add(new JLabel("选择任务"), new GridConstraints(0, 0, 2, 1, 8, 0, 0, 0, null, new Dimension(80, 36), new Dimension(-1, 36), 0, false));

        // Task selector (Column 2)
        taskSelector = new JComboBox();
        topWrapperPanel.add(taskSelector, new GridConstraints(0, 1, 2, 1, 8, 0, 0, 0, new Dimension(100, -1), null, null, 0, false));

        // Run/Stop action button (Column 3)
        runBtn = new JButton("启动");
        topWrapperPanel.add(runBtn, new GridConstraints(0, 2, 2, 1, 8, 0, 0, 0, null, null, new Dimension(80, -1), 0, false));

        // Space label (Column 4)
        topWrapperPanel.add(new JLabel(""), new GridConstraints(0, 3, 2, 1, 8, 0, 1|2, 0, null, null, null, 0, false));

        // Orders count label (Column 5)
        orderCountLabel = new JLabel("0");
        orderCountLabel.setFont(new Font("", Font.BOLD, 36));
        orderCountLabel.setForeground(Color.yellow);
        topWrapperPanel.add(orderCountLabel, new GridConstraints(0, 4, 2, 1, 8, 0, 0, 0, null, null, null, 0, false));

        // Orders count extra label (Column 6)
        topWrapperPanel.add(new JLabel("抓取订单"), new GridConstraints(0, 5, 2, 1, 8, 0, 0, 0, null, null, null, 0, false));

        // Pushed orders count label (Column 7)
        orderPushedCountLabel = new JLabel("0");
        orderPushedCountLabel.setFont(new Font("", Font.BOLD, 36));
        orderPushedCountLabel.setForeground(Color.green);

        topWrapperPanel.add(orderPushedCountLabel, new GridConstraints(0, 6, 2, 1, 8, 0, 0, 0, null, null, null, 0, false));

        // Pushed orders count extra label (Column 8)
        topWrapperPanel.add(new JLabel("推送成功"), new GridConstraints(0, 7, 2, 1, 8, 0, 0, 0, null, null, null, 0, false));

        // Space label (Column 9)
        topWrapperPanel.add(new JLabel(""), new GridConstraints(0, 8, 1, 1, 8, 0, 1|2, 0, null, null, null, 0, false));

        // Elapse time label (Column 10)
        elapseTimeLabel = new JLabel();
        elapseTimeLabel.setFont(new Font("", Font.PLAIN, 24));
        topWrapperPanel.add(elapseTimeLabel, new GridConstraints(0, 9, 2, 1, 8, 0, 0, 0, null, null, null, 0, false));

        // 右端弹性空间 (Column 12)
        topWrapperPanel.add(new JLabel(""), new GridConstraints(0, 11, 1, 1, 8, 0, 1|2, 0, null, null, null, 0, false));

        // 底部弹性空间
//        topWrapperPanel.add(new JLabel(""), new GridConstraints(1, 0, 1, 4, 0, 0, 1|2, 1|2, null, null, null, 0, false));
    }

    public JButton getRunBtn() {
        return runBtn;
    }

    public JComboBox getTaskSelector() {
        return taskSelector;
    }

    public JPanel getBotWrapperPanel() {
        return botWrapperPanel;
    }
}
