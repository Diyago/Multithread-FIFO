package ru.sbt.mipt.fifo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Insaf on 19.12.2015.
 */
public class WaitFreeQueue {
    //The Node class is to hold elements of the queues with atomic reference to the next element
    class Node {
        int value;
        AtomicReference<Node> next;
        int enqTid;
        AtomicInteger deqTid;

        Node(int val, int etid) {
            value = val;
            next = new AtomicReference<Node>(null);

            //related thread ID
            enqTid = etid; // for enqueue
            deqTid = new AtomicInteger(-1); // for dequeue
        }
    }

    // describes operation for each thread
    class OpDesc {
        long phase; // last operation on the queue
        boolean pending; //flag specifies if the thread has a pending operation
        boolean enqueue; //type of the operation
        Node node; // ref to node

        OpDesc(long ph, boolean pend, boolean enq, Node n) {
            phase = ph;
            pending = pend;
            enqueue = enq;
            node = n;
        }
    }
}
