package Repository;

import MLQ.FCFSDelivery;
import MLQ.MLQ;
import Model.Client;
import Model.DeliveryMan;
import Model.Order;
import Model.Store;
import Statistics.Statistics;
import Threads.TChargeOrders;
import Threads.TClock;
import Threads.TDeliverOrder;
import Utils.DataHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class SystemP {
    private static List<Store> stores;
    private static List<Client> clients;
    private static List<DeliveryMan> deliverers;

    private static List<Thread> threadsStop = new ArrayList<>();

    private static List<Order> ordersFromFile;
    private static Semaphore semaphoreFiles = new Semaphore(0);

    public SystemP(List<Store> stores, List<Client> clients, List<DeliveryMan> deliverers, List<Order> ordersFromFile) {
        this.stores = stores;
        this.clients = clients;
        this.deliverers = deliverers;
        this.ordersFromFile = ordersFromFile;
    }

    public SystemP() {

    }

    public static void realeaseFiles() {
        semaphoreFiles.release();
    }

    private static Semaphore semOrder = new Semaphore(0);
    private static Semaphore semDelivery = new Semaphore(0);
    private static Semaphore semClient = new Semaphore(0);

    public static void semOrderRelease() {
        semOrder.release();
    }

    public static void semDeliveryRealease() {
        semDelivery.release();
    }

    public static void semClientRelease() {
        semClient.release();
    }

    public static void initDayOfWork(int i) throws InterruptedException {

        List<Client> clients = DataHandler.getClientsFromFile("clients");
        setClients(clients);

        semClient.acquire();

        List<DeliveryMan> deliverers = DataHandler.getDeliverersFromFile("deliverers");
        setDeliverers(deliverers);
        semDelivery.acquire();

        for (DeliveryMan delivery : deliverers) {
            FCFSDelivery.push(delivery);
        }

        List<Order> orders = DataHandler.getOrdersFromFile("orders");
        semOrder.acquire();
        // TODO: Inicializar reloj contador
        TClock tClock = new TClock();
        tClock.start();

        // TODO: inicializar procesamiento de ordenes
        // 1. Cargar en el MLQ
        TChargeOrders changeOrders = new TChargeOrders(orders);
        changeOrders.start();
        // 2. Procesar de ahi
        MLQ mlq = new MLQ();
        mlq.start();

        // inicializar repartidores
        // TDeliverOrder deliveryMan = new TDeliverOrder(null, null);
        // deliveryMan.start();

        // BITACORA
        semaphoreFiles.acquire();

        DataHandler.generateBitacoras(Statistics.getInstance().getOrderStatistics(),
                Statistics.getInstance().getDeliveriesStatistics());

    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
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

    public static void hilos(Thread newThread) {
        threadsStop.add(newThread);
    }

    public static void hilosDelete() {
        for (Thread hilo : threadsStop) {
            if (hilo != null) {
                hilo.stop();
            }
        }
    }
}
