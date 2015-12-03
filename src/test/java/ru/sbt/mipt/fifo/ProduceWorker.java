package ru.sbt.mipt.fifo;

import java.util.*;
import java.util.Queue;

/**
 * Created by Светлана on 03.12.2015.
 */
public abstract class ProduceWorker<T> extends Thread implements Producer<T>{

    private Queue<T> queue;
    private long waitTime = 0;

    public ProduceWorker(Queue<T> queue, long idleTime) {
        this.queue = queue;
        this.waitTime = idleTime;
        start();
    }

    @Override
    public void run() {

        while (!isInterrupted()) {
            T value = produce();
            queue.offer(value);
            if (waitTime>0)
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    return;
                }
        }
    }
}
