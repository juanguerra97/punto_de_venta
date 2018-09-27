package jguerra.punto_de_venta.gui.sucursales;

import java.sql.SQLException;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.SucursalDAO;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.db.oracle.Conexion;
import jguerra.punto_de_venta.gui.Main;

public class SucursalesController {

	@FXML
    private TableView<Sucursal> tablaSucursales;

    @FXML
    private TableColumn<Sucursal, Integer> colId;

    @FXML
    private TableColumn<Sucursal, String> colNombre;
    
    @FXML
    private MenuItem menuItemEliminar;

    @FXML
    private MenuItem menuItemDeseleccionar;

    @FXML
    private CustomTextField fieldNombre;

    @FXML
    private Button boton;
    
    private SucursalDAO daoSucursal = null;
    private ObservableList<Sucursal> sucursales;
    
    private void cargarSucursales() {
    	if(daoSucursal == null) {
    		try {
				daoSucursal = new SucursalDAO(Conexion.get());
			} catch (SQLException e) {
				daoSucursal = null;
				e.printStackTrace();
				return;
			}
    	}
    	sucursales.setAll(daoSucursal.selectAll());
    }
    
    @FXML
    private void initialize() {
    	
    	sucursales = tablaSucursales.getItems();
    	tablaSucursales.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	colId.setCellValueFactory(new PropertyValueFactory<Sucursal,Integer>("id"));
    	colNombre.setCellValueFactory(new PropertyValueFactory<Sucursal,String>("nombre"));
    	
    	tablaSucursales.getSelectionModel().selectedItemProperty().addListener((obj,viejo,nuevo)->{
    		if(nuevo == null) {
    			fieldNombre.clear();
    			boton.setText("Ingresar");
    		}else {
    			fieldNombre.setText(nuevo.getNombre());
    			boton.setText("Actualizar");
    		}
    	});
    	
    	fieldNombre.textProperty().addListener((obj,viejo,nuevo)->{
    		boton.setDisable(nuevo.trim().isEmpty());
    	});
    	
    	fieldNombre.setOnAction(e->onBoton(e));
    	
    	cargarSucursales();
    	
    }
    
    @FXML
    private void onBoton(ActionEvent event) {
    	if(daoSucursal == null) {
    		try {
				daoSucursal = new SucursalDAO(Conexion.get());
			} catch (SQLException e) {
				daoSucursal = null;
				e.printStackTrace();
				return;
			}
    	}
    	Sucursal sucursal = tablaSucursales.getSelectionModel().getSelectedItem();
    	String nombre = fieldNombre.getText().trim().toUpperCase();
    	if(sucursal == null) {
    		sucursal = new Sucursal(nombre);
    		try {
				int id = daoSucursal.insert(sucursal);
				sucursales.add(new Sucursal(id,nombre));
				Main.notificar("Se registró la sucursal \"" + nombre + "\"");
				fieldNombre.clear();
				tablaSucursales.requestFocus();
			} catch (SQLException e) {
				Main.notificar(e.getMessage());
				fieldNombre.selectAll();
				fieldNombre.requestFocus();
				e.printStackTrace();
			}
    	} else {
    		int indice = sucursales.indexOf(sucursal);
    		sucursal.setNombre(nombre);
    		try {
				daoSucursal.update(sucursal);
				sucursales.set(indice, sucursal);
				tablaSucursales.getSelectionModel().select(indice);
				Main.notificar("Se actualizó el nombre de una sucursal");
				tablaSucursales.requestFocus();
			} catch (SQLException e) {
				Main.notificar(e.getMessage());
				fieldNombre.selectAll();
				fieldNombre.requestFocus();
				e.printStackTrace();
			}
    	}
    	
    }
    
    @FXML
    private void onTablaSucursalesContextMenuShown(WindowEvent event) {
    	boolean sinSeleccion = tablaSucursales.getSelectionModel().getSelectedItem() == null;
    	menuItemEliminar.setDisable(sinSeleccion);
    	menuItemDeseleccionar.setDisable(sinSeleccion);
    }
    
    @FXML
    private void onDeseleccionar(ActionEvent event) {
    	tablaSucursales.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void onEliminar(ActionEvent event) {
    	if(daoSucursal == null) {
    		try {
				daoSucursal = new SucursalDAO(Conexion.get());
			} catch (SQLException e) {
				daoSucursal = null;
				e.printStackTrace();
				return;
			}
    	}
    	Sucursal sucursal = tablaSucursales.getSelectionModel().getSelectedItem();
    	tablaSucursales.getSelectionModel().clearSelection();
    	try {
			daoSucursal.delete(sucursal.getId());
			sucursales.remove(sucursal);
			Main.notificar("Se eliminó una sucursal");
			tablaSucursales.requestFocus();
		} catch (SQLException e) {
			Main.notificar(e.getMessage());
			e.printStackTrace();
		}
    }
	
}
