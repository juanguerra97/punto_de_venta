package jguerra.punto_de_venta.gui.compras;

import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Compra;
import jguerra.punto_de_venta.datos.modelo.DetalleCompra;

public class ComprasController {
	
	@FXML
	private CheckBox checkFiltrar;

	@FXML
	private DatePicker pickerFecha;

	@FXML
	private TableView<Compra> tablaCompras;

	@FXML
	private TableColumn<Compra, Integer> colNumero;

	@FXML
	private TableColumn<Compra, Integer> colProveedor;

	@FXML
	private TableColumn<Compra, LocalDate> colFecha;

	@FXML
	private TableColumn<Compra, BigDecimal> colTotal;

	@FXML
	private MenuItem itemNuevaCompra;

	@FXML
	private MenuItem itemDeseleccionarCompra;

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
	private MenuItem itemDeseleccionarDetalle;
	
	private DAOManager manager;
	
	private ObservableList<Compra> compras;
	private ObservableList<DetalleCompra> detalles;
	
	private void cargarCompras(final LocalDate fecha) {
		manager.compra().ifPresent(dao -> {
			if(fecha == null)
				compras.setAll(dao.selectAll());
			else
				compras.setAll(dao.selectAllByFecha(fecha));
		});
	}
	
	private void cargarDetallesCompra(final Compra compra) {
		assert compra != null;
		manager.detalleCompra().ifPresent(dao -> {
			detalles.setAll(dao.selectAllByCompra(compra.getNumero()));
		});
	}
	
	@FXML
	private void initialize() {
		
		manager = DAOManager.instance();
		
		compras = tablaCompras.getItems();
		detalles = tablaDetalles.getItems();
		
		tablaCompras.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		colProveedor.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		tablaCompras.getSelectionModel().selectedItemProperty().addListener(
				(ob,oldSelection,newSelection)->{
			if(newSelection != null)
				cargarDetallesCompra(newSelection);
			else
				detalles.clear();
		});
		
		tablaDetalles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
		colMarca.setCellValueFactory(new PropertyValueFactory<>("marcaProducto"));
		colPresentacion.setCellValueFactory(new PropertyValueFactory<>("presentacionProducto"));
		colSucursal.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal"));
		colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		
		pickerFecha.setValue(LocalDate.now());
		
		checkFiltrar.selectedProperty().addListener((ob,oldValue,newValue)->{
			pickerFecha.setDisable(!newValue);
			if(newValue) {
				cargarCompras(pickerFecha.getValue());
			}else
				cargarCompras(null);
		});
		
		pickerFecha.valueProperty().addListener((ob,oldValue,newValue)->{
			cargarCompras(newValue);
			System.out.println("Changed");
		});
		
		cargarCompras(null);
		
	}
	
	@FXML
	private void onTablaComprasContextMenuShown(WindowEvent event) {
		itemDeseleccionarCompra.setDisable(
				tablaCompras.getSelectionModel().getSelectedItem() == null);
	}
	
	@FXML
	private void onNuevaCompra(ActionEvent event) {
		
	}
	
	@FXML
	private void onDeseleccionarCompra(ActionEvent event) {
		tablaCompras.getSelectionModel().clearSelection();
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