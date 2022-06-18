import Model.Client;
import Repository.SystemP;
import Utils.DataHandler;
import Utils.FilesHandler;

import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
       SystemP systemP = SystemP.getInstance();
       systemP.initDayOfWork(1);

    //    List<Client> clients = DataHandler.getClientsFromFile("clients");
    //    System.out.println(clients.size());

    }
}
