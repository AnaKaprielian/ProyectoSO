package Threads;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import MLQ.FCFSDelivery;
import Model.DeliveryMan;
import Model.Order;
import Repository.SystemP;
import Statistics.Statistics;

public class TDeliverOrder extends Thread {
    Thread thread;
    private DeliveryMan deliveryMan;
    private Order order;

    public TDeliverOrder(DeliveryMan deliveryMan, Order order) {
        thread = new Thread(this);
        this.deliveryMan = deliveryMan;
        this.order = order;

    }

    public int timeDeliver() {
        int max;
        int min;
        int range;
        int timeDeliver;
        int finalDeliver;
        if (order.getClient().getClientType() == 0) {
            max = 1000;
            min = 100;
            range = max - min + 1;
            timeDeliver = (int) (Math.random() * range);
        } else {
            max = 100;
            min = 0;
            range = max - min + 1;
            timeDeliver = (int) (Math.random() * range);
        }

        return timeDeliver;
    }

    @Override
    public void run() {
        int finalDeliver = 0;
        int timeDeliver;
        while (deliveryMan.getDeliveryBoolean()) {
            SystemP.hilos(this);
            timeDeliver = timeDeliver();
            while (finalDeliver < timeDeliver) {
                finalDeliver++;
            }
            FCFSDelivery.push(deliveryMan);
            try {
                Statistics.addDeliveryToStatistics(deliveryMan, order, timeDeliver);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
