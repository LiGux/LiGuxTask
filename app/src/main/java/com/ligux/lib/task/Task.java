package com.ligux.lib.task;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

/**
 * Version 1.0
 * <p/>
 * Date: 2016-03-30 21:53
 * Author: flzyup@ligux.com
 * <p/>
 * Copyright © 2010-2016 LiGux.com
 */
public abstract class Task<T> implements Comparable<Task<T>> {
    /** Sequence number of this request, used to enforce FIFO ordering. */
    private Integer mSequence;

    /** The task queue this task is associated with. */
    private TaskQueue mTaskQueue;
    private boolean mCanceled;

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public int compareTo(Task<T> another) {
        Priority left = this.getPriority();
        Priority right = another.getPriority();

        // High-priority requests are "lesser" so they are sorted to the front.
        // Equal priorities are sorted by sequence number to provide FIFO ordering.
        return left == right ?
                this.mSequence - another.mSequence :
                right.ordinal() - left.ordinal();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @WorkerThread
    protected abstract TaskResult<T> doTask() throws Exception;

    @MainThread
    protected abstract void doTaskError(TaskError error);

    @MainThread
    protected abstract void postTask(T result);


    public Task<?> setTaskQueue(TaskQueue taskQueue) {
        mTaskQueue = taskQueue;
        return this;
    }

    /**
     * Sets the sequence number of this request.  Used by {@link TaskQueue}.
     *
     * @return This Request object to allow for chaining.
     */
    public final Task<?> setSequence(int sequence) {
        mSequence = sequence;
        return this;
    }

    /**
     * Returns the sequence number of this request.
     */
    public final int getSequence() {
        if (mSequence == null) {
            throw new IllegalStateException("getSequence called before setSequence");
        }
        return mSequence;
    }

    /**
     * Mark this request as canceled.  No callback will be delivered.
     */
    public void cancel() {
        mCanceled = true;
    }

    /**
     * Returns true if this request has been canceled.
     */
    public boolean isCanceled() {
        return mCanceled;
    }

    void finish(final String tag) {
        if (mTaskQueue != null) {
            mTaskQueue.finish(this);
            onFinish();
        }
    }

    protected void onFinish() {
        // Error listener to null
    }
}
