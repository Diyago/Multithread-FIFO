package ru.sbt.mipt.fifo;

/**
 * Created by Ilya on 03.12.2015.
 */
public interface Consumer<T> {
    void consume(T value);
}
