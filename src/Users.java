import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Collections;

public class Users {
    protected int userID;
    protected String password;
    protected String address;
    protected long contactNum;
    protected String name;

    List<Items> itemsList;

    private Cart cart;
    private int salesCount;
    private int amountBought;


    public Users(int userID, String password, String address, long contactNum, String name) {
        this.userID = userID;
        this.password = password;
        this.address = address;
        this.contactNum = contactNum;
        this.name = name;
        this.itemsList = new ArrayList<>();
        this.cart = new Cart(String.valueOf(userID));
        this.salesCount = 0;
        this.amountBought = 0;

    }
    public String serialize() {
        return userID + "|" + password + "|" + address + "|" + contactNum + "|" + name;
    }

    public static Users deserialize(String data) {
        String[] parts = data.split("\\|");
        int userID = Integer.parseInt(parts[0].trim());
        String password = parts[1].trim();
        String address = parts[2].trim();
        long contactNum = Long.parseLong(parts[3].trim());
        String name = parts[4].trim();
        return new Users(userID, password, address, contactNum, name);
    }
    public static Users findUserByID(ArrayList<Users> users, int userID) {
        for (Users user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }
    public static void saveUsers(ArrayList<Users> users) {
        try {
            FileWriter writer = new FileWriter("users.txt");
            for (Users user : users) {
                writer.write(user.serialize() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving users.");
            e.printStackTrace();
        }
    }
    public static ArrayList<Users> loadUsers() {
        ArrayList<Users> users = new ArrayList<>();
        File file = new File("users.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                Users user = Users.deserialize(data);
                users.add(user);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty user list.");
        }
        return users;
    }
    public static void viewAllProducts(ArrayList<Users> users) {
        int currentUserIndex = 0;
        Scanner input = new Scanner(System.in);
        char choice = 'N';

        while (choice != 'X' && currentUserIndex < users.size()) {
            Users user = users.get(currentUserIndex);

            if (!user.getItemsList().isEmpty()) {
                System.out.println("\n--BUY MENU--\n");
                System.out.format("%-12s | %-20s | %-15s | %-10s | %-8s%n",
                        "Product ID", "Item Name", "Category", "Unit Price", "Quantity");
                for (Items item : user.getItemsList()) {
                    System.out.format("%-12d | %-20s | %-15s | %-10.2f | %-8d%n",
                            item.getProductID(), item.getItemName(), item.getCategory(),
                            item.getUnitPrice(), item.getQuantityAvailable());
                }
                System.out.println("\nN. Next Seller's Product\nX. Exit");

                do {
                    System.out.print("Choice: ");
                    choice = input.nextLine().toUpperCase().charAt(0);
                    if (choice == 'N') {
                        currentUserIndex++;
                        break;
                    } else if (choice != 'X') {
                        System.out.println("Invalid option. Please enter 'N' to continue or 'X' to exit.");
                    }
                } while (choice != 'X');
            } else {
                currentUserIndex++;
            }
        }

        if (currentUserIndex >= users.size()) {
            System.out.println("No more sellers' products to display.");
        }
    }
    public static void showProductsBySeller(Scanner input, ArrayList<Users> users) {
        System.out.print("Enter Seller's ID: ");
        int sellerId = Main.getIntInput(input, "");
        boolean sellerFound = false;

        for (Users user : users) {
            if (user.getUserID() == sellerId) {
                sellerFound = true;
                List<Items> sellerItems = user.getItemsList();
                if (sellerItems.isEmpty()) {
                    System.out.println("This seller has no products listed.");
                } else {
                    System.out.format("| %-10s | %-20s | %-15s | %-12s | %-9s |%n",
                            "Product ID", "Item Name", "Category", "Unit Price", "Quantity");
                    for (Items item : sellerItems) {
                        System.out.format("| %-10d | %-20s | %-15s | %-12.2f | %-9d |%n",
                                item.getProductID(),
                                item.getItemName(),
                                item.getCategory(),
                                item.getUnitPrice(),
                                item.getQuantityAvailable());
                    }
                }
                break;
            }
        }

        if (!sellerFound) {
            System.out.println("Seller not found.");
        }
    }
    public static void searchProductsByCategory(Scanner input, ArrayList<Users> users) {
        System.out.print("Enter the category to search: ");
        String category = input.nextLine().trim();

        boolean found = false;
        for (Users user : users) {
            for (Items item : user.getItemsList()) {
                if (item.getCategory().equalsIgnoreCase(category)) {
                    if (!found) {
                        System.out.format("| %-10s | %-20s | %-15s | %-12s | %-9s |%n",
                                "Product ID", "Item Name", "Category", "Unit Price", "Quantity");
                        found = true;
                    }
                    System.out.format("| %-10d | %-20s | %-15s | $%-11.2f | %-9d |%n",
                            item.getProductID(),
                            item.getItemName(),
                            item.getCategory(),
                            item.getUnitPrice(),
                            item.getQuantityAvailable());
                }
            }
        }

        if (!found) {
            System.out.println("No items found in the specified category.");
        }
    }
    public static void searchProductsByName(Scanner input, ArrayList<Users> users) {
        System.out.print("Enter the name of the product to search: ");
        String productName = input.nextLine().trim();
        boolean productFound = false;

        for (Users user : users) {
            for (Items item : user.getItemsList()) {
                if (item.getItemName().equalsIgnoreCase(productName)) {
                    if (!productFound) {
                        System.out.format("| %-10s | %-20s | %-15s | %-12s | %-9s |%n",
                                "Product ID", "Item Name", "Category", "Unit Price", "Quantity");
                        productFound = true;
                    }
                    System.out.format("| %-10d | %-20s | %-15s | $%-11.2f | %-9d |%n",
                            item.getProductID(),
                            item.getItemName(),
                            item.getCategory(),
                            item.getUnitPrice(),
                            item.getQuantityAvailable());
                }
            }
        }

        if (!productFound) {
            System.out.println("Product not found.");
        }
    }
    public static void showAllUsers(ArrayList<Users> users) {
        System.out.println("All Users: ");
        System.out.format("| %-9s | %-14s | %-24s | %-29s | %-14s |%n", "User ID", "Password", "Name", "Address", "Phone Number");

        Collections.sort(users, (u1, u2) -> Integer.compare(u1.getUserID(), u2.getUserID()));

        for (Users user : users) {
            System.out.format("| %-9d | %-14s | %-24s | %-29s | %-14d |%n",
                    user.getUserID(),
                    user.getPassword(),
                    user.getName(),
                    user.getAddress(),
                    user.getContactNum());
        }
    }

    protected long getContactNum() {
        return contactNum;
    }

    protected String getName() {
        return name;
    }

    protected String getPassword() {
        return password;
    }

    protected String getAddress() {
        return address;
    }

    protected int getUserID() {
        return userID;
    }

    protected void addItem(Items item) {
        itemsList.add(item);
    }

    protected List<Items> getItemsList() {
        return itemsList;
    }

    public Cart getCart() {
        return cart;
    }
    public int getSalesCount() {
        return salesCount;
    }

    public void addSalesCount(double salesCount) {
        this.salesCount += salesCount;
    }

    public int getAmountBought() {
        return amountBought;
    }
    public void addAmountBought(double amount) {
        this.amountBought += amount;
    }
}
