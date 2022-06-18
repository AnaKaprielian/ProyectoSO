package Threads;
import MLQ.MLQ;
import Model.Order;
import Repository.SystemP;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TChargeOrders implements Runnable{
    Thread thread;
    TClock tClock = TClock.getInstance();
    MLQ mlq = MLQ.getInstance();
    SystemP systemP = SystemP.getInstance();
    public static TChargeOrders instance;
    public static List<Order> ordersFromFile;

    public TChargeOrders(List<Order> ordersFromFile){
        this.ordersFromFile = ordersFromFile;
        thread = new Thread(this);

        thread.start();
    }



    public static TChargeOrders getInstance() {
        if (instance == null) {
            instance = new TChargeOrders(ordersFromFile);
        }
        return instance;
    }



    @Override
    public void run() {
        while (TClock.isFlag()){
            long moment = TClock.getInstance().getMoment();
            for(Order order : ordersFromFile){
                if(order.getArriveTime() <= moment){
                    try {
                        mlq.addOrder(order);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
