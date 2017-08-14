package com.wxq.apsv.interfaces;

import com.wxq.apsv.enums.OrderAction;

import java.awt.event.ActionEvent;
import java.util.EventListener;

public interface OrderColumnActionListener extends EventListener {
    void actionPerformed(ActionEvent e, int row, int col, OrderAction action);
}