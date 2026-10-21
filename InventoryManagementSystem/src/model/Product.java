package model;

// Stores the details of one product
public class Product {
	private int id;
	private String name;
	private Category category;
	private double price;
	private int quantity;

	public Product(int id, String name, Category category, double price, int quantity) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Category getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	// Prints the table heading
	public static void printHeader() {
		System.out.printf("%-5s %-25s %-13s %10s %6s%n", "ID", "Name", "Category", "Price", "Qty");
		System.out.println("-----------------------------------------------------------");
	}

	// Prints this product as one row of the table
	public void printRow() {
		System.out.printf("%-5d %-25s %-13s %10.2f %6d%n", id, name, category, price, quantity);
	}
}
