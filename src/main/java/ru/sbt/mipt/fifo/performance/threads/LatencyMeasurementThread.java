package ru.sbt.mipt.fifo.performance.threads;

import java.util.Queue;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class LatencyMeasurementThread extends MeasurementThread {
    public LatencyMeasurementThread(Queue<Integer> queue) {

    }

    @Override
    public Long[] getResult() {
        return new Long[0];
    }
}
