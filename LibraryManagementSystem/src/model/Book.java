package model;

// A Book object stores the details of one book.
public class Book {
	private int bookId;
	private String title;
	private String author;
	private int quantity; // total copies
	private int availableQuantity; // copies on the shelf right now

	public Book(int bookId, String title, String author, int quantity, int availableQuantity) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.quantity = quantity;
		this.availableQuantity = availableQuantity;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "Book ID: " + bookId + " | " + title + " | " + author + " | Total: " + quantity + " | Available: "
				+ availableQuantity;
	}
}
