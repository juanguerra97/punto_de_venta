package jguerra.punto_de_venta.datos.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import jguerra.punto_de_venta.db.oracle.Conexion;

public class DAOManager {
	
	private Connection conexion = null;
	private ClienteDAO daoCliente = null;
	private CompraDAO daoCompra = null;
	private DetalleCompraDAO daoDetalleCompra = null;
	private DetalleVentaDAO daoDetalleVenta = null;
	private ExistenciaDAO daoExistencia = null;
	private PresentacionDAO daoPresentacion = null;
	private ProductoDAO daoProducto = null;
	private ProveedorDAO daoProveedor = null;
	private SucursalDAO daoSucursal = null;
	private VentaDAO daoVenta = null;
	
	private static final DAOManager MANAGER = new DAOManager();
	
	private DAOManager() {}
	
	public static DAOManager instance() {
		return MANAGER;
	}
	
	public Optional<ClienteDAO> cliente() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoCliente = new ClienteDAO(conexion);
			}else if(daoCliente == null)
				daoCliente = new ClienteDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoCliente = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoCliente);
	}
	
	public Optional<CompraDAO> compra() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoCompra = new CompraDAO(conexion);
			}else if(daoCompra == null)
				daoCompra = new CompraDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoCompra = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoCompra);
	}
	
	public Optional<DetalleCompraDAO> detalleCompra() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoDetalleCompra = new DetalleCompraDAO(conexion);
			}else if(daoDetalleCompra == null)
				daoDetalleCompra = new DetalleCompraDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoDetalleCompra = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoDetalleCompra);
	}
	
	public Optional<DetalleVentaDAO> detalleVenta() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoDetalleVenta = new DetalleVentaDAO(conexion);
			}else if(daoDetalleVenta == null)
				daoDetalleVenta = new DetalleVentaDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoDetalleVenta = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoDetalleVenta);
	}
	
	public Optional<ExistenciaDAO> existencia() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoExistencia = new ExistenciaDAO(conexion);
			}else if(daoExistencia == null)
				daoExistencia = new ExistenciaDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoExistencia = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoExistencia);
	}
	
	public Optional<PresentacionDAO> presentacion() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoPresentacion = new PresentacionDAO(conexion);
			}else if(daoPresentacion == null)
				daoPresentacion = new PresentacionDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoPresentacion = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoPresentacion);
	}
	
	public Optional<ProductoDAO> producto() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoProducto = new ProductoDAO(conexion);
			}else if(daoProducto == null)
				daoProducto = new ProductoDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoProducto = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoProducto);
	}
	
	public Optional<ProveedorDAO> proveedor() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoProveedor = new ProveedorDAO(conexion);
			}else if(daoProveedor == null)
				daoProveedor = new ProveedorDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoProveedor = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoProveedor);
	}
	
	public Optional<SucursalDAO> sucursal() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoSucursal = new SucursalDAO(conexion);
			}else if(daoSucursal == null)
				daoSucursal = new SucursalDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoSucursal = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoSucursal);
	}
	
	public Optional<VentaDAO> venta() {
		try {
			if(conexion == null || !conexion.isValid(80)) {
				conexion = Conexion.get();
				daoVenta = new VentaDAO(conexion);
			}else if(daoVenta == null)
				daoVenta = new VentaDAO(conexion);
		} catch (SQLException e) {
			conexion = null;
			daoVenta = null;
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(daoVenta);
	}

}
