package ru.sbt.mipt.fifo;


import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ilya on 02.12.2015.
 */
public class UnboundedQueueImpl<T> extends AbstractQueue<T> implements BlockingQueue<T> {

    private Node<T> sentinel = new Node();
    private AtomicReference<Node<T>> tail = new AtomicReference<Node<T>>(sentinel);

    public UnboundedQueueImpl() {
        
    }
    
    private static class Node<T> {
        T value;
        AtomicReference<Node<T>> next;
        Node() {
            value = null;
            next = new AtomicReference<Node<T>>(null);
        }
        Node(T v) {
            value = v;
            next = new AtomicReference<Node<T>>(null);
        }
        Node(Node<T> n) {
            value = null;
            next = new AtomicReference<Node<T>>(n);
        }
        Node(T v, Node<T> n) {
            value = v;
            next = new AtomicReference<Node<T>>(n);
        }
    }


    @Override
    // return size of Queue (numbers of Nodes)
    public int size() {
        Node<T> node = sentinel.next.get();
        int count = 0;
        while (node!=null) {
            count++;
            node = node.next.get();
        }
        return count;
    }

    @Override
    public void put(T t) throws InterruptedException {
        offer(t);
    }

    @Override
    public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public T take() throws InterruptedException {
        throw new UnsupportedOperationException("try to use poll()");
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        // TODO: 06.12.2015
        throw new UnsupportedOperationException();
    }

    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param e the element to add
     */
    public boolean offer(T t) {
        Node<T> newNode = new Node<T>(t);
        while (true) {
            final Node<T> localTail = tail.get();
            Node<T> realTail = localTail;

            while (realTail.next.get()!=null)
                realTail = realTail.next.get();

            if (realTail.next.compareAndSet(null,newNode)) {
                tail.compareAndSet(localTail, newNode);       //ignore fails
                return true;
            }
        }
    }

    @Override
    public T poll() {
        while (true) {
            final Node<T> first = sentinel.next.get();
            if (first == null)
                return null;

            if (sentinel.next.compareAndSet(first, first.next.get()))
                return first.value;
        }
    }

    @Override
    public T peek() {
        final Node<T> first = sentinel.next.get();
        if (first == null)
            return null;
        return first.value;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int drainTo(Collection<? super T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int drainTo(Collection<? super T> c, int maxElements) {
        throw new UnsupportedOperationException();
    }

    /*private static class Waiters {
        private AtomicReference<Node<Thread>> head = new AtomicReference<Node<Thread>>();

        void add() {
            while (true) {
                final Node<Thread> localHead = head.get();
                Node<Thread> newHead = new Node<Thread>(Thread.currentThread(), localHead);
                if (head.compareAndSet(localHead, newHead)) {
                    LockSupport.park();
                    return;
                }

            }
        }

    }*/
}
