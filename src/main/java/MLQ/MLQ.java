package MLQ;

import Model.DeliveryMan;
import Model.Order;
import Repository.SystemP;
import Statistics.Statistics;
import Threads.TStore;
import Threads.TClock;
import Threads.TDeliverOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class MLQ extends Thread {
    private static FCFSOrder vipOrders = new FCFSOrder();
    private static FCFSOrder fastFoodOrders = new FCFSOrder();
    private static FCFSOrder mediumFoodOrders = new FCFSOrder();
    private static FCFSOrder slowFoodOrders = new FCFSOrder();
    public static Queue<Order> vipOrdersReady = new LinkedList<>();
    public static Queue<Order> fastFoodOrdersReady = new LinkedList<>();
    public static Queue<Order> mediumFoodOrdersReady = new LinkedList<>();
    public static Queue<Order> slowFoodOrdersReady = new LinkedList<>();
    private Semaphore vipSemaphore = new Semaphore(0);
    private Semaphore fastSemaphore = new Semaphore(0);
    private Semaphore mediumSemaphore = new Semaphore(0);
    private Semaphore slowSemaphore = new Semaphore(0);
    public static Semaphore vipSemaphoreReady = new Semaphore(1);
    public static Semaphore fastSemaphoreReady = new Semaphore(1);
    public static Semaphore mediumSemaphoreReady = new Semaphore(1);
    public static Semaphore slowSemaphoreReady = new Semaphore(1);
    private Semaphore vipSemaphoreIn = new Semaphore(1);
    private Semaphore fastSemaphoreIn = new Semaphore(1);
    private Semaphore mediumSemaphoreIn = new Semaphore(1);
    private Semaphore slowSemaphoreIn = new Semaphore(1);
    private Semaphore deliverySemaphoreIn = new Semaphore(1);
    private static Semaphore startSeg = new Semaphore(0);
    private static Semaphore mutex = new Semaphore(1);
    public static List<TDeliverOrder> listDeliver = new LinkedList<>();


    private FCFSOrder[] queuesPlanner = {
            vipOrders, fastFoodOrders, mediumFoodOrders, slowFoodOrders
    };

    private final Semaphore solicitudes = new Semaphore(0);
    private final Semaphore solOrder = new Semaphore(0);
    private final Semaphore order = new Semaphore(0);

    public static void addOrder(Order order) throws InterruptedException {

        int serviceTime = order.getOrderDescription().getServiceTime();
        int vipNumber = order.getClient().getClientType();
        if (vipNumber == 1) {

            vipOrders.push(order);
        } else {
            if (serviceTime < 20) {
                fastFoodOrders.push(order);

            } else if (serviceTime < 40) {
                mediumFoodOrders.push(order);
            } else {
                slowFoodOrders.push(order);
            }
        }
    }

    public Order nextOrder() throws InterruptedException {
        for (FCFSOrder queue : queuesPlanner) {
            Order order = queue.pop();
            if (order != null) {
                return order;
            }
        }
        return null;
    }

    public static Order nextOrder(FCFSOrder orders) throws InterruptedException {
        Order order = orders.pop();
        if (order != null) {
            return order;
        }
        return null;
    }

    public static void releaseSemIn() {
        startSeg.release();
    }

    public FCFSOrder getFastFoodOrders() {
        return fastFoodOrders;
    }

    public void setFastFoodOrders(FCFSOrder fastFoodOrders) {
        MLQ.fastFoodOrders = fastFoodOrders;
    }

    public FCFSOrder getMediumFoodOrders() {
        return mediumFoodOrders;
    }

    public void setMediumFoodOrders(FCFSOrder mediumFoodOrders) {
        MLQ.mediumFoodOrders = mediumFoodOrders;
    }

    public FCFSOrder getSlowFoodOrders() {
        return slowFoodOrders;
    }

    public void setSlowFoodOrders(FCFSOrder slowFoodOrders) {
        MLQ.slowFoodOrders = slowFoodOrders;
    }

    public FCFSOrder getVipOrders() {
        return vipOrders;
    }

    public void setVipOrders(FCFSOrder vipOrders) {
        MLQ.vipOrders = vipOrders;
    }

    public FCFSOrder[] getQueuesPlanner() {
        return queuesPlanner;
    }

    public void setQueuesPlanner(FCFSOrder[] queuesPlanner) {
        this.queuesPlanner = queuesPlanner;
    }

    public void setMediumReady(Order order) {
        mediumFoodOrdersReady.add(order);
    }

    public Queue<Order> getMediumReady() {
        return mediumFoodOrdersReady;
    }

    public void setFastReady(Order order) {
        mediumFoodOrdersReady.add(order);
    }

    public Queue<Order> getFastReady() {
        return fastFoodOrdersReady;
    }

    public void setSlowReady(Order order) {
        slowFoodOrdersReady.add(order);
    }

    public Queue<Order> getSlowReady() {
        return slowFoodOrdersReady;
    }

    public void setVipReady(Order order) {
        vipOrdersReady.add(order);
    }

    public Queue<Order> getVipReady() {
        return vipOrdersReady;
    }

    @Override
    public String toString() {
        return "MLQ [fastFoodOrders=" + fastFoodOrders + ", mediumFoodOrders=" + mediumFoodOrders + ", order=" + order
                + ", queuesPlanner=" + Arrays.toString(queuesPlanner) + ", slowFoodOrders=" + slowFoodOrders
                + ", vipOrders=" + vipOrders + "]";
    }

    @Override
    public void run() {
        
        while (TClock.isFlag()) {
            try {
                startSeg.acquire();
                if (!vipOrders.isEmpty()) {
                    vipSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(vipOrders);
                    if (order != null) {
                        TStore.acceptOrder(order);
                    }
                    vipSemaphoreIn.release();
                    TClock.releaseMLQ();
                    continue;
                }  if (!fastFoodOrders.isEmpty()) {
                    fastSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(fastFoodOrders);
                    if (order != null) {
                        TStore.acceptOrder(order);
                    }
                    fastSemaphoreIn.release();
                    TClock.releaseMLQ();
                    continue;

                }  if (!mediumFoodOrders.isEmpty()) {
                    mediumSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(mediumFoodOrders);
                    if (order != null) {
                        TStore.acceptOrder(order);
                    }
                    mediumSemaphoreIn.release();
                    TClock.releaseMLQ();
                    continue;
                }  if (!slowFoodOrders.isEmpty()) {
                    slowSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(slowFoodOrders);
                    if (order != null) {
                        TStore.acceptOrder(order);
                    }
                    slowSemaphoreIn.release();
                    TClock.releaseMLQ();
                    continue;

                } if (!vipOrdersReady.isEmpty()) {
                    vipSemaphoreReady.acquire();
                    // Order order = vipOrdersReady.peek();
                    // if (order != null) {
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            Order order = vipOrdersReady.poll();
                            newDeliveryMan.setDeliveryBoolean(true);
                            TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                            TClock.semaphoreDeliveryAcq();
                            listDeliver.add(newDeliver);
                            TClock.semTClockDeliverRe();
                            newDeliver.start();
                        }

                    // }
                    vipSemaphoreReady.release();
                    TClock.releaseMLQ();
                    continue;
                }
                if (!fastFoodOrdersReady.isEmpty()) {
                    fastSemaphoreReady.acquire();
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            Order order = fastFoodOrdersReady.poll();
                            if(order != null){
                                newDeliveryMan.setDeliveryBoolean(true);
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                TClock.semaphoreDeliveryAcq();
                                listDeliver.add(newDeliver);
                                TClock.semTClockDeliverRe();
                                newDeliver.start();
                            }
                        }
                    fastSemaphoreReady.release();
                    TClock.releaseMLQ();
                    continue;
                } if (!mediumFoodOrdersReady.isEmpty()) {
                    mediumSemaphoreReady.acquire();
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            Order order = mediumFoodOrdersReady.poll();
                            if(order != null){

                                newDeliveryMan.setDeliveryBoolean(true);
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                TClock.semaphoreDeliveryAcq();
                                listDeliver.add(newDeliver);
                                TClock.semTClockDeliverRe();
                                newDeliver.start();
                            }
                        }
                    mediumSemaphoreReady.release();
                    TClock.releaseMLQ();
                    continue;
                }  
                if (!slowFoodOrdersReady.isEmpty()) {
                    slowSemaphoreReady.acquire();
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            Order order =fastFoodOrdersReady.poll();
                            if(order != null){
                                newDeliveryMan.setDeliveryBoolean(true);
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                TClock.semaphoreDeliveryAcq();
                                listDeliver.add(newDeliver);
                                TClock.semTClockDeliverRe();
                                newDeliver.start();

                            }
                        }
                    slowSemaphoreReady.release();
                    TClock.releaseMLQ();
                    continue;
                } else {

                    TClock.releaseMLQ();
                    continue;
                }

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        }

        // }
    }

    
    public static void addToStatistics(Order order) throws InterruptedException {
        long endProcessedTime = TClock.getMoment();
        Statistics.addOrderToStatistics(order, endProcessedTime);

    }
}
