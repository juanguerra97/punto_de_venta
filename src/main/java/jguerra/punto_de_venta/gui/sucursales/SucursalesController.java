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
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Icono;
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
    
    private DAOManager manager;
    private ObservableList<Sucursal> sucursales;
    
    private void cargarSucursales() {
    	manager.sucursal().ifPresent(dao -> {
    		sucursales.setAll(dao.selectAll());
    	});
    }
    
    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	sucursales = tablaSucursales.getItems();
    	tablaSucursales.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	colId.setCellValueFactory(new PropertyValueFactory<Sucursal,Integer>("id"));
    	colNombre.setCellValueFactory(new PropertyValueFactory<Sucursal,String>("nombre"));
    	
    	tablaSucursales.getSelectionModel().selectedItemProperty().addListener((obj,viejo,nuevo)->{
    		if(nuevo == null) {
    			fieldNombre.clear();
    			boton.setGraphic(Icono.save16());
    			boton.setText("Guardar");
    		}else {
    			fieldNombre.setText(nuevo.getNombre());
    			boton.setGraphic(Icono.edit16());
    			boton.setText("Actualizar");
    		}
    	});
    	
    	Fields.setupClearButtonField(fieldNombre);
    	
    	fieldNombre.textProperty().addListener((obj,viejo,nuevo)->{
    		boton.setDisable(nuevo.trim().isEmpty());
    	});
    	
    	fieldNombre.setOnAction(e->onBoton(e));
    	
    	boton.setText("Guardar");
    	boton.setGraphic(Icono.save16());
    	
    	menuItemEliminar.setGraphic(Icono.clear16());
    	menuItemDeseleccionar.setGraphic(Icono.remove16());
    	
    	cargarSucursales();
    	
    }
    
    @FXML
    private void onBoton(ActionEvent event) {
    	manager.sucursal().ifPresent(dao -> {
    		Sucursal sucursal = tablaSucursales.getSelectionModel().getSelectedItem();
        	String nombre = fieldNombre.getText().trim().toUpperCase();
        	if(sucursal == null) {
        		sucursal = new Sucursal(nombre);
        		try {
    				int id = dao.insert(sucursal);
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
    				dao.update(sucursal);
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
    	});
    	
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
    	manager.sucursal().ifPresent(dao -> {
    		Sucursal sucursal = tablaSucursales.getSelectionModel().getSelectedItem();
        	tablaSucursales.getSelectionModel().clearSelection();
        	try {
    			dao.delete(sucursal.getId());
    			sucursales.remove(sucursal);
    			Main.notificar("Se eliminó una sucursal");
    			tablaSucursales.requestFocus();
    		} catch (SQLException e) {
    			Main.notificar(e.getMessage());
    			e.printStackTrace();
    		}
    	});
    }
	
}
