package jguerra.punto_de_venta.datos.dao.oracle.sequences;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SeqNumeroVenta implements Sequence {
	
	private static final String NEXT_VAL = "SELECT numero_venta_seq.NEXTVAL AS val FROM DUAL;";

	private static SeqNumeroVenta INSTANCE = null;
	
	private Connection conexion;
	
	private SeqNumeroVenta(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}

	@Override
	public int next() {
		int nextVal = 0;
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(NEXT_VAL);
			if(rs.next())
				nextVal = rs.getInt("val");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nextVal;
	}
	
	public static final SeqNumeroVenta instance(final Connection conexion) {
		if(INSTANCE == null)
			INSTANCE = new SeqNumeroVenta(conexion);
		return INSTANCE;
	}

}
