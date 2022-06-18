package Statistics;

import Model.DeliveryMan;
import Model.Order;

public class DeliverStatistic {

    private DeliveryMan delivery;
    private Order order;
    private long deliveryTime;

    public DeliverStatistic(DeliveryMan delivery, Order order, long deliveryTime) {
        this.delivery = delivery;
        this.order = order;
        this.deliveryTime = deliveryTime;
    }

    public DeliveryMan getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryMan delivery) {
        this.delivery = delivery;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "DeliverStatistic{" +
                "delivery=" + delivery +
                ", order=" + order +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
