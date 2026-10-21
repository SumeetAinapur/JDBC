package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.LeaveRequest;
import model.LeaveType;
import util.DBConnection;

// All the leave operations are written here
public class LeaveService {

	// Add a new employee with 12 leave days
	public void addEmployee(String name, String department) throws SQLException {
		String sql = "INSERT INTO employees (name, department, leave_balance) VALUES (?, ?, 12)";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, department);
		ps.executeUpdate();
		con.close();
	}

	public List<Employee> getAllEmployees() throws SQLException {
		List<Employee> list = new ArrayList<>();
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM employees");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(new Employee(rs.getInt("emp_id"), rs.getString("name"), rs.getString("department"),
					rs.getInt("leave_balance")));
		}
		con.close();
		return list;
	}

	// Apply for leave. The request is saved as PENDING.
	public void applyLeave(int empId, LeaveType type, LocalDate from, LocalDate to, String reason) throws SQLException {
		Connection con = DBConnection.getConnection();

		// Step 1: check that the employee exists and get the balance
		PreparedStatement ps = con.prepareStatement("SELECT leave_balance FROM employees WHERE emp_id = ?");
		ps.setInt(1, empId);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			System.out.println("Employee not found.");
			con.close();
			return;
		}
		int balance = rs.getInt("leave_balance");

		// Step 2: check the dates
		if (to.isBefore(from)) {
			System.out.println("To date cannot be before from date.");
			con.close();
			return;
		}
		if (from.isBefore(LocalDate.now())) {
			System.out.println("You cannot apply leave for a past date.");
			con.close();
			return;
		}

		// Step 3: count the days (both first and last day are counted)
		int days = (int) ChronoUnit.DAYS.between(from, to) + 1;

		// Step 4: check the leave balance
		if (days > balance) {
			System.out.println("Not enough leave balance. Available: " + balance + ", needed: " + days);
			con.close();
			return;
		}

		// Step 5: check that the dates do not clash with another leave
		ps = con.prepareStatement("SELECT * FROM leave_requests WHERE emp_id = ? AND status <> 'REJECTED' "
				+ "AND from_date <= ? AND to_date >= ?");
		ps.setInt(1, empId);
		ps.setDate(2, java.sql.Date.valueOf(to));
		ps.setDate(3, java.sql.Date.valueOf(from));
		rs = ps.executeQuery();
		if (rs.next()) {
			System.out.println("These dates clash with another leave request.");
			con.close();
			return;
		}

		// Step 6: save the request
		ps = con.prepareStatement("INSERT INTO leave_requests "
				+ "(emp_id, leave_type, from_date, to_date, days, reason, status, applied_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, 'PENDING', ?)");
		ps.setInt(1, empId);
		ps.setString(2, type.name());
		ps.setDate(3, java.sql.Date.valueOf(from));
		ps.setDate(4, java.sql.Date.valueOf(to));
		ps.setInt(5, days);
		ps.setString(6, reason);
		ps.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
		ps.executeUpdate();

		System.out.println("Leave applied for " + days + " day(s). Status: PENDING");
		con.close();
	}

	// Approve a pending request and reduce the employee's balance
	public void approveLeave(int leaveId) throws SQLException {
		Connection con = DBConnection.getConnection();

		// Step 1: find the pending request
		PreparedStatement ps = con
				.prepareStatement("SELECT emp_id, days FROM leave_requests WHERE leave_id = ? AND status = 'PENDING'");
		ps.setInt(1, leaveId);
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			System.out.println("Pending request not found.");
			con.close();
			return;
		}
		int empId = rs.getInt("emp_id");
		int days = rs.getInt("days");

		// Step 2: check the balance again
		ps = con.prepareStatement("SELECT leave_balance FROM employees WHERE emp_id = ?");
		ps.setInt(1, empId);
		rs = ps.executeQuery();
		rs.next();
		int balance = rs.getInt("leave_balance");
		if (days > balance) {
			System.out.println("Cannot approve. Employee has only " + balance + " day(s) left.");
			con.close();
			return;
		}

		// Step 3: mark the request as APPROVED
		ps = con.prepareStatement("UPDATE leave_requests SET status = 'APPROVED' WHERE leave_id = ?");
		ps.setInt(1, leaveId);
		ps.executeUpdate();

		// Step 4: reduce the balance
		ps = con.prepareStatement("UPDATE employees SET leave_balance = leave_balance - ? WHERE emp_id = ?");
		ps.setInt(1, days);
		ps.setInt(2, empId);
		ps.executeUpdate();

		System.out.println("Leave approved. " + days + " day(s) deducted. Balance left: " + (balance - days));
		con.close();
	}

	// Reject a pending request (balance is not changed)
	public void rejectLeave(int leaveId) throws SQLException {
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(
				"UPDATE leave_requests SET status = 'REJECTED' WHERE leave_id = ? AND status = 'PENDING'");
		ps.setInt(1, leaveId);
		int rows = ps.executeUpdate();
		if (rows > 0) {
			System.out.println("Leave rejected.");
		} else {
			System.out.println("Pending request not found.");
		}
		con.close();
	}

	// All requests that are waiting for a decision
	public List<LeaveRequest> getPendingRequests() throws SQLException {
		String sql = "SELECT l.*, e.name FROM leave_requests l JOIN employees e ON l.emp_id = e.emp_id "
				+ "WHERE l.status = 'PENDING' ORDER BY l.from_date";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		List<LeaveRequest> list = makeList(ps.executeQuery());
		con.close();
		return list;
	}

	// All requests of one employee
	public List<LeaveRequest> getLeaveHistory(int empId) throws SQLException {
		String sql = "SELECT l.*, e.name FROM leave_requests l JOIN employees e ON l.emp_id = e.emp_id "
				+ "WHERE l.emp_id = ? ORDER BY l.from_date";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, empId);
		List<LeaveRequest> list = makeList(ps.executeQuery());
		con.close();
		return list;
	}

	// Leave balance of one employee. Returns -1 if the employee is not found.
	public int getLeaveBalance(int empId) throws SQLException {
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT leave_balance FROM employees WHERE emp_id = ?");
		ps.setInt(1, empId);
		ResultSet rs = ps.executeQuery();
		int balance = -1;
		if (rs.next()) {
			balance = rs.getInt("leave_balance");
		}
		con.close();
		return balance;
	}

	// Turns the rows of a result into a list of LeaveRequest objects
	private List<LeaveRequest> makeList(ResultSet rs) throws SQLException {
		List<LeaveRequest> list = new ArrayList<>();
		while (rs.next()) {
			list.add(new LeaveRequest(rs.getInt("leave_id"), rs.getInt("emp_id"), rs.getString("name"),
					LeaveType.valueOf(rs.getString("leave_type")), rs.getDate("from_date").toLocalDate(),
					rs.getDate("to_date").toLocalDate(), rs.getInt("days"), rs.getString("reason"),
					rs.getString("status")));
		}
		return list;
	}
}
