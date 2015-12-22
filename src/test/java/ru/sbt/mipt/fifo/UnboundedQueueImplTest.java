package ru.sbt.mipt.fifo;

import org.junit.Test;
import ru.sbt.mipt.fifo.auxiliary.ConsumeWorker;
import ru.sbt.mipt.fifo.auxiliary.ProduceWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import static org.junit.Assert.*;

public class UnboundedQueueImplTest {

    @Test
    public void test1to1() throws Exception {
        java.util.Queue<Integer> queue = new UnboundedQueueImpl<Integer>();

        ProduceWorker<Integer> produceWorker = new ProduceWorker<Integer>(queue, 10) {
            int count = 1000;

            @Override
            public Integer produce() {
                if (count == 0)
                    interrupt();
                //System.out.println(count);
                return new Integer(count--);
            }
        };

        ConsumeWorker<Integer> consumeWorker = new ConsumeWorker<Integer>(queue) {
            Integer last = null;
            volatile boolean isOk = true;

            @Override
            public void consume(Integer value) {
                if (value == null) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                    return;
                }
                if (last == null) {
                    last = value;
                } else {
                    if (last - value == 1)
                        last = value;
                    else {
                        isOk = false;
                    }
                }
            }

            public Object getResult() {
                return new Boolean(isOk);
            }
        };

        produceWorker.join();
        consumeWorker.interrupt();
        assertEquals((Boolean) consumeWorker.getResult(), true);

    }

    @Test
    public void testAddSize() throws Exception {

        java.util.Queue<Integer> queue = new UnboundedQueueImpl<Integer>();
        int numberOfThread = 8;
        int targetNumber = 100;

        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < numberOfThread; i++) {
            Thread addingThread;
            addingThread = new Thread(() -> {
                for (int j = 0; j < targetNumber; j++) {
                    queue.add(j);
                }
            });
            threads.add(addingThread);
        }
        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(800, queue.size());
        assertEquals(false, queue.isEmpty());
        assertEquals((Integer) 0, queue.element());
        assertEquals((Integer) 0, queue.poll());

    }
}