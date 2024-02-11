package managers;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import utility.Console;
import general.*;
import java.sql.SQLException;
import java.util.LinkedList;
import managers.PropertyManager;
import java.time.LocalDateTime;

public class DBDragonManager {
	private final BDManager bdManager;
	
	final class DragonAndUserID {
		public Dragon d; public long u;
		public DragonAndUserID(Dragon d, long u) { this.d = d; this.u = u; }
	}

	public DBDragonManager(BDManager bdManager) {
		this.bdManager = bdManager;
	}

	public LinkedList<DragonAndUserID> select() {
		LinkedList<DragonAndUserID> lld = new LinkedList<DragonAndUserID>();
		try {
			ResultSet rs = bdManager.getStatement().executeQuery("select Tdragon.id AS id, Tdragon.name AS name, Tcoordinates.x AS coordinatesx, Tcoordinates.y AS coordinatesy, Tdragon.creationDate AS creationDate, Tdragon.age AS age, TC1.name AS color, Tdragontype.name AS type, Tdragoncharacter.name AS character, Tperson.name AS person_name, Tperson.birthday AS person_birthday, Tperson.weight AS person_weight, Tperson.passportID AS person_passportid, TC2.name AS person_eyecolor, Tdragon.id_user AS id_user from Tdragon "
			                                                   + "join Tcoordinates on Tcoordinates.id = Tdragon.id_coordinates "
			                                                   + "join Tcolor AS TC1 on TC1.id = Tdragon.id_color "
			                                                   + "join Tdragontype on Tdragontype.id = Tdragon.id_type "
			                                                   + "left join Tdragoncharacter on Tdragoncharacter.id = Tdragon.id_character "
			                                                   + "left join Tperson on Tperson.id = Tdragon.id_killer "
			                                                   + "left join Tcolor AS TC2 on TC2.id = Tperson.eyeColor;");
			
			while (rs.next()) {
				Color dcolor = null; try { dcolor = Color.valueOf(rs.getString("color")); } catch (NullPointerException | IllegalArgumentException  e) { System.out.println(e); }
				Color eyecolor = null; try { eyecolor = Color.valueOf(rs.getString("person_eyecolor")); } catch (NullPointerException | IllegalArgumentException  e) { }
				DragonType dtype = null; try { dtype = DragonType.valueOf(rs.getString("type")); } catch (NullPointerException | IllegalArgumentException  e) { }
				DragonCharacter dcharacter = null; try { dcharacter = DragonCharacter.valueOf(rs.getString("character")); } catch (NullPointerException | IllegalArgumentException  e) { }
				var coord = new Coordinates(rs.getInt("coordinatesx"), rs.getDouble("coordinatesy"));
				Person killer = null; if (rs.getString("person_name")!=null) killer = new Person(rs.getString("person_name"), rs.getObject("person_birthday", LocalDateTime.class), rs.getLong("person_weight"), rs.getString("person_passportid"), eyecolor);
				var d = new Dragon(rs.getLong("id"), rs.getString("name"), coord, rs.getObject("creationDate", LocalDateTime.class), rs.getInt("age"), dcolor, dtype, dcharacter, killer);
				if (d.validate()) lld.add(new DragonAndUserID(d, rs.getInt("id_user")));
			}
			rs.close();
			return lld;
		} catch (SQLException e) { System.out.println(e.toString()); return lld; }
	}

	private long insert(Coordinates coord) throws SQLException {
		try ( PreparedStatement stmt = bdManager.getPreparedStatementRGK("INSERT INTO Tcoordinates(x, y) VALUES (?, ?)"); ) {
			stmt.setInt(1, coord.getX()); stmt.setDouble(2, coord.getY());
			if (stmt.executeUpdate() == 0) { throw new SQLException("Creating coord failed, no rows affected."); }
			try (ResultSet gk = stmt.getGeneratedKeys()) {
				if (gk.next()) {
					return gk.getLong(1);
				} else { throw new SQLException("Creating coord failed, no ID obtained."); }
			}
		}
	}

	private long insert(Person p) throws SQLException {
		try ( PreparedStatement stmt = bdManager.getPreparedStatementRGK("INSERT INTO Tperson(name, birthday, weight, passportid, eyecolor) VALUES (?, ?, ?, ?, ?)"); ) {
			stmt.setString(1, p.getName()); stmt.setObject(2, p.getBirthday()); stmt.setLong(3, p.getWeight()); stmt.setString(4, p.getPassportID()); stmt.setInt(5, p.getEyeColor().ordinal()+1);
			if (stmt.executeUpdate() == 0) { throw new SQLException("Creating person failed, no rows affected."); }
			try (ResultSet gk = stmt.getGeneratedKeys()) {
				if (gk.next()) {
					return gk.getLong(1);
				} else { throw new SQLException("Creating person failed, no ID obtained."); }
			}
		}
	}

	public boolean insert(Dragon d, long userID) {
		try ( PreparedStatement stmt = bdManager.getPreparedStatementRGK("INSERT INTO Tdragon(name, id_coordinates, creationdate, age, id_color, id_type, id_character, id_killer, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"); ) {
			stmt.setString(1, d.getName()); stmt.setLong(2, insert(d.getCoordinates())); stmt.setObject(3, d.getCreationDate()); stmt.setInt(4, d.getAge()); stmt.setInt(5, d.getColor().ordinal()+1); stmt.setInt(6, d.getType().ordinal()+1); if (d.getCharacter()==null) stmt.setNull(7, java.sql.Types.INTEGER); else stmt.setInt(7, d.getCharacter().ordinal()+1); if (d.getKiller()==null) stmt.setNull(8, java.sql.Types.INTEGER); else stmt.setLong(8, insert(d.getKiller())); stmt.setLong(9, userID);
			
			if (stmt.executeUpdate() == 0) { throw new SQLException("Creating dragon failed, no rows affected."); }
			try (ResultSet gk = stmt.getGeneratedKeys()) {
				if (gk.next()) {
					d.setId(gk.getLong(1)); return true;
				} else { throw new SQLException("Creating dragon failed, no ID obtained."); }
			}
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}

	public boolean update(Dragon d) {
		try {
			PreparedStatement stmt = bdManager.getPreparedStatement("UPDATE Tdragon SET name = ?, id_coordinates = ?, age = ?, id_color = ?, id_type = ?, id_character = ?, id_killer = ?  WHERE id = ?");
			stmt.setString(1, d.getName()); stmt.setLong(2, insert(d.getCoordinates())); stmt.setInt(3, d.getAge()); stmt.setInt(4, d.getColor().ordinal()+1); stmt.setInt(5, d.getType().ordinal()+1); if (d.getCharacter()==null) stmt.setNull(6, java.sql.Types.INTEGER); else stmt.setInt(6, d.getCharacter().ordinal()+1); if (d.getKiller()==null) stmt.setNull(7, java.sql.Types.INTEGER); else stmt.setLong(7, insert(d.getKiller())); stmt.setLong(8, d.getId());
			if (stmt.executeUpdate()==0) throw new SQLException("Update coordinates failed, no rows affected.");
			return true;
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}

	public boolean remove(long id) {
		try {
			PreparedStatement deleteStmt = bdManager.getPreparedStatement("DELETE FROM Tdragon WHERE id = ?");
			deleteStmt.setLong(1, id);
			return deleteStmt.executeUpdate()>0;
		} catch (SQLException e) { System.out.println(e.toString()); return false; }
	}
}