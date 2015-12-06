

import org.junit.Test;
import ru.sbt.mipt.fifo.ConsumeWorker;
import ru.sbt.mipt.fifo.ProduceWorker;
import ru.sbt.mipt.fifo.UnboundedQueueImpl;

import static org.junit.Assert.*;

public class UnboundedQueueImplTest {

    @Test
    public void test1to1() throws Exception {
        java.util.Queue<Integer> q = new UnboundedQueueImpl<Integer>();

        ProduceWorker<Integer> produceWorker = new ProduceWorker<Integer>(q, 10) {
            int count = 1000;
            @Override
            public Integer produce() {
                if (count == 0)
                    interrupt();
                //System.out.println(count);
                return new Integer(count--);
            }
        };

        ConsumeWorker<Integer> consumeWorker = new ConsumeWorker<Integer>(q) {
            Integer last = null;
            volatile boolean isOk = true;
            @Override
            public void consume(Integer value) {
                if (value == null) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                    return;
                }
                if (last == null) {
                    last = value;
                } else {
                    if (last - value == 1)
                        last = value;
                    else {
                        isOk = false;
                    }
                }
            }
            public Object getResult() {
                return new Boolean(isOk);
            }
        };

        produceWorker.join();
        consumeWorker.interrupt();
        assertEquals((Boolean) consumeWorker.getResult(), true);

    }

    @Test
    public void testAddSize() throws Exception {
        /*
        java.util.Queue<Integer> q = new UnboundedQueueImpl<Integer>();

        ProduceWorker<Integer> produceWorker = new ProduceWorker<Integer>(q, 10) {
            int count = 1000;
            @Override
            public Integer produce() {
                if (count == 0)
                    interrupt();
                //System.out.println(count);
                return new Integer(count--);
            }
        };

        ConsumeWorker<Integer> consumeWorker = new ConsumeWorker<Integer>(q) {
            Integer last = null;
            volatile boolean isOk = true;
            @Override
            public void consume(Integer value) {
                if (value == null) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                    return;
                }
                if (last == null) {
                    last = value;
                } else {
                    if (last - value == 1)
                        last = value;
                    else {
                        isOk = false;
                    }
                }
            }
            public Object getResult() {
                return new Boolean(isOk);
            }
        };
        */

    }
}