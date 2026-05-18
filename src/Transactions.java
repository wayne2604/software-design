import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Transactions extends Users {
    protected Date date;
    protected List<Cart.CartItem> cartItems;
    protected int buyerUserID;
    protected double totalAmount;

    public Transactions(int buyerUserID, int sellerUserID, Date date, List<Cart.CartItem> cartItems, double totalAmount) {
        super(sellerUserID, "", "", 0, "");
        this.buyerUserID = buyerUserID;
        this.date = date;
        this.cartItems = cartItems;
        this.totalAmount = totalAmount;
    }

    public void saveTransactionToFile(String filename) {
        try (FileWriter fw = new FileWriter(filename, true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            for (Cart.CartItem cartItem : cartItems) {
                Items item = cartItem.getItem();
                if (item != null) {
                    double itemTotal = item.getUnitPrice() * cartItem.getQuantity();
                    writer.write(buyerUserID + ";" + formattedDate + ";" + itemTotal + ";" + item.getUserID());
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save transaction to file: " + e.getMessage());
        }
    }

    protected Date getDate() {
        return date;
    }

    protected double getTotalAmount() {
        return totalAmount;
    }

    protected int getBuyerUserID() {
        return buyerUserID;
    }
}
