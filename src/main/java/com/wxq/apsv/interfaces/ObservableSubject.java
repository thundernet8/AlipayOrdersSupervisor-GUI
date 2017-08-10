package com.wxq.apsv.interfaces;

public interface ObservableSubject {
    public void RegisterObserver(Observer o);

    public void RemoveObserver(Observer o);

    public void NotifyAllObservers();
}
