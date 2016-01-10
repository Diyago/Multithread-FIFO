package ru.sbt.mipt.fifo.performance.threads;

/**
 * Created by Ilya3_000 on 10.01.2016.
 */
public abstract class MeasurementThread extends Thread{
    public abstract Long[] getResult();

}
