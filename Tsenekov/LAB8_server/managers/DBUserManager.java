package managers;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import utility.Console;
import general.Dragon;
import utility.User;
import java.sql.SQLException;
import java.util.LinkedList;
import managers.PropertyManager;

public class DBUserManager {
	private final BDManager bdManager;
	public DBUserManager(BDManager bdManager) {
		this.bdManager = bdManager;
	}

	public LinkedList<User> select() {
		LinkedList<User> llu = new LinkedList<User>();
		try {
			ResultSet rs = bdManager.getStatement().executeQuery("select * from Tuser;");
			while (rs.next()) {
				llu.add(new User(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("role")));
			}
			rs.close();
			return llu;
		} catch (SQLException e) { System.out.println(e.toString()); return llu; }
	}

	public LinkedList<String> selectFunctionality(String role) {
		LinkedList<String> llf = new LinkedList<String>();
		try {
			var stmt = bdManager.getPreparedStatement("SELECT functionality AS func FROM Tuserfunc WHERE role = ?;");
			stmt.setString(1, role);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				llf.add(rs.getString("func"));
			}
			rs.close();
			return llf;
		} catch (SQLException e) { System.out.println(e.toString()); return llf; }
	}

	public boolean insert(User user) {
		try ( PreparedStatement stmt = bdManager.getPreparedStatementRGK("INSERT INTO Tuser(login, password, role) VALUES (?, ?, ?)"); ) {
			stmt.setString(1, user.getLogin()); stmt.setString(2, user.getPassword()); stmt.setString(3, user.getRole());
			if (stmt.executeUpdate() == 0) { throw new SQLException("Creating user failed, no rows affected."); }
			try (ResultSet gk = stmt.getGeneratedKeys()) {
				if (gk.next()) {
					user.setID(gk.getLong(1)); return true;
				} else { throw new SQLException("Creating user failed, no ID obtained."); }
			}
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}

	public boolean update(User user) {
		try {
			PreparedStatement stmt = bdManager.getPreparedStatement("UPDATE Tuser SET login = ?, password = ?, role = ? WHERE id = ?");
			stmt.setString(1, user.getLogin()); stmt.setString(2, user.getPassword()); stmt.setString(3, user.getRole()); stmt.setLong(4, user.getID());
			if (stmt.executeUpdate() == 0) { throw new SQLException("Update user failed, no rows affected."); }
			return true;
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}

	public boolean insertFunctionality(String role, String[] functionality) {
		try {
			var pstmt = bdManager.getPreparedStatement("INSERT INTO Tuserfunc VALUES (?, ?) ON CONFLICT DO NOTHING"); var i = 0;
			for(String func: functionality) { pstmt.setString(1, role); pstmt.setString(2, func); pstmt.executeUpdate(); }
			return true;
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}

	public boolean removeFunctionality(String role, String[] functionality) {
		try {
			var pstmt = bdManager.getPreparedStatement("DELETE FROM Tuserfunc WHERE role = ? AND functionality = ?"); var i = 0;
			for(String func: functionality) { pstmt.setString(1, role); pstmt.setString(2, func); pstmt.executeUpdate(); }
			return true;
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}
}