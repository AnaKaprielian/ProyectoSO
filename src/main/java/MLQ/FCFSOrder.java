package MLQ;

import Model.Order;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class FCFSOrder {
    private Queue<Order> orders = new LinkedList<>();
    private Semaphore mutex = new Semaphore(1);

    public void push(Order order) throws InterruptedException {
        mutex.acquire();
        if (!orders.contains(order)) {
            orders.add(order);
        }
        mutex.release();
    }

    public Order pop() throws InterruptedException {
        mutex.acquire();
        Order order = orders.poll();
        mutex.release();
        return order;
    }

    public boolean isEmpty() throws InterruptedException {
        mutex.acquire();
        boolean isEmpty = orders.isEmpty();
        mutex.release();
        return isEmpty;
    }
}
