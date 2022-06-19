package Model;

public class DeliveryMan {
    private String deliveryManId;
    public boolean newOrderToDeliver = true;

    public DeliveryMan(String deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public String getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(String deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public void setDeliveryBoolean(boolean newBool) {
        newOrderToDeliver = newBool;
    }

    public boolean getDeliveryBoolean() {
        return newOrderToDeliver;
    }

    @Override
    public String toString() {
        return "DeliveryMan{" +
                "deliveryManId='" + deliveryManId + '\'' +
                '}';
    }
}
