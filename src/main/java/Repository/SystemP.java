package Repository;

import MLQ.FCFSDelivery;
import MLQ.MLQ;
import Model.Client;
import Model.DeliveryMan;
import Model.Order;
import Statistics.Statistics;
import Threads.TStore;
import Threads.TChargeOrders;
import Threads.TClock;
import Threads.TDeliverOrder;
import Utils.DataHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class SystemP {
    private static List<TStore> stores;
    private static List<Client> clients;
    private static List<DeliveryMan> deliverers;

    private static List<Thread> threadsStop = new ArrayList<>();

    private static List<Order> ordersFromFile;
    private static Semaphore semaphoreFiles = new Semaphore(0);
    private static Semaphore semaphoreDelivery = new Semaphore(1);
    private static Semaphore semOrder = new Semaphore(0);
    private static Semaphore semDelivery = new Semaphore(0);
    private static Semaphore semClient = new Semaphore(0);

    public SystemP(List<TStore> stores, List<Client> clients, List<DeliveryMan> deliverers, List<Order> ordersFromFile) {
        this.stores = stores;
        this.clients = clients;
        this.deliverers = deliverers;
        this.ordersFromFile = ordersFromFile;
    }

    public SystemP() {

    }

    public static void releaseFiles() {
        semaphoreFiles.release();
    }


    public static void semOrderRelease() {
        semOrder.release();
    }

    public static void semDeliveryRealease() {
        semDelivery.release();
    }

    public static void semDeliveryPushRel() {
        semaphoreDelivery.release();
    }

    public static void semDeliveryPushAqu() throws InterruptedException {
        semaphoreDelivery.acquire();
    }

    public static void semClientRelease() {
        semClient.release();
    }

    public static void initDayOfWork(int i) throws InterruptedException {

        System.out.println("Cargando clientes...");
        List<Client> clients = DataHandler.getClientsFromFile("clients");
        setClients(clients);
        System.out.println("Se cargaron: " + clients.size() + " clientes");
        semClient.acquire();

        System.out.println("Cargando repartidores...");
        List<DeliveryMan> deliverers = DataHandler.getDeliverersFromFile("deliverers");
        setDeliverers(deliverers);
        System.out.println("Se cargaron: " + deliverers.size() + " repartidores");

        semDelivery.acquire();

        for (DeliveryMan delivery : deliverers) {
            FCFSDelivery.push(delivery);
        }

        System.out.println("Cargando ordenes...");
        List<Order> orders = DataHandler.getOrdersFromFile("orders");
        System.out.println("Se han cargado: " + orders.size() + " ordenes");
        semOrder.acquire();

        // TODO: Inicializar reloj contador
        TClock tClock = new TClock();
        tClock.start();

        // TODO: inicializar procesamiento de ordenes
        // 1. Cargar en el MLQ
        TChargeOrders changeOrders = new TChargeOrders(orders);
        changeOrders.start();

        TStore store = new TStore(1, "Pepito", null, null);
        store.start();
        
        // 2. Procesar de ahi
        MLQ mlq = new MLQ();
        mlq.start();

        // BITACORA
        semaphoreFiles.acquire();

        Statistics.getInstance();

        DataHandler.generateBitacoras(Statistics.getOrderStatistics(),
                Statistics.getDeliveriesStatistics());


        DataHandler.generateKPIs(Statistics.getOrderStatistics(),
                Statistics.getDeliveriesStatistics());

        System.out.println("Se ha finalizado la ejecucion");

    }

    public List<TStore> getStores() {
        return stores;
    }

    public void setStores(List<TStore> stores) {
        SystemP.stores = stores;
    }

    public static List<Client> getClients() {
        return clients;
    }

    public static void setClients(List<Client> clients) {
        SystemP.clients = clients;
    }

    public List<DeliveryMan> getDeliverers() {
        return deliverers;
    }

    public static void setDeliverers(List<DeliveryMan> deliverers) {
        SystemP.deliverers = deliverers;
    }

}
