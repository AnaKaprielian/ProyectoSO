package MLQ;

import Model.DeliveryMan;
import Model.Order;
import Repository.SystemP;
import Statistics.Statistics;
import Threads.TClock;
import Threads.TDeliverOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class MLQ extends Thread {
    private static FCFSOrder vipOrders = new FCFSOrder();
    private static FCFSOrder fastFoodOrders = new FCFSOrder();
    private static FCFSOrder mediumFoodOrders = new FCFSOrder();
    private static FCFSOrder slowFoodOrders = new FCFSOrder();
    private static Queue<Order> vipOrdersReady = new LinkedList<>();
    private static Queue<Order> fastFoodOrdersReady = new LinkedList<>();
    private static Queue<Order> mediumFoodOrdersReady = new LinkedList<>();
    private static Queue<Order> slowFoodOrdersReady = new LinkedList<>();
    private Semaphore vipSemaphore = new Semaphore(0);
    private Semaphore fastSemaphore = new Semaphore(0);
    private Semaphore mediumSemaphore = new Semaphore(0);
    private Semaphore slowSemaphore = new Semaphore(0);
    private Semaphore vipSemaphoreReady = new Semaphore(1);
    private Semaphore fastSemaphoreReady = new Semaphore(1);
    private Semaphore mediumSemaphoreReady = new Semaphore(1);
    private Semaphore slowSemaphoreReady = new Semaphore(1);
    private Semaphore vipSemaphoreIn = new Semaphore(1);
    private Semaphore fastSemaphoreIn = new Semaphore(1);
    private Semaphore mediumSemaphoreIn = new Semaphore(1);
    private Semaphore slowSemaphoreIn = new Semaphore(1);
    private Semaphore deliverySemaphoreIn = new Semaphore(1);
    private static Semaphore startSeg = new Semaphore(0);
    private static Semaphore mutex = new Semaphore(1);
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

        // DataHandler.release();
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
        // while (TClock.getMoment() < 1000) {
        while (TClock.isFlag()) {
            try {
                startSeg.acquire();
                if (!vipOrders.isEmpty()) {
                    vipSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(vipOrders);
                    if (order != null) {
                        this.processVipOrder(order);
                        vipSemaphore.acquire();
                        vipOrdersReady.add(order);
                    }
                    vipSemaphoreIn.release();
                } else if (!fastFoodOrders.isEmpty()) {
                    fastSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(fastFoodOrders);
                    if (order != null) {
                        this.processFastOrder(order);
                        fastSemaphore.acquire();
                        fastFoodOrdersReady.add(order);
                    }
                    fastSemaphoreIn.release();

                } else if (!mediumFoodOrders.isEmpty()) {
                    mediumSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(mediumFoodOrders);
                    if (order != null) {
                        this.processMediumOrder(order);
                        mediumSemaphore.acquire();
                        mediumFoodOrdersReady.add(order);
                    }
                    mediumSemaphoreIn.release();

                } else if (!slowFoodOrders.isEmpty()) {
                    slowSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(slowFoodOrders);
                    if (order != null) {
                        processSlowOrder(order);
                        slowSemaphore.acquire();
                        slowFoodOrdersReady.add(order);
                    }
                    slowSemaphoreIn.release();

                } else if (!vipOrdersReady.isEmpty()) {
                    vipSemaphoreReady.acquire();
                    Order order = vipOrdersReady.peek();
                    if (order != null) {
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            vipOrdersReady.poll();
                            newDeliveryMan.setDeliveryBoolean(true);
                            TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                            newDeliver.start();
                        }

                    }
                    vipSemaphoreReady.release();
                    TClock.releaseMLQ();
                } else if (!fastFoodOrdersReady.isEmpty()) {
                    fastSemaphoreReady.acquire();
                    Order order = fastFoodOrdersReady.peek();
                    if (order != null) {
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            fastFoodOrdersReady.poll();
                            newDeliveryMan.setDeliveryBoolean(true);
                            TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                            newDeliver.start();
                        }
                    }
                    fastSemaphoreReady.release();
                    TClock.releaseMLQ();
                } else if (!mediumFoodOrdersReady.isEmpty()) {
                    mediumSemaphoreReady.acquire();
                    Order order = mediumFoodOrdersReady.peek();
                    if (order != null) {
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            mediumFoodOrdersReady.poll();
                            newDeliveryMan.setDeliveryBoolean(true);
                            TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                            newDeliver.start();
                        }
                    }
                    mediumSemaphoreReady.release();
                    TClock.releaseMLQ();
                } else if (!slowFoodOrdersReady.isEmpty()) {
                    slowSemaphoreReady.acquire();
                    Order order = fastFoodOrdersReady.peek();
                    if (order != null) {
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            fastFoodOrdersReady.poll();
                            newDeliveryMan.setDeliveryBoolean(true);
                            TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                            newDeliver.start();
                        }
                    }
                    slowSemaphoreReady.release();
                    TClock.releaseMLQ();
                } else {

                    continue;
                }

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            TClock.releaseMLQ();

        }

        // }
    }

    private void processVipOrder(Order order) throws InterruptedException {
        int service = order.getOrderDescription().getServiceTime();
        int i = 0;
        switch (service) {
            case 15:
                while (i <= 1500000) {
                    i += 1;
                }
                break;
            case 25:
                while (i <= 2500000) {
                    i += 1;
                }
                break;
            case 10:
                while (i <= 1000000) {
                    i += 1;
                }
                break;
            case 50:
                while (i <= 500000) {
                    i += 1;
                }
                break;
            default:
                break;
        }
        this.addToStatistics(order);
        vipSemaphore.release();
    }

    private void processFastOrder(Order order) throws InterruptedException {
        int i = 0;
        int service = order.getOrderDescription().getServiceTime();
        if (service == 10) {
            while (i <= 1000000) {
                i += 1;
            }
        } else if (service == 15) {
            while (i <= 1500000) {
                i += 1;
            }
        }
        this.addToStatistics(order);
        fastSemaphore.release();
    }

    private void processMediumOrder(Order order) throws InterruptedException {
        int i = 0;
        int service = order.getOrderDescription().getServiceTime();
        while (i <= 2500000) {
            i += 1;
        }
        this.addToStatistics(order);
        mediumSemaphore.release();
    }

    private void processSlowOrder(Order order) throws InterruptedException {
        int i = 0;
        int service = order.getOrderDescription().getServiceTime();
        while (i <= 5000000) {
            i += 1;
        }
        this.addToStatistics(order);
        slowSemaphore.release();
    }

    private void addToStatistics(Order order) throws InterruptedException {
        long endProcessedTime = TClock.getMoment();
        Statistics.addOrderToStatistics(order, endProcessedTime);

    }
}
