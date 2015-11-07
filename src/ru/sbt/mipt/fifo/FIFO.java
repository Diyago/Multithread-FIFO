package ru.sbt.mipt.fifo;

/**
 * Created by Insaf on 07.11.2015.
 */
public class FIFO implements Queue {
    private Node frontQueue, endQueue; //begin and end nodes
    private int length; // size

    private class Node {
        Object e;
        Node next;
    }

    public FIFO() {
        frontQueue = null;
        endQueue = null;
        length = 0;
    }

    @Override
    public boolean add(Object e) {
        Node PrevEnd = endQueue;

        endQueue = new Node();
        endQueue.e = e;
        endQueue.next = null;

        if (isEmpty()) {
            frontQueue = endQueue;
        } else {
            PrevEnd.next = endQueue;
        }
        length++;

        return true;
    }

    @Override
    public Object poll() {
        Object item = frontQueue.e;
        frontQueue = frontQueue.next;

        if (isEmpty()) {
            endQueue = null;
        }
        length--;
        return item;
    }

    @Override
    public Object element() {
        if (frontQueue != null)
            return frontQueue.e;
        else
            return null;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return (length == 0);
    }
}
