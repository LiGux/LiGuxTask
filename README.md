# LiGuxTask - A task library for doing background jobs

LiGuxTask is a library which influenced by Volley library. You can do background job like Volley style. The basis is thread pool.


The simple way to get the TaskQueue as the following:

```java
TaskQueue taskQueue = LiGuxTask.newTaskAndRun();
```

Then you will get a task queue which has default thread pool size ```
2``` and the task queue will start after call this method.

*After you do all job and you must finish the task queue or the queue will remain in the memory until you kill the process! Notice*

```java
taskQueue.stop()
```

This will try to interrupt all background threads in the thread pool.

## TODO: 
+ add maven repository 
+ add more features