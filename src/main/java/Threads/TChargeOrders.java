package Threads;

import MLQ.MLQ;
import Model.Order;
import Repository.SystemP;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public class TChargeOrders extends Thread {
    Thread thread;
    public static List<Order> ordersFromFile;
    private static Semaphore startSeg = new Semaphore(0);
    private static Semaphore mlqAdd = new Semaphore(1);

    public TChargeOrders(List<Order> ordersFromFile) {
        this.ordersFromFile = ordersFromFile;
        thread = new Thread(this);
    }

    public static void releaseSeg() {
        startSeg.release();
    }

    @Override
    public void run() {

        while (TClock.isFlag()) {
            SystemP.hilos(this);
            try {
                startSeg.acquire();
                long moment = TClock.getMoment();
                // mlqAdd.acquire();
                for (int index = 0; index < ordersFromFile.size(); index++) {
                    if (ordersFromFile.get(index).getArriveTime() <= moment) {
                        try {
                            MLQ.addOrder(ordersFromFile.get(index));
                            ordersFromFile.remove(ordersFromFile.get(index));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        TClock.releaseOrder();
                        break;
                    }
                    TClock.releaseOrder();
                }
                // mlqAdd.release();

            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }
}
