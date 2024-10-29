import java.io.*;
import java.lang.*;

public class ClientTest {
    public static void main(String[] args) {
        ClientList C = new ClientList();
        Client client1 = new Client("John", "Smith", "jsmith@gmail.com", "123 st Avon, MN", "3204958874");
        Client client2 = new Client("Jane", "Doe", "jdoe@gmail.com", "12323 Road St.Cloud, MN", "3204958874");
        C.addClient(client1);
        System.out.println("client 1:");
        System.out.println(client1.toString());
        System.out.println("client 2:");
        System.out.println(client2.toString());
        C.addClient(client2);
        System.out.println("Client List:");
        C.displayClients();
    }
}