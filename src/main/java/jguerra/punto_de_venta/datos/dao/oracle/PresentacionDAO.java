package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.dao.oracle.sequences.SeqIdPresentacion;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.modelo.Sucursal;

public class PresentacionDAO {
	
	public static final String SELECT = "SELECT id_producto,"
			+ "nombre_producto,marca_producto,"
			+ "nombre_presentacion,precio_presentacion,"
			+ "costo_presentacion FROM presentaciones"
			+ " NATURAL JOIN productos"
			+ " WHERE id_presentacion = ?";
	public static final String SELECT_ALL_BY_PRODUCTO = 
			"SELECT id_presentacion,nombre_presentacion,"
			+ "precio_presentacion,costo_presentacion"
			+ " FROM presentaciones WHERE id_producto = ?";
	public static final String SELECT_SUCURSALES_PRESENTACION = 
			"SELECT id_sucursal,nombre_sucursal"
			+ " FROM presentaciones NATURAL JOIN existencias"
			+ " NATURAL JOIN sucursales WHERE id_presentacion = ?";
	public static final String INSERT = "INSERT INTO presentaciones"
			+ "(id_presentacion,id_producto,nombre_presentacion,"
			+ "precio_presentacion,costo_presentacion) VALUES(?,?,?,?,?)";
	public static final String DELETE = "DELETE FROM presentaciones"
			+ " WHERE id_presentacion = ?";
	public static final String UPDATE = "UPDATE presentaciones"
			+ " SET nombre_presentacion = ?,precio_presentacion = ?,"
			+ "costo_presentacion = ? WHERE id_presentacion = ?";
	
	private Connection conexion;
	
	public PresentacionDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public Optional<Presentacion> select(final int idPresentacion){
		Optional<Presentacion> presentacionOpt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT)){
			st.setInt(1, idPresentacion);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				presentacionOpt = Optional.of(
						new Presentacion(idPresentacion,
								new Producto(rs.getInt("id_producto"),
										rs.getString("nombre_producto"),
										rs.getString("marca_producto")),
								rs.getString("nombre_presentacion"),
						rs.getBigDecimal("precio_presentacion"),
						rs.getBigDecimal("costo_presentacion")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return presentacionOpt;
	}
	
	public List<Presentacion> selectAllByProducto(final Producto producto){
		assert producto != null;
		List<Presentacion> presentaciones = new LinkedList<>();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_ALL_BY_PRODUCTO)){
			st.setInt(1, producto.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				presentaciones.add(new Presentacion(
						rs.getInt("id_presentacion"),
						producto,rs.getString("nombre_presentacion"),
						rs.getBigDecimal("precio_presentacion"),
						rs.getBigDecimal("costo_presentacion")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return presentaciones;
	}
	
	public List<Sucursal> selectSucursalesPresentacion(
			final int idPresentacion){
		List<Sucursal> sucursales = new LinkedList<>();
		try(PreparedStatement st = 
				conexion.prepareStatement(SELECT_SUCURSALES_PRESENTACION)){
			st.setInt(1, idPresentacion);
			ResultSet rs = st.executeQuery();
			while(rs.next())
				sucursales.add(new Sucursal(rs.getInt("id_sucursal"),
						rs.getString("nombre_sucursal")));
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return sucursales;
	}
	
	public int insert(final Presentacion presentacion) throws SQLException {
		assert presentacion != null;
		int id = 0;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			id = SeqIdPresentacion.instance(conexion).next();
			st.setInt(1, id);
			st.setInt(2, presentacion.getProducto().getId());
			st.setString(3, presentacion.getNombre());
			st.setBigDecimal(4, presentacion.getPrecio());
			st.setBigDecimal(5, presentacion.getCosto());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^ORA-00001: unique constraint.*\\n$"))
					throw new SQLException("El nombre de la presentacion esta duplicado",e);
			throw new SQLException("Error en la consulta",e);
		}
		return id;
	}
	
	public void update(final Presentacion presentacion) throws SQLException {
		assert presentacion != null;
		try(PreparedStatement st = conexion.prepareStatement(UPDATE)){
			st.setString(1, presentacion.getNombre());
			st.setBigDecimal(2, presentacion.getPrecio());
			st.setBigDecimal(3, presentacion.getCosto());
			st.setInt(4, presentacion.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^ORA-00001: unique constraint.*\\n$"))
					throw new SQLException("El nombre de la presentacion esta duplicado",e);
			throw new SQLException("Error en la consulta",e);
		}
	}
	
	public void delete(final int idPresentacion) throws SQLException {
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setInt(1, idPresentacion);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("Hay existencias del producto en esta presentacion", e);
		}
	}

}
