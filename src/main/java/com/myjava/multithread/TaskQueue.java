package com.myjava.multithread;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author lidexiu
 * @Date 2021/12/13
 * @Description
 */
public class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public void addTask(String s){
        this.queue.add(s);
    }

    public String getTask(){
        String s = queue.remove();
        return s;

    }
}
