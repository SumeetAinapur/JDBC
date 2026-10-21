package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Member;
import util.DBConnection;

// This class talks to the "members" table.
public class MemberDAO {

	public void addMember(String name, String phone) throws SQLException {
		String sql = "INSERT INTO members (member_name, phone) VALUES (?, ?)";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, phone);
		ps.executeUpdate();
		con.close();
	}

	public List<Member> getAllMembers() throws SQLException {
		List<Member> members = new ArrayList<>();
		String sql = "SELECT * FROM members";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			members.add(new Member(rs.getInt("member_id"), rs.getString("member_name"), rs.getString("phone")));
		}
		con.close();
		return members;
	}
}
