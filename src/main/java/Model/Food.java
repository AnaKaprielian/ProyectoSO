package Model;

public class Food {
    private String description;
    private int serviceTime;

    public Food(String description, int serviceTime) {
        this.description = description;
        this.serviceTime = serviceTime;
    }

    public int getServiceTime(String description) {

        switch (description) {
            case "Asado":
                return 1000;
            case "Hamburguesa":
                return 300;
            case "Empanadas":
                return 200;
            case "Pizza":
                return 20;
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
