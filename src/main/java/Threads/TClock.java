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
    private static Semaphore semTClockDeliver = new Semaphore(1);
    public static Semaphore semSyncStore = new Semaphore(0);
    private static int count ;


    public TClock() {
        thread = new Thread(this);
        flag = true;
        count = 0;
    }

    public static long getMoment() {
        return count;
    }

    public static void releaseOrder() {
        semTClockOrder.release();
    }

    public static void releaseMLQ() {
        semTClockMLQ.release();
    }
    
    public static void semTClockDeliverRe(){
        semTClockDeliver.release();
    }

    public static void semaphoreDeliveryAcq() throws InterruptedException{
        semTClockDeliver.acquire();
    }

    

    @Override
    public void run() {
        while (count < 20000) {
            count++;
            // System.out.println("PEPITO "  + counter.get());
            TChargeOrders.releaseSeg();
            MLQ.releaseSemIn();
            
            semSyncStore.release();
            
            try {
                semTClockDeliver.acquire();
                for(TDeliverOrder deliverOrder : MLQ.listDeliver){
                    deliverOrder.releaseCounter();
                }
                semTClockDeliver.release();

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
