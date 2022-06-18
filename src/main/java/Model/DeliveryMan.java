package Model;

public class DeliveryMan {
    private String deliveryManId;

    public DeliveryMan(String deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public String getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(String deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    @Override
    public String toString() {
        return "DeliveryMan{" +
                "deliveryManId='" + deliveryManId + '\'' +
                '}';
    }
}
