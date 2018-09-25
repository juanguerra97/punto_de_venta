package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jguerra.punto_de_venta.datos.dao.oracle.sequences.SeqIdProducto;
import jguerra.punto_de_venta.datos.modelo.Producto;

public class ProductoDAO {
	
	public static final String SELECT_BY_ID = "SELECT nombre,marca FROM productos"
			+ " WHERE id_producto = ?";
	public static final String SELECT_ALL = "SELECT id_producto,nombre,marca FROM"
			+ " productos ORDER BY id_producto,nombre";
	public static final String SELECT_ALL_BY_MARCA = "SELECT id_producto,nombre,marca FROM"
			+ " productos WHERE marca = ? ORDER BY id_producto,nombre";
	public static final String SELECT_ALL_MARCAS = "SELECT DISTINCT(marca) AS nombre_marca"
			+ " FROM productos ORDER BY nombre_marca";
	public static final String SELECT_MARCAS_REGEX = "SELECT DISTINCT(marca) AS nombre_marca"
			+ " FROM productos WHERE REGEXP_LIKE(marca,?) ORDER BY nombre_marca";
	public static final String INSERT = "INSERT INTO productos(id_producto,nombre,"
			+ "marca) VALUES(?,?,?)";
	public static final String DELETE = "DELETE FROM productos WHERE id_producto = ?";
	public static final String UPDATE = "UPDATE productos SET nombre = ?,marca=?"
			+ " WHERE id_producto = ?";
	
	private Connection conexion;
	
	public ProductoDAO(final Connection conexion) {
		assert conexion != null;
		this.conexion = conexion;
	}
	
	public Optional<Producto> selectById(final int idProducto){
		Optional<Producto> productoOpt = Optional.empty();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_BY_ID)){
			st.setInt(1, idProducto);
			ResultSet rs = st.executeQuery();
			if(rs.next())
				productoOpt = Optional.of(new Producto(idProducto,
						rs.getString("nombre"),rs.getString("marca")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productoOpt;
	}
	
	public List<Producto> selectAll(){
		List<Producto> productos = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL);
			while(rs.next())
				productos.add(new Producto(rs.getInt("id_producto"),
						rs.getString("nombre"),rs.getString("marca")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productos;
	}
	
	public List<Producto> selectAllByMarca(final String marca){
		assert marca != null;
		List<Producto> productos = new LinkedList<>();
		try(PreparedStatement st = conexion.prepareStatement(SELECT_ALL_BY_MARCA)){
			st.setString(1, marca);
			ResultSet rs = st.executeQuery();
			while(rs.next())
				productos.add(new Producto(rs.getInt("id_producto"),
						rs.getString("nombre"),rs.getString("marca")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productos;
	}
	
	public List<String> selectAllMarcas(){
		List<String> marcas = new LinkedList<>();
		try(Statement st = conexion.createStatement()){
			ResultSet rs = st.executeQuery(SELECT_ALL_MARCAS);
			while(rs.next())
				marcas.add(rs.getString("nombre_marca"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return marcas;
	}
	
	public List<String> selectMarcasByRegex(final String regex, final int max){
		assert regex != null;
		List<String> marcas = new LinkedList<>();
		if(max <= 0)
			return marcas;
		try(PreparedStatement st = conexion.prepareStatement(SELECT_MARCAS_REGEX)){
			st.setString(1, regex);
			ResultSet rs = st.executeQuery();
			for(int i = 0; rs.next() && i < max; ++i)
				marcas.add(rs.getString("nombre_marca"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return marcas;
	}
	
	public int insert(final Producto producto) throws SQLException {
		assert producto != null;
		int id = 0;
		try(PreparedStatement st = conexion.prepareStatement(INSERT)){
			id = SeqIdProducto.instance(conexion).next();
			st.setInt(1, id);
			st.setString(2, producto.getNombre());
			st.setString(3, producto.getMarca());
			st.executeUpdate();
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^unique.*"))
					throw new SQLException("Los datos del producto están duplicados", e);
			throw new SQLException("Ocurrió un error con la consulta", e);
		}
		return id;
	}
	
	public void delete(final int idProducto) throws SQLException {
		try(PreparedStatement st = conexion.prepareStatement(DELETE)){
			st.setInt(1, idProducto);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("No se puede eliminar el producto porque hay registros de existencias que lo referencian", e);
		}
	}
	
	public void update(final Producto producto) throws SQLException {
		assert producto != null;
		try(PreparedStatement st = conexion.prepareStatement(UPDATE)){
			
		} catch (SQLException e) {
			String msg = e.getMessage();
			if(msg != null)
				if(msg.matches("^unique.*"))
					throw new SQLException("Los datos del producto están duplicados", e);
			throw new SQLException("Ocurrió un error con la consulta", e);
		}
	}

}