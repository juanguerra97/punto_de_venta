package jguerra.punto_de_venta.gui.productos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Existencia;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.gui.Main;

public class ProductosController {
	
	@FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, Integer> colIdProducto;

    @FXML
    private TableColumn<Producto, String> colNombreProducto;

    @FXML
    private TableColumn<Producto, String> colMarcaProducto;

    @FXML
    private MenuItem itemEliminarProducto;

    @FXML
    private MenuItem itemDeseleccionarProducto;

    @FXML
    private TableView<Presentacion> tablaPresentaciones;

    @FXML
    private TableColumn<Presentacion, String> colNombrePresentacion;

    @FXML
    private TableColumn<Presentacion, BigDecimal> colPrecioPresentacion;

    @FXML
    private TableColumn<Presentacion, BigDecimal> colCostoPresentacion;

    @FXML
    private MenuItem itemNuevaPresentacion;

    @FXML
    private MenuItem itemEliminarPresentacion;

    @FXML
    private MenuItem itemDeseleccionarPresentacion;

    @FXML
    private TableView<Existencia> tablaExistencias;

    @FXML
    private TableColumn<Existencia, Integer> colSucursalExistencia;

    @FXML
    private TableColumn<Existencia, Integer> colCantidadExistencia;

    @FXML
    private MenuItem itemNuevaExistencia;

    @FXML
    private MenuItem itemEliminarExistencia;

    @FXML
    private MenuItem itemDeseleccionarExistencia;
    
    private DAOManager manager;
    
    private ObservableList<Producto> productos;
    private ObservableList<Presentacion> presentaciones;
    private ObservableList<Existencia> existencias;
    
    private Stage windowNuevoProducto;
    private NuevoProductoController controllerNuevoProducto;
    
    private void cargarProductos() {
    	manager.producto().ifPresent(dao -> {
    		productos.setAll(dao.selectAll());
    	});
    }
    
    private void cargarPresentaciones(final Producto producto) {
    	manager.presentacion().ifPresent(dao -> {
    		presentaciones.setAll(dao.selectAllByProducto(producto.getId()));
    	});
    }
    
    private void cargarExistencias(final Presentacion presentacion) {
    	manager.existencia().ifPresent(dao -> {
    		existencias.setAll(dao.selectAllByPresentacion(presentacion.getId()));
    	});
    }
	
	@FXML
	private void initialize() {
		
		manager = DAOManager.instance();
		productos = tablaProductos.getItems();
		presentaciones = tablaPresentaciones.getItems();
		existencias = tablaExistencias.getItems();
		
		tablaProductos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		colIdProducto.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("id"));
		colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto,String>("nombre"));
		colMarcaProducto.setCellValueFactory(new PropertyValueFactory<Producto,String>("marca"));
		
		tablaPresentaciones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		colNombrePresentacion.setCellValueFactory(new PropertyValueFactory<Presentacion,String>("nombre"));
		colPrecioPresentacion.setCellValueFactory(new PropertyValueFactory<Presentacion,BigDecimal>("precio"));
		colCostoPresentacion.setCellValueFactory(new PropertyValueFactory<Presentacion,BigDecimal>("costo"));
		
		tablaExistencias.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		colSucursalExistencia.setCellValueFactory(new PropertyValueFactory<Existencia,Integer>("idSucursal"));
		colCantidadExistencia.setCellValueFactory(new PropertyValueFactory<Existencia,Integer>("cantidad"));
		
		tablaProductos.getSelectionModel().selectedItemProperty().addListener((obj,viejo,nuevo)->{
			if(nuevo != null) {
				cargarPresentaciones(nuevo);
			}else {
				presentaciones.clear();
			}
		});
		
		tablaPresentaciones.getSelectionModel().selectedItemProperty().addListener((obj,viejo,nuevo)->{
			if(nuevo != null) {
				cargarExistencias(nuevo);
			}else {
				existencias.clear();
			}
		});
		
		try {
			FXMLLoader loaderNuevoProducto = new FXMLLoader(getClass().getResource("/fxml/productos/NuevoProducto.fxml"));
			Scene sceneNuevoProducto = new Scene(loaderNuevoProducto.load(),380,200);
			controllerNuevoProducto = loaderNuevoProducto.getController();
			windowNuevoProducto = new Stage();
			windowNuevoProducto.setScene(sceneNuevoProducto);
			windowNuevoProducto.setTitle("Nuevo producto");
			windowNuevoProducto.setMinWidth(380);
			windowNuevoProducto.setMinHeight(200);
			windowNuevoProducto.initModality(Modality.APPLICATION_MODAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cargarProductos();
		
	}
	
	@FXML
	private void onProductosContextMenuShown(WindowEvent event) {
		boolean sinSeleccion = tablaProductos.getSelectionModel().getSelectedItem() == null;
		itemEliminarProducto.setDisable(sinSeleccion);
		itemDeseleccionarProducto.setDisable(sinSeleccion);
	}
	
	@FXML
	private void onNuevoProducto(ActionEvent event) {
		controllerNuevoProducto.reset();
		windowNuevoProducto.showAndWait();
		controllerNuevoProducto.getProducto().ifPresent(producto -> {
			productos.add(producto);
			productos.sort((prod1,prod2)->Integer.compare(prod1.getId(), prod2.getId()));
			Main.notificar("Se ingresó un nuevo producto");
		});
	}
	
	@FXML
	private void onEliminarProducto(ActionEvent event) {
		manager.producto().ifPresent(dao -> {
			final Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
			try {
				dao.delete(producto.getId());
				productos.remove(producto);
				Main.notificar("Se eliminó un producto");
				tablaProductos.requestFocus();
			} catch (SQLException e) {
				Main.notificar(e.getMessage());
				e.printStackTrace();
			}
		});
	}
	
	@FXML
	private void onDeseleccionarProducto(ActionEvent event) {
		tablaProductos.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void onPresentacionesContextMenuShown(WindowEvent event) {
		boolean sinSeleccion = tablaPresentaciones.getSelectionModel().getSelectedItem() == null;
		itemNuevaPresentacion.setDisable(tablaProductos.getSelectionModel().getSelectedItem() == null);
		itemEliminarPresentacion.setDisable(sinSeleccion);
		itemDeseleccionarPresentacion.setDisable(sinSeleccion);
	}
	
	@FXML
	private void onNuevaPresentacion(ActionEvent event) {
		
	}
	
	@FXML
	private void onEliminarPresentacion(ActionEvent event) {
		manager.presentacion().ifPresent(dao ->{
			final Presentacion presentacion = tablaPresentaciones.getSelectionModel().getSelectedItem();
			try {
				dao.delete(presentacion.getId());
				presentaciones.remove(presentacion);
				Main.notificar("Se eliminó una presentación");
				tablaPresentaciones.requestFocus();
			} catch (SQLException e) {
				Main.notificar(e.getMessage());
				e.printStackTrace();
			}
		});
	}
	
	@FXML
	private void onDeseleccionarPresentacion(ActionEvent event) {
		tablaPresentaciones.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void onExistenciasContextMenuShown(WindowEvent event) {
		final Existencia existencia = tablaExistencias.getSelectionModel().getSelectedItem();
		boolean sinSeleccion = existencia == null;
		itemNuevaExistencia.setDisable(tablaPresentaciones.getSelectionModel().getSelectedItem() == null);
		itemEliminarExistencia.setDisable(sinSeleccion || existencia.getCantidad() > 0);
		itemDeseleccionarExistencia.setDisable(sinSeleccion);
	}
	
	@FXML
	private void onNuevaExistencia(ActionEvent event) {
		
	}
	
	@FXML
	private void onEliminarExistencia(ActionEvent event) {
		manager.existencia().ifPresent(dao -> {
			final Existencia existencia = tablaExistencias.getSelectionModel().getSelectedItem();
			dao.delete(existencia);
			existencias.remove(existencia);
			Main.notificar("Se eliminó una existencia");
			tablaExistencias.requestFocus();
		});
	}
	
	@FXML
	private void onDeseleccionarExistencia(ActionEvent event) {
		tablaExistencias.getSelectionModel().clearSelection();
	}

}
