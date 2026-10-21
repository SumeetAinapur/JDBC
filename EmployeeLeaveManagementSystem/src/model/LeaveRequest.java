package model;

import java.time.LocalDate;

// Stores one leave request
public class LeaveRequest {
	private int leaveId;
	private int empId;
	private String empName;
	private LeaveType type;
	private LocalDate fromDate;
	private LocalDate toDate;
	private int days;
	private String reason;
	private String status; // PENDING, APPROVED or REJECTED

	public LeaveRequest(int leaveId, int empId, String empName, LeaveType type, LocalDate fromDate, LocalDate toDate,
			int days, String reason, String status) {
		this.leaveId = leaveId;
		this.empId = empId;
		this.empName = empName;
		this.type = type;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.days = days;
		this.reason = reason;
		this.status = status;
	}

	public static void printHeader() {
		System.out.printf("%-6s %-6s %-16s %-8s %-11s %-11s %-5s %-9s %s%n", "Leave", "Emp", "Name", "Type", "From",
				"To", "Days", "Status", "Reason");
		System.out
				.println("-------------------------------------------------------------------------------------------");
	}

	public void printRow() {
		System.out.printf("%-6d %-6d %-16s %-8s %-11s %-11s %-5d %-9s %s%n", leaveId, empId, empName, type, fromDate,
				toDate, days, status, reason);
	}
}
