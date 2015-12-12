import ru.sbt.mipt.fifo.SingeThreadQueue;
import ru.sbt.mipt.fifo.UnboundedQueueImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Insaf on 07.11.2015.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        int numberOfAdds = 1000000;
        //####    TEST SINGLE THREAD QUERY    ####\\\

        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();

        // measuring adding time
        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfAdds; i++) {
            singeThreadQueue.add(5555555.55);
        }
        System.out.println("SINGLE THREAD QUERY adding time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

        // measuring polling time
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfAdds; i++) {
            singeThreadQueue.poll();
        }

        System.out.println("SINGLE THREAD QUERY polling time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

        // checking under pressure
        singeThreadQueue = new SingeThreadQueue();

        // measuring adding time
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfAdds; i++) {
            singeThreadQueue.add(heaveCalcFunc(5555555.55));
        }
        System.out.println("SINGLE THREAD QUERY adding time under pressure: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

        // measuring polling time
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfAdds; i++) {
            singeThreadQueue.poll();
        }
        System.out.println("SINGLE THREAD QUERY polling time under pressure: " + (System.nanoTime() - startTime) / Math.pow(10, 9));


        //####    TEST MULTI THREAD UNBOUNDED QUERY    ####\\\
        java.util.Queue<Double> queue = new UnboundedQueueImpl<Double>();
        int numberOfThread = 4;
        int targetNumber = numberOfAdds / numberOfThread;

        List<Thread> threads = new ArrayList<Thread>();
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    queue.add(5555555.55);
                }
            });
            threads.add(addingThread);
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("MULTI THREAD (" + numberOfThread + ") QUERY adding time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));
        threads.clear();
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    queue.poll();
                }
            });
            threads.add(addingThread);
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("MULTI THREAD (" + numberOfThread + ") QUERY polling time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

        // under pressure

        threads.clear();
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    queue.add(heaveCalcFunc(5555555.55));
                }
            });
            threads.add(addingThread);
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("MULTI THREAD (" + numberOfThread + ") QUERY adding time under pressure: " + (System.nanoTime() - startTime) / Math.pow(10, 9));
        threads.clear();
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    queue.poll();
                }
            });
            threads.add(addingThread);
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("MULTI THREAD (" + numberOfThread + ") QUERY polling time under pressure: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

        return;
    }

    static double heaveCalcFunc(double cur_num) {
        return Math.pow(cur_num, 0.567) / 2;
    }
}

/*
SINGLE THREAD QUERY adding time: 1.03313518
SINGLE THREAD QUERY polling time: 0.024947099
SINGLE THREAD QUERY adding time under pressure: 0.133770534
SINGLE THREAD QUERY polling time under pressure: 0.012594972
MULTI THREAD (4) QUERY adding time: 0.852639727
MULTI THREAD (4) QUERY polling time: 0.2392535
MULTI THREAD (4) QUERY adding time under pressure: 0.815223576
MULTI THREAD (4) QUERY polling time under pressure: 0.017239012
 */
