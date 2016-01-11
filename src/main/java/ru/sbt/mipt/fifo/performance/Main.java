package ru.sbt.mipt.fifo.performance;

import ru.sbt.mipt.fifo.UnboundedQueueImpl;
import ru.sbt.mipt.fifo.performance.threads.MyThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class Main {
    static final int N_ELEMENTS = 10_000_000;
    static final int WARM_UP_ELEMENTS = 1_000_000;

    public static void main(String[] args) {
        //UnboundedQueueImpl<Integer> q = new UnboundedQueueImpl<>();
        //doWarmUp(q);
        //doTest(q);

        ConcurrentLinkedQueue<Integer> c = new ConcurrentLinkedQueue<>();
        doWarmUp(c);
        doTest(c);
    }

    public static void doTest(Queue<Integer> queue) {
        System.out.println("Start performance test for "+queue.getClass().getSimpleName());
        List<Double> throughput = new ArrayList<>();
        List<Double> latency = new ArrayList<>();
        for (int i = 1; i <= 32; i *= 2) {
            throughput.add(QueueHelper.doThroughputMeasurement(i,queue, N_ELEMENTS));
            latency.add(QueueHelper.doLatencyMeasurement(i,queue,N_ELEMENTS));
        }
        System.out.println("RESULT FOR "+queue.getClass().getSimpleName()+":");
        for (int i = 0 ; i < latency.size() ; i++) {
            System.out.println(Math.pow(2,i)+" threads: throughput = "+throughput.get(i)+", latency = "+latency.get(i));
        }
        System.out.println("=================================");
    }

    public static void doWarmUp(Queue<Integer> queue) {
        System.out.println("Start warm up for "+queue.getClass().getSimpleName());
        List<Double> throughput = new ArrayList<>();
        List<Double> latency = new ArrayList<>();
        for (int i = 1; i <= 32; i *= 2) {
            throughput.add(QueueHelper.doThroughputMeasurement(i,queue, WARM_UP_ELEMENTS));
            latency.add(QueueHelper.doLatencyMeasurement(i,queue,WARM_UP_ELEMENTS));
        }
        System.out.println("WARM UP RESULT FOR "+queue.getClass().getSimpleName()+":");
        for (int i = 0 ; i < latency.size() ; i++) {
            System.out.println(Math.pow(2,i)+" threads: throughput = "+throughput.get(i)+", latency = "+latency.get(i));
        }
        System.out.println("=================================");
    }

}
