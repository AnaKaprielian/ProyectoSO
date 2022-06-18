package Statistics;


import Model.Order;

public class OrderStatistic {

    private long inputOrderTime;
    private Order order;

    public OrderStatistic(long inputOrderTime, Order order) {
        this.inputOrderTime = inputOrderTime;
        this.order = order;
    }

    public long getInputOrderTime() {
        return inputOrderTime;
    }

    public void setInputOrderTime(int inputOrderTime) {
        this.inputOrderTime = inputOrderTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderStatistic{" +
                "inputOrderTime=" + inputOrderTime +
                ", order=" + order +
                '}';
    }
}
