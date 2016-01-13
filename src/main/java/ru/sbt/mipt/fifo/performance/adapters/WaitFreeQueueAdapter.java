package ru.sbt.mipt.fifo.performance.adapters;

import ru.sbt.mipt.fifo.WaitFreeQueue;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by Светлана on 13.01.2016.
 */
public class WaitFreeQueueAdapter extends AbstractQueue<Integer> {
    private WaitFreeQueue waitFreeQueue;

    public WaitFreeQueueAdapter(WaitFreeQueue waitFreeQueue) {
        this.waitFreeQueue = waitFreeQueue;
    }

    @Override
    public Iterator<Integer> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw  new UnsupportedOperationException();
    }

    @Override
    public boolean offer(Integer integer) {
        waitFreeQueue.enq(integer);
        return true;
    }

    @Override
    public Integer poll() {
        Double value = waitFreeQueue.deq();
        return value==null?null:value.intValue();
    }

    @Override
    public Integer peek() {
        throw new UnsupportedOperationException();
    }
}
