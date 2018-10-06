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
import jguerra.punto_de_venta.datos.modelo.Sucursal;

public class ExistenciaDAO {
	
	public static final String SELECT = "SELECT cantidad FROM existencias WHERE id_presentacion = ?"
			+ " AND id_sucursal = ?";
	public static final String SELECT_ALL_BY_PRESENTACION = "SELECT id_sucursal,cantidad"
			+ " FROM existencias WHERE id_presentacion = ?";
	public static final String SELECT_BY_DETALLE = "SELECT id_presentacion,id_sucursal,cantidad"
			+ " FROM (SELECT id_presentacion,id_sucursal,cantidad FROM (SELECT id_presentacion"
			+ " FROM (SELECT id_producto FROM productos WHERE nombre = ? AND marca = ?)"
			+ " NATURAL JOIN presentaciones WHERE presentaciones.nombre = ?)"
			+ " NATURAL JOIN existencias) NATURAL JOIN (SELECT id_sucursal FROM sucursales"
			+ " WHERE nombre = ?)";
	public static final String INSERT = "INSERT INTO existencias(id_presentacion,"
			+ "id_sucursal,cantidad) VALUES(?,?,?)";
	public static final String UPDATE = "UPDATE existencias SET cantidad = ?"
			+ " WHERE id_presentacion = ? AND id_sucursal = ?";
	public static final String DELETE = "DELETE FROM existencias WHERE id_presentacion = ?"
			+ " AND id_sucursal = ?";
	
	private Connection conexion;
	
	public ExistenciaDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public Optional<Existencia> select(final Existencia existencia){
		assert existencia != null;
		Optional<Existencia> opt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT)){
			st.setInt(1, existencia.getIdPresentacion());
			st.setInt(2, existencia.getIdSucursal());
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
	
	public List<Existencia> selectAllByPresentacion(final int idPresentacion){
		List<Existencia> existencias = new LinkedList<>();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_ALL_BY_PRESENTACION)){
			st.setInt(1, idPresentacion);
			ResultSet rs = st.executeQuery();
			while(rs.next())
				existencias.add(new Existencia(idPresentacion,rs.getInt("id_sucursal"),
						rs.getInt("cantidad")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existencias;
	}
	
	public Optional<Existencia> selectByDetalle(final DetalleCompra detalle){
		assert detalle != null;
		Optional<Existencia> opt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_BY_DETALLE)){
			st.setString(1, detalle.getNombreProducto());
			st.setString(2, detalle.getMarcaProducto());
			st.setString(3, detalle.getPresentacionProducto());
			st.setString(4, detalle.getNombreSucursal());
			ResultSet rs = st.executeQuery();
			if(rs.next())
				opt = Optional.of(new Existencia(rs.getInt("id_presentacion"), 
						rs.getInt("id_sucursal"), rs.getInt("cantidad")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return opt;
	}
	
	public Optional<Existencia> selectByDetalle(final DetalleVenta detalle, 
			final Sucursal sucursal){
		assert detalle != null;
		assert sucursal != null;
		Optional<Existencia> opt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_BY_DETALLE)){
			st.setString(1, detalle.getNombreProducto());
			st.setString(2, detalle.getMarcaProducto());
			st.setString(3, detalle.getPresentacionProducto());
			st.setString(4, sucursal.getNombre());
			ResultSet rs = st.executeQuery();
			if(rs.next())
				opt = Optional.of(new Existencia(rs.getInt("id_presentacion"), 
						rs.getInt("id_sucursal"), rs.getInt("cantidad")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return opt;
	}
	
	public void insert(final Existencia existencia) throws SQLException {
		assert existencia != null;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			st.setInt(1, existencia.getIdPresentacion());
			st.setInt(2, existencia.getIdSucursal());
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
			st.setInt(2, existencia.getIdPresentacion());
			st.setInt(3, existencia.getIdSucursal());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(final Existencia existencia) {
		assert existencia != null;
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setInt(1, existencia.getIdPresentacion());
			st.setInt(2, existencia.getIdSucursal());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
