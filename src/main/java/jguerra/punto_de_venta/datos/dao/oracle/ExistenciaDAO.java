package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.modelo.DetalleCompra;
import jguerra.punto_de_venta.datos.modelo.DetalleVenta;
import jguerra.punto_de_venta.datos.modelo.Existencia;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.modelo.Sucursal;

public class ExistenciaDAO {
	
	public static final String SELECT = "SELECT cantidad"
			+ " FROM existencias WHERE id_presentacion = ?"
			+ " AND id_sucursal = ?";
	public static final String SELECT_ALL_BY_PRESENTACION = 
			"SELECT id_sucursal,nombre_sucursal,cantidad"
			+ " FROM existencias NATURAL JOIN sucursales"
			+ " WHERE id_presentacion = ?";
	public static final String SELECT_BY_DETALLE = 
			"SELECT id_presentacion,id_producto,nombre_presentacion,"
			+ " precio_presentacion,costo_presentacion,id_sucursal,"
			+ " nombre_sucursal,cantidad FROM productos"
			+ " NATURAL JOIN presentaciones NATURAL JOIN existencias"
			+ " NATURAL JOIN sucursales WHERE nombre_producto = ?"
			+ " AND marca_producto = ? AND nombre_presentacion = ?"
			+ " AND nombre_sucursal = ?";
	public static final String INSERT = "INSERT INTO existencias"
			+ "(id_presentacion,id_sucursal,cantidad) VALUES(?,?,?)";
	public static final String UPDATE = "UPDATE existencias"
			+ " SET cantidad = ? WHERE id_presentacion = ?"
			+ " AND id_sucursal = ?";
	public static final String DELETE = "DELETE FROM existencias"
			+ " WHERE id_presentacion = ? AND id_sucursal = ?";
	
	private Connection conexion;
	
	public ExistenciaDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public Optional<Existencia> select(final Existencia existencia){
		assert existencia != null;
		Optional<Existencia> opt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT)){
			st.setInt(1, existencia.getPresentacion().getId());
			st.setInt(2, existencia.getSucursal().getId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				existencia.setCantidad(rs.getInt("cantidad"));
				opt = Optional.of(existencia);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return opt;
	}
	
	public List<Existencia> selectAllByPresentacion(
			final Presentacion presentacion){
		assert presentacion != null;
		List<Existencia> existencias = new LinkedList<>();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_ALL_BY_PRESENTACION)){
			st.setInt(1, presentacion.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next())
				existencias.add(new Existencia(presentacion,
						new Sucursal(rs.getInt("id_sucursal"),
								rs.getString("nombre_sucursal")),
						rs.getInt("cantidad")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existencias;
	}
	
	public Optional<Existencia> selectByDetalle(
			final DetalleCompra detalle){
		assert detalle != null;
		Optional<Existencia> opt = Optional.empty();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_BY_DETALLE)){
			st.setString(1, detalle.getNombreProducto());
			st.setString(2, detalle.getMarcaProducto());
			st.setString(3, detalle.getPresentacionProducto());
			st.setString(4, detalle.getNombreSucursal());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				opt = Optional.of(new Existencia(
						new Presentacion(rs.getInt("id_presentacion"),
								new Producto(rs.getInt("id_producto"),
										detalle.getNombreProducto(),
										detalle.getMarcaProducto()),
								rs.getString("nombre_presentacion"),
								rs.getBigDecimal("precio_presentacion"),
								rs.getBigDecimal("costo_presentacion")), 
						new Sucursal(rs.getInt("id_sucursal"),
								rs.getString("nombre_sucursal")), 
						rs.getInt("cantidad")));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return opt;
	}
	
	public Optional<Existencia> selectByDetalle(
			final DetalleVenta detalle, final Sucursal sucursal){
		assert detalle != null;
		assert sucursal != null;
		Optional<Existencia> opt = Optional.empty();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_BY_DETALLE)){
			st.setString(1, detalle.getNombreProducto());
			st.setString(2, detalle.getMarcaProducto());
			st.setString(3, detalle.getPresentacionProducto());
			st.setString(4, sucursal.getNombre());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				opt = Optional.of(new Existencia(
						new Presentacion(rs.getInt("id_presentacion"),
								new Producto(rs.getInt("id_producto"),
										detalle.getNombreProducto(),
										detalle.getMarcaProducto()),
								rs.getString("nombre_presentacion"),
								rs.getBigDecimal("precio_presentacion"),
								rs.getBigDecimal("costo_presentacion")), 
						new Sucursal(rs.getInt("id_sucursal"),
								rs.getString("nombre_sucursal")), 
						rs.getInt("cantidad")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return opt;
	}
	
	public void insert(final Existencia existencia) throws SQLException {
		assert existencia != null;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			st.setInt(1, existencia.getPresentacion().getId());
			st.setInt(2, existencia.getSucursal().getId());
			st.setInt(3, existencia.getCantidad());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^ORA-00001: unique constraint.*\\n$"))
					throw new SQLException("Ya hay existencias del producto para la sucursal", e);
			throw new SQLException("Ocurrió un error con la consulta", e);
		}
	}
	
	public void update(final Existencia existencia) {
		assert existencia != null;
		try(PreparedStatement st = conexion.prepareStatement(UPDATE)){
			st.setInt(1, existencia.getCantidad());
			st.setInt(2, existencia.getPresentacion().getId());
			st.setInt(3, existencia.getSucursal().getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(final Existencia existencia) {
		assert existencia != null;
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setInt(1, existencia.getPresentacion().getId());
			st.setInt(2, existencia.getSucursal().getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
