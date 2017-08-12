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

    // Form字段
    private JTextField taskNameField = new JTextField();
    private JTextField taskNoteField = new JTextField();
    private JTextField pushApiField = new JTextField();
    private JTextField pushSecretField = new JTextField();
    private JTextArea alipayCookieField = new JTextArea();
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
        this.taskListTable.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        JScrollPane scrollPane = new JScrollPane(taskListTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(40, 0));
        this.wrapperPanel.add(scrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(600, 50), null, 0, false));

//        this.taskListTable.setAutoCreateColumnsFromModel(true);

        // Columns
        taskListTable.columns = new String[]{"ID", "名称", "备注", "Push地址", "操作"};

        // Data Rows
        taskListTable.setModel(new DefaultTableModel(null, taskListTable.columns));

        // 注册Observer
        this.taskListModel = new TaskListModel();
        this.taskListModel.RegisterObserver(this.taskListTable);
        this.taskListModel.NotifyAllObservers();

        // 行高列宽
        this.taskListTable.setRowHeight(40);
        this.taskListTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.taskListTable.getColumnModel().getColumn(0).setMaxWidth(100);
        this.taskListTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        this.taskListTable.getColumnModel().getColumn(1).setMaxWidth(200);
        this.taskListTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        this.taskListTable.getColumnModel().getColumn(2).setMaxWidth(250);
        this.taskListTable.getColumnModel().getColumn(3).setPreferredWidth(250);
        this.taskListTable.getColumnModel().getColumn(3).setMaxWidth(250);

        this.updateUI();
    }

    private void InitListeners() {
        // Add task button
        this.addTaskBtn.addActionListener((ActionEvent e) -> {
            this.AddTask();
        });
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
        this.taskListModel.AddTask(new ApsvTask(){
            {
                name = _name;
                note = _note;
                cookie = _cookie;
                pushApi = _api;
                pushSecret = _secret;
            }
        });
    }
}
