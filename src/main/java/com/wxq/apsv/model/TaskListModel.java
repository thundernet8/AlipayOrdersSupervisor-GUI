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
        o.Update(this);
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
        if (task.id > 0) {
            this.tasks.removeIf(t -> t.id == task.id);
        } else {
            task.id = this.tasks.size() + 1;
        }
        logger.debug("Add task with id: {}", task.id);
        this.tasks.add(task);
        NotifyAllObservers();
    }

    public void RemoveTask(int taskIndex) {
        boolean remove = this.tasks.removeIf(t -> t.id == taskIndex + 1);
        if (remove) {
            NotifyAllObservers();
        }
    }

    public ArrayList<ApsvTask> getTasks() {
        return tasks;
    }
}
