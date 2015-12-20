package ru.sbt.mipt.fifo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by Insaf on 19.12.2015.
 */
public class WaitFreeQueue {

    AtomicReference<Node> head, tail;
    AtomicReferenceArray<OpDesc> state;
    Integer NUM_THRDS;

    public WaitFreeQueue(Integer NUM_THRDS) {
        // // TODO: 20.12.2015 constructor to be updated
        this.NUM_THRDS = NUM_THRDS;
    }

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

    /// enqueue operations:

    void enq(int value) {
        long phase = maxPhase() + 1;
        state.set(TID, new OpDesc(phase, true, true, new Node(value, TID)));
        help(phase);
        helpFinishEnq();
    }

    void helpEnq(int tid, long phase) {
        while (isStillPending(tid, phase)) {
            Node last = tail.get();
            Node next = last.next.get();
            if (last == tail.get()) {
                if (next == null) { //enqueue can be applied
                    if (isStillPending(tid, phase)) {
                        if (last.next.compareAndSet(next, state.get(tid).node)) {

                            helpFinishEnq();
                            return;
                        }
                    }
                } else {  //some enqueue is in progress
                    helpFinishEnq();//help it first, then retry
                }
            }
        }
    }

    void helpFinishEnq() {
        Node last = tail.get();
        Node next = last.next.get();
        if (next != null) {
            int tid = next.enqTid;//read enqTid of the last element
            OpDesc curDesc = state.get(tid);
            if (last == tail.get() && state.get(tid).node == next) {
                OpDesc newDesc = new
                        OpDesc(state.get(tid).phase, false, true, next);
                state.compareAndSet(tid, curDesc, newDesc);
                tail.compareAndSet(last, next);
            }
        }
    }

    ///  deenqueue functions

    int deq() throws IllegalStateException {
        long phase = maxPhase() + 1;
        state.set(TID, new OpDesc(phase, true, false, null));
        help(phase);
        helpFinishDeq();
        Node node = state.get(TID).node;
        if (node == null) {
            throw new IllegalStateException();
        }
        return node.next.get().value;
    }

    void helpDeq(int tid, long phase) {
        while (isStillPending(tid, phase)) {
            Node first = head.get();
            Node last = tail.get();
            Node next = first.next.get();
            if (first == head.get()) {
                if (first == last) { // queue might be empty
                    if (next == null) { // queue is empty
                        OpDesc curDesc = state.get(tid);
                        if (last == tail.get() && isStillPending(tid, phase)) {
                            OpDesc newDesc = new
                                    OpDesc(state.get(tid).phase, false, false, null);
                            state.compareAndSet(tid, curDesc, newDesc);
                        }
                    } else { // some enqueue is in progress
                        helpFinishEnq(); // help it first, then retry
                    }
                } else { // queue is not empty
                    OpDesc curDesc = state.get(tid);
                    Node node = curDesc.node;
                    if (!isStillPending(tid, phase)) break;
                    if (first == head.get() && node != first) {
                        OpDesc newDesc = new
                                OpDesc(state.get(tid).phase, true, false, first);
                        if (!state.compareAndSet(tid, curDesc, newDesc)) {
                            continue;
                        }
                    }
                    first.deqTid.compareAndSet(-1, tid);
                    helpFinishDeq();
                }
            }
        }
    }

    void helpFinishDeq() {
        Node first = head.get();
        Node next = first.next.get();
        int tid = first.deqTid.get();// read deqTid of the first element
        if (tid != -1) {
            OpDesc curDesc = state.get(tid);
            if (first == head.get() && next != null) {
                OpDesc newDesc = new
                        OpDesc(state.get(tid).phase, false, false,
                        state.get(tid).node);
                state.compareAndSet(tid, curDesc, newDesc);
                head.compareAndSet(first, next);
            }
        }
    }


    // Auxiliary methods
    boolean isStillPending(int tid, long ph) {
        return state.get(tid).pending && state.get(tid).phase <= ph;
    }

    void WFQueue() {
        Node sentinel = new Node(-1, -1);
        head = new AtomicReference<Node>(sentinel);
        tail = new AtomicReference<Node>(sentinel);
        state = new AtomicReferenceArray<OpDesc>(NUM_THRDS);
        for (int i = 0; i < state.length(); i++) {
            state.set(i, new OpDesc(-1, false, true, null));
        }
    }

    void help(long phase) {
        for (int i = 0; i < state.length(); i++) {
            OpDesc desc = state.get(i);
            if (desc.pending && desc.phase <= phase) {
                if (desc.enqueue) {
                    helpEnq(i, phase);
                } else {
                    helpDeq(i, phase);
                }
            }
        }
    }

    long maxPhase() {
        long maxPhase = -1;
        for (int i = 0; i < state.length(); i++) {
            long phase = state.get(i).phase;
            if (phase > maxPhase) {
                maxPhase = phase;
            }
        }
        return maxPhase;
    }


}
