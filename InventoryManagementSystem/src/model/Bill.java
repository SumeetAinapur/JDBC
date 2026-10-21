package model;

import java.time.LocalDate;

// Stores one sale and prints the bill
public class Bill {
	private static final double GST_RATE = 0.18; // 18% GST

	private String productName;
	private int quantity;
	private double price;
	private double subtotal;
	private double gst;
	private double total;

	public Bill(String productName, int quantity, double price) {
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.subtotal = price * quantity;
		this.gst = subtotal * GST_RATE;
		this.total = subtotal + gst;
	}

	public void printBill() {
		System.out.println("\n========== BILL ==========");
		System.out.println("Date: " + LocalDate.now());
		System.out.printf("Product  : %s%n", productName);
		System.out.printf("Price    : Rs. %.2f%n", price);
		System.out.printf("Quantity : %d%n", quantity);
		System.out.println("--------------------------");
		System.out.printf("Subtotal : Rs. %.2f%n", subtotal);
		System.out.printf("GST 18%%  : Rs. %.2f%n", gst);
		System.out.printf("TOTAL    : Rs. %.2f%n", total);
		System.out.println("==========================");
	}
}
