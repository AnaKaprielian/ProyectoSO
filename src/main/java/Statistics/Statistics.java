package Statistics;

import Model.DeliveryMan;
import Model.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Statistics {

    private static Statistics instance;
    static List<OrderStatistic> orderStatistics = new CopyOnWriteArrayList<>();
    static List<DeliverStatistic> deliveriesStatistics = new CopyOnWriteArrayList<>();

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public void addOrderToStatistics(Order order, long inputOrderTime) throws InterruptedException {
        OrderStatistic orderStatistic = new OrderStatistic(inputOrderTime, order);
        orderStatistics.add(orderStatistic);
    }

    public static void addDeliveryToStatistics(DeliveryMan delivery, Order order, int deliveryTime) throws InterruptedException {
        DeliverStatistic deliveryModel = new DeliverStatistic(delivery, order, deliveryTime);
        deliveriesStatistics.add(deliveryModel);
    }

    public static List<OrderStatistic> getOrderStatistics() {
        return orderStatistics;
    }

    public static List<DeliverStatistic> getDeliveriesStatistics() {
        return deliveriesStatistics;
    }

    



//     for (EstadisticoOrder order : orderStatistics) {
//         if (order.getOrder().getClient().getType() == 1) {
//             int horasOrderVIP = Integer.parseInt(order.getHour());
//             // int minOrderVIP = Integer.parseInt(order.getHour().split(":")[1]);
//             contadorTiempoOrderVIP += horasOrderVIP;
//             cantidadOrderVIP++;
//         } else {
//             int horasNoVIP = Integer.parseInt(order.getHour());
//             // int minNoVIP = Integer.parseInt(order.getHour().split(":")[1]);
//             contadorOrderTiempoNoVIP += horasNoVIP;
//             cantidadOrderNoVIP++;
//         }

//     for (EstadisticoDelivery delivery : deliveriesStatistics) {
//         if (delivery.getOrder().getClient().getType() == 1) {
//             int horasDeliveryVIP = Integer.parseInt(delivery.getHour());
//             // int minDeliveryVIP = Integer.parseInt(delivery.getHour().split(":")[1]);
//             contadorTiempoDeliveryVIP += horasDeliveryVIP ;
//             cantidadDeliveryVIP++;
//         } else {
//             int horasDeliveryNoVIP = Integer.parseInt(delivery.getHour());
//             // int minDeliveryNoVIP = Integer.parseInt(delivery.getHour().split(":")[1]);
//             contadorDeliveryTiempoNoVIP += horasDeliveryNoVIP;
//             cantidadDeliveryNoVIP++;
//         }

}
    
