package ru.sbt.mipt.fifo;

import org.junit.Test;

    import static org.junit.Assert.*;

/**
 * Created by Insaf on 06.12.2015.
 */
public class SingeThreadQueueTest {

    @Test
    public void testAdd() throws Exception {
        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singeThreadQueue.add("item1");
        singeThreadQueue.add("item2");
        assertEquals(singeThreadQueue.element(), "item1");
    }

    @Test
    public void testPoll() throws Exception {
        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singeThreadQueue.add("item1");
        singeThreadQueue.add("item2");
        assertEquals(singeThreadQueue.poll(), "item1");

        singeThreadQueue.poll();
        assertEquals(singeThreadQueue.isEmpty(), true);


    }

    @Test
    public void testElement() throws Exception {
        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singeThreadQueue.add("item1");
        singeThreadQueue.add("item2");
        assertEquals(singeThreadQueue.element(), "item1");
    }

    @Test
    public void testGetLength() throws Exception {
        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singeThreadQueue.add("item1");
        singeThreadQueue.add("item2");
        assertEquals(singeThreadQueue.getLength(), 2);
    }

    @Test
    public void testIsEmpty() throws Exception {
        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();
        assertEquals(singeThreadQueue.isEmpty(), true);
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();
        singeThreadQueue.add("item1");
        assertEquals(singeThreadQueue.isEmpty(), false);

    }
}