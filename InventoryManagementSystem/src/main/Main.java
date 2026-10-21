package main;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import model.Bill;
import model.Category;
import model.Product;
import service.InventoryService;

// Shows the menu and calls InventoryService
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		InventoryService service = new InventoryService();

		while (true) {
			System.out.println("\n===== ELECTRONICS SHOP INVENTORY =====");
			System.out.println("1. Add Product");
			System.out.println("2. Display All Products");
			System.out.println("3. Search Product");
			System.out.println("4. Update Price / Quantity");
			System.out.println("5. Delete Product");
			System.out.println("6. Sell Product (Generate Bill)");
			System.out.println("7. Low Stock Report");
			System.out.println("8. Total Stock Value");
			System.out.println("0. Exit");
			System.out.print("Enter choice: ");

			try {
				int choice = Integer.parseInt(sc.nextLine().trim());

				switch (choice) {
				case 1:
					System.out.print("Product name: ");
					String name = sc.nextLine().trim();
					Category category = chooseCategory(sc);
					System.out.print("Price: ");
					double price = Double.parseDouble(sc.nextLine().trim());
					System.out.print("Quantity: ");
					int quantity = Integer.parseInt(sc.nextLine().trim());

					if (name.isEmpty() || category == null || price <= 0 || quantity < 0) {
						System.out.println("Invalid details. Product not added.");
					} else {
						service.addProduct(name, category, price, quantity);
						System.out.println("Product added successfully.");
					}
					break;

				case 2:
					showProducts(service.getAllProducts());
					break;

				case 3:
					System.out.print("Enter product name: ");
					showProducts(service.searchProducts(sc.nextLine().trim()));
					break;

				case 4:
					System.out.print("Product ID: ");
					int updateId = Integer.parseInt(sc.nextLine().trim());
					System.out.print("New price: ");
					double newPrice = Double.parseDouble(sc.nextLine().trim());
					System.out.print("New quantity: ");
					int newQuantity = Integer.parseInt(sc.nextLine().trim());

					if (newPrice <= 0 || newQuantity < 0) {
						System.out.println("Invalid price or quantity.");
					} else if (service.updateProduct(updateId, newPrice, newQuantity)) {
						System.out.println("Product updated.");
					} else {
						System.out.println("Product not found.");
					}
					break;

				case 5:
					System.out.print("Product ID to delete: ");
					int deleteId = Integer.parseInt(sc.nextLine().trim());
					if (service.deleteProduct(deleteId)) {
						System.out.println("Product deleted.");
					} else {
						System.out.println("Product not found.");
					}
					break;

				case 6:
					System.out.print("Product ID: ");
					int sellId = Integer.parseInt(sc.nextLine().trim());
					System.out.print("Quantity to sell: ");
					int sellQty = Integer.parseInt(sc.nextLine().trim());

					if (sellQty <= 0) {
						System.out.println("Quantity must be more than 0.");
					} else {
						Bill bill = service.sellProduct(sellId, sellQty);
						if (bill != null) {
							bill.printBill();
						}
					}
					break;

				case 7:
					System.out.println("\n--- LOW STOCK PRODUCTS ---");
					showProducts(service.getLowStockProducts());
					break;

				case 8:
					System.out.printf("Total stock value: Rs. %.2f%n", service.getTotalStockValue());
					break;

				case 0:
					System.out.println("Application closed.");
					sc.close();
					return;

				default:
					System.out.println("Invalid choice.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			} catch (SQLException e) {
				System.out.println("Database error: " + e.getMessage());
			}
		}
	}

	// Shows the list of categories and lets the user pick one
	private static Category chooseCategory(Scanner sc) {
		Category[] all = Category.values();
		for (int i = 0; i < all.length; i++) {
			System.out.println((i + 1) + ". " + all[i]);
		}
		System.out.print("Choose category number: ");
		int number = Integer.parseInt(sc.nextLine().trim());
		if (number < 1 || number > all.length) {
			return null;
		}
		return all[number - 1];
	}

	// Prints a list of products as a table
	private static void showProducts(List<Product> products) {
		if (products.isEmpty()) {
			System.out.println("No products found.");
			return;
		}
		Product.printHeader();
		for (Product p : products) {
			p.printRow();
		}
	}
}
