package Threads;

import java.util.Random;
import java.util.concurrent.Semaphore;
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
    private static Semaphore statistics = new Semaphore(1);
    // private static Semaphore statistics = new Semaphore(1);

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
        long finalDeliver = 0;
        long timeDeliver;
        long endProcessedTime = TClock.getMoment();
        
        try {
            statistics.acquire();
            timeDeliver = timeDeliver();
            while (finalDeliver < timeDeliver) {
                finalDeliver++;
            }
            FCFSDelivery.push(deliveryMan);

            Statistics.addDeliveryToStatistics(deliveryMan, order, (timeDeliver + endProcessedTime));
            statistics.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
