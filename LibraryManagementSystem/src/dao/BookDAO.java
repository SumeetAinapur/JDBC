package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import util.DBConnection;

// DAO = Data Access Object. This class talks to the "books" table.
public class BookDAO {

	// Add a new book. Available copies = total copies at the start.
	public void addBook(String title, String author, int quantity) throws SQLException {
		String sql = "INSERT INTO books (title, author, quantity, available_quantity) VALUES (?, ?, ?, ?)";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, title);
		ps.setString(2, author);
		ps.setInt(3, quantity);
		ps.setInt(4, quantity);
		ps.executeUpdate();
		con.close();
	}

	// Get all books from the table.
	public List<Book> getAllBooks() throws SQLException {
		List<Book> books = new ArrayList<>();
		String sql = "SELECT * FROM books";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			books.add(makeBook(rs));
		}
		con.close();
		return books;
	}

	// Search books whose title contains the keyword.
	public List<Book> searchBooks(String keyword) throws SQLException {
		List<Book> books = new ArrayList<>();
		String sql = "SELECT * FROM books WHERE title LIKE ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, "%" + keyword + "%");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			books.add(makeBook(rs));
		}
		con.close();
		return books;
	}

	// Change the title and author of a book. Returns true if the book was found.
	public boolean updateBook(int id, String title, String author) throws SQLException {
		String sql = "UPDATE books SET title = ?, author = ? WHERE book_id = ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, title);
		ps.setString(2, author);
		ps.setInt(3, id);
		int rows = ps.executeUpdate();
		con.close();
		return rows > 0;
	}

	// Delete a book. Returns true if the book was found.
	public boolean deleteBook(int id) throws SQLException {
		String sql = "DELETE FROM books WHERE book_id = ?";
		Connection con = DBConnection.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// This happens when the book was already issued to someone.
			throw new SQLException("Cannot delete this book because it has issue records.");
		} finally {
			con.close();
		}
	}

	// Turn one row of the result into a Book object.
	private Book makeBook(ResultSet rs) throws SQLException {
		return new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getInt("quantity"),
				rs.getInt("available_quantity"));
	}
}
