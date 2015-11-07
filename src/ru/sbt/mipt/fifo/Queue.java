package ru.sbt.mipt.fifo;

/**
 * Created by Insaf on 07.11.2015.
 */
public interface Queue<Object> {

    boolean add(Object e);
     /*
     * Inserts the specified element into the end of queue if it is possible to do
     *
     * @param e the element to add
     * @return true if success, false otherwise
     * @throws Exception if fails
	 */

    Object poll();
    /*
     * Retrieves and removes the head of this queue, or null if this queue is empty.
     *
     * @param  none
     * @return the head of this queue, or null if this queue is empty
     * @throws Exception if fails
	 */

    Object element();
    /*
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @param e the element to add
     * @return Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     * @throws Exception if fails
	 */

    int length();
     /*
     * Return the length of Queue
     *
     * @param none
     * @return the length of Queue
     * @throws Exception if fails
	 */
    boolean isEmpty();
    /*
     * Returns true if queue is empty, false otherwise
     *
     * @param none
     * @return true if queue is empty, false otherwise
     * @throws Exception if fails
	 */

}
