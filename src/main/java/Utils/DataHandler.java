package Utils;

import Model.Client;
import Model.DeliveryMan;
import Model.Food;
import Model.Order;
import Repository.SystemP;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class DataHandler {

    public static SystemP systemP = SystemP.getInstance();

    public DataHandler(){

    }
    public static List<Client> getClientsFromFile(String fileName) {
        List<Client> clients = new ArrayList<>();
        String[] userFiles = FilesHandler.readFile(fileName);
        for (String userFile : userFiles) {
            Client client = generateClientFromFileLine(userFile);
            clients.add(client);
        }
        return clients;
    }

    public static Client generateClientFromFileLine(String fileLine) {
        String[] lineSplit = fileLine.split(",");
        String id = lineSplit[0];
        int type = Integer.parseInt(lineSplit[1]);
        Client newClient = new Client(id, type);
        return newClient;
    }

    public static List<DeliveryMan> getDeliverersFromFile(String fileName) {
        List<DeliveryMan> deliverys = new ArrayList<>();
        String[] deliveryFiles = FilesHandler.readFile(fileName);
        for (String deliveryFile : deliveryFiles) {
            DeliveryMan delivery = generateDeliveryFromFileLine(deliveryFile);
            deliverys.add(delivery);
        }
        return deliverys;
    }

    public static DeliveryMan generateDeliveryFromFileLine(String fileLine) {

        DeliveryMan newDelivery = new DeliveryMan(fileLine);
        return newDelivery;
    }

    public static Queue<Order> getOrdersFromFile(String fileName) {
        Queue<Order> orders = new LinkedList();
        String[] orderFiles = FilesHandler.readFile(fileName);
        for (String orderFile : orderFiles) {
                Order order = generateOrderFromFileLine(orderFile);
                orders.add(order);
        }
        return orders;
    }

    public static Order generateOrderFromFileLine(String fileLine) {
        String[] lineSplit = fileLine.split(",");
        int orderId =  Integer.parseInt(lineSplit[0]);
        int arriveTime = Integer.parseInt(lineSplit[1]);
        String orderDescription = lineSplit[2];
        Food newFood = new Food(orderDescription, arriveTime);
        String clientId = lineSplit[3];

        Client client = systemP.getClients().stream()
                .filter(client1 -> clientId.equals(client1.getClientId()))
                .collect(Collectors.toList())
                .get(0);

        if (client != null){
            Order newOrder = new Order(orderId, arriveTime, newFood, client);
            return newOrder;
        }
        return null;
    }
}
