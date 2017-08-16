package com.wxq.apsv.interfaces;

import com.wxq.apsv.enums.TaskAction;

import java.awt.event.ActionEvent;
import java.util.EventListener;

public interface TaskColumnActionListener extends EventListener {
    void actionPerformed(ActionEvent e, int row, int col, TaskAction action);
}
