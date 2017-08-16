package com.wxq.apsv.controller;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.IconUIResource;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.wxq.apsv.App;
import com.wxq.apsv.enums.*;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.view.*;
import com.wxq.apsv.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class ConfigTaskController extends JPanel implements TabController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTaskController.class);

    private JPanel wrapperPanel;
    private TaskListTable taskListTable;
    private TaskListModel taskListModel;

    private int editingTaskId = 0;

    // Form字段
    private JTextField taskNameField = new JTextField("");
    private JTextField taskNoteField = new JTextField("");
    private JTextField pushApiField = new JTextField("");
    private JTextField pushSecretField = new JTextField("");
    private JTextArea alipayCookieField = new JTextArea("");
    private JButton addTaskBtn = new JButton("添加", new ImageIcon(getClass().getResource("/images/icons/add.png")));

    @Override
    public WinTab getTabType() {
        return WinTab.CONFIGTASKS;
    }

    public ConfigTaskController() {
        this.InitViews();
        this.InitListeners();
    }

    private void InitViews() {
        // Layout设置要尽可能早
        this.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));

        this.wrapperPanel = new JPanel();
        this.wrapperPanel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), 0, 0));
        this.add(wrapperPanel, new GridConstraints(0, 0, 1, 1, 0, 3, 1|2, 1|2, null, null, null, 0, false));

        // Border
        EtchedBorder line = new EtchedBorder();
        Border border = BorderFactory.createTitledBorder(line, "任务编辑列表");
        this.wrapperPanel.setBorder(border);

        this.InitAddForm();
        this.InitTaskListTable();
    }

    private void InitAddForm() {
        alipayCookieField.setLineWrap(true);

        Map<String, JComponent> fieldsMap = new HashMap<String, JComponent>(){
            {
                put("1.任务名称", taskNameField);
                put("2.任务备注", taskNoteField);
                put("3.Push API", pushApiField);
                put("4.Push Secret", pushSecretField);
                put("5.支付宝Cookie", alipayCookieField);
                put("6", addTaskBtn);
            }
        };

        // 添加任务Form
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(fieldsMap.size(), 2, new Insets(5, 10, 20, 5), -1, 5));

        this.wrapperPanel.add(panel, new GridConstraints(0, 0, 1, 1, 0, 3, 1|2, 1|2, null, null, null, 0, false));


        Iterator entries = new TreeMap<>(fieldsMap).entrySet().iterator();
        int row = 0;
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String title = (String)entry.getKey();
            JComponent component = (JComponent)entry.getValue();

            if (component instanceof JTextArea) {
                ((JTextArea) component).setRows(3);
            }

            if (!(component instanceof JButton)) {
                panel.add(new JLabel(title), new GridConstraints(row, 0, 1, 1, 8, 0, 0, 0, null, new Dimension(120, -1), null, 0, false));
            }

            panel.add(component, new GridConstraints(row, 1, 1, 1, 8, 1, 6, 0, null, new Dimension(150, -1), null, 0, false));

            row++;
        }
    }

    private void InitTaskListTable() {
        // 任务列表Table
        this.taskListTable = new TaskListTable();
        JScrollPane scrollPane = new JScrollPane(taskListTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40, 0));
        this.wrapperPanel.add(scrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(600, 50), null, 0, false));

        this.taskListTable.setAutoCreateColumnsFromModel(true);

        // Data Rows
        taskListTable.setModel(new DefaultTableModel(null, taskListTable.getColumns()));

        // Actions
        new TaskActionsColumn(taskListTable, taskListTable.getColumns().length - 1, (ActionEvent e, int row, int column, TaskAction action) -> {
            logger.info("row {} col {} action {}", row, column, action);
        });

        // 注册Observer
        this.taskListModel = TaskListModel.getInstance();
        this.taskListModel.RegisterObserver(this.taskListTable);
    }

    private void InitListeners() {
        // Add task button
        this.addTaskBtn.addActionListener((ActionEvent e) -> {
            this.AddTask();
        });

        // Task remove/edit actions
        this.taskListTable.addColumnActionListener((ActionEvent e, int row, int column, TaskAction action) -> {
            Toolkit.getDefaultToolkit().beep();
            logger.info("row {} col {} action {}", row, column, action);
            if (column != taskListTable.getColumns().length - 1) {
                return;
            }
            if (action == TaskAction.REMOVE) {
                this.RemoveTask(row);
            } else if (action == TaskAction.EDIT) {
                this.EditingTask(row);
            }
        });
    }

    private void RenderTaskForm(ApsvTask task) {
        this.editingTaskId = task.id;
        this.taskNameField.setText(task.name);
        this.taskNoteField.setText(task.note);
        this.pushApiField.setText(task.pushApi);
        this.pushSecretField.setText(task.pushSecret);
        this.alipayCookieField.setText(task.cookie);

        if (task.id > 0) {
            this.addTaskBtn.setText("更新");
        } else {
            this.addTaskBtn.setText("添加");
        }
    }

    /**
     * 添加任务
     */
    private void AddTask() {
        logger.info("Add task");
        String _name = this.taskNameField.getText();
        String _note = this.taskNoteField.getText();
        String _api = this.pushApiField.getText();
        String _secret = this.pushSecretField.getText();
        String _cookie = this.alipayCookieField.getText();
        if (StringUtils.isEmpty(_name) || StringUtils.isEmpty(_api) || StringUtils.isEmpty(_secret) || StringUtils.isEmpty(_cookie)) {
            JOptionPane.showMessageDialog(this.wrapperPanel, "任务名, api地址, secret以及cookie不能为空");
            return;
        }
        int _id = this.editingTaskId;
        ApsvTask task = new ApsvTask(){
            {
                name = _name;
                note = _note;
                cookie = _cookie;
                pushApi = _api;
                pushSecret = _secret;
                id = _id;
            }
        };
        String validate = this.taskListModel.ValidateAdding(task);
        if (StringUtils.isEmpty(validate)) {
            this.taskListModel.AddTask(task);
            this.RenderTaskForm(new ApsvTask());
        } else {
            JOptionPane.showMessageDialog(Frame.getFrames()[0], validate, "错误", JOptionPane.YES_OPTION);
        }
    }

    /**
     * 删除任务
     * @param taskIndex
     */
    private void RemoveTask(int taskIndex) {
        int isDelete = JOptionPane.showConfirmDialog(Frame.getFrames()[0], "确认移除？", "确认",
                JOptionPane.YES_NO_OPTION);
        if (isDelete == JOptionPane.YES_OPTION) {
            logger.info("Remove task at index {}", taskIndex);
            String removeMsg = this.taskListModel.RemoveTask(taskIndex);
            if (StringUtils.isEmpty(removeMsg)) {
                this.RenderTaskForm(new ApsvTask());
            } else {
                JOptionPane.showMessageDialog(Frame.getFrames()[0], removeMsg, "错误", JOptionPane.YES_OPTION);
            }
        }
    }

    /**
     * 设置编辑任务
     * @param taskIndex
     */
    private void EditingTask(int taskIndex) {
        ApsvTask task = this.taskListModel.getTasks().get(taskIndex);
        this.RenderTaskForm(task);
    }
}
