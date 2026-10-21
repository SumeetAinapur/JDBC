package model;

// A Member object stores the details of one library member.
public class Member {
	private int memberId;
	private String memberName;
	private String phone;

	public Member(int memberId, String memberName, String phone) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.phone = phone;
	}

	public String getMemberName() {
		return memberName;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public String toString() {
		return "Member ID: " + memberId + " | Name: " + memberName + " | Phone: " + phone;
	}
}
