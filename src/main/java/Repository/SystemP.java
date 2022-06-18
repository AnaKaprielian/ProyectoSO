package Repository;

import MLQ.FCFSDelivery;
import MLQ.MLQ;
import Model.Client;
import Model.DeliveryMan;
import Model.Order;
import Model.Store;
import Threads.TChargeOrders;
import Threads.TClock;
import Utils.DataHandler;

import java.util.List;
import java.util.Queue;

public class SystemP {
    private static List<Store> stores;
    private static List<Client> clients;
    private static List<DeliveryMan> deliverers;
    private static SystemP instance;
    private static List<Order> ordersFromFile;
    private static MLQ mlq = MLQ.getInstance();

    public SystemP(List<Store> stores, List<Client> clients, List<DeliveryMan> deliverers, List<Order> ordersFromFile) {
        SystemP.stores = stores;
        SystemP.clients = clients;
        SystemP.deliverers = deliverers;
        SystemP.ordersFromFile = ordersFromFile;
    }

    public SystemP() {

    }

    public static SystemP getInstance() {
        if (instance == null) {
            instance = new SystemP();
        }
        return instance;
    }


    public void initDayOfWork(int i) throws InterruptedException {


        List<Client> clients = DataHandler.getClientsFromFile("clients");
        this.setClients(clients);

        List<DeliveryMan> deliverers = DataHandler.getDeliverersFromFile("deliverers");
        this.setDeliverers(deliverers);


        for (DeliveryMan delivery : deliverers) {
            FCFSDelivery.getInstance().push(delivery);
        }


        Queue<Order> orders = DataHandler.getOrdersFromFile("orders");

        // TODO: Inicializar reloj contador
        TClock tClock = new TClock();
        tClock.run();

        // TODO: inicializar procesamiento de ordenes
        // 1. Cargar en el MLQ
        TChargeOrders.getInstance().run();
        // 2. Procesar de ahi
        mlq.run();

        // TOD
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public static void setInstance(SystemP instance) {
        SystemP.instance = instance;
    }

    public List<DeliveryMan> getDeliverers() {
        return deliverers;
    }

    public void setDeliverers(List<DeliveryMan> deliverers) {
        this.deliverers = deliverers;
    }
}
