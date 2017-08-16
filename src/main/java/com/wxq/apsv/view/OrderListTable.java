package com.wxq.apsv.view;

import com.wxq.apsv.enums.OrderAction;
import com.wxq.apsv.interfaces.ObservableSubject;
import com.wxq.apsv.interfaces.Observer;
import com.wxq.apsv.interfaces.OrderColumnActionListener;
import com.wxq.apsv.model.ApsvOrder;
import com.wxq.apsv.model.RunTasksModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public final class OrderListTable extends JTable implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(OrderListTable.class);

    private String[] columns = new String[]{"交易号", "交易时间", "备注", "付款人", "金额", "交易状态", "推送状态", "操作"};

    public String[] getColumns() {
        return columns;
    }

    protected EventListenerList listenerList = new EventListenerList();

    public void addColumnActionListener(OrderColumnActionListener l) {
        listenerList.add(OrderColumnActionListener.class, l);
    }

    public void removeColumnActionListener(OrderColumnActionListener l) {
        listenerList.remove(OrderColumnActionListener.class, l);
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
    synchronized public void Update(ObservableSubject s) {
        if (s instanceof RunTasksModel) {
            ArrayList<ApsvOrder> orders = ((RunTasksModel)s).getOrders();
            Object[][] cellData = new String[orders.size()][columns.length];

            for (int i=0; i<orders.size(); i++) {
                ApsvOrder order = orders.get(i);
                cellData[i] = new String[]{order.tradeNo, order.time, order.memo, order.username, Float.toString(order.amount), order.status, order.pushed ? "推送成功" : "未推送成功", order.pushed ? "1" : "0"};
            }

            TableModel tableModel = new DefaultTableModel(cellData, columns);
            this.setModel(tableModel);

            // https://stackoverflow.com/questions/7229284/refreshing-gui-by-another-thread-in-java-swing#answer-7237224
            SwingUtilities.invokeLater(this::UpdateOthers);
        }
    }

    private void UpdateOthers() {
        // notes https://stackoverflow.com/questions/8355445/java-swing-jtable-arrayindexoutofboundsexception#answer-8355554
//        logger.info("current thread: " + (Long.toString(Thread.currentThread().getId())));
//        logger.info("In event dispatch thread? :" + SwingUtilities.isEventDispatchThread());
        TableColumnModel columnModel = this.getColumnModel();
        // 行高列宽
        this.setRowHeight(40);
        columnModel.getColumn(0).setPreferredWidth(175);
        columnModel.getColumn(0).setMaxWidth(175);
        columnModel.getColumn(1).setPreferredWidth(120);
//        columnModel.getColumn(2).setPreferredWidth(200);
//        columnModel.getColumn(2).setMaxWidth(200);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(3).setMaxWidth(100);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(4).setMaxWidth(60);
        columnModel.getColumn(5).setPreferredWidth(90);
        columnModel.getColumn(5).setMinWidth(90);
        columnModel.getColumn(5).setMaxWidth(90);
        columnModel.getColumn(7).setPreferredWidth(120);
        columnModel.getColumn(7).setMinWidth(120);

        // Actions columns
        new OrderActionsColumn(this, columns.length - 1, (ActionEvent e, int row, int column, OrderAction action) -> {
            logger.info("row {} col {} action {}", row, column, action);
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2) {
                if (listeners[i] == OrderColumnActionListener.class) {
                    ((OrderColumnActionListener)listeners[i+1]).actionPerformed(new ActionEvent(this, 0, ""), row, column, action);
                }
            }
        });

        this.setDragEnabled(false);
    }
}