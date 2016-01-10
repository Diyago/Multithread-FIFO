package ru.sbt.mipt.fifo.performance;

import ru.sbt.mipt.fifo.UnboundedQueueImpl;
import ru.sbt.mipt.fifo.performance.threads.MyThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public class Main {

    public static void main(String[] args) {
        UnboundedQueueImpl<Integer> q = new UnboundedQueueImpl<>();
        /*long t = System.nanoTime();
        for (int i = 0 ; i < 10000000; i++) {
            q.add(1);
        }
        t = System.nanoTime() - t;
        System.out.println("Adding Time: "+t/Math.pow(10,9)+ "Size: "+ q.size());*/

        double t = QueueHelper.addElementsToQueue(30000000, q, 1);
        System.out.println("Adding Time: "+t);

        ThreadManager threadManager = new ThreadManager(8, MyThreadFactory.ThroughputMeasurementThreads, q);
        double time = threadManager.getExecutionTime();
        System.out.println("Execution time "+ time);

        Long[][] result = threadManager.getResult();

        for (int i = 0; i < result.length; i++) {
            System.out.println("Thread-"+i+" gets "+result[i][0]+" elements and its sum is "+result[i][1]);
        }

    }

}
