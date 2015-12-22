package ru.sbt.mipt.fifo;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Insaf on 20.12.2015.
 */
public class WaitFreeQueueTest {

    @Test
    public void simpleTest() throws Exception {
        WaitFreeQueue q = new WaitFreeQueue(3);

        q.enq(10);
        q.enq(20);

        Assert.assertEquals(10, q.deq(), 0);
        Assert.assertEquals(20, q.deq(), 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void checkNumberOfThreads() {
        WaitFreeQueue q = new WaitFreeQueue(2);

        // создаем 2 различных потока, которые будут пытаться вставить элемент
        Thread thread1 = new Thread(new SomeEnq(q));
        Thread thread2 = new Thread(new SomeEnq(q));

        thread1.start();
        thread2.start();

        // ждем пока они встяавят элементы
        try {
            thread1.join();
            thread2.join();
        } catch (Exception e) {
        }

        // в этом потоке (3-й поток, а очередь только на 2 потока) пытаемся вставить элемент

        q.enq(20);   // должено броситься исключение, т.к. очередь расчитана на 2 потока

    }
    @Test
    public void checkEnqDeg() throws Exception {
        int numberOfThread = 2;
        int numberOfAdds = 100_000;
        int targetNumber = numberOfAdds / numberOfThread;

        WaitFreeQueue waitFreeQueue = new WaitFreeQueue(numberOfThread);
        List<Thread> threads = new ArrayList<Thread>();
        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    waitFreeQueue.enq(5555555.55);
                }
            });
            threads.add(addingThread);
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("MULTI THREAD (" + numberOfThread + ") WAIT-FREE QUERY adding time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));
        threads.clear();
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    waitFreeQueue.deq();
                }
            });
            threads.add(addingThread);
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("MULTI THREAD (" + numberOfThread + ") WAIT-FREE QUERY polling time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

    }

    private class SomeEnq implements Runnable {
        WaitFreeQueue queue;

        public SomeEnq(WaitFreeQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            queue.enq(10);
        }
    }

}