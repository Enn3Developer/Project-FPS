package com.enn3developer.mc.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferedQueue<T> {
    private final ConcurrentLinkedQueue<T> queue;
    private final List<T> buffer;

    public BufferedQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.buffer = new ArrayList<>();
    }

    public void write(T element) {
        this.queue.add(element);
    }

    public List<T> readAll() {
        return this.buffer;
    }

    public void swap() {
        this.buffer.clear();

        T element;

        while ((element = this.queue.poll()) != null) {
            this.buffer.add(element);
        }
    }
}
