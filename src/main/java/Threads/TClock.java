package Threads;

import java.util.concurrent.atomic.AtomicLong;

public class TClock implements Runnable {
    Thread thread;
    AtomicLong counter = new AtomicLong(1);
    public static TClock instance;
    public static boolean flag;

    public TClock(){
        thread = new Thread(this);
        thread.start();
        flag = true;
    }

    public static TClock getInstance() {
        if (instance == null) {
            instance = new TClock();
        }
        return instance;
    }

    public long getMoment() {
        return counter.get();
    }

    @Override
    public void run() {
            while (counter.get() < 400){

                counter.getAndIncrement();
            }
            setFlag(flag);

        }

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        TClock.flag = flag;
    }
}
