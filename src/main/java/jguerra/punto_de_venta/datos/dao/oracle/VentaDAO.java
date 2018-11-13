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

import jguerra.punto_de_venta.datos.dao.oracle.sequences.SeqNumeroVenta;
import jguerra.punto_de_venta.datos.modelo.Cliente;
import jguerra.punto_de_venta.datos.modelo.DetalleVenta;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.datos.modelo.Venta;

public class VentaDAO {
	
	public static final String SELECT_ALL = "SELECT numero_venta,"
			+ "nit_cliente,nombre_cliente,apellido_cliente,"
			+ " telefono_cliente,id_sucursal,nombre_sucursal,"
			+ " fecha_venta, total_venta,id_sucursal FROM ventas"
			+ " NATURAL JOIN sucursales NATURAL JOIN clientes"
			+ " ORDER BY numero_venta";
	public static final String SELECT_ALL_BY_FECHA = "SELECT numero_venta,"
			+ " nit_cliente,nombre_cliente,apellido_cliente,"
			+ " telefono_cliente,id_sucursal,nombre_sucursal,"
			+ " total_venta FROM ventas NATURAL JOIN sucursales"
			+ " WHERE fecha_venta = ?";
	public static final String INSERT = "INSERT INTO ventas(numero_venta,"
			+ " nit_cliente,fecha_venta,total_venta,id_sucursal)"
			+ " VALUES(?,?,?,?,?)";
	public static final String INSERT_DETALLEVENTA = "INSERT INTO"
			+ " detalle_venta(numero_venta,nombre_producto,marca_producto,"
			+ " presentacion_producto,costo,precio,cantidad)"
			+ " VALUES(?,?,?,?,?,?,?)";
	
	private Connection conexion;
	
	public VentaDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public List<Venta> selectAll(){
		List<Venta> ventas = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next()) {
				ventas.add(new Venta(rs.getInt("numero_venta"),
						new Cliente(rs.getString("nit_cliente"),
								rs.getString("nombre_cliente"),
								rs.getString("apellido_cliente"),
								rs.getString("telefono_cliente")),
						new Sucursal(rs.getInt("id_sucursal"),
								rs.getString("nombre_sucursal")),
						rs.getTimestamp("fecha_venta").toLocalDateTime()
							.toLocalDate(),
						rs.getBigDecimal("total_venta")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ventas;
	}
	
	public List<Venta> selectAllByFecha(final LocalDate fecha){
		assert fecha != null;
		List<Venta> ventas = new LinkedList<>();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_ALL_BY_FECHA)){
			st.setTimestamp(1,Timestamp.valueOf(fecha.atStartOfDay()));
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				ventas.add(new Venta(rs.getInt("numero_venta"),
						new Cliente(rs.getString("nit_cliente"),
								rs.getString("nombre_cliente"),
								rs.getString("apellido_cliente"),
								rs.getString("telefono_cliente")),
						new Sucursal(rs.getInt("id_sucursal"),
								rs.getString("nombre_sucursal")),
						fecha, rs.getBigDecimal("total_venta")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ventas;
	}

	public int insert(final Venta venta, final List<DetalleVenta> items) throws SQLException {
		assert venta != null;
		assert items != null;
		int numero = 0;
		try {
			conexion.setAutoCommit(false);
			try(PreparedStatement st = conexion.prepareStatement(INSERT)){
				numero = SeqNumeroVenta.instance(conexion).next();
				st.setInt(1, numero);
				st.setString(2, venta.getCliente().getNit());
				st.setTimestamp(3, Timestamp.valueOf(
						venta.getFecha().atStartOfDay()));
				st.setBigDecimal(4, venta.getTotal());
				st.setInt(5, venta.getSucursal().getId());
				st.executeUpdate();
				try(final PreparedStatement st2 = 
						conexion.prepareStatement(INSERT_DETALLEVENTA)){
					for(DetalleVenta item : items) {
						st2.setInt(1, numero);
						st2.setString(2, item.getNombreProducto());
						st2.setString(3, item.getMarcaProducto());
						st2.setString(4, item.getPresentacionProducto());
						st2.setBigDecimal(5, item.getCosto());
						st2.setBigDecimal(6, item.getPrecio());
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
			throw new SQLException("No se pudo registrar la venta", e);
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
