import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.InputMismatchException;


public class Main {
    private static boolean exitUserMenuStatus = false;
    private static final String ADMIN_PASSWORD = "H3LLo?";// Admin password
    public static void main(String[] args) {
        char choice, subChoice = 0;
        Scanner input = new Scanner(System.in);
        ArrayList<Users> user = Users.loadUsers();
        ArrayList<Items> itemsList = new ArrayList<>(Items.loadItems());

        for (Users u: user) {
            for (Items i: itemsList) {
                if (i.getUserID() == u.getUserID()) {
                    u.getItemsList().add(new Items(i.getUserID(), i.getProductID(), i.getItemName(), i.getCategory(), i.getItemDescription(), i.quantityAvailable, i.getUnitPrice()));
                }
            }
        }
        int id;
        String password;

        do {
            System.out.println("MAIN MENU\n\n1. Register as a User \n2. User Menu \n3. Admin Menu \n4. Exit \n");

            while (true) {
                try {
                    System.out.print("Enter choice: ");
                    choice = input.nextLine().charAt(0);
                    break;
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Invalid input.");
                }
            }

            switch (choice) {
                case '1':
                    System.out.print("Please register ID: ");
                    while (true) {
                        try {
                            id = Integer.parseInt(input.nextLine());
                            if (!isIDExists(user, id)) {
                                break;
                            } else {
                                System.out.print("ID already exists. Try again: ");
                            }
                        } catch (NumberFormatException | InputMismatchException e) {
                            System.out.print("Invalid input. Please enter a valid ID: ");
                        }
                    }
                    Users newUser = new Users(id, getPassword(input), getAddress(input), getContactNum(input), getName(input));
                    user.add(newUser);
                    Users.saveUsers(user);
                    break;
                case '2':
                    System.out.println("Please input your ID and password. \n");
                    id = getIntInput(input, "ID: ");
                    System.out.print("Password: ");
                    password = input.nextLine();

                    // loading nga wow kaayo by jamis
                    System.out.println("Processing input...");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted.");
                    }

                    if (!isIDPasswordMatch(user, id, password)) {
                        System.out.println("Invalid ID and Password. \nGoing back to menu...\n");
                        break;
                    }
                    else {
                        Users currentUser = Users.findUserByID(user, id);
                        if (currentUser != null) {
                            currentUser.getCart().loadCartFromFile();
                            System.out.println("Login successful. Cart loaded.");

                            do {
                                System.out.println("1. Sell Menu \n2. Buy Menu \n3. Exit User Menu");
                                System.out.print("Enter choice: ");
                                try {
                                    String inputStr = input.nextLine();
                                    if (inputStr.isEmpty()) {
                                        System.out.println("Please enter a valid choice.");
                                        continue;
                                    }
                                    subChoice = inputStr.charAt(0);
                                    switch (subChoice) {
                                        case '1':
                                            sellMenu(input, user, id, itemsList);
                                            break;
                                        case '2':
                                            buyMenu(input, user, id, itemsList);
                                            break;
                                        case '3':
                                            exitUserMenu();
                                            break;
                                        default:
                                            System.out.println("Invalid Choice.");
                                    }
                                } catch (StringIndexOutOfBoundsException e) {
                                    System.out.println("Please enter a valid choice.");
                                }
                            } while (!exitUserMenuStatus && subChoice != '3');
                        } else {
                            System.out.println("User not found.");
                        }
                    }
                    break;

                case '3':
                    System.out.println("Please input your Admin password. \n");
                    System.out.print("Admin Password: ");
                    password = input.nextLine();

                    System.out.println("Processing input...");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!password.equals(ADMIN_PASSWORD)) {
                        System.out.println("Invalid Password. \nGoing back to menu...\n");
                        break;
                    }
                    else {
                        char adminChoice;
                        do {
                            System.out.println("1. Show All Users \n2. Show All Sellers \n3. Show Total Sales in Given Duration \n4. Show Sellers Sales \n5. Show Shopaholics \n6. Back to Main Menu");
                            System.out.print("Enter choice: ");
                            adminChoice = input.nextLine().charAt(0);
                            switch (adminChoice) {
                                case '1':
                                    Users.showAllUsers(user);
                                    break;
                                case '2':
                                    showAllSellers(user,itemsList);
                                    break;
                                case '3':
                                    showTotalSalesInGivenDuration();
                                    break;
                                case '4':
                                    showSellersSales(user);
                                    break;
                                case '5':
                                    showShopaholics(user);
                                    break;
                                case '6':
                                    System.out.println("Returning to Main Menu...");
                                default:
                                    System.out.println("Invalid Choice.");
                            }
                        } while (adminChoice != '6');
                    }
                    break;
                case '4':
                    System.out.println("Exiting application...");
                    Users.saveUsers(user);
                    Items.saveItems(itemsList);
                    System.out.println("All data saved. Exiting now.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice.");
            }
        } while (choice != '4');
    }

    public static int getIntInput(Scanner input, String prompt) {
        int result = 0;
        boolean isValidInput = false;
        while (!isValidInput) {
            try {
                System.out.print(prompt);
                result = Integer.parseInt(input.nextLine());
                isValidInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return result;
    }
    public static String getStringInput(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }
    public static double getDoubleInput(Scanner input, String prompt){
        double result = 0.0;
        boolean isValidInput = false;
        while(!isValidInput){
            try{
                System.out.print(prompt);
                result = Double.parseDouble(input.nextLine());
                isValidInput = true;
            }catch(NumberFormatException e){
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return result;
    }
    public static boolean isIDExists(ArrayList<Users> s, int id) {
        for (Users user: s) {
            if (user.getUserID() == id) {
                return true;
            }
        }
        return false;
    }
    public static boolean isIDPasswordMatch(ArrayList<Users> s, int id, String password) {
        for(Users user: s) {
            if (( user.getUserID() == id) && (user.getPassword().equals(password))) {
                return true;
            }
        }
        return false;
    }

    public static String getPassword(Scanner input) {
        String password;
        while (true) {
            System.out.print("Please enter your password: ");
            password = input.nextLine();

            if (password.length() <= 10) {
                break;
            } else {
                System.out.println("Invalid password. Password must be at most 10 characters long.");
            }
        }
        return password;
    }
    public static String getAddress(Scanner input) {
        String address;
        while (true) {
            System.out.print("Please enter your address: ");
            address = input.nextLine();

            if (address.length() <= 30) {
                break;
            } else {
                System.out.println("Invalid address. Address must be at most 30 characters long.");
            }
        }
        return address;
    }
    public static long getContactNum(Scanner input) {
        long cn;
        long max = 99999999999L;
        while (true) {
            System.out.print("Please enter your contact number: ");
            try {
                cn = Long.parseLong(input.nextLine());

                if (cn >= max) {
                    System.out.println("Invalid contact number. Contact number must be at most 11 digits long.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid contact number.");
            }
        }
        return cn;
    }
    public static String getName(Scanner input) {
        String name;
        while (true) {
            System.out.print("Please enter your name: ");
            name = input.nextLine();

            if (name.length() < 20) {
                break;
            } else {
                System.out.println("Invalid name. Name must be at most 20 characters long.");
            }
        }
        return name;
    }


    public static void sellMenu(Scanner input, ArrayList<Users> users, int userId, ArrayList<Items> itemsList) {
        char subsubChoice = 0;
        do {
            System.out.println("\n1. Add New Item \n2. Edit Stock \n3. Show My Products \n4. Show My Low Stock Products \n5. Exit Sell Menu");
            System.out.print("Enter choice: ");
            try {
                String inputStr = input.nextLine();
                if (inputStr.isEmpty()) {
                    System.out.println("Please enter a valid choice.");
                    continue;
                }
                subsubChoice = inputStr.charAt(0);
                switch (subsubChoice) {
                    case '1':
                        addNewItem(input, users, userId, itemsList);
                        break;
                    case '2':
                        editStock(input, users, userId, itemsList);
                        break;
                    case '3':
                        Items.showMyProducts(users, userId);
                        break;
                    case '4':
                        Items.showMyLowStockProducts(input, users, userId);
                        break;
                    case '5':
                        System.out.println("Exiting Sell Menu...");
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please enter a valid choice.");
                input.nextLine();
                subsubChoice = '0';
            }
        } while (subsubChoice != '5');
    }

    public static void addNewItem(Scanner input, ArrayList<Users> users, int userId, ArrayList<Items> itemsList) {
        boolean isUnique;
        int itemId;

        do {
            itemId = getIntInput(input, "Enter Item ID: ");
            isUnique = true;

            for (Users user : users) {
                if (user.getUserID() == userId) {
                    for (Items item : user.getItemsList()) {
                        if (item.getProductID() == itemId) {
                            System.out.println("Item ID already exists within your items. Please enter a unique Item ID.");
                            isUnique = false;
                            break;
                        }
                    }
                    if (!isUnique) break;
                }
            }
        } while (!isUnique);


        String itemName = getStringInput(input, "Enter Item Name: ");
        String category = getStringInput(input, "Enter Category: ");
        String description = getStringInput(input, "Enter Description: ");
        int quantity = getIntInput(input, "Enter Quantity: ");
        double price = getDoubleInput(input, "Enter Price: ");

        Items newItem = new Items(userId, itemId, itemName, category, description, quantity, price);


        for (Users user : users) {
            if (user.getUserID() == userId) {
                itemsList.add(newItem);
                user.addItem(newItem);
                System.out.println("Item successfully added.");
                return;
            }
        }

        System.out.println("User not found.");
    }

    public static void editStock(Scanner input, ArrayList<Users> users, int userID, ArrayList<Items> itemsList) {
        System.out.println("\n--SELL MENU--\n");
        Users editingUser = null;
        for (Users user : users) {
            if (user.getUserID() == userID) {
                editingUser = user;
                break;
            }
        }
        if (editingUser == null) {
            System.out.println("User not found.");
            return;
        }
        System.out.println("Your items:");
        for (Items item : editingUser.getItemsList()) {
            System.out.println("Item ID: " + item.getProductID() + ", Name: " + item.getItemName());
        }

        System.out.print("Enter Item ID: ");
        int itemId = Integer.parseInt(input.nextLine());
        Items itemToEdit = null;
        for (Items item : editingUser.getItemsList()) {
            if (item.getProductID() == itemId) {
                itemToEdit = item;
                break;
            }
        }


        if (itemToEdit == null) {
            System.out.println("Invalid Item ID or item does not belong to this user.");
            return;
        }
        System.out.format("| %-10s | %-20s | %-10s | %-10s | %-10s |%n", "Product ID", "Item Name", "Category", "Unit Price", "Quantity");
        System.out.format("| %-10d | %-20s | %-10s | ₱%-9.2f | %-10d |%n",
                itemToEdit.getProductID(),
                itemToEdit.getItemName(),
                itemToEdit.getCategory(),
                itemToEdit.getUnitPrice(),
                itemToEdit.getQuantityAvailable());



        boolean finishedEditing = false;
        while (!finishedEditing) {
            System.out.println("\n[1]. Replenish");
            System.out.println("[2]. Change Price");
            System.out.println("[3]. Change Item name");
            System.out.println("[4]. Change Category");
            System.out.println("[5]. Change Description");
            System.out.println("[6]. Finish Editing");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to add: ");
                    int amount = Integer.parseInt(input.nextLine());
                    itemToEdit.replenishStock(amount);
                    System.out.println("Replenish Done.");
                    break;
                case 2:
                    System.out.print("Enter the new price: ");
                    double newPrice = Double.parseDouble(input.nextLine());
                    itemToEdit.setUnitPrice(newPrice);

                    System.out.println("Price Updated.");
                    break;
                case 3:
                    System.out.print("Enter the new Item Name: ");
                    String newName = input.nextLine();
                    itemToEdit.setItemName(newName);
                    System.out.println("Item name Updated.");
                    break;
                case 4:
                    System.out.print("Enter the new Category: ");
                    String newCategory = input.nextLine();
                    itemToEdit.setCategory(newCategory);
                    System.out.println("Category Updated.");
                    break;
                case 5:
                    System.out.print("Enter the new Description: ");
                    String newDescription = input.nextLine();
                    itemToEdit.setItemDescription(newDescription);
                    System.out.println("Description Updated.");
                    break;
                case 6:
                    for (Items i : editingUser.getItemsList()) {
                        System.out.println(i.getQuantityAvailable());

                        for (Items item : itemsList) {
                            if (item.getProductID() == i.getProductID()) {
                                item.setQuantityAvailable(i.getQuantityAvailable());
                                item.setUnitPrice(i.getUnitPrice());
                                item.setItemName(i.getItemName());
                                item.setCategory(i.getCategory());
                                item.setItemDescription(i.getItemDescription());
                                break;
                            }
                        }
                    }
                    finishedEditing = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    public static void buyMenu(Scanner input, ArrayList<Users> users, int userId, ArrayList<Items> itemsList)
    {
        char subChoice;
        do {
            System.out.println("\n1. View all Products");
            System.out.println("2. Show all Products by a Specific Seller");
            System.out.println("3. Search Products by Category");
            System.out.println("4. Search Products by Name");
            System.out.println("5. Add to Cart");
            System.out.println("6. Edit Cart");
            System.out.println("7. Check Out Menu");
            System.out.println("8. Exit Buy Menu");
            System.out.print("Enter choice: ");
            subChoice = input.nextLine().charAt(0);

            switch (subChoice) {
                case '1':
                    Users.viewAllProducts(users);
                    break;
                case '2':
                    Users.showProductsBySeller(input, users);
                    break;
                case '3':
                    Users.searchProductsByCategory(input, users);
                    break;
                case '4':
                    Users.searchProductsByName(input, users);
                    break;
                case '5':
                    addToCart(input, users, userId, itemsList);
                    Users currentUser = Users.findUserByID(users, userId);
                    if (currentUser != null) {
                        currentUser.getCart().saveCartToFile();
                    } else {
                        System.out.println("Error: User not found, cart not saved.");
                    }
                    break;

                case '6':
                    editCart(input, users, userId);
                    break;
                case '7':
                    checkOutMenu(input, users, userId, itemsList);
                    break;
                case '8':
                    System.out.println("Exiting Buy Menu...");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } while (subChoice != '8');
    }


    public static void exitUserMenu() {
        System.out.println("Exiting User Menu...");
        exitUserMenuStatus = true;
    }


    public static void addToCart(Scanner input, ArrayList<Users> users, int userId, ArrayList<Items> itemsList) {
        Users currentUser = Users.findUserByID(users, userId);
        if (currentUser == null) {
            System.out.println("User not found.");
            return;
        }

        Cart userCart = currentUser.getCart();
        if (userCart == null) {
            System.out.println("There is an issue with the user's cart.");
            return;
        }

        System.out.print("Enter Product ID to add to cart: ");
        int productId;
        try {
            productId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a numeric value.");
            return;
        }

        Items itemToAdd = Items.getItemByID(productId);
        if (itemToAdd == null) {
            System.out.println("Product not found.");
            return;
        }

        if (itemToAdd.getQuantityAvailable() <= 0) {
            System.out.println("This item is currently out of stock.");
            return;
        }

        if (itemToAdd.getUserID() == userId) {
            System.out.println("You cannot buy your own product.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int quantity;
        try {
            quantity = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a numeric value.");
            return;
        }
        userCart.addItemToCart(itemToAdd, quantity);
    }



    public static void editCart(Scanner input, ArrayList<Users> users, int userId) {
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

        Cart cart = currentUser.getCart();
        cart.displayCart();

        char choice;
        do {
            System.out.println("\n1. Remove all items from Seller");
            System.out.println("2. Remove Specific Item");
            System.out.println("3. Edit Quantity");
            System.out.println("4. Finish Edit Cart");
            System.out.print("Enter choice: ");
            choice = input.nextLine().charAt(0);

            switch (choice) {
                case '1':
                    removeItemsFromSeller(input, cart, users);
                    break;
                case '2':
                    removeSpecificItem(input, cart, users);
                    break;
                case '3':
                    editItemQuantity(input, cart, users);
                    break;
                case '4':
                    System.out.println("Finished editing cart.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            cart.displayCart();

        } while (choice != '4');
    }

    private static void removeItemsFromSeller(Scanner input, Cart cart, ArrayList<Users> users) {
        System.out.print("Enter Seller ID to remove all items from this seller: ");
        int sellerId = Integer.parseInt(input.nextLine());

        List<Cart.CartItem> removedCartItems = new ArrayList<>();

        Iterator<Cart.CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            Cart.CartItem cartItem = iterator.next();
            Items item = cartItem.getItem();
            if (item.getUserID() == sellerId) {
                removedCartItems.add(cartItem);
                iterator.remove();
            }
        }

        if (removedCartItems.isEmpty()) {
            System.out.println("No items found from seller ID " + sellerId + " in your cart.");
        } else {
            System.out.println("All items from seller ID " + sellerId + " removed from your cart.");
            cart.saveCartToFile();
        }
    }



    private static void removeSpecificItem(Scanner input, Cart cart, ArrayList<Users> users) {
        System.out.print("Enter Product ID to remove from cart: ");
        int productId = Integer.parseInt(input.nextLine());

        Iterator<Cart.CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            Cart.CartItem cartItem = iterator.next();
            Items item = cartItem.getItem();
            if (item.getProductID() == productId) {
                iterator.remove();

                System.out.println("Item with Product ID " + productId + " removed from your cart.");

                cart.saveCartToFile();

                return;
            }
        }

        System.out.println("Item with Product ID " + productId + " not found in your cart.");
    }


    private static void editItemQuantity(Scanner input, Cart cart, ArrayList<Users> users) {
        System.out.print("Enter Product ID to edit quantity: ");
        int productId;
        try {
            productId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Product ID. Please enter a numeric value.");
            return;
        }

        System.out.print("Enter new quantity: ");
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a numeric value.");
            return;
        }

        for (Cart.CartItem cartItem : cart.getCartItems()) {
            Items item = cartItem.getItem();
            if (item != null && item.getProductID() == productId) {
                int currentQuantity = cartItem.getQuantity();

                if (newQuantity > item.getQuantityAvailable()) {
                    System.out.println("Insufficient stock to adjust the quantity. Available stock is only " + item.getQuantityAvailable());
                    return;
                }
                cartItem.setQuantity(newQuantity);
                System.out.println("Quantity for Product ID " + productId + " updated to " + newQuantity + ".");
                cart.saveCartToFile();
                return;
            }
        }
        System.out.println("Product ID " + productId + " not found in your cart.");
    }

    public static void checkOutMenu(Scanner input, ArrayList<Users> users, int userId, ArrayList<Items> itemsList) {
        Users currentUser = Users.findUserByID(users, userId);
        if (currentUser == null) {
            System.out.println("User not found.");
            return;
        }

        Cart cart = currentUser.getCart();

        System.out.println("Please enter the date for the transaction (yyyy-mm-dd):");
        String dateString = input.nextLine();
        Date parsedDate = parseDate(dateString);

        char choice;
        do {
            System.out.println("\n1. Checkout All");
            System.out.println("2. Checkout by a Specific Seller");
            System.out.println("3. Checkout Specific Item");
            System.out.println("4. Exit Check Out");
            System.out.print("Enter choice: ");
            choice = input.nextLine().charAt(0);

            switch (choice) {
                case '1':
                    checkoutAll(currentUser, parsedDate);
                    break;
                case '2':
                    checkoutBySeller(input, currentUser.getCart(), parsedDate, currentUser, itemsList);
                    break;
                case '3':
                    checkoutSpecificItem(input, cart, parsedDate, currentUser, itemsList);
                    break;
                case '4':
                    Users.saveUsers(users);
                    Items.saveItems(itemsList);
                    System.out.println("Exiting Checkout...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != '4');
    }

    public static void checkoutAll(Users currentUser, Date transactionDate) {
        if (currentUser == null) {
            System.out.println("User not found.");
            return;
        }

        double totalAmount = 0.0;
        boolean allItemsAvailable = true;
        List<Cart.CartItem> cartItemsProcessed = new ArrayList<>();

        for (Cart.CartItem cartItem : currentUser.getCart().getCartItems()) {
            Items item = Items.getItemByID(cartItem.getItem().getProductID());
            if (item != null && item.getQuantityAvailable() >= cartItem.getQuantity()) {
                totalAmount += item.getUnitPrice() * cartItem.getQuantity();
                item.setQuantityAvailable(item.getQuantityAvailable() - cartItem.getQuantity());
                cartItemsProcessed.add(cartItem);
            } else {
                allItemsAvailable = false;
                System.out.println("Not enough stock for item ID: " + cartItem.getItem().getProductID());
            }
        }

        if (allItemsAvailable) {
            System.out.println("Transaction completed and saved. Total amount: " + totalAmount);
            currentUser.getCart().clearCartItems();
            Transactions transaction = new Transactions(currentUser.getUserID(), currentUser.getUserID(), transactionDate, cartItemsProcessed, totalAmount);
            transaction.saveTransactionToFile("transactions.txt");
        } else {
            System.out.println("Transaction not completed due to insufficient stock.");
        }
    }

    private static void checkoutBySeller(Scanner input, Cart cart, Date transactionDate, Users currentUser, ArrayList<Items> itemsList) {
        System.out.print("Enter Seller ID to checkout their items: ");
        int sellerId = Integer.parseInt(input.nextLine());

        List<Transactions> transactionsToSave = new ArrayList<>();
        boolean allItemsAvailable = true;

        Iterator<Cart.CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            Cart.CartItem cartItem = iterator.next();
            Items item = Items.getItemByID(cartItem.getItem().getProductID());

            if (item != null && item.getUserID() == sellerId) {
                if (item.getQuantityAvailable() >= cartItem.getQuantity()) {
                    transactionsToSave.add(new Transactions(currentUser.getUserID(), sellerId, transactionDate, Arrays.asList(cartItem), item.getUnitPrice() * cartItem.getQuantity()));
                    item.setQuantityAvailable(item.getQuantityAvailable() - cartItem.getQuantity());
                    iterator.remove();
                } else {
                    allItemsAvailable = false;
                    System.out.println("Not enough stock for item ID: " + item.getProductID() + " from seller: " + sellerId);
                }
            }
        }

        if (allItemsAvailable) {
            for (Transactions transaction : transactionsToSave) {
                transaction.saveTransactionToFile("transactions.txt");
            }
            System.out.println("All transactions completed successfully.");
        } else {
            System.out.println("Some transactions could not be completed due to stock issues.");
        }

        cart.saveCartToFile();
    }


    private static void checkoutSpecificItem(Scanner input, Cart cart, Date transactionDate, Users currentUser, ArrayList<Items> itemsList) {
        System.out.print("Enter Product ID to checkout: ");
        int productId = Integer.parseInt(input.nextLine());

        cart.loadCartFromFile();
        boolean itemAvailable = false;

        Iterator<Cart.CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            Cart.CartItem cartItem = iterator.next();
            Items item = Items.getItemByID(cartItem.getItem().getProductID());

            if (item != null && item.getProductID() == productId) {
                if (item.getQuantityAvailable() >= cartItem.getQuantity()) {
                    Transactions transaction = new Transactions(currentUser.getUserID(), item.getUserID(), transactionDate, Arrays.asList(cartItem), item.getUnitPrice() * cartItem.getQuantity());
                    transaction.saveTransactionToFile("transactions.txt");
                    item.setQuantityAvailable(item.getQuantityAvailable() - cartItem.getQuantity());
                    iterator.remove();
                    itemAvailable = true;
                    break;
                }
            }
        }

        if (itemAvailable) {
            System.out.println("Transaction for Product ID " + productId + " completed and saved.");
        } else {
            System.out.println("Transaction not completed due to insufficient stock or item not found.");
        }

        cart.saveCartToFile();
    }

    private static void printReceipt(List<Cart.CartItem> cartItems) {
        for (Cart.CartItem item : cartItems) {
            System.out.println("Product ID: " + item.getItem().getProductID() + ", Quantity: " + item.getQuantity() + ", Price: " + (item.getItem().getUnitPrice() * item.getQuantity()));
        }
    }

    private static Date parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void showAllSellers(ArrayList<Users> users, ArrayList<Items> itemsList) {
        System.out.format("%-7s | %-8s | %-20s | %-20s | %-13s | %-21s%n",
                "User ID", "Password", "Name", "Address", "Phone number", "Number of Items for Sale");
        for (Users user : users) {
            if (user.getItemsList() != null && !user.getItemsList().isEmpty()) {
                System.out.format("%-7d | %-8s | %-20s | %-20s | %-13s | %-21d%n",
                        user.getUserID(),
                        user.getPassword(),
                        user.getName(),
                        user.getAddress(),
                        user.getContactNum(),
                        user.getItemsList().size());
            }
        }
    }
    public static void showTotalSalesInGivenDuration() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.print("Enter the start date (yyyy-MM-dd): ");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(scanner.nextLine());
            System.out.print("Enter the end date (yyyy-MM-dd): ");
            endDate = dateFormat.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        if (startDate != null && endDate != null && !startDate.after(endDate)) {
            double totalAmount = 0;

            try (Scanner fileScanner = new Scanner(new File("transactions.txt"))) {
                System.out.format("%-12s | %-12s%n", "Date", "Amount");
                while (fileScanner.hasNextLine()) {
                    String transactionLine = fileScanner.nextLine();
                    String[] parts = transactionLine.split(";");
                    Date transactionDate = dateFormat.parse(parts[1]);

                    if (!transactionDate.before(startDate) && !transactionDate.after(endDate)) {
                        double amount = Double.parseDouble(parts[2]);
                        totalAmount += amount;
                        System.out.format("%-12s | %-12.2f%n", parts[1], amount);
                    }
                }
                System.out.println("Total Amount: " + totalAmount);
            } catch (FileNotFoundException e) {
                System.out.println("Transactions file not found.");
            } catch (ParseException e) {
                System.out.println("Error parsing the date. Please check the format in transactions.txt.");
            }
        } else {
            if (startDate.after(endDate)) {
                System.out.println("Start date cannot be after end date.");
            } else {
                System.out.println("Invalid dates. Please check your inputs.");
            }
        }
    }
    private static void showSellersSales(ArrayList<Users> users) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.print("Enter the start date (yyyy-MM-dd):");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(scanner.nextLine());
            System.out.print("Enter the end date (yyyy-MM-dd):");
            endDate = dateFormat.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        if (startDate != null && endDate != null && !startDate.after(endDate)) {
            HashMap<Integer, Double> salesTotals = new HashMap<>();
            double grandTotalSales = 0;

            try (Scanner fileScanner = new Scanner(new File("transactions.txt"))) {
                while (fileScanner.hasNextLine()) {
                    String transactionLine = fileScanner.nextLine();
                    String[] parts = transactionLine.split(";");
                    Date transactionDate = dateFormat.parse(parts[1]);

                    if (transactionDate != null && !transactionDate.before(startDate) && !transactionDate.after(endDate)) {
                        int lineUserId = Integer.parseInt(parts[3]);
                        double amount = Double.parseDouble(parts[2]);
                        salesTotals.merge(lineUserId, amount, Double::sum);
                        grandTotalSales += amount;
                    }
                }

                System.out.format("| %-10s | %-20s | %-10s |%n", "Seller ID", "Seller Name", "Total Sales");
                for (Users u : users) {
                    double totalSales = salesTotals.getOrDefault(u.getUserID(), 0.0);
                    System.out.format("| %-10d | %-20s | %-10.2f |%n", u.getUserID(), u.getName(), totalSales);
                }
                System.out.println("Total Sales for All Sellers: " + grandTotalSales);

            } catch (FileNotFoundException | ParseException e) {
                System.out.println("Error reading or parsing transactions file: " + e.getMessage());
            }
        } else {
            if (startDate != null && endDate != null) {
                System.out.println("Start date cannot be after end date.");
            } else {
                System.out.println("Invalid dates. Please check your inputs.");
            }
        }
    }

    private static void showShopaholics(ArrayList<Users> users) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the start date (yyyy-MM-dd):");
        Date startDate = parseDate(scanner.nextLine());

        System.out.print("Enter the end date (yyyy-MM-dd):");
        Date endDate = parseDate(scanner.nextLine());

        if (startDate != null && endDate != null) {
            try (Scanner fileScanner = new Scanner(new File("transactions.txt"))) {
                while (fileScanner.hasNextLine()) {
                    String transactionLine = fileScanner.nextLine();
                    String[] parts = transactionLine.split(";");
                    Date transactionDate = parseDate(parts[1]);

                    if (transactionDate != null && !transactionDate.before(startDate) && !transactionDate.after(endDate)) {
                        int lineBuyerId = Integer.parseInt(parts[0]);
                        double amount = Double.parseDouble(parts[2]);

                        for (Users u : users) {
                            if (u.getUserID() == lineBuyerId) {
                                u.addAmountBought(amount);
                                break;
                            }
                        }
                    }
                }
                System.out.format("| %-10s | %-20s | %-10s |%n", "User ID", "User Name", "Sales Count");

                for (Users u : users) {
                    System.out.format("| %-10d | %-20s | %-10d  |%n", u.getUserID(), u.getName(), u.getAmountBought());
                }

            } catch (FileNotFoundException e) {
                System.out.println("Transactions file not found.");
            }
        } else {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}

