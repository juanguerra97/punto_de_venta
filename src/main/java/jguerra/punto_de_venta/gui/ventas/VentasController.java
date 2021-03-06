package jguerra.punto_de_venta.gui.ventas;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Cliente;
import jguerra.punto_de_venta.datos.modelo.DetalleVenta;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.datos.modelo.Venta;
import jguerra.punto_de_venta.gui.Icono;
import jguerra.punto_de_venta.gui.Main;

public class VentasController {
	
	@FXML
    private CheckBox checkFiltrar;

    @FXML
    private DatePicker pickerFecha;

    @FXML
    private TableView<Venta> tablaVentas;

    @FXML
    private TableColumn<Venta, Integer> colNumero;

    @FXML
    private TableColumn<Venta, Cliente> colCliente;

    @FXML
    private TableColumn<Venta, Sucursal> colSucursal;

    @FXML
    private TableColumn<Venta, LocalDate> colFecha;

    @FXML
    private TableColumn<Venta, BigDecimal> colTotal;
    
    @FXML
    private MenuItem itemNuevaVenta;

    @FXML
    private MenuItem itemDeseleccionarVenta;
    

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
    private MenuItem itemDeseleccionarDetalle;
    
    private DAOManager manager;
	
	private ObservableList<Venta> ventas;
	private ObservableList<DetalleVenta> detalles;
	
	private Stage windowNuevaVenta;
	private NuevaVentaController controllerNuevaVenta;
	
	private void cargarVentas(final LocalDate fecha) {
		manager.venta().ifPresent(dao -> {
			if(fecha == null)
				ventas.setAll(dao.selectAll());
			else
				ventas.setAll(dao.selectAllByFecha(fecha));
		});
	}
	
	private void cargarDetallesVenta(final Venta venta) {
		assert venta != null;
		manager.detalleVenta().ifPresent(dao -> {
			detalles.setAll(dao.selectAllByVenta(venta.getNumero()));
		});
	}
	
	@FXML
	private void initialize() {
		
		manager = DAOManager.instance();
		
		ventas = tablaVentas.getItems();
		detalles = tablaDetalles.getItems();
		
		tablaVentas.getSelectionModel()
			.setSelectionMode(SelectionMode.SINGLE);
		colNumero.setCellValueFactory(
				new PropertyValueFactory<>("numero"));
		colCliente.setCellValueFactory(
				new PropertyValueFactory<>("cliente"));
		colSucursal.setCellValueFactory(
				new PropertyValueFactory<>("sucursal"));
		colFecha.setCellValueFactory(
				new PropertyValueFactory<>("fecha"));
		colTotal.setCellValueFactory(
				new PropertyValueFactory<>("total"));
		tablaVentas.getSelectionModel().selectedItemProperty()
		.addListener((o,oldVentaSelected,newVentaSelected)->{
			if(newVentaSelected != null) {
				cargarDetallesVenta(newVentaSelected);
			}else {
				detalles.clear();
			}
		});
		
		tablaDetalles.getSelectionModel()
			.setSelectionMode(SelectionMode.SINGLE);
		colProducto.setCellValueFactory(
				new PropertyValueFactory<>("nombreProducto"));
		colMarca.setCellValueFactory(
				new PropertyValueFactory<>("marcaProducto"));
		colPresentacion.setCellValueFactory(
				new PropertyValueFactory<>("presentacionProducto"));
		colCosto.setCellValueFactory(
				new PropertyValueFactory<>("costo"));
		colPrecio.setCellValueFactory(
				new PropertyValueFactory<>("precio"));
		colCantidad.setCellValueFactory(
				new PropertyValueFactory<>("cantidad"));
		
		pickerFecha.setValue(LocalDate.now());
		
		checkFiltrar.selectedProperty().addListener((o,oldValue,newValue)->{
			pickerFecha.setDisable(!newValue);
			if(newValue) {
				cargarVentas(pickerFecha.getValue());
			}else
				cargarVentas(null);
		});
		
		pickerFecha.valueProperty().addListener((o,oldValue,newValue)->{
			cargarVentas(newValue);
		});
		
		try {
			FXMLLoader loaderNuevaVenta = new FXMLLoader(getClass()
					.getResource("/fxml/ventas/NuevaVenta.fxml"));
			Scene sceneNuevaVenta = new Scene(loaderNuevaVenta.load(),750,450);
			controllerNuevaVenta = loaderNuevaVenta.getController();
			windowNuevaVenta = new Stage();
			windowNuevaVenta.setScene(sceneNuevaVenta);
			windowNuevaVenta.setTitle("Nueva Venta");
			windowNuevaVenta.setMinWidth(750);
			windowNuevaVenta.setMinHeight(450);
			windowNuevaVenta.initModality(Modality.APPLICATION_MODAL);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		itemNuevaVenta.setGraphic(Icono.add16());
		itemDeseleccionarVenta.setGraphic(Icono.remove16());
		itemDeseleccionarDetalle.setGraphic(Icono.remove16());
		
		cargarVentas(null);
		
	}
	
	@FXML
	private void onTablaVentasContextMenuShown(WindowEvent event) {
		itemDeseleccionarVenta.setDisable(
				tablaVentas.getSelectionModel().getSelectedItem() == null);
	}
	
	@FXML
	private void onNuevaVenta(ActionEvent event) {
		controllerNuevaVenta.reset();
		controllerNuevaVenta.cargarClientes();
		controllerNuevaVenta.cargarSucursales();
		controllerNuevaVenta.cargarProductos();
		windowNuevaVenta.showAndWait();
		controllerNuevaVenta.getVenta().ifPresent(venta -> {
			if(!checkFiltrar.isSelected() || 
					pickerFecha.getValue().equals(venta.getFecha()))
				ventas.add(venta);
			Main.notificar("Se ingresó una nueva venta");
		});
	}
	
	@FXML
	private void onDeseleccionarVenta(ActionEvent event) {
		tablaVentas.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void onTablaDetallesContextMenuShown(WindowEvent event) {
		itemDeseleccionarDetalle.setDisable(
				tablaDetalles.getSelectionModel().getSelectedItem() == null);
	}
	
	@FXML
	private void onDeseleccionarDetalle(ActionEvent event) {
		tablaDetalles.getSelectionModel().clearSelection();
	}

}
