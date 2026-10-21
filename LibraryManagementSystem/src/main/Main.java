package main;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.BookDAO;
import dao.IssueDAO;
import dao.MemberDAO;
import model.Book;
import model.Member;

// Main class: shows the menu and calls the DAO classes.
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		BookDAO bookDAO = new BookDAO();
		MemberDAO memberDAO = new MemberDAO();
		IssueDAO issueDAO = new IssueDAO();

		while (true) {
			System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
			System.out.println("1. Add Book");
			System.out.println("2. Display All Books");
			System.out.println("3. Search Book");
			System.out.println("4. Update Book");
			System.out.println("5. Delete Book");
			System.out.println("6. Register Member");
			System.out.println("7. Display Members");
			System.out.println("8. Issue Book");
			System.out.println("9. Return Book");
			System.out.println("10. Display Active Issues");
			System.out.println("0. Exit");
			System.out.print("Enter choice: ");

			try {
				int choice = Integer.parseInt(sc.nextLine().trim());

				switch (choice) {
				case 1:
					System.out.print("Book title: ");
					String title = sc.nextLine().trim();
					System.out.print("Author: ");
					String author = sc.nextLine().trim();
					System.out.print("Quantity: ");
					int quantity = Integer.parseInt(sc.nextLine().trim());
					if (title.isEmpty() || author.isEmpty() || quantity <= 0) {
						System.out.println("Enter a title, author and a quantity above 0.");
					} else {
						bookDAO.addBook(title, author, quantity);
						System.out.println("Book added successfully.");
					}
					break;

				case 2:
					List<Book> books = bookDAO.getAllBooks();
					if (books.isEmpty()) {
						System.out.println("No books found.");
					}
					for (Book book : books) {
						System.out.println(book);
					}
					break;

				case 3:
					System.out.print("Title keyword: ");
					List<Book> found = bookDAO.searchBooks(sc.nextLine().trim());
					if (found.isEmpty()) {
						System.out.println("No matching books found.");
					}
					for (Book book : found) {
						System.out.println(book);
					}
					break;

				case 4:
					System.out.print("Book ID: ");
					int updateId = Integer.parseInt(sc.nextLine().trim());
					System.out.print("New title: ");
					String newTitle = sc.nextLine().trim();
					System.out.print("New author: ");
					String newAuthor = sc.nextLine().trim();
					if (bookDAO.updateBook(updateId, newTitle, newAuthor)) {
						System.out.println("Book updated.");
					} else {
						System.out.println("Book ID not found.");
					}
					break;

				case 5:
					System.out.print("Book ID to delete: ");
					int deleteId = Integer.parseInt(sc.nextLine().trim());
					if (bookDAO.deleteBook(deleteId)) {
						System.out.println("Book deleted.");
					} else {
						System.out.println("Book ID not found.");
					}
					break;

				case 6:
					System.out.print("Member name: ");
					String name = sc.nextLine().trim();
					System.out.print("Phone: ");
					String phone = sc.nextLine().trim();
					if (name.isEmpty()) {
						System.out.println("Member name is required.");
					} else {
						memberDAO.addMember(name, phone);
						System.out.println("Member registered successfully.");
					}
					break;

				case 7:
					List<Member> members = memberDAO.getAllMembers();
					if (members.isEmpty()) {
						System.out.println("No members found.");
					}
					for (Member member : members) {
						System.out.println(member);
					}
					break;

				case 8:
					System.out.print("Book ID: ");
					int bookId = Integer.parseInt(sc.nextLine().trim());
					System.out.print("Member ID: ");
					int memberId = Integer.parseInt(sc.nextLine().trim());
					issueDAO.issueBook(bookId, memberId);
					break;

				case 9:
					System.out.print("Issue ID: ");
					int issueId = Integer.parseInt(sc.nextLine().trim());
					issueDAO.returnBook(issueId);
					break;

				case 10:
					issueDAO.showActiveIssues();
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
}
