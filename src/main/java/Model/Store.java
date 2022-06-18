package Model;

import java.util.List;

public class Store {
    private int storeId;
    private String storeDescription;
    private List<Order> storeOrders;
    private List<DeliveryMan> storeDelivers;

    public Store(int storeId, String storeDescription, List<Order> storeOrders, List<DeliveryMan> storeDelivers) {
        this.storeId = storeId;
        this.storeDescription = storeDescription;
        this.storeOrders = storeOrders;
        this.storeDelivers = storeDelivers;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public List<Order> getStoreOrders() {
        return storeOrders;
    }

    public void setStoreOrders(List<Order> storeOrders) {
        this.storeOrders = storeOrders;
    }

    public List<DeliveryMan> getStoreDelivers() {
        return storeDelivers;
    }

    public void setStoreDelivers(List<DeliveryMan> storeDelivers) {
        this.storeDelivers = storeDelivers;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", storeDescription='" + storeDescription + '\'' +
                ", storeOrders=" + storeOrders +
                ", storeDelivers=" + storeDelivers +
                '}';
    }


}
