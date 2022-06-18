package Model;

public class Food {
    private String description;
    private int serviceTime;

    public Food(String description, int serviceTime) {
        this.description = description;
        this.serviceTime = serviceTime;
    }

    public int getServiceTime(String description) {

        switch (description){
            case "Asado":
                return 50;
            case "Hamburguesa":
                return 25;
            case "Empanadas":
                return 15;
            case "Pizza":
                return 10;
            default:
                return 0;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "Food{" +
                "description='" + description + '\'' +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
