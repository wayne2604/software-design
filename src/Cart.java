import java.util.*;
import java.io.*;

public class Cart {
    private List<CartItem> cartItems;
    private String buyerID;

    public Cart(String buyerID) {
        this.buyerID = buyerID;
        this.cartItems = new ArrayList<>();
    }

    public void clearCartItems() {
        this.cartItems.clear();
        System.out.println("Cart has been cleared.");

        try (PrintWriter out = new PrintWriter(new FileWriter("cart_" + this.buyerID + ".txt"))) {
            System.out.println("Cart file has been emptied.");
        } catch (IOException e) {
            System.out.println("Failed to clear cart file: " + e.getMessage());
        }
    }

    public void addItemToCart(Items item, int quantity) {
        if (item == null || quantity <= 0) {
            System.out.println("Invalid item or quantity. Item not added.");
            return;
        }
        int existingQuantityInCart = getTotalQuantityForItem(item.getProductID());
        if (existingQuantityInCart + quantity > item.getQuantityAvailable()) {
            System.out.println("Insufficient quantity available. Item not added to cart.");
            return;
        }
        for (CartItem cartItem : this.cartItems) {
            if (cartItem.getItem().getProductID() == item.getProductID()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                System.out.println("Item quantity updated.");
                return;
            }
        }
        this.cartItems.add(new CartItem(item, quantity));
        System.out.println("New item added to cart.");
    }

    public boolean containsItem(int productId) {
        return cartItems.stream().anyMatch(item -> item.getItem().getProductID() == productId);
    }
    public int getTotalQuantityForItem(int productId) {
        return cartItems.stream()
                .filter(item -> item.getItem().getProductID() == productId)
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void saveCartToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter("cart_" + this.buyerID + ".txt"))) {
            for (CartItem cartItem : this.cartItems) {
                Items item = cartItem.getItem();
                if (item != null) {
                    out.printf("%s|%d|%d|%d\n", this.buyerID, item.getProductID(), item.getUserID(), cartItem.getQuantity());
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to save cart to file: " + e.getMessage());
        }
    }
    public void loadCartFromFile() {
        try (Scanner scanner = new Scanner(new File("cart_" + this.buyerID + ".txt"))) {
            this.cartItems.clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                int productID = Integer.parseInt(parts[1]);
                int sellerUserID = Integer.parseInt(parts[2]);
                int quantity = Integer.parseInt(parts[3]);

                Items item = Items.getItemByID(productID);
                if (item != null && item.getUserID() == sellerUserID) {
                    this.cartItems.add(new CartItem(item, quantity));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No cart file found. Starting with an empty cart.");
        }
    }
    public void displayCart() {
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Items in your cart:");
            System.out.println("Product ID\t|   Item Name\t|   Category\t|   Unit Price\t|   Quantity");
            for (CartItem cartItem : cartItems) {
                Items item = cartItem.getItem();
                if (item == null) {
                    System.out.println("A null item found in the cart and cannot be displayed.");
                    continue;
                }
                System.out.printf("%-12d|   %-15s|   %-12s|   %-12.2f|   %-8d%n",
                        item.getProductID(), item.getItemName(), item.getCategory(), item.getUnitPrice(), cartItem.getQuantity());
            }
        }
    }

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public class CartItem {
        private Items item;
        private int quantity;

        public CartItem(Items item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public Items getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
