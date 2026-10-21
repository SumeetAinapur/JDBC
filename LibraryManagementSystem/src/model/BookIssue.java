package model;

import java.time.LocalDate;

public class BookIssue {
	private int issueId;
	private int bookId;
	private int memberId;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private LocalDate returnDate;

	public BookIssue() {
	}

	public BookIssue(int issueId, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate,
			LocalDate returnDate) {
		this.issueId = issueId;
		this.bookId = bookId;
		this.memberId = memberId;
		this.issueDate = issueDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
	}

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public String toString() {
		return "Issue ID: " + issueId + " | Book ID: " + bookId + " | Member ID: " + memberId + " | Issued: "
				+ issueDate + " | Due: " + dueDate + " | Returned: "
				+ (returnDate == null ? "Not returned" : returnDate);
	}
}
