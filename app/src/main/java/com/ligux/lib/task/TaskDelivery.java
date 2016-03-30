package com.ligux.lib.task;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public interface TaskDelivery {
    void postTask(Task<?> task, TaskResult<?> result);

    void postError(Task<?> task, TaskError error);
}
