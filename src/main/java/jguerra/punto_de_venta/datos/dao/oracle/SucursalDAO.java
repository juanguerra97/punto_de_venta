package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.dao.oracle.sequences.SeqIdSucursal;
import jguerra.punto_de_venta.datos.modelo.Sucursal;

public class SucursalDAO {
	
	public static final String SELECT = "SELECT nombre FROM sucursales WHERE id_sucursal = ?";
	public static final String SELECT_ALL = "SELECT id_sucursal,nombre FROM sucursales ORDER BY nombre";
	public static final String INSERT = "INSERT INTO sucursales(id_sucursal,nombre) VALUES(?,?)";
	public static final String DELETE = "DELETE FROM sucursales WHERE id_sucursal = ?";
	public static final String UPDATE = "UPDATE sucursales SET nombre = ? WHERE id_sucursal = ?";
	
	private Connection conexion;
	
	public SucursalDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public Optional<Sucursal> select(final int id){
		Optional<Sucursal> sucursalOpt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT)){
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if(rs.next())
				sucursalOpt = Optional.of(new Sucursal(id,rs.getString("nombre")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sucursalOpt;
	}
	
	public List<Sucursal> selectAll(){
		List<Sucursal> sucursales = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next())
				sucursales.add(new Sucursal(rs.getInt("id_sucursal"),rs.getString("nombre")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sucursales;
	}
	
	public int insert(final Sucursal sucursal) throws SQLException {
		assert sucursal != null;
		int id = 0;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			id = SeqIdSucursal.instance(conexion).next();
			st.setInt(1, id);
			st.setString(2, sucursal.getNombre());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.equals("ORA-00001: unique constraint (ADMIN_PV.SYS_C007472) violated\n"))
					throw new SQLException("El nombre de la sucursal está duplicado", e);
			throw new SQLException("Error en la consulta", e);
		}
		return id;
	}
	
	public void update(final Sucursal sucursal) throws SQLException {
		assert sucursal != null;
		try(PreparedStatement st = conexion.prepareStatement(UPDATE)){
			st.setString(1, sucursal.getNombre());
			st.setInt(2, sucursal.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.equals("ORA-00001: unique constraint (ADMIN_PV.SYS_C007472) violated\n"))
					throw new SQLException("El nombre de la sucursal está duplicado", e);
			throw new SQLException("Error en la consulta", e);
		}
	}
	
	public void delete(final int idSucursal) throws SQLException {
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setInt(1, idSucursal);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("Hay registros de ventas y existencias de productos en esta sucursal", e);
		}
	}

}
