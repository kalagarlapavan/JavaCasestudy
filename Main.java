import java.util.ArrayList;
import java.util.List;

// ============================================================
// 1. ABSTRACTION - Abstract class representing any User
// ============================================================
abstract class User {
    private String name;      // Encapsulation: private fields
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Encapsulation: public getters/setters
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    // Abstraction: abstract method — each subclass must implement
    public abstract void displayRole();

    // Regular method shared by all users
    public void login() {
        System.out.println(name + " logged in.");
    }
}

// ============================================================
// 2. INTERFACE - Payment behaviour (Interface)
// ============================================================
interface Payable {
    void makePayment(double amount);   // All payment types must implement this
    String getPaymentMethod();
}

// ============================================================
// 3. INTERFACE - Trackable behaviour
// ============================================================
interface Trackable {
    void trackStatus();
}

// ============================================================
// 4. INHERITANCE - Customer extends User, implements Payable
// ============================================================
class Customer extends User implements Payable, Trackable {
    private String address;
    private String paymentMethod;
    private List<Order> orders;

    public Customer(String name, String email, String address, String paymentMethod) {
        super(name, email);   // Calling parent constructor
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.orders = new ArrayList<>();
    }

    // Abstraction: implementing abstract method
    @Override
    public void displayRole() {
        System.out.println("Role: Customer | Name: " + getName());
    }

    // Interface method
    @Override
    public void makePayment(double amount) {
        System.out.println(getName() + " paid Rs." + amount + " via " + paymentMethod);
    }

    @Override
    public String getPaymentMethod() { return paymentMethod; }

    @Override
    public void trackStatus() {
        System.out.println(getName() + " is tracking their order...");
        for (Order o : orders) {
            System.out.println("  Order #" + o.getOrderId() + " -> " + o.getStatus());
        }
    }

    public void placeOrder(Order order) {
        orders.add(order);
        System.out.println(getName() + " placed Order #" + order.getOrderId());
    }

    public String getAddress() { return address; }
}

// ============================================================
// 5. INHERITANCE - Restaurant extends User
// ============================================================
class Restaurant extends User {
    private String location;
    private List<MenuItem> menu;

    public Restaurant(String name, String email, String location) {
        super(name, email);
        this.location = location;
        this.menu = new ArrayList<>();
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Restaurant | Name: " + getName());
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
        System.out.println("Menu item added: " + item.getName());
    }

    public void acceptOrder(Order order) {
        order.setStatus("Accepted");
        System.out.println(getName() + " accepted Order #" + order.getOrderId());
    }

    public void prepareFood(Order order) {
        order.setStatus("Preparing");
        System.out.println(getName() + " is preparing Order #" + order.getOrderId());
    }

    public List<MenuItem> getMenu() { return menu; }
    public String getLocation()     { return location; }
}

// ============================================================
// 6. INHERITANCE - DeliveryAgent extends User, implements Trackable
// ============================================================
class DeliveryAgent extends User implements Trackable {
    private boolean available;
    private String currentOrderId;

    public DeliveryAgent(String name, String email) {
        super(name, email);
        this.available = true;
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Delivery Agent | Name: " + getName());
    }

    public void pickUpOrder(Order order) {
        order.setStatus("Out for Delivery");
        this.available = false;
        this.currentOrderId = order.getOrderId();
        System.out.println(getName() + " picked up Order #" + order.getOrderId());
    }

    public void deliverOrder(Order order) {
        order.setStatus("Delivered");
        this.available = true;
        System.out.println(getName() + " delivered Order #" + order.getOrderId());
    }

    @Override
    public void trackStatus() {
        if (!available)
            System.out.println(getName() + " is delivering Order #" + currentOrderId);
        else
            System.out.println(getName() + " is available for delivery.");
    }

    public boolean isAvailable() { return available; }
}

// ============================================================
// 7. ENCAPSULATION - MenuItem class
// ============================================================
class MenuItem {
    private String name;
    private double price;
    private String category;

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName()     { return name; }
    public double getPrice()    { return price; }
    public String getCategory() { return category; }

    public void display() {
        System.out.println("  " + name + " (" + category + ") - Rs." + price);
    }
}

// ============================================================
// 8. ENCAPSULATION - Order class
// ============================================================
class Order implements Trackable {
    private String orderId;
    private Customer customer;
    private List<MenuItem> items;
    private String status;
    private double totalAmount;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = "Pending";
        this.totalAmount = 0;
    }

    public void addItem(MenuItem item) {
        items.add(item);
        totalAmount += item.getPrice();
    }

    // Interface method
    @Override
    public void trackStatus() {
        System.out.println("Order #" + orderId + " Status: " + status);
    }

    // Getters and Setters
    public String getOrderId()       { return orderId; }
    public String getStatus()        { return status; }
    public double getTotalAmount()   { return totalAmount; }
    public Customer getCustomer()    { return customer; }
    public List<MenuItem> getItems() { return items; }
    public void setStatus(String status) {
        this.status = status;
        System.out.println("Order #" + orderId + " status updated to: " + status);
    }

    public void displayOrderSummary() {
        System.out.println("\n--- Order Summary ---");
        System.out.println("Order ID : " + orderId);
        System.out.println("Customer : " + customer.getName());
        System.out.println("Items:");
        for (MenuItem item : items) item.display();
        System.out.printf("Total    : Rs.%.2f%n", totalAmount);
        System.out.println("Status   : " + status);
        System.out.println("---------------------");
    }
}

// ============================================================
// 9. POLYMORPHISM - System class that handles multiple users
// ============================================================
class FoodOrderingSystem {
    private String systemName;

    public FoodOrderingSystem(String systemName) {
        this.systemName = systemName;
    }

    // Polymorphism: same method works for Customer, Restaurant, DeliveryAgent
    public void showUserRole(User user) {
        user.displayRole();   // runtime polymorphism / method overriding
    }

    // Polymorphism: same method works for Order, Customer, DeliveryAgent
    public void showTracking(Trackable trackable) {
        trackable.trackStatus();
    }

    public void processPayment(Payable payable, double amount) {
        payable.makePayment(amount);
    }

    public void printHeader() {
        System.out.println("==========================================");
        System.out.println("  Welcome to " + systemName);
        System.out.println("==========================================\n");
    }
}

// ============================================================
// 10. MAIN - Demonstrates all OOP concepts together
// ============================================================
public class Main {
    public static void main(String[] args) {

        // System object
        FoodOrderingSystem system = new FoodOrderingSystem("Online Food Ordering System");
        system.printHeader();

        // --- Create Users (Inheritance + Encapsulation) ---
        Customer customer = new Customer("Pavan", "pavan@email.com", "Amritapuri", "UPI");
        Restaurant restaurant = new Restaurant("Spice Garden", "spice@email.com", "Kollam");
        DeliveryAgent agent = new DeliveryAgent("Ravi", "ravi@email.com");

        // --- Polymorphism: showUserRole() works for all User types ---
        System.out.println("=== User Roles ===");
        system.showUserRole(customer);
        system.showUserRole(restaurant);
        system.showUserRole(agent);
        System.out.println();

        // --- Login (inherited method) ---
        System.out.println("=== Login ===");
        customer.login();
        agent.login();
        System.out.println();

        // --- Build Menu (Encapsulation) ---
        System.out.println("=== Menu Setup ===");
        MenuItem item1 = new MenuItem("Paneer Butter Masala", 180.0, "Main Course");
        MenuItem item2 = new MenuItem("Garlic Naan", 40.0, "Bread");
        MenuItem item3 = new MenuItem("Mango Lassi", 60.0, "Drink");
        restaurant.addMenuItem(item1);
        restaurant.addMenuItem(item2);
        restaurant.addMenuItem(item3);
        System.out.println();

        // --- Place Order ---
        System.out.println("=== Placing Order ===");
        Order order = new Order("ORD001", customer);
        order.addItem(item1);
        order.addItem(item2);
        order.addItem(item3);
        customer.placeOrder(order);
        System.out.println();

        // --- Order Summary ---
        order.displayOrderSummary();

        // --- Payment (Interface: Payable) ---
        System.out.println("\n=== Payment ===");
        system.processPayment(customer, order.getTotalAmount());
        System.out.println();

        // --- Restaurant accepts and prepares ---
        System.out.println("=== Restaurant Actions ===");
        restaurant.acceptOrder(order);
        restaurant.prepareFood(order);
        System.out.println();

        // --- Delivery Agent picks up ---
        System.out.println("=== Delivery ===");
        agent.pickUpOrder(order);
        System.out.println();

        // --- Tracking (Interface: Trackable, Polymorphism) ---
        System.out.println("=== Tracking Status ===");
        system.showTracking(order);     // Order implements Trackable
        system.showTracking(customer);  // Customer implements Trackable
        system.showTracking(agent);     // DeliveryAgent implements Trackable
        System.out.println();

        // --- Delivery complete ---
        System.out.println("=== Delivery Complete ===");
        agent.deliverOrder(order);
        System.out.println();

        // --- Final Status ---
        System.out.println("=== Final Order Status ===");
        order.trackStatus();
        System.out.println("\nThank you for using " + "Online Food Ordering System" + "!");
    }
}