import java.util.*;
import java.io.*;

public class ClientList implements Serializable{
    // Creates a linked list for client
    private LinkedList<Client> clients = new LinkedList<Client>();
    private static ClientList clientList;

    public ClientList() {
        clients = new LinkedList<>();
    }

    public static ClientList getInstance() {
        if (clientList == null) {
            clientList = new ClientList();
        }
        return clientList;
        }

    public LinkedList<Client> getList()
    {
        return clients;
    }

    public Iterator getClient() {
        return clients.iterator();
    }

    public boolean addClient(Client client) {
        return clients.add(client);
    }

    public Client find(int cID)
    {
        System.out.println("Why wont this work");
        String findID = "C" + (cID);
        for (int i=0; i < clients.size(); i++)
        {
            if (clients.get(i).getClientID() == findID) 
            {
                
                return clients.get(i);
            }
        }
        return null;
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
        output.writeObject(clientList);
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (clientList == null) {
            clientList = (ClientList) input.readObject();
        } else {
            input.readObject();
        }
    }
    //Displays client list
    public void displayClients() {
            System.out.println("Client List:");
            for (Client client : clients) {
                System.out.println(client.toString());
            }
    }



}

