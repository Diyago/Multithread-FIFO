package ru.sbt.mipt.fifo.performance.threads;

import java.util.Queue;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class LatencyMeasurementThread extends MeasurementThread {
    private Queue<Integer> queue;
    private long resultTime = 0;
    private long resultCount = 0;

    public LatencyMeasurementThread(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        int count = 0;
        long time = System.nanoTime();
        Integer value = queue.poll();
        while (value != null) {
            count++;
            value = queue.poll();
        }
        time = System.nanoTime() - time;

        //сохранить результат вычисления
        resultTime = time;
        resultCount = count;
    }

    @Override
    public Long[] getResult() {
        return new Long[] {resultTime, resultCount};
    }
}
