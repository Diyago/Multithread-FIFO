import ru.sbt.mipt.fifo.FIFO;

/**
 * Created by Insaf on 07.11.2015.
 */
public class Main {
    public static void main(String[] args) {

        FIFO fifo = new FIFO();

        //Using the add method to add items.
        //Should anything go wrong an exception will be thrown.
        fifo.add("item1");
        fifo.add("item2");


        //Removing the first item from the queue.
        System.out.println("remove: " + fifo.poll());

        //Checking what item is first in line without removing it
        System.out.println("element: " + fifo.element());

        //Removing the first item from the queue.
        System.out.println("poll: " + fifo.poll());

        //Checking what item is first in line without removing it
        //If the queue is empty a null value will be returned.
        System.out.println("peek: " + fifo.element());

        return;
    }
}
