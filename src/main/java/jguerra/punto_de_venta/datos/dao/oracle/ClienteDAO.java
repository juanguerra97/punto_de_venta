package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.modelo.Cliente;

public class ClienteDAO {
	
	public static final String SELECT = "SELECT nombre,apellido,telefono FROM clientes WHERE nit = ?";
	public static final String SELECT_ALL = "SELECT nit,nombre,apellido,telefono FROM clientes ORDER BY nombre,apellido";
	public static final String INSERT = "INSERT INTO clientes(nit,nombre,apellido,telefono) VALUES(?,?,?,?)";
	public static final String DELETE = "DELETE FROM clientes WHERE nit = ?";
	public static final String UPDATE = "UPDATE clientes SET nombre=?,apellido=?,telefono=? WHERE nit = ?";
	
	private Connection conexion;
	
	public ClienteDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public Optional<Cliente> select(final String nit){
		assert nit != null;
		
		Optional<Cliente> clienteOpt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT)){
			st.setString(1, nit);
			ResultSet rs = st.executeQuery();
			if(rs.next())
				clienteOpt = Optional.of(
						new Cliente(nit,rs.getString("nombre"),rs.getString("apellido"),
								rs.getString("telefono")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clienteOpt;
	}
	
	public List<Cliente> selectAll(){
		List<Cliente> clientes = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next())
				clientes.add(
						new Cliente(rs.getString("nit"),rs.getString("nombre"),
								rs.getString("apellido"),rs.getString("telefono")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clientes;		
	}
	
	public void insert(final Cliente cliente) throws SQLException {
		assert cliente != null;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			st.setString(1, cliente.getNit());
			st.setString(2, cliente.getNombre());
			st.setString(3, cliente.getApellido());
			st.setString(4, cliente.getTelefono());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^ORA-00001: unique constraint.*\\n$"))
					throw new SQLException("El NIT del cliente se encuentra duplicado",e);
			throw new SQLException("Error en la consulta",e);
		}
	}
	
	public void update(final Cliente datosCliente) {
		assert datosCliente != null;
		try(PreparedStatement st = conexion.prepareStatement(UPDATE)){
			st.setString(1, datosCliente.getNombre());
			st.setString(2, datosCliente.getApellido());
			st.setString(3, datosCliente.getTelefono());
			st.setString(4, datosCliente.getNit());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(final String nitCliente) throws SQLException {
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setString(1, nitCliente);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("El cliente no se puede eliminar porque se encuentra en registros de ventas",e);
		}
	}
	
}
