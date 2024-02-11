package managers;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utility.Console;

public class BDManager {
	private final Console console;
	private final String DB_URL;
	private final String USER;
	private final String PASS;
	private Connection connection = null;

	public BDManager(String DB_URL, String USER, String PASS, Console console) {
		this.console = console;
		this.DB_URL = DB_URL;
		this.USER = USER;
		this.PASS = PASS;
	}

	public boolean init() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) { console.printError("PostgreSQL JDBC Driver is not found. Include it in your library path "); return false; }
		
		try {
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			
			getStatement().executeQuery("select pg_catalog.set_config('search_path', 's367601', false);");
			//"SET search_path TO S367601;"
		} catch (SQLException e) { console.printError("Connection Failed"); return false; }
		
		if (connection == null) { console.printError("Failed to make connection to database"); return false; }
		
		return true;
	}

	public Statement getStatement() throws SQLException { return connection.createStatement(); }
	public PreparedStatement  getPreparedStatement(String s) throws SQLException { return connection.prepareStatement(s); }
	public PreparedStatement  getPreparedStatementRGK(String s) throws SQLException { return connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS); }
}