package Threads;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import MLQ.MLQ;
import Model.Order;
import Repository.SystemP;

public class TClock extends Thread {
    Thread thread;
    static AtomicLong counter = new AtomicLong(1);
    public static boolean flag;
    
    private static Semaphore semTClock = new Semaphore(0);
    private static Semaphore semTClockOrder = new Semaphore(0);
    private static Semaphore semTClockMLQ = new Semaphore(0);
    public static Semaphore semSyncStore = new Semaphore(0);
    
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
        while (counter.get() < 200000) {
            counter.getAndIncrement();
            // System.out.println("PEPITO "  + counter.get());
            TChargeOrders.releaseSeg();
            MLQ.releaseSemIn();
            semSyncStore.release();
            try {
                semTClockMLQ.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        SystemP.releaseFiles();
        // SystemP.hilosDelete();
        setFlag(false);

    }

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        TClock.flag = flag;
    }

    public static void TClock() {
        semTClock.release();
    }
}
