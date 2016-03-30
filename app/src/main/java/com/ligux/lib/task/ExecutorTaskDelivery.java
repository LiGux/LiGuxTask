package com.ligux.lib.task;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public class ExecutorTaskDelivery implements TaskDelivery {
    /** Used for posting event to main thread */
    private final Executor mTaskPoster;

    /**
     * Creates a new task delivery interface
     *
     * @param handler {@link Handler} to post event
     */
    public ExecutorTaskDelivery(final Handler handler) {
        this.mTaskPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void postTask(final Task<?> task, final TaskResult<?> result) {
        mTaskPoster.execute(new ResultDeliveryRunnable(task, result, null));
    }

    @Override
    public void postError(final Task<?> task, final TaskError error) {
        TaskResult<?> result = TaskResult.error(error);
        mTaskPoster.execute(new ResultDeliveryRunnable(task, result, null));
    }

    private class ResultDeliveryRunnable implements Runnable {
        private final Task mTask;
        private final TaskResult mResult;
        private final Runnable mRunnable;

        public ResultDeliveryRunnable(Task task, TaskResult result, Runnable runnable) {
            mTask = task;
            mResult = result;
            mRunnable = runnable;
        }

        @Override
        public void run() {
            if (mTask.isCanceled()) {
                mTask.finish("cancel-at-delivery");
                return;
            }

            if (mResult.isSuccess()) {
                mTask.postTask(mResult.result);
            } else {
                mTask.doTaskError(mResult.error);
            }

            if (mRunnable != null) {
                mRunnable.run();
            }
        }
    }
}
