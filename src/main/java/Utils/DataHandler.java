package Utils;

import Model.Client;
import Model.DeliveryMan;
import Model.Food;
import Model.Order;
import Repository.SystemP;
import Statistics.DeliverStatistic;
import Statistics.OrderStatistic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class DataHandler {

    public DataHandler() {

    }

    public static List<Client> getClientsFromFile(String fileName) {
        List<Client> clients = new ArrayList<>();
        String[] userFiles = FilesHandler.readFile(fileName);
        for (String userFile : userFiles) {
            Client client = generateClientFromFileLine(userFile);
            clients.add(client);
        }
        SystemP.semClientRelease();
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
        SystemP.semDeliveryRealease();
        return deliverys;
    }

    public static DeliveryMan generateDeliveryFromFileLine(String fileLine) {

        DeliveryMan newDelivery = new DeliveryMan(fileLine);
        return newDelivery;
    }

    public static List<Order> getOrdersFromFile(String fileName) {
        List<Order> orders = new LinkedList();
        String[] orderFiles = FilesHandler.readFile(fileName);
        for (String orderFile : orderFiles) {
            Order order = generateOrderFromFileLine(orderFile);
            orders.add(order);
        }
        SystemP.semOrderRelease();
        return orders;

    }

    public static Order generateOrderFromFileLine(String fileLine) {
        String[] lineSplit = fileLine.split(",");
        String orderId = lineSplit[0];
        int arriveTime = Integer.parseInt(lineSplit[1]);
        String orderDescription = lineSplit[2];
        Food newFood = new Food(orderDescription, arriveTime);
        String clientId = lineSplit[3];

        Client client = SystemP.getClients().stream()
                .filter(client1 -> clientId.equals(client1.getClientId()))
                .collect(Collectors.toList())
                .get(0);

        if (client != null) {
            Order newOrder = new Order(orderId, arriveTime, newFood, client);
            return newOrder;
        }
        return null;
    }

    public static void generateBitacoras(List<OrderStatistic> ordersStatistics,
            List<DeliverStatistic> deliverersStatistics) throws InterruptedException {
        generateBitacoraOrder(ordersStatistics);
        generateBitacoraDelivery(deliverersStatistics);

    }

    public static void generateBitacoraOrder(List<OrderStatistic> orderStatistics) throws InterruptedException {
        String[] fileLines = new String[orderStatistics.size()];
        int contador = 0;
        for (OrderStatistic order : orderStatistics) {
            if (order != null && order.getOrder() != null && order.getOrder().getClient() != null) {
                String fileLine = "La orden del cliente: " + order.getOrder().getClient().getClientId()
                        + " - Con descripcion " + order.getOrder().getOrderDescription() +
                        " ingreso en el tiempo: " + order.getOrder().getArriveTime();
                fileLines[contador] = fileLine;
                contador++;
            }
        }
        FilesHandler.writeFile("BitacoraOrder", fileLines);
    }

    public static void generateBitacoraDelivery(List<DeliverStatistic> deliveriesStatistics)
            throws InterruptedException {

        String[] fileLines = new String[deliveriesStatistics.size()];
        int contador = 0;
        for (DeliverStatistic delivery : deliveriesStatistics) {
            if (delivery != null && delivery.getOrder().getClient() != null && delivery.getDelivery() != null) {
                String fileLine = "El repartidor: " + delivery.getDelivery().getDeliveryManId()
                        + " entregó la orden del Cliente: " + delivery.getOrder().getClient().getClientId()
                        + " en el tiempo: " + delivery.getDeliveryTime();
                fileLines[contador] = fileLine;
            }

            contador++;
        }
        // System.out.println("Hay " + deliveriesStatistics.size() + " lineas");

        FilesHandler.writeFile("BitacoraDelivery", fileLines);

    }

    public static void generateKPIs(List<OrderStatistic> orderStatistics, List<DeliverStatistic> deliveriesStatistics)
            throws InterruptedException {
        String[] fileLines = new String[29];

        long endProcessedTimeVIP_SUM = 0;
        int counterVIP = 0;
        long endProcessedTimeVIP_Average = 0;

        long endProcessedTime_SUM = 0;
        int counter = 0;
        long endProcessedTime_Average = 0;

        long endProcessedTime_FAST_VIP_SUM = 0;
        int counter_FAST_VIP = 0;
        long endProcessedTime_FAST_VIP_Average = 0;

        long endProcessedTime_MED_VIP_SUM = 0;
        int counter_MED_VIP = 0;
        long endProcessedTime_MED_VIP_Average = 0;

        long endProcessedTime_SLOW_VIP_SUM = 0;
        int counter_SLOW_VIP = 0;
        long endProcessedTime_SLOW_VIP_Average = 0;

        long endProcessedTime_FAST_SUM = 0;
        int counter_FAST = 0;
        long endProcessedTime_FAST_Average = 0;

        long endProcessedTime_MED_SUM = 0;
        int counter_MED = 0;
        long endProcessedTime_MED_Average = 0;

        long endProcessedTime_SLOW_SUM = 0;
        int counter_SLOW = 0;
        long endProcessedTime_SLOW_Average = 0;

        // -------PROCESAMIENTO PEDIDOS--------------

        for (OrderStatistic orderSt : orderStatistics) {
            long endProcessedTime = orderSt.getInputOrderTime();
            if (orderSt.getOrder().getClient().getClientType() == 1) {
                endProcessedTimeVIP_SUM = endProcessedTimeVIP_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                counterVIP++;
                if ("Pizza".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_FAST_VIP_SUM = endProcessedTime_FAST_VIP_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_FAST_VIP++;
                }
                if ("Empanada".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_MED_VIP_SUM = endProcessedTime_MED_VIP_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_MED_VIP++;
                }
                if ("Hamburguesa".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_MED_VIP_SUM = endProcessedTime_MED_VIP_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_MED_VIP++;
                }
                if ("Asado".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_SLOW_VIP_SUM = endProcessedTime_SLOW_VIP_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_SLOW_VIP++;
                }
            } else {
                endProcessedTime_SUM = endProcessedTime_SUM + endProcessedTime;
                counter++;
                if ("Pizza".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_FAST_SUM = endProcessedTime_FAST_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_FAST++;
                }
                if ("Empanada".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_MED_SUM = endProcessedTime_MED_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_MED++;
                }
                if ("Hamburguesa".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_MED_SUM = endProcessedTime_MED_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_MED++;
                }
                if ("Asado".equals(orderSt.getOrder().getOrderDescription().getDescription())) {
                    endProcessedTime_SLOW_SUM = endProcessedTime_SLOW_SUM + endProcessedTime - orderSt.getOrder().getArriveTime();
                    counter_SLOW++;
                }
            }
        }

        if (counterVIP != 0)
            endProcessedTimeVIP_Average = endProcessedTimeVIP_SUM / counterVIP;
        if (counter != 0)
            endProcessedTime_Average = endProcessedTime_SUM / counter;

        if (counter_FAST_VIP != 0)
            endProcessedTime_FAST_VIP_Average = endProcessedTime_FAST_VIP_SUM / counter_FAST_VIP;
        if (counter_MED_VIP != 0)
            endProcessedTime_MED_VIP_Average = endProcessedTime_MED_VIP_SUM / counter_MED_VIP;
        if (counter_SLOW_VIP != 0)
            endProcessedTime_SLOW_VIP_Average = endProcessedTime_SLOW_VIP_SUM / counter_SLOW_VIP;

        if (counter_FAST != 0)
            endProcessedTime_FAST_Average = endProcessedTime_FAST_SUM / counter_FAST;
        if (counter_MED != 0)
            endProcessedTime_MED_Average = endProcessedTime_MED_SUM / counter_MED;
        if (counter_SLOW != 0)
            endProcessedTime_SLOW_Average = endProcessedTime_SLOW_SUM / counter_SLOW;

        boolean velProcessingVIPSisHigh = endProcessedTimeVIP_Average < endProcessedTime_Average;
        boolean velProcFastisHigh_VipsOnly = (endProcessedTime_FAST_VIP_Average < endProcessedTime_MED_VIP_Average)
                && (endProcessedTime_MED_VIP_Average < endProcessedTime_SLOW_VIP_Average);
        boolean velProcFastisHigh = (endProcessedTime_FAST_Average < endProcessedTime_MED_Average)
                && (endProcessedTime_MED_Average < endProcessedTime_SLOW_Average);

        // -------ENTREGA PEDIDOS--------------

        long endDeliveryTime_VIP_SUM = 0;
        int counterD_VIP = 0;
        long endDeliveryTime_VIP_Average = 0;

        long endDeliveryTime_SUM = 0;
        int counterD = 0;
        long endDeliveryTime_Average = 0;

        for (DeliverStatistic deliverySt : deliveriesStatistics) {
            long serviceDeliverTime = deliverySt.getDeliveryTime();
            if (deliverySt.getOrder().getClient().getClientType() == 1) {
                endDeliveryTime_VIP_SUM = endDeliveryTime_VIP_SUM + serviceDeliverTime;
                counterD_VIP++;
            } else {
                endDeliveryTime_SUM = endDeliveryTime_SUM + serviceDeliverTime;
                counterD++;
            }
        }

        if (counterD_VIP != 0)
            endDeliveryTime_VIP_Average = endDeliveryTime_VIP_SUM / counterD_VIP;
        if (counterD != 0)
            endDeliveryTime_Average = endDeliveryTime_SUM / counterD;

        boolean velDeliveryVIPSisHigh = endDeliveryTime_VIP_Average < endDeliveryTime_Average;

        boolean allOrdersDelivered = deliveriesStatistics.size() >= (0.8 * orderStatistics.size());
        int porcentajeEntregas = deliveriesStatistics.size() * 100 /orderStatistics.size();

        fileLines[0] = "VELOCIDAD PROCESAMIENTO DE ORDENES DE CLIENTES VIPS VS NO-VIPS";
        fileLines[1] = "";
        fileLines[2] = "Tiempo promedio de finalizacion de procesamiento de Ordenes VIP -> "
                + endProcessedTimeVIP_Average;
        fileLines[3] = "Tiempo promedio de finalizacion de procesamiento de Ordenes No-VIP -> "
                + endProcessedTime_Average;
        fileLines[4] = "VEL. procesamiento VIPS > VEL. procesamiento NO-VIPS -> " + velProcessingVIPSisHigh;
        fileLines[5] = "";
        fileLines[6] = "----------------------------------------------";
        fileLines[7] = "";
        fileLines[8] = "VELOCIDAD PROCESAMIENTO DE ORDENES FAST/MED/SLOW";
        fileLines[9] = "";
        fileLines[10] = "Tiempo promedio de finalizacion de procesamiento de Ordenes Fast -> "
                + endProcessedTime_FAST_Average;
        fileLines[11] = "Tiempo promedio de finalizacion de procesamiento de Ordenes Med -> "
                + endProcessedTime_MED_Average;
        fileLines[12] = "Tiempo promedio de finalizacion de procesamiento de Ordenes Slow -> "
                + endProcessedTime_SLOW_Average;
        fileLines[13] = "VEL. procesamiento FAST > MED > SLOW -> " + velProcFastisHigh;
        fileLines[14] = "";
        fileLines[15] = "----------------------------------------------";
        fileLines[16] = "";
        fileLines[17] = "VELOCIDAD ENTREGA - ORDENES VIPS VS NO VIPS";
        fileLines[18] = "";
        fileLines[19] = "Tiempo promedio de Tiempo de Entrega de Ordenes VIP -> " + endDeliveryTime_VIP_Average;
        fileLines[20] = "Tiempo promedio de Tiempo de Entrega de Ordenes No-VIP -> " + endDeliveryTime_Average;
        fileLines[21] = "VEL. Entrega VIPs > VEL. Entrega NO-VIPS -> " + velDeliveryVIPSisHigh;
        fileLines[22] = "";
        fileLines[23] = "----------------------------------------------";
        fileLines[24] = "";
        fileLines[25] = "CANTIDAD DE PEDIDOS VS CANTIDAD DE ENTREGAS";
        fileLines[26] = "";
        fileLines[27] = "Porcentaje pedidos enviados -> " + porcentajeEntregas;
        fileLines[28] = "Se entregaron mas del 80% de los pedidos recibidos -> " + allOrdersDelivered;

        FilesHandler.writeFile("KPIs", fileLines);

    }

}
