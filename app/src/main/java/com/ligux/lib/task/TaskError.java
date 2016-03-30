package com.ligux.lib.task;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public class TaskError extends Exception {
    public TaskError() {
        super();
    }

    public TaskError(String detailMessage) {
        super(detailMessage);
    }

    public TaskError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TaskError(Throwable throwable) {
        super(throwable);
    }
}
