import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Items extends Users {
    protected int productID;
    protected int sellerID;
    protected String itemName;
    protected String category;
    protected String itemDescription;
    protected int quantityAvailable;
    protected double unitPrice;

    private static final ArrayList<Items> allItems = new ArrayList<>();
    public Items(int userID, int productID, String itemName, String category, String itemDescription, int quantityAvailable, double unitPrice) {
        super(userID, "", "", 0, "");
        this.productID = productID;
        this.itemName = itemName;
        this.category = category;
        this.itemDescription = itemDescription;
        this.quantityAvailable = quantityAvailable;
        this.unitPrice = unitPrice;

        allItems.add(this);
    }

    public static Items getItemByID(int productID) {
        for (Items item : allItems) {
            if (item.getProductID() == productID) {
                return item;
            }
        }
        return null;
    }

    public String serialize() {
        // Format namo para sayon: ProductID;UserID;ItemName;Category;ItemDescription;QuantityAvailable;UnitPrice
        return productID + ";" + userID + ";" + itemName + ";" + category + ";" + itemDescription + ";" + quantityAvailable + ";" + unitPrice;
    }

    public static Items deserialize(String data) {
        String[] parts = data.split(";");
        int productID = Integer.parseInt(parts[0]);
        int userID = Integer.parseInt(parts[1]);
        String itemName = parts[2];
        String category = parts[3];
        String itemDescription = parts[4];
        int quantityAvailable = Integer.parseInt(parts[5]);
        double unitPrice = Double.parseDouble(parts[6]);

        return new Items(userID, productID, itemName, category, itemDescription, quantityAvailable, unitPrice);
    }

    public static void saveItems(ArrayList<Items> items) {
        File file = new File("Items.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Items item : items) {
                pw.println(item.serialize());
            }
            System.out.println("Items saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Items> loadItems() {
        ArrayList<Items> items = new ArrayList<>();
        File file = new File("Items.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (!data.trim().isEmpty()) {
                    items.add(deserialize(data.trim()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Items file not found. Starting with an empty item list.");
        }
        return items;
    }

    public static void showMyProducts(ArrayList<Users> users, int userId) {
        Users currentUser = null;
        for (Users user : users) {
            if (user.getUserID() == userId) {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.format("| %-10s | %-30s | %-20s | %-10s | %-10s |%n", "Product ID", "Item Name", "Category", "Unit Price", "Quantity");
        for (Items item : currentUser.getItemsList()) {
            System.out.format("| %-10d | %-30s | %-20s | ₱%-9.2f | %-10d |%n",
                    item.getProductID(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getUnitPrice(),
                    item.getQuantityAvailable());
        }
    }
    public static void showMyLowStockProducts(Scanner input, ArrayList<Users> users, int userId) {
        final int quantity = 5;

        Users currentUser = null;
        for (Users user : users) {
            if (user.getUserID() == userId) {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.format("| %-10s | %-20s | %-10s | %-12s | %-10s |%n", "Product ID", "Item Name", "Category", "Unit Price", "Quantity");

        ArrayList<Items> lowStockItems = new ArrayList<>();
        for (Items item : currentUser.getItemsList()) {
            if (item.getQuantityAvailable() <= quantity) {
                lowStockItems.add(item);
            }
        }

        int index = 0;
        while (index < lowStockItems.size()) {
            Items currentItem = lowStockItems.get(index);
            System.out.format("| %-10d | %-20s | %-10s | ₱%-11.2f | %-10d |%n",
                    currentItem.getProductID(),
                    currentItem.getItemName(),
                    currentItem.getCategory(),
                    currentItem.getUnitPrice(),
                    currentItem.getQuantityAvailable());

            System.out.print("\nEnter 'N' to see the next Item or Enter 'X' to Exit: ");
            char viewNext = Character.toUpperCase(input.nextLine().charAt(0));
            if (viewNext == 'N') {
                index++;
            } else if (viewNext == 'X') {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'N' or 'X'.");
            }
        }

        if (index == lowStockItems.size()) {
            System.out.println("No more low stock products.");
        }
    }
    public int getProductID() {
        return productID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public void replenishStock(int amountToAdd) {this.quantityAvailable += amountToAdd;}
}
