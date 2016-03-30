package com.ligux.lib.task;

/**
 * Version 1.0
 * <p>
 * Date: 2016-03-30 22:03
 * Author: flzyup@ligux.com
 * <p>
 * Copyright © 2010-2016 LiGux.com
 */

/**
 * A helper class thats create {@link TaskQueue}
 */
public final class LiGuxTask {
    public static TaskQueue newTaskAndRun() {
        TaskQueue taskQueue = new TaskQueue();
        taskQueue.start();
        return taskQueue;
    }

    public static TaskQueue newTaskAndRun(int taskPoolSize) {
        TaskQueue taskQueue = new TaskQueue(taskPoolSize);
        taskQueue.start();
        return taskQueue;
    }
}
