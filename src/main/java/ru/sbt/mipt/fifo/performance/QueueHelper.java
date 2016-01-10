package ru.sbt.mipt.fifo.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class QueueHelper {
    private static final int N = 5;
    private static ExecutorService executor = Executors.newFixedThreadPool(N);

    public static double addElementsToQueue(int numberOfElements, Queue<Integer> queue, int element) {
        int part = numberOfElements / N;
        int residual = numberOfElements % N;

        List<Future> list = new ArrayList<>();

        long time = System.nanoTime();

        list.add(executor.submit(new DoAdd(part+residual, queue, element)));
        for (int i = 1; i < N; i++) {
            list.add(executor.submit(new DoAdd(part, queue, element)));
        }

        //wait for execution ends
        list.forEach(t -> {
            try {
                t.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        time = System.nanoTime() - time;
        return time/Math.pow(10,9);

    }

    private static class DoAdd implements Runnable {
        int numberOfElements;
        Queue<Integer> queue;
        int element;

        public DoAdd(int numberOfElements, Queue<Integer> queue, int element) {
            this.numberOfElements = numberOfElements;
            this.queue = queue;
            this.element = element;
        }

        @Override
        public void run() {
            for (int i = 0 ; i< numberOfElements; i++) {
                queue.add(element);
            }
        }
    }
}
