package com.wxq.apsv.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.stream.Collectors;

import com.wxq.apsv.enums.TaskStatus;
import com.wxq.apsv.interfaces.*;
import com.wxq.apsv.utils.Settings;
import com.wxq.apsv.worker.ApsvTimerTask;
import com.wxq.apsv.worker.ApsvTimerManager;
import org.apache.commons.lang.StringUtils;

public class RunTasksModel implements ObservableSubject {
    private static RunTasksModel instance;

    private ApsvTask currentSelectTask;

    private TaskListModel taskListModel;

    private ArrayList<Observer> observers;

    private ArrayList<ApsvOrder> orders;

    private RunTasksModel(TaskListModel model) {
        this.taskListModel = model;
        this.observers = new ArrayList<>();
        this.orders = new ArrayList<>();

        ArrayList<ApsvTask> tasks = taskListModel.getTasks();
        if (tasks.size() > 0) {
            currentSelectTask = tasks.get(0);
        }
    }

    synchronized public static RunTasksModel getInstance() {
        if (instance == null) {
            instance = new RunTasksModel(TaskListModel.getInstance());
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

    //
    public void SelectTask(String name) {
        ApsvTask task = this.taskListModel.getTasks().stream().filter(t -> StringUtils.equals(t.name, name)).findFirst().get();
        if (currentSelectTask == null || task.id != currentSelectTask.id) {
            this.currentSelectTask = task;
            NotifyAllObservers();
        }
    }

    public ApsvTask getCurrentSelectTask() {
        return currentSelectTask;
    }

    public ArrayList<ApsvTask> getTasks() {
        return taskListModel.getTasks();
    }

    public ArrayList<ApsvOrder> getOrders() {
        // 只返回当前选中task的orders
        return orders.stream().filter(o -> o.taskId == currentSelectTask.id).collect(Collectors.toCollection(ArrayList::new));
    }

    synchronized public void AddOrder(ApsvOrder order) {
//        ArrayList<ApsvOrder> taskOrders = orders.stream().filter(o -> o.taskId == order.taskId).collect(Collectors.toCollection(ArrayList::new));
        orders.removeIf(o -> StringUtils.equals(o.tradeNo, order.tradeNo) && o.taskId == order.taskId);
        orders.add(order);
        if (order.taskId == currentSelectTask.id) {
            NotifyAllObservers();
        }
    }

    public void SwitchTaskStatus() {
        if (currentSelectTask.status == TaskStatus.STOPPED) {
            StartTask();
        } else {
            StopTask();
        }
    }

    /**
     * 开始当前任务
     */
    public void StartTask() {
        this.taskListModel.MarkTaskStatus(currentSelectTask.id, TaskStatus.RUNNING);
        NotifyAllObservers();

        // 开始抓取定时任务
        Timer timer = new Timer();
        timer.schedule(new ApsvTimerTask(currentSelectTask), 2000, Math.max(Settings.getInstance().getGrapInterval() * 1000, 30000));
        ApsvTimerManager.AddTimer(timer, currentSelectTask.id);
    }

    /**
     * 停止当前任务
     */
    public void StopTask() {
        this.taskListModel.MarkTaskStatus(currentSelectTask.id, TaskStatus.STOPPED);
        NotifyAllObservers();

        // 停止定时抓取任务
        Timer timer = ApsvTimerManager.GetTimer(currentSelectTask.id);
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
