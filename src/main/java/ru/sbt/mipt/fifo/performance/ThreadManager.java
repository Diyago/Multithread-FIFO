package ru.sbt.mipt.fifo.performance;

import ru.sbt.mipt.fifo.performance.threads.MeasurementThread;
import ru.sbt.mipt.fifo.performance.threads.MyThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class ThreadManager  {

    private List<MeasurementThread> threads;
    public ThreadManager(int threadsNumber, MyThreadFactory factory, Queue<Integer> queue) {
        threads = new ArrayList<>(threadsNumber);
        for (int i = 0 ; i < threadsNumber; i++) {
            threads.add(factory.create(queue));
        }
    }

    /**
     * start all threads and join
     * @return execution time (sec)
     */
    public double getExecutionTime() {
        long time = System.nanoTime();
        threads.forEach(t -> t.start());
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        time = System.nanoTime() - time;
        return time/Math.pow(10,9);
    }

    public Long[][] getResult() {
        final int[] i = {0};
        Long[][] result = new Long[threads.size()][];
        threads.forEach(t -> result[i[0]++] = t.getResult());
        return result;
    }

}
