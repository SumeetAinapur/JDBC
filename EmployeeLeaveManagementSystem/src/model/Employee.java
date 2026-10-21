package model;

// Stores the details of one employee
public class Employee {
	private int empId;
	private String name;
	private String department;
	private int leaveBalance;

	public Employee(int empId, String name, String department, int leaveBalance) {
		this.empId = empId;
		this.name = name;
		this.department = department;
		this.leaveBalance = leaveBalance;
	}

	public static void printHeader() {
		System.out.printf("%-6s %-20s %-12s %14s%n", "ID", "Name", "Department", "Leave Balance");
		System.out.println("---------------------------------------------------");
	}

	public void printRow() {
		System.out.printf("%-6d %-20s %-12s %14d%n", empId, name, department, leaveBalance);
	}
}
