package Statistics;

import Model.DeliveryMan;
import Model.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Statistics {

    private static Statistics instance;
    List<OrderStatistic> orderStatistics = new CopyOnWriteArrayList<>();
    List<DeliverStatistic> deliveriesStatistics = new CopyOnWriteArrayList<>();

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

    public void addDeliveryToStatistics(DeliveryMan delivery, Order order, int deliveryTime) throws InterruptedException {

        DeliverStatistic deliveryModel = new DeliverStatistic(delivery, order, deliveryTime);
        deliveriesStatistics.add(deliveryModel);

        // System.out.println("deliverysize" + deliveriesStatistics.size());

//        TDelivery.release();
    }

}
