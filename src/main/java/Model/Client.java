package Model;

public class Client {
    private String clientId;
    private int clientType;

    public Client(String clientId, int clientType) {
        this.clientId = clientId;
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + clientId + '\'' +
                ", clientType=" + clientType +
                '}';
    }
}
