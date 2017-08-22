package com.wxq.apsv.model;

import java.util.ArrayList;
import java.util.Optional;

import com.wxq.apsv.enums.TaskStatus;
import com.wxq.apsv.utils.Settings;
import com.wxq.apsv.interfaces.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 任务列表的模型
 */
public class TaskListModel implements ObservableSubject {
    private static final Logger logger = LoggerFactory.getLogger(TaskListModel.class);

    private static TaskListModel instance;

    private ArrayList<Observer> observers;

    private ArrayList<ApsvTask> tasks;

    private TaskListModel() {
        observers = new ArrayList<>();
        tasks = new ArrayList<>();
        String savedTasksString = Settings.getInstance().getTasks();
        if (!StringUtils.isEmpty(savedTasksString)) {
            Gson gson = new Gson();
            ArrayList<String> tasksStrings = gson.fromJson(savedTasksString, ArrayList.class);
            tasksStrings.forEach(s -> {
                tasks.add(gson.fromJson(s, new TypeToken<ApsvTask>() {
                }.getType()));
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
        for (Observer o : observers) {
            o.Update(this);
        }
    }

    /**
     * 添加任务之前检查任务数据
     *
     * @param task ApsvTask
     * @return String
     */
    public String ValidateAdding(ApsvTask task) {
        // 编辑任务的情况
        if (task.id > 0) {
            if (tasks.stream().anyMatch(t -> StringUtils.equals(t.name, task.name) && t.id != task.id)) {
                return "任务名不能重复";
            }
            Optional<ApsvTask> op = tasks.stream().filter(t -> t.id == task.id).findFirst();
            if (op.isPresent()) {
                TaskStatus taskStatus = op.get().status;
                if (taskStatus != TaskStatus.STOPPED) {
                    return "不允许修改正在执行的任务，请先停止任务";
                }
            }
            return "";
        }

        // 新增任务的情况
        if (task.id == 0 && tasks.stream().anyMatch(t -> StringUtils.equals(t.name, task.name))) {
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
        java.lang.reflect.Type taskType = new TypeToken<ApsvTask>() {
        }.getType();
        ArrayList<String> jsons = new ArrayList<>();
        tasks.forEach(t -> {
            //持久化时，所有任务状态置为停止
            t.status = TaskStatus.STOPPED;
            String json = gson.toJson(t, taskType);
            jsons.add(json);
        });
        String string = gson.toJson(jsons);
        logger.info("Saving tasks: {}", string);
        Settings.getInstance().setTasks(string);
    }

    public void MarkTaskStatus(int id, TaskStatus status) {
        Optional<ApsvTask> optional = tasks.stream().filter(t -> t.id == id).findFirst();
        if (!optional.isPresent()) {
            return;
        }
        ApsvTask task = optional.get();
        if (task.status != status) {
            task.status = status;
            NotifyAllObservers();
        }
    }

    public ArrayList<ApsvTask> getTasks() {
        return tasks;
    }
}
