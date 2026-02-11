import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    // ----------------- Product Attributes -----------------
    private final int id;
    private String name;
    private int quantity;
    private double price;

    // ----------------- Constructor -----------------
    public Product(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // ----------------- Getters & Setters -----------------
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }

    // ----------------- Inventory Storage -----------------
    static HashMap<Integer, Product> inventory = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "inventory.dat";

    // ================= INPUT METHODS =================

    public static int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please enter a valid integer.");
            }
        }
    }

    public static double getDoubleInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number! Please enter a valid price.");
            }
        }
    }

    // ================= CRUD METHODS =================

    public static void addProduct() {

        int id = getIntInput("Enter Product ID: ");

        if (inventory.containsKey(id)) {
            System.out.println("Product ID already exists!");
            return;
        }

        System.out.print("Enter Product Name: ");
        String name = scanner.next().trim();

        if (name.isEmpty()) {
            System.out.println("Product name cannot be empty!");
            return;
        }

        int quantity = getIntInput("Enter Quantity: ");
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!");
            return;
        }

        double price = getDoubleInput("Enter Price: ");
        if (price < 0) {
            System.out.println("Price cannot be negative!");
            return;
        }

        inventory.put(id, new Product(id, name, quantity, price));
        System.out.println("Product added successfully!");
    }

    public static void viewProducts() {

        if (inventory.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\n--- Product List ---");
        for (Product p : inventory.values()) {
            System.out.printf(
                    "ID: %d | Name: %s | Qty: %d | Price: Rs %.2f%n",
                    p.getId(), p.getName(),
                    p.getQuantity(), p.getPrice()
            );
        }
    }


    public static void updateProduct() {

        int id = getIntInput("Enter Product ID to update: ");

        if (!inventory.containsKey(id)) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter new name: ");
        String name = scanner.next().trim();

        if (name.isEmpty()) {
            System.out.println("Product name cannot be empty!");
            return;
        }

        int quantity = getIntInput("Enter new quantity: ");
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!");
            return;
        }

        double price = getDoubleInput("Enter new price: ");
        if (price < 0) {
            System.out.println("Price cannot be negative!");
            return;
        }

        Product p = inventory.get(id);
        p.setName(name);
        p.setQuantity(quantity);
        p.setPrice(price);

        System.out.println("Product updated successfully!");
    }

    public static void deleteProduct() {

        int id = getIntInput("Enter Product ID to delete: ");

        if (!inventory.containsKey(id)) {
            System.out.println("Product not found!");
            return;
        }

        inventory.remove(id);
        System.out.println("Product deleted successfully!");
    }

    // ================= SUMMARY METHOD =================

    public static void displaySummary() {

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        int totalProducts = inventory.size();
        int totalQty = 0;
        double totalValue = 0;

        for (Product p : inventory.values()) {
            totalQty += p.getQuantity();
            totalValue += p.getQuantity() * p.getPrice();
        }

        System.out.println("\n=== Inventory Summary ===");
        System.out.println("Total Products: " + totalProducts);
        System.out.println("Total Quantity: " + totalQty);
        System.out.printf("Total Inventory Value: Rs %.2f%n", totalValue);
    }

    // ================= FILE HANDLING =================

    public static void saveToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(inventory);

        } catch (IOException e) {
            System.out.println("Error saving inventory file.");
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            inventory = (HashMap<Integer, Product>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory file.");
        }
    }

    // ================= MENU =================

    public static void displayMenu() {
        System.out.println("\n=== Inventory Management System ===");
        System.out.println("1. Add Product");
        System.out.println("2. View Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Display Inventory Summary");
        System.out.println("6. Exit");
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        loadFromFile();
        boolean running = true;

        while (running) {

            displayMenu();
            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> viewProducts();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> displaySummary();
                case 6 -> {
                    saveToFile();
                    running = false;
                    System.out.println("Exiting system...");
                }
                default -> System.out.println("Invalid choice! Please select 1-6.");
            }
        }

        scanner.close();
    }
}
