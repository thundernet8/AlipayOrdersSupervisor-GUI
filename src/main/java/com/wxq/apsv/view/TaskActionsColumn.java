package com.wxq.apsv.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;

import com.wxq.apsv.interfaces.TaskColumnActionListener;
import com.wxq.apsv.enums.TaskAction;

public class TaskActionsColumn extends AbstractCellEditor implements
        TableCellRenderer, TableCellEditor {
    private JPanel renderPanel;
    private JPanel editPanel;
    private JButton renderButton1;
    private JButton renderButton2;
    private JButton editButton1;
    private JButton editButton2;

    public TaskActionsColumn(JTable table, int column, TaskColumnActionListener l) {
        super();
        renderPanel = new JPanel();
        editPanel = new JPanel();
        renderButton1 = new JButton();
        renderButton2 = new JButton();
        editButton1 = new JButton();
        editButton1.setFocusPainted(false);
        editButton1.addActionListener((ActionEvent e) -> {
            l.actionPerformed(e, table.getSelectedRow(), column, TaskAction.REMOVE);
        });
        editButton2 = new JButton();
        editButton2.setFocusPainted(false);
        editButton2.addActionListener((ActionEvent e) -> {
            l.actionPerformed(e, table.getSelectedRow(), column, TaskAction.EDIT);
        });

        renderPanel.add(renderButton1);
        renderPanel.add(renderButton2);

        editPanel.add(editButton1);
        editPanel.add(editButton2);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    public JComponent getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderButton1.setForeground(table.getForeground());
            renderButton1.setBackground(UIManager.getColor("Button.background"));
            renderButton2.setForeground(table.getForeground());
            renderButton2.setBackground(UIManager.getColor("Button.background"));
        } else if (isSelected) {
            renderButton1.setForeground(table.getSelectionForeground());
            renderButton1.setBackground(table.getSelectionBackground());
            renderButton2.setForeground(table.getSelectionForeground());
            renderButton2.setBackground(table.getSelectionBackground());
        } else {
            renderButton1.setForeground(table.getForeground());
            renderButton1.setBackground(UIManager.getColor("Button.background"));
            renderButton2.setForeground(table.getForeground());
            renderButton2.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton1.setText("移除");
        renderButton1.setIcon(new ImageIcon(getClass().getResource("/images/icons/remove.png")));
        renderButton2.setText("编辑");
        renderButton2.setIcon(new ImageIcon(getClass().getResource("/images/icons/edit.png")));
        return renderPanel;
    }

    public JComponent getTableCellEditorComponent(JTable table, Object value,
                                                  boolean isSelected, int row, int column) {
        editButton1.setText("移除");
        editButton1.setIcon(new ImageIcon(getClass().getResource("/images/icons/remove.png")));
        editButton2.setText("编辑");
        editButton2.setIcon(new ImageIcon(getClass().getResource("/images/icons/edit.png")));
        return editPanel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}