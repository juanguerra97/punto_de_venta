package jguerra.punto_de_venta.gui.ventas;

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
import jguerra.punto_de_venta.datos.modelo.Cliente;
import jguerra.punto_de_venta.datos.modelo.DetalleVenta;
import jguerra.punto_de_venta.datos.modelo.Existencia;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.datos.modelo.Venta;
import jguerra.punto_de_venta.datos.validacion.Validacion;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Icono;
import jguerra.punto_de_venta.gui.Main;
import jguerra.punto_de_venta.gui.utils.FiltradorProductos;

public class NuevaVentaController {
	
	@FXML
	private DatePicker pickerFecha;

	@FXML
	private ChoiceBox<Sucursal> choiceSucursal;
	
	@FXML
    private CustomTextField fieldNit;

	@FXML
	private ChoiceBox<Cliente> choiceCliente;

	@FXML
	private TableView<DetalleVenta> tablaDetalles;

	@FXML
	private TableColumn<DetalleVenta, String> colProducto;

	@FXML
	private TableColumn<DetalleVenta, String> colMarca;

	@FXML
	private TableColumn<DetalleVenta, String> colPresentacion;

	@FXML
	private TableColumn<DetalleVenta, BigDecimal> colCosto;

	@FXML
	private TableColumn<DetalleVenta, BigDecimal> colPrecio;

	@FXML
	private TableColumn<DetalleVenta, Integer> colCantidad;

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
	private Spinner<Integer> spinnerCantidad;

	@FXML
	private Button btnAgregar;

	@FXML
	private Label labelTotal;

	@FXML
	private Button btnListo;
	
	private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;
    
    private DAOManager manager;
    
    private Venta venta = null;
    private BigDecimal total;
    
    private ObservableList<DetalleVenta> detalles;
    
    public void reset() {
    	total = BigDecimal.ZERO;
    	labelTotal.setText("Q.0");
    	pickerFecha.setValue(LocalDate.now());
    	choiceSucursal.getSelectionModel().clearSelection();
    	choiceSucursal.getItems().clear();
    	choiceCliente.getSelectionModel().clearSelection();
    	choiceCliente.getItems().clear();
    	tablaProductos.getSelectionModel().clearSelection();
    	tablaProductos.setDisable(true);
		listaPresentaciones.setDisable(true);
    	detalles.clear();
    	spinnerValueFactory.setValue(1);
    	btnListo.setDisable(true);
    	fieldFiltroProductos.clear();
    	choiceSucursal.requestFocus();
    }
    
    public Optional<Venta> getVenta(){
    	if(venta == null)
    		return Optional.empty();
    	Optional<Venta> opt = Optional.of(venta);
    	venta = null;
    	return opt;
    }
    
    public void cargarClientes() {
    	manager.cliente().ifPresent(dao -> {
    		choiceCliente.getItems().setAll(dao.selectAll());
    	});
    }
    
    public void cargarSucursales() {
    	manager.sucursal().ifPresent(dao -> {
    		choiceSucursal.getItems().setAll(dao.selectAll());
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

    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	detalles = tablaDetalles.getItems();
    	total = BigDecimal.ZERO;
    	
    	tablaDetalles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
    	colMarca.setCellValueFactory(new PropertyValueFactory<>("marcaProducto"));
    	colPresentacion.setCellValueFactory(new PropertyValueFactory<>("presentacionProducto"));
    	colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
    	colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
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
    		boolean sinSeleccion = newSelection == null;
    		spinnerCantidad.setDisable(sinSeleccion);
    		btnAgregar.setDisable(sinSeleccion);
    		if(!sinSeleccion) {
    			final Sucursal sucursal = choiceSucursal.getValue();
    			final Producto prod = tablaProductos.getSelectionModel().getSelectedItem();
    	    	final Presentacion pres = newSelection;
    	    	
    	    	final DetalleVenta detalle = new DetalleVenta(prod.getNombre(), prod.getMarca(), 
    	    			pres.getNombre(), pres.getCosto(), pres.getPrecio(),spinnerCantidad.getValue());
    	    	manager.existencia().ifPresent(dao -> {
    	    		Existencia exis = dao.selectByDetalle(detalle, sucursal).orElse(null);
    	    		boolean exisIsNull = exis == null;
    	    		boolean agotado = false;
    	    		if(!exisIsNull) {
    	    			int max = exis.getCantidad();
    	    			int indice = detalles.indexOf(detalle);
    	    			if(indice >= 0) {
    	    				max -= detalles.get(indice).getCantidad();
    	    			}
    	    			agotado = max <= 0;
    	    			spinnerValueFactory.setMax(max);
    	    		}
    	    		spinnerCantidad.setDisable(exisIsNull || agotado);
    	    		btnAgregar.setDisable(exisIsNull || agotado);
    	    	});;
    		}
    		spinnerValueFactory.setValue(1);
    	});
    	
    	choiceCliente.getSelectionModel().selectedItemProperty()
    		.addListener((ob,oldSeleccion,newSeleccion)->{
    		if(newSeleccion != null) {
    			if(!fieldNit.getText().trim().equalsIgnoreCase(newSeleccion.getNit()))
    				fieldNit.setText(newSeleccion.getNit());
    		}
    		btnListo.setDisable(newSeleccion == null || detalles.size() <= 0
    				|| choiceSucursal.getSelectionModel().getSelectedItem() == null);
    	});
    	
    	choiceSucursal.getSelectionModel().selectedItemProperty()
    		.addListener((ob,oldSelection,newSelection)->{
    		choiceSucursal.setDisable(newSelection != null);
    		tablaProductos.setDisable(newSelection == null);
    		listaPresentaciones.setDisable(newSelection == null);
    		fieldFiltroProductos.setDisable(newSelection == null);
    	});
    	
    	spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000, 1, 1);
    	spinnerCantidad.setValueFactory(spinnerValueFactory);
    	
    	Fields.setupClearButtonField(fieldNit);
    	
    	fieldNit.textProperty().addListener((ob,oldText,newText)->{
    		boolean nitValido = Validacion.validarNit(newText.trim());
    		boolean existeCliente = true;
    		final Cliente c = choiceCliente.getValue();
    		if(c == null || !c.getNit().equalsIgnoreCase(newText.trim())) {
    			if(nitValido) {
        			Optional<Cliente> opt = choiceCliente.getItems().stream()
        				.filter(cli -> newText.equals(cli.getNit())).findFirst();
        			existeCliente = opt.isPresent();
        			opt.ifPresent(cliente -> choiceCliente.getSelectionModel()
        				.select(cliente));
        		}
        		if(!nitValido || !existeCliente)
        			choiceCliente.getSelectionModel().clearSelection();
    		}
    		
    		btnListo.setDisable(!nitValido || !existeCliente || detalles.size() <= 0
    				|| choiceSucursal.getSelectionModel().getSelectedItem() == null);
    	});
    	
    	Fields.setupClearButtonField(fieldFiltroProductos);
    	
    	fieldFiltroProductos.setOnAction(e -> onFiltrarProductos(e));
    	
    	fieldFiltroProductos.textProperty().addListener((ob,oldText,newText)->{
    		btnFiltrarProductos.setDisable(newText.trim().length() < 3);
    		if(newText.trim().isEmpty())
    			cargarProductos();
    	});
    	
    	itemQuitarDetalle.setGraphic(Icono.clear16());
    	itemDeseleccionarDetalle.setGraphic(Icono.remove16());
    	itemDeseleccionarProducto.setGraphic(Icono.remove16());
    	itemDeseleccionarPresentacion.setGraphic(Icono.remove16());
    	
    }
    
    @FXML
    private void onListo(ActionEvent event) {
    	manager.venta().ifPresent(dao -> {
    		final Sucursal sucursal = choiceSucursal.getValue();
    		venta = new Venta(choiceCliente.getValue().getNit(),
    				sucursal.getId(), pickerFecha.getValue(), total);
    		try {
				int numero = dao.insert(venta, detalles);
				venta.setNumero(numero);
				manager.existencia().ifPresent(daoExistencia -> {
					detalles.forEach(detalle -> {
						 daoExistencia.selectByDetalle(detalle,sucursal).ifPresent(existencia -> {
							 existencia.setCantidad(existencia.getCantidad() - detalle.getCantidad());
							 daoExistencia.update(existencia);
						 });
					});
				});
				btnListo.getScene().getWindow().hide();
			} catch (SQLException e) {
				venta = null;
				Main.alertError("ERROR", "Ocurrió un error al guardar la venta", e.getMessage());
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
    	
    	final DetalleVenta detalle = new DetalleVenta(prod.getNombre(), prod.getMarca(), 
    			pres.getNombre(), pres.getCosto(), pres.getPrecio(),spinnerCantidad.getValue());
    	total = total.add(detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
    	
    	int indice = detalles.indexOf(detalle);
    	
    	if(indice >= 0) {
    		detalle.setCantidad(detalle.getCantidad() + detalles.get(indice).getCantidad());
    		detalles.set(indice, detalle);
    	} else {
    		detalles.add(detalle);
    		btnListo.setDisable(detalles.size() <= 0 || choiceCliente.getValue() == null);
    	}
    	labelTotal.setText(total.toPlainString());
    	
    	listaPresentaciones.getSelectionModel().clearSelection();
    	listaPresentaciones.requestFocus();
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
    	final DetalleVenta detalle = tablaDetalles.getSelectionModel().getSelectedItem();
    	total = total.subtract(detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
    	labelTotal.setText(total.toPlainString());
    	tablaDetalles.getSelectionModel().clearSelection();
    	detalles.remove(detalle);
    	btnListo.setDisable(detalles.size() <= 0 || 
    			choiceSucursal.getSelectionModel().getSelectedItem() == null ||
    			choiceCliente.getSelectionModel().getSelectedItem() == null);
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

}
