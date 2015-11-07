package ru.sbt.mipt.fifo;

/**
 * Created by Insaf on 07.11.2015.
 */
public class FIFO implements  Queue{

    @Override
    public boolean add(Object e) {
        return false;
    }

    @Override
    public Object poll() {
        return null;
    }

    @Override
    public Object element() {
        return null;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
