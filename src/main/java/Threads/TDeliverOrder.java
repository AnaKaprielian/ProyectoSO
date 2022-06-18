package Threads;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import Model.DeliveryMan;
import Model.Order;
import Statistics.Statistics;

public class TDeliverOrder implements Runnable {
    Thread thread;
    private DeliveryMan deliveryMan;
    private Order order;
    

    public TDeliverOrder(DeliveryMan deliveryMan, Order order){
        thread = new Thread(this);
        thread.start();
        this.deliveryMan = deliveryMan;
        this.order = order;
    }

    @Override
    public  void run() {
        int max = 40;
        int min = 1;
        int range = max - min + 1;
        int finalDeliver = 0;
        while (TClock.getInstance().isFlag()){
            int timeDeliver = (int)(Math.random() * range);
            while(finalDeliver < timeDeliver){
                finalDeliver++;
            }
            try {
                Statistics.addDeliveryToStatistics(deliveryMan, order, timeDeliver);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
    }

    
}
