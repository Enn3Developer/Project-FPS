package com.enn3developer.mc.thread;

import java.util.concurrent.*;

public class ThreadPoolService {
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

    private final ExecutorService executor;
    private final ExecutorCompletionService<Object> completionService;

    private int taskCounter;

    public ThreadPoolService(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.completionService = new ExecutorCompletionService<>(this.executor);
        this.taskCounter = 0;
    }

    public void execute(Runnable command) {
        this.executor.execute(command);
    }

    public void submitTask(Runnable task) {
        this.completionService.submit(task, null);
        this.taskCounter++;
    }

    public void joinAll() {
        while (this.taskCounter > 0) {
            try {
                this.completionService.take();
                this.taskCounter--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        this.executor.awaitTermination(timeout, unit);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
