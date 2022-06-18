package Model;

public class Order {
    private String orderId;
    private int arriveTime;
    private Food orderDescription;
    private Client client;

    public Order(String orderId, int arriveTime, Food orderDescription, Client client) {
        this.orderId = orderId;
        this.arriveTime = arriveTime;
        this.orderDescription = orderDescription;
        this.client = client;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
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
