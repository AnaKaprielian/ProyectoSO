
package MLQ;

import java.util.LinkedList;
import java.util.Queue;

import Model.DeliveryMan;

public class FCFSDelivery {

    private static Queue<DeliveryMan> deliverys = new LinkedList();

    public static void push(DeliveryMan delivery) {
        deliverys.add(delivery);
        delivery.setDeliveryBoolean(false);
    }

    public static DeliveryMan pop() throws InterruptedException {

        DeliveryMan delivery = deliverys.poll();

        return delivery;
    }

    public boolean isEmpty() throws InterruptedException {

        boolean isEmpty = deliverys.isEmpty();

        return isEmpty;
    }

    public static DeliveryMan nextDelivery() throws InterruptedException {
        if (deliverys != null) {
            DeliveryMan newDelivery = pop();
            return newDelivery;
        }
        return null;
    }
}
