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

    @Override
    public void run() {
        int max = 40;
        int min = 1;
        int range = max - min + 1;
        int finalDeliver = 0;

        while (deliveryMan.getDeliveryBoolean()) {
            SystemP.hilos(this);
            int timeDeliver = (int) (Math.random() * range);
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
