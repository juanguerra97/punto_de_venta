package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import jguerra.punto_de_venta.datos.modelo.DetalleVenta;

public class DetalleVentaDAO {
	
	public static final String SELECT_ALL_BY_VENTA = "SELECT nombre_producto,marca_producto,"
			+ "presentacion_producto,costo,precio,cantidad FROM detalle_venta WHERE numero_venta = ?";
	
	private Connection conexion;
	
	public DetalleVentaDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}	
	
	public List<DetalleVenta> selectAllByVenta(final int numeroVenta){
		List<DetalleVenta> items = new LinkedList<>();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_ALL_BY_VENTA)){
			st.setInt(1, numeroVenta);
			ResultSet rs = st.executeQuery();
			while(rs.next())
				items.add(new DetalleVenta(numeroVenta,rs.getString("nombre_producto"),
						rs.getString("marca_producto"),rs.getString("presentacion_producto"),
						rs.getBigDecimal("costo"),rs.getBigDecimal("precio"),rs.getInt("cantidad")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

}
