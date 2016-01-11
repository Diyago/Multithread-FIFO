package ru.sbt.mipt.fifo.performance.threads;

import java.util.Queue;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class ThroughputMeasurementThread extends MeasurementThread {
    private Queue<Integer> queue;
    private long resultSum = 0;
    private long resultCount = 0;

    public ThroughputMeasurementThread(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        int sum = 0;
        int count = 0;
        Integer value = queue.poll();
        while (value != null) {
            sum += value;
            count++;
            value = queue.poll();
        }

        //сохранить результат вычисления
        resultSum = sum;
        resultCount = count;
    }

    @Override
    public Long[] getResult() {
        return new Long[] {resultCount,resultSum};
    }


}
