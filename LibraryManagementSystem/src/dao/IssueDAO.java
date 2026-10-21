package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import util.DBConnection;

// This class handles issuing and returning books.
public class IssueDAO {
	private static final int LOAN_DAYS = 14; // a book can be kept for 14 days
	private static final double FINE_PER_DAY = 5.0; // Rs. 5 fine for every late day

	public void issueBook(int bookId, int memberId) throws SQLException {
		Connection con = DBConnection.getConnection();

		// Step 1: check that the book exists and a copy is available
		PreparedStatement ps = con.prepareStatement("SELECT available_quantity FROM books WHERE book_id = ?");
		ps.setInt(1, bookId);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			System.out.println("Book not found.");
			con.close();
			return;
		}
		if (rs.getInt("available_quantity") <= 0) {
			System.out.println("No copies are available.");
			con.close();
			return;
		}

		// Step 2: check that the member exists
		ps = con.prepareStatement("SELECT * FROM members WHERE member_id = ?");
		ps.setInt(1, memberId);
		rs = ps.executeQuery();
		if (!rs.next()) {
			System.out.println("Member not found.");
			con.close();
			return;
		}

		// Step 3: save the issue record
		LocalDate issueDate = LocalDate.now();
		LocalDate dueDate = issueDate.plusDays(LOAN_DAYS);
		ps = con.prepareStatement(
				"INSERT INTO book_issues (book_id, member_id, issue_date, due_date) VALUES (?, ?, ?, ?)");
		ps.setInt(1, bookId);
		ps.setInt(2, memberId);
		ps.setDate(3, java.sql.Date.valueOf(issueDate));
		ps.setDate(4, java.sql.Date.valueOf(dueDate));
		ps.executeUpdate();

		// Step 4: reduce available copies by 1
		ps = con.prepareStatement("UPDATE books SET available_quantity = available_quantity - 1 WHERE book_id = ?");
		ps.setInt(1, bookId);
		ps.executeUpdate();

		System.out.println("Book issued successfully. Due date: " + dueDate);
		con.close();
	}

	public void returnBook(int issueId) throws SQLException {
		Connection con = DBConnection.getConnection();

		// Step 1: find the issue record that is not returned yet
		PreparedStatement ps = con.prepareStatement(
				"SELECT book_id, due_date FROM book_issues WHERE issue_id = ? AND return_date IS NULL");
		ps.setInt(1, issueId);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			System.out.println("Issue not found or book already returned.");
			con.close();
			return;
		}
		int bookId = rs.getInt("book_id");
		LocalDate dueDate = rs.getDate("due_date").toLocalDate();

		// Step 2: calculate the fine
		LocalDate today = LocalDate.now();
		long lateDays = ChronoUnit.DAYS.between(dueDate, today);
		if (lateDays < 0) {
			lateDays = 0;
		}
		double fine = lateDays * FINE_PER_DAY;

		// Step 3: save the return date
		ps = con.prepareStatement("UPDATE book_issues SET return_date = ? WHERE issue_id = ?");
		ps.setDate(1, java.sql.Date.valueOf(today));
		ps.setInt(2, issueId);
		ps.executeUpdate();

		// Step 4: increase available copies by 1
		ps = con.prepareStatement("UPDATE books SET available_quantity = available_quantity + 1 WHERE book_id = ?");
		ps.setInt(1, bookId);
		ps.executeUpdate();

		System.out.println("Book returned. Late days: " + lateDays);
		System.out.println("Fine: Rs. " + fine);
		con.close();
	}

	// Show all books that are issued and not yet returned.
	public void showActiveIssues() throws SQLException {
		String sql = "SELECT bi.issue_id, b.title, m.member_name, bi.issue_date, bi.due_date " + "FROM book_issues bi "
				+ "JOIN books b ON bi.book_id = b.book_id " + "JOIN members m ON bi.member_id = m.member_id "
				+ "WHERE bi.return_date IS NULL";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		System.out.println("\n--- ACTIVE BOOK ISSUES ---");
		boolean found = false;
		while (rs.next()) {
			found = true;
			System.out.println("Issue ID: " + rs.getInt("issue_id") + " | Book: " + rs.getString("title")
					+ " | Member: " + rs.getString("member_name") + " | Issued: " + rs.getDate("issue_date")
					+ " | Due: " + rs.getDate("due_date"));
		}
		if (!found) {
			System.out.println("No active issues.");
		}
		con.close();
	}
}
