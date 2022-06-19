package Threads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import Model.DeliveryMan;
import Model.Order;
import MLQ.MLQ;

public class TStore extends Thread{
    private int storeId;
    private String storeDescription;
    private static ArrayList<Order> storeOrders;
    private List<DeliveryMan> storeDelivers;
    private static Semaphore semStore = new Semaphore(1);
    

    public TStore(int storeId, String storeDescription, List<Order> storeOrders, List<DeliveryMan> storeDelivers) {
        this.storeId = storeId;
        this.storeDescription = storeDescription;
        TStore.storeOrders = new ArrayList<Order>();
        this.storeDelivers = storeDelivers;
    }

    public static void acceptOrder(Order order) throws InterruptedException{
        // semStore.acquire();
        storeOrders.add(order);
        // semStore.release();
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

    public void setStoreOrders(ArrayList<Order> storeOrders) {
        TStore.storeOrders = storeOrders;
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

    public void procesamientoOrders(){
        List<Order> orderAux = new LinkedList<>();
        for(Order order : storeOrders){
            order.reduceProcessTime();
            if(order.getProcessTime() == 0 ){

                orderAux.add(order);
                try {
                    if(order.getClient().getClientType() ==1){
                        MLQ.vipSemaphoreReady.acquire();
                        MLQ.vipOrdersReady.add(order);
                        MLQ.addToStatistics(order);
                        MLQ.vipSemaphoreReady.release();
                    }else if(order.getOrderDescription().getDescription().equals("Asado")){
                        MLQ.slowSemaphoreReady.acquire();
                        MLQ.slowFoodOrdersReady.add(order);
                        MLQ.addToStatistics(order);
                        MLQ.slowSemaphoreReady.release();
                    }else if(order.getOrderDescription().getDescription().equals("Hamburguesa")){
                        MLQ.mediumSemaphoreReady.acquire();
                        MLQ.mediumFoodOrdersReady.add(order);
                        MLQ.addToStatistics(order);
                        MLQ.mediumSemaphoreReady.release();
                    }else if(order.getOrderDescription().getDescription().equals("Empanadas")){
                        MLQ.fastSemaphoreReady.acquire();
                        MLQ.fastFoodOrdersReady.add(order);
                        MLQ.addToStatistics(order);
                        MLQ.fastSemaphoreReady.release();
                    }else if(order.getOrderDescription().getDescription().equals("Pizza")){
                        MLQ.fastSemaphoreReady.acquire();
                        MLQ.fastFoodOrdersReady.add(order);
                        MLQ.addToStatistics(order);
                        MLQ.fastSemaphoreReady.release();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }


            }
        }
        for(Order order : orderAux){
            storeOrders.remove(order);
        }
    }

    public void run(){
        while(TClock.isFlag()){
            
                try {
                    procesamientoOrders();
                    TClock.semSyncStore.wait();

                } catch (Exception e) {
                    //TODO: handle exception
                }
            }
        }

    }
    



