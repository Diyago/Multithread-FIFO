package ru.sbt.mipt.fifo;


/**
 * Created by Insaf on 08.11.2015.
 */
public interface Lock {
    Lock readLock();

    Lock writeLock();
}
