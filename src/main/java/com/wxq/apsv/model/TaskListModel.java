package com.wxq.apsv.model;

import java.util.ArrayList;

import com.wxq.apsv.utils.Settings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.wxq.apsv.interfaces.*;

public class TaskListModel implements ObservableSubject {
    private static final Logger logger = LoggerFactory.getLogger(TaskListModel.class);

    private static TaskListModel instance;

    private ArrayList<Observer> observers;

    private ArrayList<ApsvTask> tasks;

    private TaskListModel(){
        observers = new ArrayList<>();
        tasks = new ArrayList<>();
        String savedTasksString = Settings.getInstance().getTasks();
        if (!StringUtils.isEmpty(savedTasksString)) {
            Gson gson = new Gson();
            ArrayList<String> tasksStrings = gson.fromJson(savedTasksString, ArrayList.class);
            tasksStrings.forEach(s -> {
                tasks.add(gson.fromJson(s, new TypeToken<ApsvTask>(){}.getType()));
            });
        }
    }

    public static TaskListModel getInstance() {
        if (!(instance instanceof TaskListModel)) {
            instance = new TaskListModel();
        }
        return instance;
    }

    @Override
    public void RegisterObserver(Observer o) {
        observers.add(o);
        o.Update(this);
    }

    @Override
    public void RemoveObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void NotifyAllObservers() {
        for (Observer o:observers) {
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
        this.SaveTasks();
    }

    public void RemoveTask(int taskIndex) {
        boolean remove = this.tasks.removeIf(t -> t.id == taskIndex + 1);
        if (remove) {
            NotifyAllObservers();
            this.SaveTasks();
        }
    }

    /**
     * 持久化任务列表
     */
    public void SaveTasks() {
        // https://stackoverflow.com/questions/39538677/why-does-gson-tojson-always-return-null#answer-39539133
        Gson gson = new Gson();
        java.lang.reflect.Type taskType = new TypeToken<ApsvTask>(){}.getType();
        ArrayList<String> jsons = new ArrayList<>();
        tasks.forEach(t -> {
            String json = gson.toJson(t, taskType);
            jsons.add(json);
        });
        String string = gson.toJson(jsons);
        logger.info("Saving tasks: {}", string);
        Settings.getInstance().setTasks(string);
    }

    public ArrayList<ApsvTask> getTasks() {
        return tasks;
    }
}
