package com.ligux.lib.task;

import android.os.Process;

import java.util.concurrent.BlockingQueue;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public class TaskDispatcher extends Thread {
    /** The queue of requests to service. */
    private final BlockingQueue<Task<?>> mQueue;

    /** For posting result and errors */
    private final TaskDelivery mDelivery;

    /** Used for telling us to die. */
    private volatile boolean mQuit = false;

    public TaskDispatcher(BlockingQueue<Task<?>> mQueue, TaskDelivery mDelivery) {
        this.mQueue = mQueue;
        this.mDelivery = mDelivery;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Task<?> task;

        while (true) {
            try {
                task = mQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }
            try {
                if (task.isCanceled()) {
                    task.finish("task-discard-cancelled");
                    continue;
                }

                TaskResult<?> result = task.doTask();
                mDelivery.postTask(task, result);
            } catch (Exception e) {
                e.printStackTrace();
                TaskError error = new TaskError(e);
                mDelivery.postError(task, error);
            }
        }
    }
}
