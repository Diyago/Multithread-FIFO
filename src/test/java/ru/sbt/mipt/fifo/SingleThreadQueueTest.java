package ru.sbt.mipt.fifo;

import org.junit.Test;

    import static org.junit.Assert.*;

/**
 * Created by Insaf on 06.12.2015.
 */
public class SingleThreadQueueTest {

    @Test
    public void testAdd() throws Exception {
        SingleThreadQueue singleThreadQueue = new SingleThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singleThreadQueue.add("item1");
        singleThreadQueue.add("item2");
        assertEquals(singleThreadQueue.element(), "item1");
    }

    @Test
    public void testPoll() throws Exception {
        SingleThreadQueue singleThreadQueue = new SingleThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singleThreadQueue.add("item1");
        singleThreadQueue.add("item2");
        assertEquals(singleThreadQueue.poll(), "item1");

        singleThreadQueue.poll();
        assertEquals(singleThreadQueue.isEmpty(), true);


    }

    @Test
    public void testElement() throws Exception {
        SingleThreadQueue singleThreadQueue = new SingleThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singleThreadQueue.add("item1");
        singleThreadQueue.add("item2");
        assertEquals(singleThreadQueue.element(), "item1");
    }

    @Test
    public void testGetLength() throws Exception {
        SingleThreadQueue singleThreadQueue = new SingleThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singleThreadQueue.add("item1");
        singleThreadQueue.add("item2");
        assertEquals(singleThreadQueue.getLength(), 2);
    }

    @Test
    public void testIsEmpty() throws Exception {
        SingleThreadQueue singleThreadQueue = new SingleThreadQueue();
        assertEquals(singleThreadQueue.isEmpty(), true);
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        SingleThreadQueue singleThreadQueue = new SingleThreadQueue();
        singleThreadQueue.add("item1");
        assertEquals(singleThreadQueue.isEmpty(), false);

    }
}