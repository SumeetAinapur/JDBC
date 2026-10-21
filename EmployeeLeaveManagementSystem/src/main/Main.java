package main;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import model.Employee;
import model.LeaveRequest;
import model.LeaveType;
import service.LeaveService;

// Shows the menu and calls LeaveService
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		LeaveService service = new LeaveService();

		while (true) {
			System.out.println("\n===== EMPLOYEE LEAVE MANAGEMENT =====");
			System.out.println("1. Add Employee");
			System.out.println("2. Display Employees");
			System.out.println("3. Apply Leave");
			System.out.println("4. View Pending Requests");
			System.out.println("5. Approve Leave");
			System.out.println("6. Reject Leave");
			System.out.println("7. Employee Leave History");
			System.out.println("8. Check Leave Balance");
			System.out.println("0. Exit");
			System.out.print("Enter choice: ");

			try {
				int choice = Integer.parseInt(sc.nextLine().trim());

				switch (choice) {
				case 1:
					System.out.print("Employee name: ");
					String name = sc.nextLine().trim();
					System.out.print("Department: ");
					String department = sc.nextLine().trim();
					if (name.isEmpty() || department.isEmpty()) {
						System.out.println("Name and department are required.");
					} else {
						service.addEmployee(name, department);
						System.out.println("Employee added with 12 leave days.");
					}
					break;

				case 2:
					List<Employee> employees = service.getAllEmployees();
					if (employees.isEmpty()) {
						System.out.println("No employees found.");
					} else {
						Employee.printHeader();
						for (Employee e : employees) {
							e.printRow();
						}
					}
					break;

				case 3:
					System.out.print("Employee ID: ");
					int empId = Integer.parseInt(sc.nextLine().trim());
					LeaveType type = chooseLeaveType(sc);
					System.out.print("From date (yyyy-mm-dd): ");
					LocalDate from = LocalDate.parse(sc.nextLine().trim());
					System.out.print("To date (yyyy-mm-dd): ");
					LocalDate to = LocalDate.parse(sc.nextLine().trim());
					System.out.print("Reason: ");
					String reason = sc.nextLine().trim();
					if (type == null) {
						System.out.println("Invalid leave type.");
					} else {
						service.applyLeave(empId, type, from, to, reason);
					}
					break;

				case 4:
					System.out.println("\n--- PENDING REQUESTS ---");
					showRequests(service.getPendingRequests());
					break;

				case 5:
					System.out.print("Leave ID to approve: ");
					service.approveLeave(Integer.parseInt(sc.nextLine().trim()));
					break;

				case 6:
					System.out.print("Leave ID to reject: ");
					service.rejectLeave(Integer.parseInt(sc.nextLine().trim()));
					break;

				case 7:
					System.out.print("Employee ID: ");
					showRequests(service.getLeaveHistory(Integer.parseInt(sc.nextLine().trim())));
					break;

				case 8:
					System.out.print("Employee ID: ");
					int balance = service.getLeaveBalance(Integer.parseInt(sc.nextLine().trim()));
					if (balance == -1) {
						System.out.println("Employee not found.");
					} else {
						System.out.println("Leave balance: " + balance + " day(s)");
					}
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
			} catch (DateTimeParseException e) {
				System.out.println("Please enter the date like 2026-11-25.");
			} catch (SQLException e) {
				System.out.println("Database error: " + e.getMessage());
			}
		}
	}

	// Shows the leave types and lets the user pick one
	private static LeaveType chooseLeaveType(Scanner sc) {
		LeaveType[] all = LeaveType.values();
		for (int i = 0; i < all.length; i++) {
			System.out.println((i + 1) + ". " + all[i]);
		}
		System.out.print("Choose leave type number: ");
		int number = Integer.parseInt(sc.nextLine().trim());
		if (number < 1 || number > all.length) {
			return null;
		}
		return all[number - 1];
	}

	// Prints a list of leave requests as a table
	private static void showRequests(List<LeaveRequest> requests) {
		if (requests.isEmpty()) {
			System.out.println("No leave requests found.");
			return;
		}
		LeaveRequest.printHeader();
		for (LeaveRequest r : requests) {
			r.printRow();
		}
	}
}
