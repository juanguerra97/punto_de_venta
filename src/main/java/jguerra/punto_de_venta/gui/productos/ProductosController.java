package jguerra.punto_de_venta.gui.productos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.controlsfx.control.table.TableRowExpanderColumn;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Existencia;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.gui.Icono;
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
    private MenuItem itemNuevoProducto;
    
    @FXML
    private MenuItem itemEliminarProducto;

    @FXML
    private MenuItem itemDeseleccionarProducto;
    
    @FXML
    private CheckMenuItem checkItemFiltrar;
    
    @FXML
    private ListView<String> listaMarcas;
    
    @FXML
    private MenuItem itemDeseleccionarMarca;

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
    private TableColumn<Existencia, Sucursal> colSucursalExistencia;

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
    private ObservableList<String> marcas;
    private ObservableList<Presentacion> presentaciones;
    private ObservableList<Existencia> existencias;
    
    private Stage windowNuevoProducto;
    private NuevoProductoController controllerNuevoProducto;
    
    private Stage windowNuevaPresentacion;
    private NuevaPresentacionController controllerNuevaPresentacion;
    
    private Stage windowNuevaExistencia;
    private NuevaExistenciaController controllerNuevaExistencia;
    
    private EditorProducto editorProducto;
    private TableRowExpanderColumn.TableRowDataFeatures<Producto> expanderColProducto = null;
    
    private EditorPresentacion editorPresentacion;
    private TableRowExpanderColumn.TableRowDataFeatures<Presentacion> expanderColPresentacion = null;
    
    public void updateExistencias() {
    	if(tablaProductos.getSelectionModel().getSelectedItem() != null) {
    		final Presentacion presentacion = tablaPresentaciones
    				.getSelectionModel().getSelectedItem();
    		if(presentacion != null) {
    			existencias.clear();
    			cargarExistencias(presentacion);
    		}
    	}
    }
    
    private void cargarProductos() {
    	manager.producto().ifPresent(dao -> {
    		productos.setAll(dao.selectAll());
    	});
    }
    
    private void cargarProductos(final String marca) {
    	assert marca != null;
    	manager.producto().ifPresent(dao -> {
    		productos.setAll(dao.selectAllByMarca(marca));
    	});
    }
    
    private void cargarMarcas() {
    	manager.producto().ifPresent(dao -> {
    		marcas.setAll(dao.selectAllMarcas());
    	});
    }
    
    private void cargarPresentaciones(final Producto producto) {
    	assert producto != null;
    	manager.presentacion().ifPresent(dao -> {
    		presentaciones.setAll(dao.selectAllByProducto(producto));
    	});
    }
    
    private void cargarExistencias(final Presentacion presentacion) {
    	assert presentacion != null;
    	manager.existencia().ifPresent(dao -> {
    		existencias.setAll(dao.selectAllByPresentacion(presentacion));
    	});
    }
    
    private GridPane createEditorProducto(TableRowExpanderColumn
    		.TableRowDataFeatures<Producto> expanderCol) {
        
    	expanderCol.expandedProperty()
    	.addListener((o,oldExpanded,newExpanded)->{
    		if(!newExpanded) {
    			expanderColProducto = null;
    		}
    	});
    	
    	if(expanderColProducto != null) {
    		if(expanderColProducto.isExpanded()) {
    			expanderColProducto.toggleExpanded();
    		}
    	}
    	
    	tablaProductos.requestFocus();
    	tablaProductos.getSelectionModel().clearSelection();
    	expanderColProducto = expanderCol;

        final Producto producto = expanderCol.getValue();
        tablaProductos.getSelectionModel().select(producto);
        editorProducto.setProducto(producto);
        
        return editorProducto;
    }
    
    private GridPane createEditorPresentacion(TableRowExpanderColumn
    		.TableRowDataFeatures<Presentacion> expanderCol) {
    	
    	expanderCol.expandedProperty()
    	.addListener((o,oldExpanded,newExpanded)->{
    		if(!newExpanded) {
    			expanderColPresentacion = null;
    		}
    	});
    	
    	if(expanderColPresentacion != null) {
    		if(expanderColPresentacion.isExpanded()) {
    			expanderColPresentacion.toggleExpanded();
    		}
    	}
    	
    	tablaPresentaciones.requestFocus();
    	tablaPresentaciones.getSelectionModel().clearSelection();
    	expanderColPresentacion = expanderCol;
    	
    	final Presentacion pres = expanderCol.getValue();
    	tablaPresentaciones.getSelectionModel().select(pres);
    	editorPresentacion.setPresentacion(pres);
    	
    	return editorPresentacion;
    }
	
	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() {
		
		manager = DAOManager.instance();
		productos = tablaProductos.getItems();
		marcas = listaMarcas.getItems();
		presentaciones = tablaPresentaciones.getItems();
		existencias = tablaExistencias.getItems();
		
		tablaProductos.getSelectionModel()
			.setSelectionMode(SelectionMode.SINGLE);
		colIdProducto.setCellValueFactory(
				new PropertyValueFactory<Producto,Integer>("id"));
		colNombreProducto.setCellValueFactory(
				new PropertyValueFactory<Producto,String>("nombre"));
		colMarcaProducto.setCellValueFactory(
				new PropertyValueFactory<Producto,String>("marca"));
		
		editorProducto = new EditorProducto(event -> {
			final Producto producto = expanderColProducto.getValue();
			final Producto actualizado = new Producto(producto.getId(), 
					editorProducto.getNombre().trim().toUpperCase(),
					editorProducto.getMarca().trim().toUpperCase());
			manager.producto().ifPresent(dao -> {
				try {
					dao.update(actualizado);
					producto.setNombre(actualizado.getNombre());
					producto.setMarca(actualizado.getMarca());
					Main.notificar("Se actualizó un producto");
					if (checkItemFiltrar.isSelected()) {
						String marca = listaMarcas.getSelectionModel()
								.getSelectedItem();
						if (marca != null && !producto.getMarca().equals(marca))
							productos.remove(producto);
					}
					tablaProductos.requestFocus();
					expanderColProducto.toggleExpanded();
				} catch (SQLException ex) {
					Main.notificar(ex.getMessage());
					editorProducto.focusNombre();
					ex.printStackTrace();
				}
			});
		}, e -> {
			tablaProductos.requestFocus();
			expanderColProducto.toggleExpanded();
		});
		
		TableRowExpanderColumn<Producto> expColProducto = 
				new TableRowExpanderColumn<>(this::createEditorProducto);
		expColProducto.setMinWidth(25);
		expColProducto.setMaxWidth(25);
		tablaProductos.getColumns().setAll(expColProducto,
				colIdProducto,colNombreProducto,colMarcaProducto);
		
		tablaPresentaciones.getSelectionModel()
			.setSelectionMode(SelectionMode.SINGLE);
		colNombrePresentacion.setCellValueFactory(
				new PropertyValueFactory<Presentacion,String>("nombre"));
		colPrecioPresentacion.setCellValueFactory(
				new PropertyValueFactory<Presentacion,BigDecimal>("precio"));
		colCostoPresentacion.setCellValueFactory(
				new PropertyValueFactory<Presentacion,BigDecimal>("costo"));
		
		editorPresentacion = new EditorPresentacion(e -> {
			final Presentacion pres = expanderColPresentacion.getValue();
			final Presentacion actualizada = new Presentacion(pres.getId(), 
					pres.getProducto(),
					editorPresentacion.getNombre().trim().toUpperCase(),
					new BigDecimal(editorPresentacion.getPrecio().trim()),
					new BigDecimal(editorPresentacion.getCosto().trim()));
			manager.presentacion().ifPresent(dao -> {
				try {
					dao.update(actualizada);
					pres.setNombre(actualizada.getNombre());
					pres.setCosto(actualizada.getCosto());
					pres.setPrecio(actualizada.getPrecio());
					Main.notificar("Presentación actualizada");
					tablaPresentaciones.requestFocus();
					expanderColPresentacion.toggleExpanded();
				} catch (SQLException ex) {
					Main.notificar(ex.getMessage());
					editorPresentacion.focusNombre();
					ex.printStackTrace();
				}
			});
		}, e -> {
			tablaPresentaciones.requestFocus();
			expanderColPresentacion.toggleExpanded();
		});
		
		TableRowExpanderColumn<Presentacion> expColPresentacion = 
				new TableRowExpanderColumn<>(this::createEditorPresentacion);
		expColPresentacion.setMinWidth(25);
		expColPresentacion.setMaxWidth(25);
		tablaPresentaciones.getColumns().setAll(expColPresentacion,
				colNombrePresentacion,colCostoPresentacion,
				colPrecioPresentacion);
		
		tablaExistencias.getSelectionModel().setSelectionMode(
				SelectionMode.SINGLE);
		colSucursalExistencia.setCellValueFactory(
				new PropertyValueFactory<>("sucursal"));
		colCantidadExistencia.setCellValueFactory(
				new PropertyValueFactory<>("cantidad"));
		
		tablaProductos.getSelectionModel().selectedItemProperty()
		.addListener((o,oldProdSelected,newProdSelected)->{
			if(newProdSelected != null) {
				cargarPresentaciones(newProdSelected);
			}else {
				presentaciones.clear();
			}
		});
		
		listaMarcas.getSelectionModel().selectedItemProperty()
		.addListener((o,oldMarcaSelected,newMarcaSelected)->{
			boolean hayFiltro = checkItemFiltrar.isSelected();
			if(hayFiltro) {
				if(newMarcaSelected == null) {
					productos.clear();
				}else {
					cargarProductos(newMarcaSelected);
				}
			}
		});
		
		tablaPresentaciones.getSelectionModel().selectedItemProperty()
		.addListener((o,oldPresSelected,newPresSelected)->{
			if(newPresSelected != null) {
				cargarExistencias(newPresSelected);
			}else {
				existencias.clear();
			}
		});
		
		checkItemFiltrar.selectedProperty()
		.addListener((o,oldFiltroSelected,newFiltroSelected)->{
			if(newFiltroSelected) {
				final String marca = listaMarcas.getSelectionModel()
						.getSelectedItem();
				if(marca != null) {
					cargarProductos(marca);
				}else {
					productos.clear();
				}
			} else {
				cargarProductos();
			}
		});
		
		try {
			FXMLLoader loaderNuevoProducto = new FXMLLoader(getClass()
					.getResource("/fxml/productos/NuevoProducto.fxml"));
			Scene sceneNuevoProducto = 
					new Scene(loaderNuevoProducto.load(),380,200);
			controllerNuevoProducto = loaderNuevoProducto.getController();
			windowNuevoProducto = new Stage();
			windowNuevoProducto.setScene(sceneNuevoProducto);
			windowNuevoProducto.setTitle("Nuevo producto");
			windowNuevoProducto.getIcons().add(Icono.new128());
			windowNuevoProducto.setMinWidth(380);
			windowNuevoProducto.setMinHeight(200);
			windowNuevoProducto.initModality(Modality.APPLICATION_MODAL);
			
			FXMLLoader loaderNuevaPresentacion = new FXMLLoader(getClass()
					.getResource("/fxml/productos/NuevaPresentacion.fxml"));
			Scene sceneNuevaPresentacion = 
					new Scene(loaderNuevaPresentacion.load(),380,220);
			controllerNuevaPresentacion = loaderNuevaPresentacion.getController();
			windowNuevaPresentacion = new Stage();
			windowNuevaPresentacion.setScene(sceneNuevaPresentacion);
			windowNuevaPresentacion.setTitle("Nueva presentación");
			windowNuevaPresentacion.getIcons().add(Icono.new128());
			windowNuevaPresentacion.setMinWidth(380);
			windowNuevaPresentacion.setMinHeight(220);
			windowNuevaPresentacion.initModality(Modality.APPLICATION_MODAL);
			
			FXMLLoader loaderNuevaExistencia = new FXMLLoader(getClass()
					.getResource("/fxml/productos/NuevaExistencia.fxml"));
			Scene sceneNuevaExistencia = 
					new Scene(loaderNuevaExistencia.load(),380,200);
			controllerNuevaExistencia = loaderNuevaExistencia.getController();
			windowNuevaExistencia = new Stage();
			windowNuevaExistencia.setScene(sceneNuevaExistencia);
			windowNuevaExistencia.setTitle("Nueva existencia");
			windowNuevaExistencia.getIcons().add(Icono.new128());
			windowNuevaExistencia.setMinWidth(380);
			windowNuevaExistencia.setMinHeight(200);
			windowNuevaExistencia.initModality(Modality.APPLICATION_MODAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		itemNuevoProducto.setGraphic(Icono.add16());
		itemEliminarProducto.setGraphic(Icono.clear16());
		itemDeseleccionarProducto.setGraphic(Icono.remove16());
		checkItemFiltrar.setGraphic(Icono.filter12());
		
		itemDeseleccionarMarca.setGraphic(Icono.remove16());
		
		itemNuevaPresentacion.setGraphic(Icono.add16());
		itemEliminarPresentacion.setGraphic(Icono.clear16());
		itemDeseleccionarPresentacion.setGraphic(Icono.remove16());
		
		itemNuevaExistencia.setGraphic(Icono.add16());
		itemEliminarExistencia.setGraphic(Icono.clear16());
		itemDeseleccionarExistencia.setGraphic(Icono.remove16());
		
		cargarProductos();
		cargarMarcas();
		
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
			final String marca = listaMarcas.getSelectionModel().getSelectedItem();
			if(!checkItemFiltrar.isSelected() || 
					(marca != null && marca.equals(producto.getMarca())))
				productos.add(producto);
			if(!marcas.contains(producto.getMarca()))
				marcas.add(producto.getMarca());
			//productos.sort((prod1,prod2)->Integer.compare(prod1.getId(), prod2.getId()));
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
	private void onMarcasContextMenuShown(WindowEvent event) {
		itemDeseleccionarMarca.setDisable(
				listaMarcas.getSelectionModel().getSelectedItem() == null);
	}
	
	@FXML
	private void onDeseleccionarMarca(ActionEvent event) {
		listaMarcas.getSelectionModel().clearSelection();
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
		controllerNuevaPresentacion.reset();
		controllerNuevaPresentacion.setProducto(tablaProductos.getSelectionModel().getSelectedItem());
		windowNuevaPresentacion.showAndWait();
		controllerNuevaPresentacion.getPresentacion().ifPresent(presentacion -> {
			presentaciones.add(presentacion);
			Main.notificar("Se agregó una nueva presentación");
		});
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
		controllerNuevaExistencia.reset();
		controllerNuevaExistencia.cargarSucursales();
		controllerNuevaExistencia.setPresentacion(tablaPresentaciones.getSelectionModel().getSelectedItem());
		windowNuevaExistencia.showAndWait();
		controllerNuevaExistencia.getExistencia().ifPresent(existencia -> {
			existencias.add(existencia);
			Main.notificar("Se agregó una nueva existencia");
		});
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
