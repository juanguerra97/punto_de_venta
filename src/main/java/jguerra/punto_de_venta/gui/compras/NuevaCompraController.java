package jguerra.punto_de_venta.gui.compras;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Compra;
import jguerra.punto_de_venta.datos.modelo.DetalleCompra;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.modelo.Proveedor;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Main;
import jguerra.punto_de_venta.gui.utils.FiltradorProductos;

public class NuevaCompraController {
	
	@FXML
    private DatePicker pickerFecha;

    @FXML
    private ChoiceBox<Proveedor> choiceProveedor;

    @FXML
    private TableView<DetalleCompra> tablaDetalles;

    @FXML
    private TableColumn<DetalleCompra, String> colProducto;

    @FXML
    private TableColumn<DetalleCompra, String> colMarca;

    @FXML
    private TableColumn<DetalleCompra, String> colPresentacion;

    @FXML
    private TableColumn<DetalleCompra, String> colSucursal;

    @FXML
    private TableColumn<DetalleCompra, BigDecimal> colCosto;

    @FXML
    private TableColumn<DetalleCompra, Integer> colCantidad;

    @FXML
    private MenuItem itemQuitarDetalle;

    @FXML
    private MenuItem itemDeseleccionarDetalle;
    
    @FXML
    private CustomTextField fieldFiltroProductos;

    @FXML
    private Button btnFiltrarProductos;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colMarcaProducto;

    @FXML
    private MenuItem itemDeseleccionarProducto;

    @FXML
    private ListView<Presentacion> listaPresentaciones;

    @FXML
    private MenuItem itemDeseleccionarPresentacion;

    @FXML
    private ListView<Sucursal> listaSucursales;

    @FXML
    private MenuItem itemDeseleccionarExistencia;

    @FXML
    private Spinner<Integer> spinnerCantidad;

    @FXML
    private Button btnAgregar;

    @FXML
    private Label labelTotal;

    @FXML
    private Button btnListo;
    
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;
    
    private DAOManager manager;
    
    private Compra compra = null;
    private BigDecimal total;
    
    private ObservableList<DetalleCompra> detalles;
    
    public void reset() {
    	total = BigDecimal.ZERO;
    	labelTotal.setText("Q.0");
    	pickerFecha.setValue(LocalDate.now());
    	choiceProveedor.getSelectionModel().clearSelection();
    	choiceProveedor.getItems().clear();
    	tablaProductos.getSelectionModel().clearSelection();
    	detalles.clear();
    	spinnerValueFactory.setValue(1);
    	btnListo.setDisable(true);
    	fieldFiltroProductos.clear();
    	choiceProveedor.requestFocus();
    }
    
    public Optional<Compra> getCompra(){
    	if(compra == null)
    		return Optional.empty();
    	Optional<Compra> opt = Optional.of(compra);
    	compra = null;
    	return opt;
    }
    
    public void cargarProveedores() {
    	manager.proveedor().ifPresent(dao -> {
    		choiceProveedor.getItems().setAll(dao.selectAll());
    	});
    }
    
    public void cargarProductos() {
    	manager.producto().ifPresent(dao -> {
    		tablaProductos.getItems().setAll(dao.selectAll());
    	});
    }
    
    private void cargarPresentaciones(final Producto producto) {
    	manager.presentacion().ifPresent(dao -> {
    		listaPresentaciones.getItems()
    			.setAll(dao.selectAllByProducto(producto.getId()));
    	});
    }
    
    private void cargarSucursales(final Presentacion presentacion) {
    	manager.presentacion().ifPresent(dao -> {
    		listaSucursales.getItems()
    			.setAll(dao.selectSucursalesPresentacion(presentacion.getId()));
    	});
    }

    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	detalles = tablaDetalles.getItems();
    	total = BigDecimal.ZERO;
    	
    	tablaDetalles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
    	colMarca.setCellValueFactory(new PropertyValueFactory<>("marcaProducto"));
    	colPresentacion.setCellValueFactory(new PropertyValueFactory<>("presentacionProducto"));
    	colSucursal.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal"));
    	colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
    	colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    	
    	tablaProductos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	colMarcaProducto.setCellValueFactory(new PropertyValueFactory<>("marca"));
    	tablaProductos.getSelectionModel().selectedItemProperty().addListener(
    			(ob,oldSelection,newSelection)->{
    		if(newSelection == null) {
    			listaPresentaciones.getItems().clear();
    		}else {
    			cargarPresentaciones(newSelection);
    		}
    	});
    	
    	listaPresentaciones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	listaPresentaciones.getSelectionModel().selectedItemProperty()
    		.addListener((ob,oldSelection,newSelection)->{
    		if(newSelection == null) {
    			listaSucursales.getItems().clear();
    		}else {
    			cargarSucursales(newSelection);
    		}
    	});
    	
    	listaSucursales.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	listaSucursales.getSelectionModel().selectedItemProperty()
    		.addListener((ob,oldSelection,newSelection)->{
    		boolean sinSeleccion = newSelection == null;
    		spinnerCantidad.setDisable(sinSeleccion);
    		btnAgregar.setDisable(sinSeleccion);
    		spinnerValueFactory.setValue(1);
    	});
    	
    	choiceProveedor.getSelectionModel().selectedItemProperty()
    		.addListener((ob,oldSeleccion,newSeleccion)->{
    		btnListo.setDisable(newSeleccion == null || detalles.size() <= 0);
    	});
    	
    	spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000, 1, 1);
    	spinnerCantidad.setValueFactory(spinnerValueFactory);
    	
    	Fields.setupClearButtonField(fieldFiltroProductos);
    	
    	fieldFiltroProductos.setOnAction(e -> onFiltrarProductos(e));
    	
    	fieldFiltroProductos.textProperty().addListener((ob,oldText,newText)->{
    		btnFiltrarProductos.setDisable(newText.trim().length() < 3);
    		if(newText.trim().isEmpty()) {
    			cargarProductos();
    		}
    	});
    	
    }
    
    @FXML
    private void onListo(ActionEvent event) {
    	manager.compra().ifPresent(dao -> {
    		compra = new Compra(
    				choiceProveedor.getSelectionModel().getSelectedItem().getId(), 
    				pickerFecha.getValue(), total);
    		try {
				int numero = dao.insert(compra, detalles);
				compra.setNumero(numero);
				manager.existencia().ifPresent(daoExistencia -> {
					detalles.forEach(detalle -> {
						 daoExistencia.selectByDetalle(detalle).ifPresent(existencia -> {
							 existencia.setCantidad(existencia.getCantidad() + detalle.getCantidad());
							 daoExistencia.update(existencia);
						 });
					});
				});
				btnListo.getScene().getWindow().hide();
			} catch (SQLException e) {
				compra = null;
				Main.alertError("ERROR", "Ocurrió un error al guardar la compra", e.getMessage());
				e.printStackTrace();
			}
    	});
    }
    
    @FXML
    private void onCancelar(ActionEvent event) {
    	btnListo.getScene().getWindow().hide();
    }
    
    @FXML
    private void onAgregar(ActionEvent event) {
    	
    	final Producto prod = tablaProductos.getSelectionModel().getSelectedItem();
    	final Presentacion pres = listaPresentaciones.getSelectionModel().getSelectedItem();
    	final Sucursal suc = listaSucursales.getSelectionModel().getSelectedItem();
    	
    	final DetalleCompra detalle = new DetalleCompra(prod.getNombre(), prod.getMarca(), pres.getNombre(), suc.getNombre(), pres.getCosto(), spinnerCantidad.getValue());
    	total = total.add(detalle.getCosto().multiply(new BigDecimal(detalle.getCantidad())));
    	
    	int indice = detalles.indexOf(detalle);
    	
    	if(indice >= 0) {
    		detalle.setCantidad(detalle.getCantidad() + detalles.get(indice).getCantidad());
    		detalles.set(indice, detalle);
    	} else {
    		detalles.add(detalle);
    		btnListo.setDisable(detalles.size() <= 0 || 
        			choiceProveedor.getSelectionModel().getSelectedItem() == null);
    	}
    	labelTotal.setText(total.toPlainString());
    	
    	listaSucursales.getSelectionModel().clearSelection();
    	listaSucursales.requestFocus();
    }
    
    @FXML
    private void onDetallesContextMenuShown(WindowEvent event) {
    	boolean sinSeleccionar = tablaDetalles.getSelectionModel()
    			.getSelectedItem() == null;
    	itemQuitarDetalle.setDisable(sinSeleccionar);
    	itemDeseleccionarDetalle.setDisable(sinSeleccionar);
    }
    
    @FXML
    private void onQuitarDetalle(ActionEvent event) {
    	final DetalleCompra detalle = tablaDetalles.getSelectionModel().getSelectedItem();
    	total = total.subtract(detalle.getCosto().multiply(new BigDecimal(detalle.getCantidad())));
    	labelTotal.setText(total.toPlainString());
    	tablaDetalles.getSelectionModel().clearSelection();
    	detalles.remove(detalle);
    	btnListo.setDisable(detalles.size() <= 0 || 
    			choiceProveedor.getSelectionModel().getSelectedItem() == null);
    }
    
    @FXML
    private void onDeseleccionarDetalle(ActionEvent event) {
    	tablaDetalles.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void onFiltrarProductos(ActionEvent event) {
    	if(btnFiltrarProductos.isDisable())
    		return;
    	tablaProductos.getItems().setAll(
    			FiltradorProductos.filtrar(fieldFiltroProductos.getText().trim()));
    }
    
    @FXML
    private void onProductosContextMenuShown(WindowEvent event) {
    	itemDeseleccionarProducto.setDisable(tablaProductos
    			.getSelectionModel().getSelectedItem() == null);
    }
    
    @FXML
    private void onDeseleccionarProducto(ActionEvent event) {
    	tablaProductos.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void onPresentacionesContextMenuShown(WindowEvent event) {
    	itemDeseleccionarPresentacion.setDisable(listaPresentaciones
    			.getSelectionModel().getSelectedItem() == null);
    }
    
    @FXML
    private void onDeseleccionarPresentacion(ActionEvent event) {
    	listaPresentaciones.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void onSucursalesContextMenuShown(WindowEvent event) {
    	itemDeseleccionarExistencia.setDisable(listaSucursales
    			.getSelectionModel().getSelectedItem() == null);
    }
    
    @FXML
    private void onDeseleccionarSucursal(ActionEvent event) {
    	listaSucursales.getSelectionModel().clearSelection();
    }
    
}
