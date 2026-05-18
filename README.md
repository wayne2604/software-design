<div align="center">
  <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=32&pause=1000&color=F7F7F7&center=true&vCenter=true&width=900&lines=🛒+Console+Marketplace+Management+System" alt="Title" />
</div>

A robust console-based e-commerce marketplace application built in Java. It allows users to register, buy and sell products, manage custom persistent shopping carts, and features a secure admin dashboard to monitor sales metrics and top consumers.

---

### 📦 Stack
- Java SE (JDK 8+)
- Plain Text Database File Persistence
- OOP Architecture (Object-Oriented Design)
- Console User Interface

---

### ✨ Quick start
```bash
# Clone the repository
git clone https://github.com/manubag/software-design-midterm.git

# Navigate to the directory
cd software-design-midterm

# Compile all Java files into an output folder
javac -d out src/*.java

# Run the compiled application
java -cp out Main
```
Ensure you run the application from the project root directory so that local text database files (`users.txt`, `Items.txt`, and `transactions.txt`) are created and read correctly.

---

### ⚙️ Features
- **Dual Marketplace Roles** — Seamless switching between specialized "Buy Menu" and "Sell Menu" roles for active users.
- **Inventory Management** — Sellers can add new items, replenish stock levels, adjust unit prices, edit product details (name, category, description), and list low-stock items.
- **Persistent Shopping Carts** — Dedicated user carts saved under `cart_<userID>.txt`, dynamically loading and saving items across active user sessions.
- **Targeted Checkouts** — Multi-option checkout support enabling users to purchase their entire cart, items from a specific seller, or individual selected products.
- **Secure Admin Panel** — Password-protected administrator console (`H3LLo?`) to view users/sellers, check period sales, seller performance, and top shoppers ("Shopaholics").
- **Transactional Ledger** — Auto-saving financial statements stored persistently within local flat-file storage files.

---

### 🛠️ How it works
The system follows a clean object-oriented architecture designed in pure Java:
- **Modular Structure**: Key domain entities (`Users`, `Items`, `Cart`, `Transactions`) defined as individual classes linked by logical associations and inheritance.
- **Custom Serialization**: Custom parsing mechanisms designed to serialize and deserialize object properties into plain-text databases using delimiters (`|` and `;`).
- **File System Database**: Employs pure Java I/O (`Scanner`, `FileWriter`, `PrintWriter`) to orchestrate a fast, lightweight, and fully persistent offline database system.

---

### 📁 Project structure
```text
/
├── src/
│   ├── Cart.java           # Shopping cart entity, item aggregation, and cart file I/O
│   ├── Items.java          # Product details, seller product listings, and stock management
│   ├── Main.java           # Central application menu orchestration and user control flow
│   ├── Transactions.java   # Financial ledger, transactional records, and sales reports
│   └── Users.java          # User profile entity, auth procedures, and serialization logic
├── Items.txt               # Flat-file database containing all listed products
├── transactions.txt        # Local database storing all transactional checkout records
├── users.txt               # Local database storing registered user profiles
└── README.md               # Project documentation
```

---

### 👤 Authors
- **James** — [github.com/tengkyuuu](https://github.com/tengkyuuu)
- **Rhett** — [github.com/wayne2604](https://github.com/wayne2604)
