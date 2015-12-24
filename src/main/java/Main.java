import ru.sbt.mipt.fifo.SingleThreadQueue;
import ru.sbt.mipt.fifo.UnboundedQueueImpl;
import ru.sbt.mipt.fifo.WaitFreeQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Collections.checkedQueue;
import static java.util.Collections.synchronizedCollection;

/**
 * Created by Insaf on 07.11.2015.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        int numberOfAdds = 100_000;

        while (numberOfAdds < 100_000_000) {
            int numberOfThread = 2;
            System.out.println("The number of elements, that will be addded: " + numberOfAdds);
            //####    TEST SINGLE THREAD QUERY    ####\\\

            SingleThreadQueue singleThreadQueue = new SingleThreadQueue();

            // measuring adding time
            long startTime = System.nanoTime();

            for (int i = 0; i < numberOfAdds; i++) {
                singleThreadQueue.add(5555555.55);
            }
            System.out.println("SINGLE THREAD QUERY adding time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

            // measuring polling time
            startTime = System.nanoTime();
            for (int i = 0; i < numberOfAdds; i++) {
                singleThreadQueue.poll();
            }

            System.out.println("SINGLE THREAD QUERY polling time: " + (System.nanoTime() - startTime) / Math.pow(10, 9));

            // checking under pressure
            singleThreadQueue = new SingleThreadQueue();

            // measuring adding time
            startTime = System.nanoTime();

            for (int i = 0; i < numberOfAdds; i++) {
                singleThreadQueue.add(heaveCalcFunc(5555555.55));
            }
            System.out.println("SINGLE THREAD QUERY adding time under pressure: " + (System.nanoTime() - startTime)
                    / Math.pow(10, 9));

            // measuring polling time
            startTime = System.nanoTime();
            for (int i = 0; i < numberOfAdds; i++) {
                singleThreadQueue.poll();
            }
            System.out.println("SINGLE THREAD QUERY polling time under pressure: " + (System.nanoTime() - startTime)
                    / Math.pow(10, 9));

            int targetNumber = numberOfAdds / numberOfThread;

            //####    TEST MULTI THREAD UNBOUNDED QUERY    ####\\\
            while (numberOfThread < 33) {
                java.util.Queue<Double> queue = new UnboundedQueueImpl<Double>();


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
                System.out.println("MULTI THREAD (" + numberOfThread + ") unbounded QUERY adding time: " +
                        (System.nanoTime() - startTime) / Math.pow(10, 9));
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
                System.out.println("MULTI THREAD (" + numberOfThread + ")unbounded QUERY polling time: " +
                        (System.nanoTime() - startTime) / Math.pow(10, 9));

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
                System.out.println("MULTI THREAD (" + numberOfThread + ")unbounded QUERY adding time under pressure: "
                        + (System.nanoTime() - startTime) / Math.pow(10, 9));
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
                System.out.println("MULTI THREAD (" + numberOfThread + ")unbounded QUERY polling time under pressure: "
                        + (System.nanoTime() - startTime) / Math.pow(10, 9));


                //####    TEST MULTI THREAD WAIT-FREE QUERY    ####\\\

                //x2 because to enq and to deq will used different threads
                WaitFreeQueue waitFreeQueue = new WaitFreeQueue(numberOfThread * 2);
                threads = new ArrayList<Thread>();
                startTime = System.nanoTime();

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
                System.out.println("MULTI THREAD (" + numberOfThread + ") WAIT-FREE QUERY adding time: " +
                        (System.nanoTime() - startTime) / Math.pow(10, 9));
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
                System.out.println("MULTI THREAD (" + numberOfThread + ") WAIT-FREE QUERY polling time: " +
                        (System.nanoTime() - startTime) / Math.pow(10, 9));

                // under pressure
                WaitFreeQueue waitFreeQueueP = new WaitFreeQueue(numberOfThread * 2);


                threads.clear();
                startTime = System.nanoTime();

                for (int i = 0; i < numberOfThread; i++) {
                    Thread addingThread;
                    addingThread = new Thread(() -> {
                        for (int j = 0; j < targetNumber; j++) {
                            waitFreeQueueP.enq(heaveCalcFunc(5555555.55));
                        }
                    });
                    threads.add(addingThread);
                }

                threads.forEach(Thread::start);

                for (Thread thread : threads) {
                    thread.join();
                }
                System.out.println("MULTI THREAD (" + numberOfThread + ") WAIT-FREE  QUERY adding time under pressure: "
                        + (System.nanoTime() - startTime) / Math.pow(10, 9));
                threads.clear();
                startTime = System.nanoTime();

                for (int i = 0; i < numberOfThread; i++) {
                    Thread addingThread;
                    addingThread = new Thread(() -> {
                        for (int j = 0; j < targetNumber; j++) {
                            waitFreeQueueP.deq();
                        }
                    });
                    threads.add(addingThread);
                }

                threads.forEach(Thread::start);

                for (Thread thread : threads) {
                    thread.join();
                }
                System.out.println("MULTI THREAD (" + numberOfThread + ") WAIT-FREE  QUERY polling time under pressure: "
                        + (System.nanoTime() - startTime) / Math.pow(10, 9));


                //####    TEST MULTI THREAD ConcurrentLinkedQueue    ####\\\

                ConcurrentLinkedQueue<Double> concurrentLinkedQueue = new ConcurrentLinkedQueue<Double>();
                threads = new ArrayList<Thread>();
                startTime = System.nanoTime();

                for (int i = 0; i < numberOfThread; i++) {
                    Thread addingThread;
                    addingThread = new Thread(() -> {
                        for (int j = 0; j < targetNumber; j++) {
                            concurrentLinkedQueue.add(5555555.55);
                        }
                    });
                    threads.add(addingThread);
                }

                threads.forEach(Thread::start);

                for (Thread thread : threads) {
                    thread.join();
                }
                System.out.println("MULTI THREAD (" + numberOfThread + ") concurrent Linked Queue adding time: " +
                        (System.nanoTime() - startTime) / Math.pow(10, 9));
                threads.clear();
                startTime = System.nanoTime();

                for (int i = 0; i < numberOfThread; i++) {
                    Thread addingThread;
                    addingThread = new Thread(() -> {
                        for (int j = 0; j < targetNumber; j++) {
                            concurrentLinkedQueue.poll();
                        }
                    });
                    threads.add(addingThread);
                }

                threads.forEach(Thread::start);

                for (Thread thread : threads) {
                    thread.join();
                }
                System.out.println("MULTI THREAD (" + numberOfThread + ") concurrent Linked Queue polling time: " +
                        (System.nanoTime() - startTime) / Math.pow(10, 9));

                // under pressure
                ConcurrentLinkedQueue<Double> concurrentLinkedQueueP = new ConcurrentLinkedQueue<Double>();


                threads.clear();
                startTime = System.nanoTime();

                for (int i = 0; i < numberOfThread; i++) {
                    Thread addingThread;
                    addingThread = new Thread(() -> {
                        for (int j = 0; j < targetNumber; j++) {
                            concurrentLinkedQueueP.add(heaveCalcFunc(5555555.55));
                        }
                    });
                    threads.add(addingThread);
                }

                threads.forEach(Thread::start);

                for (Thread thread : threads) {
                    thread.join();
                }
                System.out.println("MULTI THREAD (" + numberOfThread + ") concurrent Linked Queue adding time under pressure: "
                        + (System.nanoTime() - startTime) / Math.pow(10, 9));
                threads.clear();
                startTime = System.nanoTime();

                for (int i = 0; i < numberOfThread; i++) {
                    Thread addingThread;
                    addingThread = new Thread(() -> {
                        for (int j = 0; j < targetNumber; j++) {
                            concurrentLinkedQueueP.poll();
                        }
                    });
                    threads.add(addingThread);
                }

                threads.forEach(Thread::start);

                for (Thread thread : threads) {
                    thread.join();
                }
                System.out.println("MULTI THREAD (" + numberOfThread + ") concurrent Linked Queue polling time under pressure: "
                        + (System.nanoTime() - startTime) / Math.pow(10, 9));



                numberOfThread = numberOfThread * 2;
            }
            System.out.println("\n");
            numberOfAdds = numberOfAdds * 10;
        }


    }

    static double heaveCalcFunc(double cur_num) {
        return Math.pow(cur_num, 0.567) / 2;
    }
}

