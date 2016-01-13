import ru.sbt.mipt.fifo.SingleThreadQueue;
import ru.sbt.mipt.fifo.UnboundedQueueImpl;
import ru.sbt.mipt.fifo.WaitFreeQueue;
import ru.sbt.mipt.fifo.performance.QueueHelper;
import ru.sbt.mipt.fifo.performance.adapters.SingleThreadQueueAdapter;
import ru.sbt.mipt.fifo.performance.adapters.WaitFreeQueueAdapter;
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
    static final int N_ELEMENTS = 10_000_000;             //number of elements for test
    static final int WARM_UP_ELEMENTS = 3_000_000;        // -||- for warm up

    public static void main(String[] args) {
        SingleThreadQueueAdapter q0 = new SingleThreadQueueAdapter(new SingleThreadQueue());
        doWarmUp(q0);
        doTest(q0);

        //WaitFreeQueueAdapter q1 = new WaitFreeQueueAdapter(new WaitFreeQueue(32));
        //can't do that (we should count all threads and pass to the constructor
        doWaitFreeQueueTest();

        UnboundedQueueImpl<Integer> q2 = new UnboundedQueueImpl<>();
        doWarmUp(q2);
        doTest(q2);

        ConcurrentLinkedQueue<Integer> q3 = new ConcurrentLinkedQueue<>();
        doWarmUp(q3);
        doTest(q3);
    }

    public static void doTest(Queue<Integer> queue) {
        System.out.println("Start performance test for "+queue.getClass().getSimpleName());
        List<Double> throughput = new ArrayList<>();
        List<Double> latency = new ArrayList<>();
        for (int i = 1; i <= 32; i *= 2) {
            throughput.add(QueueHelper.doThroughputMeasurement(i, queue, N_ELEMENTS));
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

    public static void doWaitFreeQueueTest() {

        //warm up
        System.out.println("Start warm up for WaitFreeQueue");
        List<Double> throughput = new ArrayList<>();
        List<Double> latency = new ArrayList<>();
        for (int i = 1; i <= 32; i *= 2) {
            WaitFreeQueueAdapter queue = new WaitFreeQueueAdapter(new WaitFreeQueue(i+1));          //1 is adding thread and i are testing
            throughput.add(QueueHelper.doThroughputMeasurement(i,queue, WARM_UP_ELEMENTS));
            queue = new WaitFreeQueueAdapter(new WaitFreeQueue(i+1));                               // next text will with new threads (1 is adding thread and i are testing)
            latency.add(QueueHelper.doLatencyMeasurement(i,queue,WARM_UP_ELEMENTS));
        }
        System.out.println("WARM UP RESULT FOR WaitFreeQueue:");
        for (int i = 0 ; i < latency.size() ; i++) {
            System.out.println(Math.pow(2,i)+" threads: throughput = "+throughput.get(i)+", latency = "+latency.get(i));
        }
        System.out.println("=================================");

        //test
        System.out.println("Start performance test for WaitFreeQueue");
        throughput.clear();
        latency.clear();
        for (int i = 1; i <= 32; i *= 2) {
            WaitFreeQueueAdapter queue = new WaitFreeQueueAdapter(new WaitFreeQueue(i+1));          //1 is adding thread and i are testing
            throughput.add(QueueHelper.doThroughputMeasurement(i,queue, N_ELEMENTS));
            queue = new WaitFreeQueueAdapter(new WaitFreeQueue(i+1));                               // next text will with new threads (1 is adding thread and i are testing)
            latency.add(QueueHelper.doLatencyMeasurement(i,queue,N_ELEMENTS));
        }
        System.out.println("RESULT FOR WaitFreeQueue:");
        for (int i = 0 ; i < latency.size() ; i++) {
            System.out.println(Math.pow(2,i)+" threads: throughput = "+throughput.get(i)+", latency = "+latency.get(i));
        }
        System.out.println("=================================");

    }

}
