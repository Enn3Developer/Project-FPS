package com.enn3developer.mc.thread;

import java.util.concurrent.*;

public class ThreadPool {
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

    private ThreadPoolService threadPool;
    private final int nThreads;
    private int updates;

    private boolean isReady;

    public ThreadPool() {
        this(NUM_CORES * 2);
    }

    public ThreadPool(int nThreads) {
        this.isReady = true;
        this.nThreads = nThreads;
        this.threadPool = new ThreadPoolService(nThreads);
    }

    private void initializeThreadPool() {
        this.threadPool.shutdown();

        try {
            threadPool.awaitTermination(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.threadPool = null;
        this.threadPool = new ThreadPoolService(this.nThreads);
    }

    public boolean update() {
        this.updates++;

        if (this.updates > 256) {
            this.updates = 0;
            initializeThreadPool();
            return true;
        }

        return false;
    }

    public void execute(Runnable command) {
        this.threadPool.execute(command);
    }

    public void submitTask(Runnable task) {
        while (!this.isReady) ;
        this.threadPool.submitTask(task);
    }

    public void joinAll() {
        this.threadPool.joinAll();
    }

    public void parallelUpdate() {
        if (!isReady) return;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                joinAll();
                update();
                isReady = true;
            }
        });

        this.isReady = false;
        thread.start();
    }
}
