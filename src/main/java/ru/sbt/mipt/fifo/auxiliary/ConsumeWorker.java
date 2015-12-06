package ru.sbt.mipt.fifo.auxiliary;

/**
 * Created by Илья on 03.12.2015.
 */
public abstract class ConsumeWorker<T> extends Thread implements Consumer<T> {
    private java.util.Queue<T> queue;

    public ConsumeWorker(java.util.Queue<T> queue) {
        this.queue = queue;
        start();
    }

    @Override
    public void run() {

        while (!isInterrupted()) {
            T value = queue.poll();
            consume(value);
        }
    }

    private volatile Object result;

    public Object getResult() {
        return result;
    }

}
