package com.ligux.lib.task;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public class TaskResult<T> {
    public final T result;

    public final TaskError error;

    public static <T> TaskResult<T> error(TaskError error) {
        return new TaskResult<T>(null, error);
    }

    public static <T> TaskResult<T> result(T result) {
        return new TaskResult<>(result, null);
    }

    public TaskResult(T result, TaskError error) {
        this.result = result;
        this.error = error;
    }

    public boolean isSuccess() {
        return error == null;
    }
}
