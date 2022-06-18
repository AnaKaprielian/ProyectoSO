package Model;

public class Order {
    private int orderId;
    private int arriveTime;
    private Food orderDescription;
    private Client client;

    public Order(int orderId, int arriveTime, Food orderDescription, Client client) {
        this.orderId = orderId;
        this.arriveTime = arriveTime;
        this.orderDescription = orderDescription;
        this.client = client;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Food getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(Food orderDescription) {
        this.orderDescription = orderDescription;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + orderId +
                ", arriveTime=" + arriveTime +
                ", orderDescription=" + orderDescription +
                ", client=" + client +
                '}';
    }
}
