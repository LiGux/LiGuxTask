package com.ligux.lib.task;

import android.os.Handler;
import android.os.Looper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public class TaskQueue {
    /** Used for generating monotonically-increasing sequence numbers for requests. */
    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private final Set<Task<?>> mCurrentTasks = new HashSet<>();

    private final PriorityBlockingQueue<Task<?>> mTaskQueue = new PriorityBlockingQueue<>();

    private static final int DEFAULT_TASK_THREAD_POOL_SIZE = 2;

    private final TaskDelivery mDelivery;

    private TaskDispatcher[] mDispatchers;


    protected TaskQueue(int taskPoolSize, TaskDelivery delivery) {
        mDispatchers = new TaskDispatcher[taskPoolSize];
        mDelivery = delivery;
    }

    protected TaskQueue(int taskPoolSize) {
        this(taskPoolSize, new ExecutorTaskDelivery(new Handler(Looper.getMainLooper())));
    }

    protected TaskQueue() {
        this(DEFAULT_TASK_THREAD_POOL_SIZE);
    }

    public void start() {
        stop();

        for (int i = 0; i < mDispatchers.length; i++) {
            TaskDispatcher dispatcher = new TaskDispatcher(mTaskQueue, mDelivery);
            mDispatchers[i] = dispatcher;
            dispatcher.start();
        }
    }

    public void stop() {
        for (int i = 0; i < mDispatchers.length; i++) {
            if (mDispatchers[i] != null) {
                mDispatchers[i].quit();
            }
        }
    }

    /**
     * Gets a sequence number.
     */
    public int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }

    public <T> Task<T> add(Task<T> task) {
        task.setTaskQueue(this);
        synchronized (mCurrentTasks) {
            mCurrentTasks.add(task);
        }

        task.setSequence(getSequenceNumber());

        mTaskQueue.add(task);

        return task;
    }

    /**
     * A simple predicate or filter interface for Requests, for use by
     * {@link TaskQueue#cancelAll(TaskFilter)}.
     */
    public interface TaskFilter {
        boolean apply(Task<?> task);
    }

    public void cancelAll(TaskFilter filter) {
        synchronized (mCurrentTasks) {
            for (Task<?> task : mCurrentTasks) {
                if (filter == null || filter.apply(task)) {
                    task.cancel();
                }
            }
        }
    }

    <T> void finish(Task<T> task) {
        synchronized (mCurrentTasks) {
            mCurrentTasks.remove(task);
        }
    }
}
