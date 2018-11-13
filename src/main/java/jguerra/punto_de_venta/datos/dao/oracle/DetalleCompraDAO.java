package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import jguerra.punto_de_venta.datos.modelo.DetalleCompra;

public class DetalleCompraDAO {

	public static final String SELECT_ALL_BY_COMPRA = 
			"SELECT nombre_producto,marca_producto,presentacion_producto,"
			+ " nombre_sucursal,costo,cantidad FROM detalle_compra"
			+ " WHERE numero_compra = ?";
	
	private Connection conexion;
	
	public DetalleCompraDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}	
	
	public List<DetalleCompra> selectAllByCompra(final int numeroCompra){
		List<DetalleCompra> items = new LinkedList<>();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_ALL_BY_COMPRA)){
			st.setInt(1, numeroCompra);
			ResultSet rs = st.executeQuery();
			while(rs.next())
				items.add(new DetalleCompra(numeroCompra,
						rs.getString("nombre_producto"),
						rs.getString("marca_producto"),
						rs.getString("presentacion_producto"),
						rs.getString("nombre_sucursal"),
						rs.getBigDecimal("costo"),
						rs.getInt("cantidad")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
}
