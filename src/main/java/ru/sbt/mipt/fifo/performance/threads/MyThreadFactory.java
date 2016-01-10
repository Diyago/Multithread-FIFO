package ru.sbt.mipt.fifo.performance.threads;


import java.util.Queue;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public enum  MyThreadFactory {
    LatencyMeasurementThreads {
        @Override
        public MeasurementThread create(Queue<Integer> queue) {
            return new LatencyMeasurementThread(queue);
        }
    },
    ThroughputMeasurementThreads {
        @Override
        public MeasurementThread create(Queue<Integer> queue) {
            return new ThroughputMeasurementThread(queue);
        }
    };



    public abstract MeasurementThread create(Queue<Integer> queue);
}
