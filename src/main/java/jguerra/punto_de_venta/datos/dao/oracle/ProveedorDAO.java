package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.dao.oracle.sequences.SeqIdProveedor;
import jguerra.punto_de_venta.datos.modelo.Proveedor;

public class ProveedorDAO {
	
	public static final String SELECT = "SELECT nombre_proveedor,telefono_proveedor FROM proveedores"
			+ " WHERE id_proveedor = ?";
	public static final String SELECT_ALL = "SELECT id_proveedor,nombre_proveedor,telefono_proveedor"
			+ " FROM proveedores ORDER BY nombre_proveedor";
	public static final String INSERT = "INSERT INTO proveedores(id_proveedor,nombre_proveedor,"
			+ "telefono_proveedor) VALUES(?,?,?)";
	public static final String DELETE = "DELETE FROM proveedores WHERE id_proveedor = ?";
	public static final String UPDATE = "UPDATE proveedores SET nombre_proveedor = ?,telefono_proveedor = ?"
			+ " WHERE id_proveedor = ?";
	
	private Connection conexion;
	
	public ProveedorDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}

	public Optional<Proveedor> select(final int idProveedor){
		Optional<Proveedor> proveedorOpt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT)){
			st.setInt(1, idProveedor);
			ResultSet rs = st.executeQuery();
			if(rs.next())
				proveedorOpt = Optional.of(new Proveedor(idProveedor,
						rs.getString("nombre_proveedor"),rs.getString("telefono_proveedor")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return proveedorOpt;
	}
	
	public List<Proveedor> selectAll(){
		List<Proveedor> proveedores = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next())
				proveedores.add(new Proveedor(rs.getInt("id_proveedor"),
						rs.getString("nombre_proveedor"),rs.getString("telefono_proveedor")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return proveedores;
	}
	
	public int insert(final Proveedor proveedor) throws SQLException {
		assert proveedor != null;
		int id = 0;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			id = SeqIdProveedor.instance(conexion).next();
			st.setInt(1, id);
			st.setString(2, proveedor.getNombre());
			st.setString(3, proveedor.getTelefono());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^unique.*"))
					throw new SQLException("El ID del proveedor está duplicado");
			throw new SQLException("Ocurrió un error con la consulta");
		}
		return id;
	}
	
	public void delete(final int idProveedor) throws SQLException {
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setInt(1, idProveedor);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("No se puede eliminar el proveedor porque hay registros de compras que lo referencian",e);
		}
	}
	
	public void update(final Proveedor proveedor) {
		try(PreparedStatement st = conexion.prepareStatement(UPDATE)){
			st.setString(1, proveedor.getNombre());
			st.setString(2, proveedor.getTelefono());
			st.setInt(3, proveedor.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
