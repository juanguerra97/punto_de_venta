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

public class PresentacionDAO {
	
	public static final String SELECT = "SELECT id_producto,nombre,precio,costo FROM presentaciones"
			+ " WHERE id_presentacion = ?";
	public static final String SELECT_ALL_BY_PRODUCTO = "SELECT id_presentacion,nombre,precio,costo"
			+ " FROM presentaciones WHERE id_producto = ?";
	public static final String INSERT = "INSERT INTO presentaciones(id_presentacion,id_producto,nombre"
			+ ",precio,costo) VALUES(?,?,?,?,?)";
	public static final String DELETE = "DELETE FROM presentaciones WHERE id_presentacion = ?";
	public static final String UPDATE = "UPDATE presentaciones SET nombre = ?,precio = ?,costo = ?"
			+ " WHERE id_presentacion = ?";
	
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
			if(rs.next())
				presentacionOpt = Optional.of(new Presentacion(idPresentacion,
						rs.getInt("id_producto"),rs.getString("nombre"),
						rs.getBigDecimal("precio"),rs.getBigDecimal("costo")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return presentacionOpt;
	}
	
	public List<Presentacion> selectAllByProducto(final int idProducto){
		List<Presentacion> presentaciones = new LinkedList<>();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_ALL_BY_PRODUCTO)){
			st.setInt(1, idProducto);
			ResultSet rs = st.executeQuery();
			while(rs.next())
				presentaciones.add(new Presentacion(rs.getInt("id_presentacion"),
						idProducto,rs.getString("nombre"),rs.getBigDecimal("precio"),
						rs.getBigDecimal("costo")));
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return presentaciones;
	}
	
	public int insert(final Presentacion presentacion) throws SQLException {
		assert presentacion != null;
		int id = 0;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			id = SeqIdPresentacion.instance(conexion).next();
			st.setInt(1, id);
			st.setInt(2, presentacion.getIdProducto());
			st.setString(3, presentacion.getNombre());
			st.setBigDecimal(4, presentacion.getPrecio());
			st.setBigDecimal(5, presentacion.getCosto());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^unique.*"))
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
				if(msg.matches("^unique.*"))
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