package com.wxq.apsv.view;

import com.wxq.apsv.enums.OrderAction;
import com.wxq.apsv.interfaces.OrderColumnActionListener;
import com.wxq.apsv.model.ApsvOrder;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;

public class OrderActionsColumn extends AbstractCellEditor implements
        TableCellRenderer, TableCellEditor {
    private JPanel renderPanel;
    private JPanel editPanel;
    private JButton renderButton1;
    private JButton editButton1;

    public OrderActionsColumn(JTable table, int column, OrderColumnActionListener l) {
        super();
        renderPanel = new JPanel();
        editPanel = new JPanel();
        renderButton1 = new JButton();
        editButton1 = new JButton();
        editButton1.setFocusPainted(false);
        editButton1.addActionListener((ActionEvent e) -> {
            l.actionPerformed(e, table.getSelectedRow(), column, OrderAction.REPUSH);
        });

        renderPanel.add(renderButton1);

        editPanel.add(editButton1);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    public JComponent getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (StringUtils.equals(value.toString(), "1")) {
            return null;
        }
        if (hasFocus) {
            renderButton1.setForeground(table.getForeground());
            renderButton1.setBackground(UIManager.getColor("Button.background"));
        } else if (isSelected) {
            renderButton1.setForeground(table.getSelectionForeground());
            renderButton1.setBackground(table.getSelectionBackground());
        } else {
            renderButton1.setForeground(table.getForeground());
            renderButton1.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton1.setText("重新推送");
        renderButton1.setIcon(new ImageIcon(getClass().getResource("/images/icons/push.png")));
        return renderPanel;
    }

    public JComponent getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (StringUtils.equals(value.toString(), "1")) {
            return null;
        }
        editButton1.setText("重新推送");
        editButton1.setIcon(new ImageIcon(getClass().getResource("/images/icons/push.png")));
        return editPanel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}