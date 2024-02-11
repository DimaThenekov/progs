package managers;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import utility.Console;
import utility.User;
import general.Dragon;
import java.sql.SQLException;
import java.util.LinkedList;
import managers.PropertyManager;

public class DumpManager {
	private final BDManager bdManager;
	private final Console console;
	private final PropertyManager propertyManager;
	private final DBUserManager dbUserManager;
	private final DBDragonManager dbDragonManager;

	public DumpManager(BDManager bdManager, PropertyManager propertyManager, Console console) {
		this.console = console;
		//bdManager.setURL(propertyManager.getProperty("DB_URL", "jdbc:postgresql://127.0.0.1:5432/studs"));
		//bdManager.setUser(propertyManager.getProperty("DB_USER", "postgres"));
		//bdManager.setPassword(propertyManager.getProperty("DB_PASSWORD", "pass"));
		this.bdManager = bdManager;
		this.propertyManager = propertyManager;
		this.dbDragonManager = new DBDragonManager(bdManager);
		this.dbUserManager = new DBUserManager(bdManager);
	}

	public boolean initTables() {
		try {
			if (!bdManager.init()) { System.exit(1); }
			
			var stmt = bdManager.getStatement(); var i = 0;
			for(String script: propertyManager.getProperty("DB_CREATE_TABLE_SQL", "").split(";")) { stmt.executeUpdate(script+";"); }
			stmt.close();
			
			var pstmt = bdManager.getPreparedStatement(propertyManager.getProperty("DB_INSERT_COLORS", "")); i = 0; // fillColor
			for(String val: propertyManager.getProperty("DB_INSERT_COLORS_VALS", "").split(";")) { pstmt.setInt(1, ++i); pstmt.setString(2, val); pstmt.executeUpdate(); }
			
			pstmt = bdManager.getPreparedStatement(propertyManager.getProperty("DB_INSERT_DRAGON_TYPES", "")); i = 0; // fillDragonType
			for(String val: propertyManager.getProperty("DB_INSERT_DRAGON_TYPES_VALS", "").split(";")) { pstmt.setInt(1, ++i); pstmt.setString(2, val); pstmt.executeUpdate(); }
			
			
			pstmt = bdManager.getPreparedStatement(propertyManager.getProperty("DB_INSERT_DRAGON_CHARACTE", "")); i = 0; // fillDragonCharacter
			for(String val: propertyManager.getProperty("DB_INSERT_DRAGON_CHARACTE_VALS", "").split(";")) { pstmt.setInt(1, ++i); pstmt.setString(2, val); pstmt.executeUpdate(); }
			
			
			return true;
		} catch (SQLException e) { console.printError(e.toString()); return false; }
	}

	public LinkedList<DBDragonManager.DragonAndUserID> selectD() { return dbDragonManager.select(); }
	public boolean insertD(Dragon d, long userID) { return dbDragonManager.insert(d, userID); }
	public boolean updateD(Dragon d) { return dbDragonManager.update(d); }
	public boolean removeD(long id) { return dbDragonManager.remove(id); }

	public LinkedList<User> selectU() { return dbUserManager.select(); }
	public boolean insertU(User d) { return dbUserManager.insert(d); }
	public boolean updateU(User d) { return dbUserManager.update(d); }
	public LinkedList<String> selectF(String r) { return dbUserManager.selectFunctionality(r); }
	public boolean insertF(String r, String[] fs) { return dbUserManager.insertFunctionality(r, fs); }
	public boolean removeF(String r, String[] fs) { return dbUserManager.removeFunctionality(r, fs); }
}

