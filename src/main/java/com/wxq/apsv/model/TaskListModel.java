package com.wxq.apsv.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxq.apsv.interfaces.*;

public class TaskListModel implements ObservableSubject {
    private static final Logger logger = LoggerFactory.getLogger(TaskListModel.class);

    private ArrayList<Observer> configTaskViews;

    private ArrayList<ApsvTask> tasks;

    public TaskListModel(){
        configTaskViews = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    @Override
    public void RegisterObserver(Observer o) {
        configTaskViews.add(o);
    }

    @Override
    public void RemoveObserver(Observer o) {
        configTaskViews.remove(o);
    }

    @Override
    public void NotifyAllObservers() {
        for (Observer o:configTaskViews) {
            o.Update(this);
        }
    }

    public void AddTask(ApsvTask task) {
        logger.debug("Add task with id: {}", task.id);
        this.tasks.add(task);
        NotifyAllObservers();
    }

    public void RemoveTask(ApsvTask task) {
        boolean remove = this.tasks.removeIf(t -> t.id == task.id);
        if (remove) {
            NotifyAllObservers();
        }
    }

    public ArrayList<ApsvTask> getTasks() {
        return tasks;
    }
}
