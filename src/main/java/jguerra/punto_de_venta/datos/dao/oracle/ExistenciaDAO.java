package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.modelo.Existencia;

public class ExistenciaDAO {
	
	public static final String SELECT = "SELECT cantidad FROM existencias WHERE id_presentacion = ?"
			+ " AND id_sucursal = ?";
	public static final String SELECT_ALL_BY_PRESENTACION = "SELECT id_sucursal,cantidad"
			+ " FROM existencias WHERE id_presentacion = ?";
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