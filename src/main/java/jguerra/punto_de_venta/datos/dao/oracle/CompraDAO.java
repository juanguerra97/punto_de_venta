package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import jguerra.punto_de_venta.datos.dao.oracle.sequences.SeqNumeroCompra;
import jguerra.punto_de_venta.datos.modelo.Compra;
import jguerra.punto_de_venta.datos.modelo.DetalleCompra;

public class CompraDAO {
	
	public static final String SELECT_ALL = "SELECT numero,id_proveedor,fecha,total"
			+ " FROM compras ORDER BY numero";
	public static final String SELECT_ALL_BY_FECHA = "SELECT numero,id_proveedor,total"
			+ " FROM compras WHERE fecha = ?";
	public static final String INSERT = "INSERT INTO compras(numero,id_proveedor,fecha,total)"
			+ " VALUES(?,?,?,?)";
	public static final String INSERT_DETALLECOMPRA = "INSERT INTO detalle_compra(numero_compra,nombre_producto"
			+ ",marca_producto,presentacion_producto,nombre_sucursal,costo,cantidad) VALUES(?,?,?,?,?,?,?)";

	private Connection conexion;
	
	public CompraDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public List<Compra> selectAll(){
		List<Compra> compras = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next())
				compras.add(new Compra(rs.getInt("numero"),rs.getInt("id_proveedor"),
						rs.getTimestamp("fecha").toLocalDateTime().toLocalDate(),
						rs.getBigDecimal("total")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compras;
	}
	
	public List<Compra> selectAllByFecha(final LocalDate fecha){
		assert fecha != null;
		List<Compra> compras = new LinkedList<>();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_ALL_BY_FECHA)){
			st.setTimestamp(1,Timestamp.valueOf(fecha.atStartOfDay()));
			ResultSet rs = st.executeQuery();
			while(rs.next())
				compras.add(new Compra(rs.getInt("numero"),rs.getInt("id_proveedor"),
						fecha, rs.getBigDecimal("total")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compras;
	}

	public int insert(final Compra compra, final List<DetalleCompra> items) throws SQLException {
		assert compra != null;
		assert items != null;
		int numero = 0;
		try {
			conexion.setAutoCommit(false);
			try(PreparedStatement st = conexion.prepareStatement(INSERT)){
				numero = SeqNumeroCompra.instance(conexion).next();
				st.setInt(1, numero);
				st.setInt(2, compra.getIdProveedor());
				st.setTimestamp(3, Timestamp.valueOf(compra.getFecha().atStartOfDay()));
				st.setBigDecimal(4, compra.getTotal());
				st.executeUpdate();
				try(final PreparedStatement st2 = conexion.prepareStatement(INSERT_DETALLECOMPRA)){
					for(DetalleCompra item : items) {
						st2.setInt(1, numero);
						st2.setString(2, item.getNombreProducto());
						st2.setString(3, item.getMarcaProducto());
						st2.setString(4, item.getPresentacionProducto());
						st2.setString(5, item.getNombreSucursal());
						st2.setBigDecimal(6, item.getCosto());
						st2.setInt(7, item.getCantidad());
						st2.executeUpdate();
					}
				}
			}
			conexion.commit();
		} catch (SQLException e) {
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SQLException("No se pudo registrar la compra", e);
		} finally {
			try {
				conexion.setAutoCommit(true);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return numero;
	}
	
}
