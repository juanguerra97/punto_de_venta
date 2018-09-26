package jguerra.punto_de_venta.db.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
	
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USERNAME = "admin_pv";
	private static final String PASSWORD = "admin_pv";
	
	private static Connection conexion = null;
	
	public static Connection get() throws SQLException {
		if(conexion == null) {
			try {
				conexion = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			} catch (SQLException e) {
				throw new SQLException("NO se puede conectar a la base de datos");
			}
		}
		return conexion;
	}
	
}
