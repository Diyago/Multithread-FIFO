package ru.sbt.mipt.fifo.performance;

import ru.sbt.mipt.fifo.UnboundedQueueImpl;
import ru.sbt.mipt.fifo.performance.threads.MyThreadFactory;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static double doLatencyMeasurement(int numberOfThreads, Queue<Integer> queue, int numberOfElements) {
        double time = addElementsToQueueInSingleThread(numberOfElements, queue, 1);
        System.out.println("Added "+numberOfElements+" elements to "+queue.getClass().getSimpleName()+" for "+time+" seconds");

        ThreadManager threadManager = new ThreadManager(numberOfThreads, MyThreadFactory.LatencyMeasurementThreads, queue);
        System.out.println("Start latency performance test for "+numberOfThreads+" threads");
        time = threadManager.getExecutionTime();
        System.out.println("Polling time "+ time);

        Long[][] result = threadManager.getResult();
        List<Double> latency = new ArrayList<>(result.length);

        for (int i = 0; i < result.length; i++) {
            if (result[i][1] == 0)
                continue;
            time = result[i][0]/Math.pow(10,9);
            latency.add(time/result[i][1]);
            System.out.println("Thread-"+i+" executes poll "+result[i][1]+" times for "+time+" seconds. Calculated latency is "+time/result[i][1]);
        }

        double average = latency.stream().mapToDouble(a -> a).average().getAsDouble();

        System.out.println("Calculated average latency("+numberOfElements+" elements and "+numberOfThreads+" threads) is "+average);
        System.out.println("-------------------------------------------------------");
        return average;
    }

    public static double doThroughputMeasurement(int numberOfThreads, Queue<Integer> queue, int numberOfElements) {
        double time = addElementsToQueueInSingleThread(numberOfElements, queue, 1);
        System.out.println("Added "+numberOfElements+" elements to "+queue.getClass().getSimpleName()+" for "+time+" seconds");

        ThreadManager threadManager = new ThreadManager(numberOfThreads, MyThreadFactory.ThroughputMeasurementThreads, queue);
        System.out.println("Start throughput performance test for "+numberOfThreads+" threads");
        time = threadManager.getExecutionTime();
        System.out.println("Polling time "+ time);

        Long[][] result = threadManager.getResult();

        for (int i = 0; i < result.length; i++) {
            System.out.println("Thread-"+i+" gets "+result[i][0]+" elements and its sum is "+result[i][1]);
        }

        double throughput = numberOfElements/time;

        System.out.println("Calculated throughput("+numberOfElements+" elements and "+numberOfThreads+" threads) is "+ throughput);
        System.out.println("-------------------------------------------------------");
        return  throughput;
    }

    /**
     *
     * @param numberOfElements
     * @param queue
     * @param element
     * @return execution time (sec)
     */
    public static double addElementsToQueueInSingleThread(int numberOfElements, Queue<Integer> queue, int element) {
        long t = System.nanoTime();
        for (int i = 0 ; i < numberOfElements; i++) {
            queue.add(element);
        }
        t = System.nanoTime() - t;
        return t/Math.pow(10, 9);

    }

    public static void main(String[] args) {
        UnboundedQueueImpl<Integer> q = new UnboundedQueueImpl();
        q.add(10);
        System.out.println(q.poll());

    }

}
