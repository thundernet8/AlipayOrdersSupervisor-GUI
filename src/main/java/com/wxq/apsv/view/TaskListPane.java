package com.wxq.apsv.view;

import java.awt.*;
import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.model.*;

public class TaskListPane extends JPanel implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(TaskListPane.class);

    @Override
    public void Update(ObservableSubject s) {
        // TODO
        logger.info("TaskListPane update");
        if (s.getClass() == TaskListModel.class) {
            TaskListModel ts = (TaskListModel)s;
            ts.getTasks().forEach(task -> System.out.print("Task " + task.id + "; "));
            System.out.println("");
        }
    }
}
