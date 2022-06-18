package Statistics;

import Model.DeliveryMan;
import Model.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

public class Statistics {

    private static Statistics instance;
    static List<OrderStatistic> orderStatistics = new CopyOnWriteArrayList<>();
    static List<DeliverStatistic> deliveriesStatistics = new CopyOnWriteArrayList<>();
    private static Semaphore semOrderStatistic = new Semaphore(1);
    private static Semaphore semDeliveryStatistic = new Semaphore(1);

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public static void addOrderToStatistics(Order order, long inputOrderTime) throws InterruptedException {
        semOrderStatistic.acquire();
        OrderStatistic orderStatistic = new OrderStatistic(inputOrderTime, order);
        orderStatistics.add(orderStatistic);
        semOrderStatistic.release();
    }

    public static void addDeliveryToStatistics(DeliveryMan delivery, Order order, int deliveryTime)
            throws InterruptedException {
        semDeliveryStatistic.acquire();
        DeliverStatistic deliveryModel = new DeliverStatistic(delivery, order, deliveryTime);
        deliveriesStatistics.add(deliveryModel);
        semDeliveryStatistic.release();

    }

    public static List<OrderStatistic> getOrderStatistics() {
        return orderStatistics;
    }

    public static List<DeliverStatistic> getDeliveriesStatistics() {
        return deliveriesStatistics;
    }

    // for (EstadisticoOrder order : orderStatistics) {
    // if (order.getOrder().getClient().getType() == 1) {
    // int horasOrderVIP = Integer.parseInt(order.getHour());
    // // int minOrderVIP = Integer.parseInt(order.getHour().split(":")[1]);
    // contadorTiempoOrderVIP += horasOrderVIP;
    // cantidadOrderVIP++;
    // } else {
    // int horasNoVIP = Integer.parseInt(order.getHour());
    // // int minNoVIP = Integer.parseInt(order.getHour().split(":")[1]);
    // contadorOrderTiempoNoVIP += horasNoVIP;
    // cantidadOrderNoVIP++;
    // }

    // for (EstadisticoDelivery delivery : deliveriesStatistics) {
    // if (delivery.getOrder().getClient().getType() == 1) {
    // int horasDeliveryVIP = Integer.parseInt(delivery.getHour());
    // // int minDeliveryVIP = Integer.parseInt(delivery.getHour().split(":")[1]);
    // contadorTiempoDeliveryVIP += horasDeliveryVIP ;
    // cantidadDeliveryVIP++;
    // } else {
    // int horasDeliveryNoVIP = Integer.parseInt(delivery.getHour());
    // // int minDeliveryNoVIP = Integer.parseInt(delivery.getHour().split(":")[1]);
    // contadorDeliveryTiempoNoVIP += horasDeliveryNoVIP;
    // cantidadDeliveryNoVIP++;
    // }

}
