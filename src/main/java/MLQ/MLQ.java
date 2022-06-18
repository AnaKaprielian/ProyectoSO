package MLQ;

import Model.DeliveryMan;
import Model.Order;
import Repository.SystemP;
import Statistics.Statistics;
import Threads.TClock;
import Threads.TDeliverOrder;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class MLQ extends Thread {
    private static FCFSOrder vipOrders = new FCFSOrder();
    private static FCFSOrder fastFoodOrders = new FCFSOrder();
    private static FCFSOrder mediumFoodOrders = new FCFSOrder();
    private static FCFSOrder slowFoodOrders = new FCFSOrder();
    private Semaphore vipSemaphore = new Semaphore(0);
    private Semaphore fastSemaphore = new Semaphore(0);
    private Semaphore mediumSemaphore = new Semaphore(0);
    private Semaphore slowSemaphore = new Semaphore(0);
    private Semaphore vipSemaphoreIn = new Semaphore(1);
    private Semaphore fastSemaphoreIn = new Semaphore(1);
    private Semaphore mediumSemaphoreIn = new Semaphore(1);
    private Semaphore slowSemaphoreIn = new Semaphore(1);
    private Semaphore deliverySemaphoreIn = new Semaphore(1);
    private static Semaphore startSeg = new Semaphore(0);
    private static Semaphore mutex = new Semaphore(2);
    private FCFSOrder[] queuesPlanner = {
            vipOrders, fastFoodOrders, mediumFoodOrders, slowFoodOrders
    };

    private final Semaphore solicitudes = new Semaphore(0);
    private final Semaphore solOrder = new Semaphore(0);
    private final Semaphore order = new Semaphore(0);

    public static void addOrder(Order order) throws InterruptedException {
        mutex.acquire();
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
        mutex.release();
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
        this.fastFoodOrders = fastFoodOrders;
    }

    public FCFSOrder getMediumFoodOrders() {
        return mediumFoodOrders;
    }

    public void setMediumFoodOrders(FCFSOrder mediumFoodOrders) {
        this.mediumFoodOrders = mediumFoodOrders;
    }

    public FCFSOrder getSlowFoodOrders() {
        return slowFoodOrders;
    }

    public void setSlowFoodOrders(FCFSOrder slowFoodOrders) {
        this.slowFoodOrders = slowFoodOrders;
    }

    public FCFSOrder getVipOrders() {
        return vipOrders;
    }

    public void setVipOrders(FCFSOrder vipOrders) {
        this.vipOrders = vipOrders;
    }

    public FCFSOrder[] getQueuesPlanner() {
        return queuesPlanner;
    }

    public void setQueuesPlanner(FCFSOrder[] queuesPlanner) {
        this.queuesPlanner = queuesPlanner;
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
            SystemP.hilos(this);
            try {
                startSeg.acquire();
                while (!vipOrders.isEmpty()) {
                    vipSemaphoreIn.acquire();
                    Order order = MLQ.nextOrder(vipOrders);
                    if (order != null) {
                        this.processVipOrder(order);
                        vipSemaphore.acquire();
                        DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                        if (newDeliveryMan != null) {
                            TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                            newDeliver.start();
                        }
                    }
                    vipSemaphoreIn.release();
                }

                while (!fastFoodOrders.isEmpty()) {
                    fastSemaphoreIn.acquire();
                    if (!vipOrders.isEmpty()) {
                        Order order = MLQ.nextOrder(vipOrders);
                        if (order != null) {
                            this.processVipOrder(order);
                            vipSemaphore.acquire();
                            DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                            if (newDeliveryMan != null) {
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                newDeliver.start();
                            }
                        }
                    } else {
                        Order order = MLQ.nextOrder(fastFoodOrders);
                        if (order != null) {
                            this.processFastOrder(order);
                            fastSemaphore.acquire();
                            DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                            if (newDeliveryMan != null) {
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                newDeliver.start();
                            }
                        }
                    }
                    fastSemaphoreIn.release();
                }

                while (!mediumFoodOrders.isEmpty()) {
                    mediumSemaphoreIn.acquire();
                    if (!vipOrders.isEmpty()) {
                        Order order = MLQ.nextOrder(vipOrders);
                        if (order != null) {
                            processVipOrder(order);
                            vipSemaphore.acquire();
                            DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();

                            if (newDeliveryMan != null) {
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                newDeliver.start();
                            }

                        }

                    } else {
                        Order order = MLQ.nextOrder(mediumFoodOrders);
                        if (order != null) {
                            this.processMediumOrder(order);
                            mediumSemaphore.acquire();
                            DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                            if (newDeliveryMan != null) {
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                newDeliver.start();
                            }
                        }

                    }
                    mediumSemaphoreIn.release();
                }

                while (!slowFoodOrders.isEmpty()) {
                    slowSemaphoreIn.acquire();
                    if (!vipOrders.isEmpty()) {
                        Order order = MLQ.nextOrder(vipOrders);
                        if (order != null) {
                            processVipOrder(order);
                            vipSemaphore.acquire();
                            DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                            if (newDeliveryMan != null) {

                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);

                                newDeliver.start();
                            }
                        }

                    } else {
                        Order order = MLQ.nextOrder(slowFoodOrders);
                        if (order != null) {
                            processSlowOrder(order);
                            slowSemaphore.acquire();
                            DeliveryMan newDeliveryMan = FCFSDelivery.nextDelivery();
                            if (newDeliveryMan != null) {
                                TDeliverOrder newDeliver = new TDeliverOrder(newDeliveryMan, order);
                                newDeliver.start();
                            }
                        }

                    }
                    slowSemaphoreIn.release();
                }
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
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
