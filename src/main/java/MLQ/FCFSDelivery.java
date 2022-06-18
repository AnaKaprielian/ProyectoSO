
    package MLQ;

import java.util.LinkedList;
import java.util.Queue;


import Model.DeliveryMan;

public class FCFSDelivery {
        private static FCFSDelivery instance;
        private Queue<DeliveryMan> deliverys = new LinkedList();


        public void push(DeliveryMan delivery){
            deliverys.add(delivery);
        }

        public static FCFSDelivery getInstance() {
            if (instance == null) {
                instance = new FCFSDelivery();
            }
            return instance;
        }

        public DeliveryMan pop() throws InterruptedException {

            DeliveryMan delivery = deliverys.poll();


            return delivery;
        }

        public boolean isEmpty() throws InterruptedException {

            boolean isEmpty = deliverys.isEmpty();

            return isEmpty;
        }

        public DeliveryMan nextDelivery() throws InterruptedException {
            if (this.deliverys != null) {
                DeliveryMan newDelivery = this.pop();
                return newDelivery;
            }
            return null;
        }
    }

