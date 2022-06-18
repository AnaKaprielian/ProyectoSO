package Threads;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import Model.Order;
import Repository.SystemP;

public class TClock extends Thread {
    Thread thread;
    static AtomicLong counter = new AtomicLong(1);
    public static boolean flag;

    private static Semaphore semTClock = new Semaphore(0);
    private static Semaphore semTClockOrder = new Semaphore(0);
    private static Semaphore semTClockMLQ = new Semaphore(0);

    public TClock() {
        thread = new Thread(this);
        flag = true;
    }

    public static long getMoment() {
        return counter.get();
    }

    public static void releaseOrder() {
        semTClockOrder.release();
    }

    public static void releaseMLQ() {
        semTClockMLQ.release();
    }

    @Override
    public void run() {
        while (counter.get() < 200) {

            counter.getAndIncrement();
            MLQ.MLQ.releaseSemIn();
            TChargeOrders.releaseSeg();
            try {
                semTClockMLQ.acquire();
                // semTClockOrder.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // try {
            // // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
        }
        System.out.println("aca");
        SystemP.realeaseFiles();
        SystemP.hilosDelete();
        // setFlag(false);

    }

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        TClock.flag = flag;
    }

    public static void realeaseTClock() {
        semTClock.release();
    }
}
