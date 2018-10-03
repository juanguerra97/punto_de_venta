package jguerra.punto_de_venta.gui.proveedores;

import java.sql.SQLException;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Proveedor;
import jguerra.punto_de_venta.datos.validacion.Validacion;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Main;

public class ProveedoresController {

	@FXML
    private TableView<Proveedor> tablaProveedores;

    @FXML
    private TableColumn<Proveedor, Integer> colId;

    @FXML
    private TableColumn<Proveedor, String> colNombre;

    @FXML
    private TableColumn<Proveedor, String> colTelefono;

    @FXML
    private MenuItem itemEliminar;

    @FXML
    private MenuItem itemDeseleccionar;

    @FXML
    private CustomTextField fieldNombre;

    @FXML
    private CustomTextField fieldTelefono;

    @FXML
    private Button boton;
    
    private DAOManager manager;
    
    private ObservableList<Proveedor> proveedores;
    
    private void cargarProveedores() {
    	manager.proveedor().ifPresent(dao -> {
    		proveedores.setAll(dao.selectAll());
    	});
    }
    
    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	proveedores = tablaProveedores.getItems();
    	
    	colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    	
    	tablaProveedores.getSelectionModel().selectedItemProperty().addListener(
    			(observable,oldSelection,newSelection)->{
    		if(newSelection == null) {
    			fieldNombre.clear();
    			fieldTelefono.clear();
    			boton.setText("Guardar");
    		}else {
    			fieldNombre.setText(newSelection.getNombre());
    			fieldTelefono.setText(newSelection.getTelefono());
    			boton.setText("Actualizar");
    		}
    	});
    	
    	Fields.setupClearButtonField(fieldNombre);
    	Fields.setupClearButtonField(fieldTelefono);
    	
    	fieldNombre.textProperty().addListener((ob,oldText,newText)->{
    		boton.setDisable(newText.trim().isEmpty() ||
    				!Validacion.validarTelefono(fieldTelefono.getText().trim()));
    	});
    	
    	fieldTelefono.textProperty().addListener((ob,oldText,newText)->{
    		boton.setDisable(!Validacion.validarTelefono(newText.trim()) ||
    				fieldNombre.getText().trim().isEmpty());
    	});
    	
    	cargarProveedores();
    	
    }
    
    @FXML
    private void onBoton() {
    	manager.proveedor().ifPresent(dao -> {
    		Proveedor proveedor = tablaProveedores.getSelectionModel().getSelectedItem();
        	String nombre = fieldNombre.getText().trim().toUpperCase();
        	String telefono = fieldTelefono.getText().trim();
        	if(proveedor == null) {
        		proveedor = new Proveedor(nombre,telefono);
        		try {
    				int id = dao.insert(proveedor);
    				proveedor.setId(id);
    				proveedores.add(proveedor);
    				Main.notificar("Se registró el proveedor \"" + nombre + "\"");
    				fieldNombre.clear();
    				fieldTelefono.clear();
    				tablaProveedores.requestFocus();
    			} catch (SQLException e) {
    				Main.notificar(e.getMessage());
    				fieldNombre.selectAll();
    				fieldNombre.requestFocus();
    				e.printStackTrace();
    			}
        	} else {
        		int indice = proveedores.indexOf(proveedor);
        		proveedor.setNombre(nombre);
        		proveedor.setTelefono(telefono);
        		
    			dao.update(proveedor);
    			proveedores.set(indice, proveedor);
    			tablaProveedores.getSelectionModel().select(indice);
    			Main.notificar("Se actualizó un proveedor");
    			tablaProveedores.requestFocus();
        	}
    	});
    }
    
    @FXML
    private void onTablaProveedoresContextMenuShown(WindowEvent event) {
    	boolean sinSeleccion = tablaProveedores.getSelectionModel().getSelectedItem() == null;
    	itemEliminar.setDisable(sinSeleccion);
    	itemDeseleccionar.setDisable(sinSeleccion);
    }
    
    @FXML
    private void onEliminarProveedor(ActionEvent event) {
    	manager.proveedor().ifPresent(dao -> {
    		final Proveedor proveedor = tablaProveedores.getSelectionModel().getSelectedItem();
    		tablaProveedores.getSelectionModel().clearSelection();
    		try {
				dao.delete(proveedor.getId());
				proveedores.remove(proveedor);
				Main.notificar("Se eliminó un proveedor");
    			tablaProveedores.requestFocus();
			} catch (SQLException e) {
				Main.notificar(e.getMessage());
    			e.printStackTrace();
			}
    	});
    }
    
    @FXML
    private void onDeseleccionarProveedor(ActionEvent event) {
    	tablaProveedores.getSelectionModel().clearSelection();
    }
	
}
