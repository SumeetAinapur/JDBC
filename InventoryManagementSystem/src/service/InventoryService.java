package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Bill;
import model.Category;
import model.Product;
import util.DBConnection;

// All the shop operations are written here
public class InventoryService {
	private static final int LOW_STOCK_LIMIT = 5; // below this we call it low stock

	// Add a new product
	public void addProduct(String name, Category category, double price, int quantity) throws SQLException {
		String sql = "INSERT INTO products (name, category, price, quantity) VALUES (?, ?, ?, ?)";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, category.name());
		ps.setDouble(3, price);
		ps.setInt(4, quantity);
		ps.executeUpdate();
		con.close();
	}

	// Get all products
	public List<Product> getAllProducts() throws SQLException {
		String sql = "SELECT * FROM products";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<Product> list = makeList(rs);
		con.close();
		return list;
	}

	// Search products by name
	public List<Product> searchProducts(String keyword) throws SQLException {
		String sql = "SELECT * FROM products WHERE name LIKE ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, "%" + keyword + "%");
		ResultSet rs = ps.executeQuery();
		List<Product> list = makeList(rs);
		con.close();
		return list;
	}

	// Change the price and quantity of a product (used for restocking too)
	public boolean updateProduct(int id, double price, int quantity) throws SQLException {
		String sql = "UPDATE products SET price = ?, quantity = ? WHERE product_id = ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setDouble(1, price);
		ps.setInt(2, quantity);
		ps.setInt(3, id);
		int rows = ps.executeUpdate();
		con.close();
		return rows > 0;
	}

	// Delete a product
	public boolean deleteProduct(int id) throws SQLException {
		String sql = "DELETE FROM products WHERE product_id = ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, id);
		int rows = ps.executeUpdate();
		con.close();
		return rows > 0;
	}

	// Sell a product. Returns a Bill, or null if the sale is not possible.
	public Bill sellProduct(int id, int quantity) throws SQLException {
		Connection con = DBConnection.getConnection();

		// Step 1: find the product
		PreparedStatement ps = con.prepareStatement("SELECT * FROM products WHERE product_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			System.out.println("Product not found.");
			con.close();
			return null;
		}

		// Step 2: check that we have enough stock
		String name = rs.getString("name");
		double price = rs.getDouble("price");
		int stock = rs.getInt("quantity");
		if (quantity > stock) {
			System.out.println("Not enough stock. Only " + stock + " available.");
			con.close();
			return null;
		}

		// Step 3: reduce the stock
		ps = con.prepareStatement("UPDATE products SET quantity = quantity - ? WHERE product_id = ?");
		ps.setInt(1, quantity);
		ps.setInt(2, id);
		ps.executeUpdate();
		con.close();

		// Step 4: make the bill
		return new Bill(name, quantity, price);
	}

	// Products with quantity below the limit
	public List<Product> getLowStockProducts() throws SQLException {
		String sql = "SELECT * FROM products WHERE quantity < ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, LOW_STOCK_LIMIT);
		ResultSet rs = ps.executeQuery();
		List<Product> list = makeList(rs);
		con.close();
		return list;
	}

	// Total value of all stock = sum of (price x quantity)
	public double getTotalStockValue() throws SQLException {
		String sql = "SELECT SUM(price * quantity) FROM products";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		double total = 0;
		if (rs.next()) {
			total = rs.getDouble(1);
		}
		con.close();
		return total;
	}

	// Turns the rows of a result into a list of Product objects
	private List<Product> makeList(ResultSet rs) throws SQLException {
		List<Product> list = new ArrayList<>();
		while (rs.next()) {
			Category category = Category.valueOf(rs.getString("category"));
			list.add(new Product(rs.getInt("product_id"), rs.getString("name"), category, rs.getDouble("price"),
					rs.getInt("quantity")));
		}
		return list;
	}
}
