import java.util.*;
import java.text.*;
import java.io.*;
public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCT = 2;
  private static final int SHOW_CLIENT_WISHLIST = 3;
  private static final int SHOW_CLIENT_INVOICE = 4;
  private static final int ADD_PRODUCT_TO_WISHLIST = 5;
  private static final int PLACE_CLIENT_ORDER = 6;
  private static final int ADD_CLIENT_PAYMENT = 7;
  private static final int ADD_STOCK = 8;
  private static final int SHOW_PRODUCT_WAITLIST = 9;
  private static final int SHOW_CLIENTS = 10;
  private static final int SHOW_PRODUCTS = 11;
  private static final int RETRIEVE = 12;
  private static final int SAVE = 13;
  private static final int HELP = 14;
  private UserInterface() {
    if (yesOrNo("Would you like to access the saved data? (yes/no):")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
  }
  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }
  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y or y) for yes or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number: ");
      }
    } while (true);
  }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Please enter the command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Please enter a number: ");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 14 as explained below:");
    System.out.println(EXIT + " to Exit");
    System.out.println(ADD_CLIENT + " to  add a client");
    System.out.println(ADD_PRODUCT + " to  add a product");
    System.out.println(SHOW_CLIENT_WISHLIST + " to  print wishlist for a client");
    System.out.println(SHOW_CLIENT_INVOICE + " to  print invoices for a client");
    System.out.println(ADD_PRODUCT_TO_WISHLIST + " to  add product to a clients wishlist ");
    System.out.println(PLACE_CLIENT_ORDER + " to  process a client order ");
    System.out.println(ADD_CLIENT_PAYMENT + " to  add a client payment ");
    System.out.println(ADD_STOCK + " to  add stock to a product ");
    System.out.println(SHOW_PRODUCT_WAITLIST + " to  print waitlist for a product ");
    System.out.println(SHOW_CLIENTS + " to  print client list");
    System.out.println(SHOW_PRODUCTS + " to  print products list");
    System.out.println(SAVE + " to  save data");
    System.out.println(RETRIEVE + " to  retrieve");
    System.out.println(HELP + " for help");
  }

  public void addClient() {
    String FirstName = getToken("Please enter the client's first name:");
    String LastName = getToken("Please enter the client's last name:");
    String Email = getToken("Please enter the clients email:");
    String Address = getToken("Please enter the client's address:");
    String PhoneNumber = getToken("Please enter the client's phone number:");
    Client result;
    result = warehouse.addClient(FirstName, LastName, Email, Address, PhoneNumber);
    if (result == null) {
      System.out.println("Could not add a client to the clietn list.");
    }
    System.out.println(result);
  }

  public void addProducts() {
    Product result;
    do {
      String name = getToken("Please enter the product's name:");
      double salePrice = Double.parseDouble(getToken("Please enter the product's price:"));
      int initialQuantity = Integer.parseInt(getToken("Please enter the prodcut's quantity:"));
      result = warehouse.addProduct(name, salePrice, initialQuantity);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added to product list.");
      }
      if (!yesOrNo("Would you like to add another product?")) {
        break;
      }
    } while (true);
  }
  public void addToWishlist()
  {
    WishListItem result;
    int cID = Integer.parseInt(getToken("Please enter the client's ID:"));
    int pID = Integer.parseInt(getToken("Please enter the product's Id:"));
    int initialQantity = Integer.parseInt(getToken("Please enter the quantity you want to add to the wishlist:"));
    result = warehouse.addToWishlist(cID, pID, initialQantity);
    if (result == null) {
      System.out.println("Could not add product to wishlist.");
    }
    System.out.println(result);
  }

  public void showProducts() {
      Iterator allProducts = warehouse.getProducts();
      while (allProducts.hasNext()){
        Product product = (Product)(allProducts.next());
        System.out.println(product.toString());
      }
  }

  public void showClients(){
      Iterator allClients = warehouse.getClients();
      while (allClients.hasNext()){
        Client client = (Client)(allClients.next());
        System.out.println(client.toString());
      }
  }
  public void showWishlist() {
    int cID = Integer.parseInt(getToken("Please enter the client's ID:"));
    Iterator w = warehouse.getWishlist(cID); 
    while (w.hasNext()){
      WishListItem wi = (WishListItem)(w.next());
      System.out.println(wi.toString());
    }
  }

  public void placeOrder()
  {
    int cid = Integer.parseInt(getToken("Please enter client's ID:"));
    Iterator w = warehouse.getWishlist(cid);
    while (w.hasNext())
    {
      WishListItem wi = (WishListItem)(w.next());
      System.out.println(wi.toString());
      if(!yesOrNo("Add Item to Order as is?"))
      {
        int amount = Integer.parseInt(getToken("Please enter a new amount of " + wi.getProduct().getName()+" (Enter 0 to remove)"));
        warehouse.updateWishlistItem(cid, Integer.parseInt(wi.getProduct().getProductID()), amount);
      }
    }
    Invoice i = warehouse.createOrder(cid);
    System.out.println("Order Invoice:");
    i.display();
  }

  public void addPayment()
  {
    int cid = Integer.parseInt(getToken("Please enter client's ID:"));
    double amount = Double.parseDouble(getToken("Please enter paymet amount for client:"));
    System.out.println(warehouse.addPayment(cid, amount).toString());
  }

  public void addStock() {
    int pid = Integer.parseInt(getToken("Please enter product ID:"));
    int amount = Integer.parseInt(getToken("Please enter the amount of stock to add:"));
    System.out.println(warehouse.addStock(pid, amount).toString());
  }

  public void showWaitlist() {
    int pid = Integer.parseInt(getToken("Pleasde enter the product ID:"));
    Iterator w = warehouse.getWaitlist(pid); 
    while (w.hasNext()){
      WaitListItem wi = (WaitListItem)(w.next());
      System.out.println(wi.toString());
    }
  }

  public void showInvoices() {
    int cid = Integer.parseInt(getToken("Please enter client's ID: "));
    Iterator i = warehouse.getInvoices(cid); 
    System.out.println("Printing Invoices");
    while (i.hasNext()){
      Invoice ii = (Invoice)(i.next());
      System.out.println("Client Invoice: ");
      ii.display();
    }
  }

  private void save() {
    if (warehouse.save()) {
      System.out.println(" The warehouse has saved the data. \n" );
    } else {
      System.out.println(" There an error saving the data. \n" );
    }
  }
  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse was able to retrieved data. \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("That file doesnt exist." );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }
  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:      addClient();
                                break;
        case ADD_PRODUCT:       addProducts();
                                break;
        case ADD_PRODUCT_TO_WISHLIST:      
                                addToWishlist();
                                break;
        case SHOW_CLIENT_WISHLIST:      
                                showWishlist();
                                break;
        case ADD_STOCK:         addStock();
                                break;
        case SHOW_CLIENT_INVOICE:         
                                showInvoices();
                                break;
        case SHOW_PRODUCT_WAITLIST:         
                                showWaitlist();
                                break;
        case PLACE_CLIENT_ORDER:
                                placeOrder();
                                break;
        case ADD_CLIENT_PAYMENT:
                                addPayment();
                                break;
        case SAVE:              save();
                                break;
        case RETRIEVE:          retrieve();
                                break;
        case SHOW_CLIENTS:	    showClients();
                                break; 		
        case SHOW_PRODUCTS:	    showProducts();
                                break; 		
        case HELP:              help();
                                break;
      }
    }
  }
  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
