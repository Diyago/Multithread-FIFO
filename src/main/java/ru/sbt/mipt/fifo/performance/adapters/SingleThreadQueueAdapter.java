package ru.sbt.mipt.fifo.performance.adapters;

import ru.sbt.mipt.fifo.SingleThreadQueue;
import ru.sbt.mipt.fifo.WaitFreeQueue;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Created by Светлана on 13.01.2016.
 */
public class SingleThreadQueueAdapter extends AbstractQueue<Integer> {
    private SingleThreadQueue singleThreadQueue;

    public SingleThreadQueueAdapter(SingleThreadQueue singleThreadQueue) {
        this.singleThreadQueue = singleThreadQueue;
    }

    @Override
    public Iterator<Integer> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return singleThreadQueue.getLength();
    }

    @Override
    public boolean offer(Integer integer) {
        return singleThreadQueue.add(integer);

    }

    @Override
    public Integer poll() {
        return (Integer)singleThreadQueue.poll();
    }

    @Override
    public Integer peek() {
        throw new UnsupportedOperationException();
    }
}
