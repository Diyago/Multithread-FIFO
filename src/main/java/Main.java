import ru.sbt.mipt.fifo.SingeThreadQueue;

/**
 * Created by Insaf on 07.11.2015.
 */
public class Main {
    public static void main(String[] args) {

        SingeThreadQueue singeThreadQueue = new SingeThreadQueue();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        singeThreadQueue.add("item1");
        singeThreadQueue.add("item2");
        singeThreadQueue.add("item3");


        //Removing the first item from the queue.
        System.out.println("remove: " + singeThreadQueue.poll());

        //Checking what item is first in line without removing it
        System.out.println("element: " + singeThreadQueue.element());

        //Removing the first item from the queue.
        System.out.println("poll: " + singeThreadQueue.poll());

        //Checking what item is first in line without removing it
        //If the queue is empty a null value will be returned.
        System.out.println("peek: " + singeThreadQueue.element());

        System.out.println("poll: " + singeThreadQueue.poll());
        System.out.println("poll: " + singeThreadQueue.poll());

        return;
    }
}
