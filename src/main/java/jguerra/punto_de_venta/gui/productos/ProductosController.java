package jguerra.punto_de_venta.gui.productos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.controlsfx.control.table.TableRowExpanderColumn;
import org.controlsfx.control.textfield.TextFields;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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
    
    private Stage windowNuevaPresentacion;
    private NuevaPresentacionController controllerNuevaPresentacion;
    
    private Stage windowNuevaExistencia;
    private NuevaExistenciaController controllerNuevaExistencia;
    
    private TableRowExpanderColumn.TableRowDataFeatures<Producto> param = null;
    
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
    
    private GridPane createEditorProducto(TableRowExpanderColumn.TableRowDataFeatures<Producto> param) {
        
    	param.expandedProperty().addListener((ob,viejo,nuevo)->{
    		if(!nuevo) {
    			ProductosController.this.param = null;
    		}
    	});
    	
    	if(ProductosController.this.param != null)
    		if(ProductosController.this.param.isExpanded())
    			ProductosController.this.param.toggleExpanded();
    	
    	tablaProductos.requestFocus();
    	tablaProductos.getSelectionModel().clearSelection();
    	ProductosController.this.param = param;
    	
    	GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        editor.setHgap(10);
        editor.setVgap(5);

        Producto producto = param.getValue();
        tablaProductos.getSelectionModel().select(producto);

        TextField fieldNombre = TextFields.createClearableTextField(); 
        TextField fieldMarca = TextFields.createClearableTextField();
        
        fieldNombre.setText(producto.getNombre());
        fieldMarca.setText(producto.getMarca());

        editor.addRow(0, new Label("Nombre"), fieldNombre);
        editor.addRow(1, new Label("marca"), fieldMarca);

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(event -> {
        	final Producto actualizado = new Producto(producto.getId(),
        			fieldNombre.getText().trim().toUpperCase(),
        			fieldMarca.getText().trim().toUpperCase());
        	manager.producto().ifPresent(dao -> {
        		try {
					dao.update(actualizado);
					producto.setNombre(actualizado.getNombre());
					producto.setMarca(actualizado.getMarca());
					Main.notificar("Se actualizó un producto");
					tablaProductos.requestFocus();
					param.toggleExpanded();
				} catch (SQLException e) {
					Main.notificar(e.getMessage());
					fieldNombre.selectAll();
					fieldNombre.requestFocus();
					e.printStackTrace();
				}
        	});            
        });
        btnActualizar.setDisable(true);
        
        fieldNombre.textProperty().addListener((ob,oldText,newText)->{
        	btnActualizar.setDisable(newText.trim().isEmpty() ||
        			fieldMarca.getText().trim().isEmpty());
        });
        
        fieldMarca.textProperty().addListener((ob,oldText,newText)->{
        	btnActualizar.setDisable(newText.trim().isEmpty() ||
        			fieldNombre.getText().trim().isEmpty());
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(event -> {
        	tablaProductos.requestFocus();
        	param.toggleExpanded();
        });

        editor.addRow(2, btnActualizar, btnCancelar);

        return editor;
    }
	
	@SuppressWarnings("unchecked")
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
		
		TableRowExpanderColumn<Producto> expanderColumn = new TableRowExpanderColumn<>(this::createEditorProducto);
		expanderColumn.setMinWidth(25);
		expanderColumn.setMaxWidth(25);
		tablaProductos.getColumns().setAll(expanderColumn,colIdProducto,colNombreProducto,colMarcaProducto);
		
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
			
			FXMLLoader loaderNuevaPresentacion = new FXMLLoader(getClass().getResource("/fxml/productos/NuevaPresentacion.fxml"));
			Scene sceneNuevaPresentacion = new Scene(loaderNuevaPresentacion.load(),380,220);
			controllerNuevaPresentacion = loaderNuevaPresentacion.getController();
			windowNuevaPresentacion = new Stage();
			windowNuevaPresentacion.setScene(sceneNuevaPresentacion);
			windowNuevaPresentacion.setTitle("Nueva presentación");
			windowNuevaPresentacion.setMinWidth(380);
			windowNuevaPresentacion.setMinHeight(220);
			windowNuevaPresentacion.initModality(Modality.APPLICATION_MODAL);
			
			FXMLLoader loaderNuevaExistencia = new FXMLLoader(getClass().getResource("/fxml/productos/NuevaExistencia.fxml"));
			Scene sceneNuevaExistencia = new Scene(loaderNuevaExistencia.load(),380,200);
			controllerNuevaExistencia = loaderNuevaExistencia.getController();
			windowNuevaExistencia = new Stage();
			windowNuevaExistencia.setScene(sceneNuevaExistencia);
			windowNuevaExistencia.setTitle("Nueva existencia");
			windowNuevaExistencia.setMinWidth(380);
			windowNuevaExistencia.setMinHeight(200);
			windowNuevaExistencia.initModality(Modality.APPLICATION_MODAL);
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
