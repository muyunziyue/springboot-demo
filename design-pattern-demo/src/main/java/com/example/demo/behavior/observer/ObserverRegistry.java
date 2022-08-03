package com.example.demo.behavior.observer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ldx
 * @date 2022/8/3
 */
public class ObserverRegistry {
    private ConcurrentHashMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    public void register(Object observer){

    }

    public List<ObserverAction> getMatchedObserverActions(Object event){

    }

    public
}
