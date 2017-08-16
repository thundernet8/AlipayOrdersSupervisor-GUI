package com.wxq.apsv.model;

import java.util.ArrayList;

import com.wxq.apsv.enums.TaskStatus;
import com.wxq.apsv.utils.Settings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        if (instance == null) {
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

    /**
     * 添加任务之前检查任务数据
     * @param task
     * @return
     */
    public String ValidateAdding(ApsvTask task) {
        if (tasks.stream().anyMatch(t -> StringUtils.equals(t.name, task.name))) {
            return "任务名不能重复";
        }
        return "";
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

    public String RemoveTask(int taskIndex) {
        if (this.tasks.size() > taskIndex) {
            ApsvTask task = tasks.get(taskIndex);
            if (task.status != TaskStatus.STOPPED) {
                return "任务正在运行, 不能删除";
            }
            this.tasks.remove(taskIndex);
            NotifyAllObservers();
            this.SaveTasks();
        }
        return "";
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

    public void MarkTaskStatus(int id, TaskStatus status) {
        ApsvTask task = tasks.stream().filter(t -> t.id == id).findFirst().get();
        if (task.status != status) {
            task.status = status;
            NotifyAllObservers();
        }
    }

    public ArrayList<ApsvTask> getTasks() {
        return tasks;
    }
}
