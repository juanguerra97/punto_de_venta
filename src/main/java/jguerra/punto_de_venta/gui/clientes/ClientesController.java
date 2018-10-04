package jguerra.punto_de_venta.gui.clientes;

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
import jguerra.punto_de_venta.datos.modelo.Cliente;
import jguerra.punto_de_venta.datos.validacion.Validacion;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Icono;
import jguerra.punto_de_venta.gui.Main;

public class ClientesController {

	@FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TableColumn<Cliente, String> colNit;
                                 
    @FXML                        
    private TableColumn<Cliente, String> colNombre;
                                 
    @FXML                        
    private TableColumn<Cliente, String> colApellido;
                                 
    @FXML                        
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private MenuItem itemEliminar;

    @FXML
    private MenuItem itemDeseleccionar;

    @FXML
    private CustomTextField fieldNit;

    @FXML
    private CustomTextField fieldNombre;

    @FXML
    private CustomTextField fieldApellido;

    @FXML
    private CustomTextField fieldTelefono;

    @FXML
    private Button boton;
    
    private DAOManager manager;
    
    private ObservableList<Cliente> clientes;
    
    private boolean nitInvalido,nombreInvalido,apellidoInvalido,telefonoInvalido;
    
    private void cargarClientes() {
    	manager.cliente().ifPresent(dao -> {
    		clientes.setAll(dao.selectAll());
    	});
    }
    
    private void loadCliente(final Cliente cliente) {
    	if(cliente == null) {
    		fieldNit.clear();
    		fieldNombre.clear();
    		fieldApellido.clear();
    		fieldTelefono.clear();
    	}else {
    		fieldNit.setText(cliente.getNit());
    		fieldNombre.setText(cliente.getNombre());
    		fieldApellido.setText(cliente.getApellido());
    		fieldTelefono.setText(cliente.getTelefono());
    	}
    }
    
    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	nitInvalido = nombreInvalido = apellidoInvalido = telefonoInvalido = true;
    	
    	clientes = tablaClientes.getItems();
    	
    	colNit.setCellValueFactory(new PropertyValueFactory<>("nit"));
    	colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
    	colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    	
    	tablaClientes.getSelectionModel().selectedItemProperty().addListener(
    			(ob,oldSelection,newSelection)->{
    		loadCliente(newSelection);
    		if(newSelection == null) {
    			boton.setGraphic(Icono.save16());
    			boton.setText("Guardar");
    		}else {
    			boton.setGraphic(Icono.edit16());
    			boton.setText("Actualizar");
    		}
    		fieldNit.setDisable(newSelection != null);
    	});
    	
    	Fields.setupClearButtonField(fieldNit);
    	Fields.setupClearButtonField(fieldNombre);
    	Fields.setupClearButtonField(fieldApellido);
    	Fields.setupClearButtonField(fieldTelefono);
    	
    	fieldNit.textProperty().addListener((ob,oldText,newText)->{
    		nitInvalido = !Validacion.validarNit(newText.trim());
    		boton.setDisable(nitInvalido || nombreInvalido || 
    				apellidoInvalido || telefonoInvalido);
    	});
    	
    	fieldNombre.textProperty().addListener((ob,oldText,newText)->{
    		nombreInvalido = newText.trim().isEmpty();
    		boton.setDisable(nombreInvalido || nitInvalido || 
    				apellidoInvalido || telefonoInvalido);
    	});
    	
    	fieldApellido.textProperty().addListener((ob,oldText,newText)->{
    		apellidoInvalido = newText.trim().isEmpty();
    		boton.setDisable(apellidoInvalido || nitInvalido || 
    				nombreInvalido || telefonoInvalido);
    	});
    	
    	fieldTelefono.textProperty().addListener((ob,oldText,newText)->{
    		telefonoInvalido = !Validacion.validarTelefono(newText.trim());
    		boton.setDisable(telefonoInvalido || nitInvalido || 
    				nombreInvalido || apellidoInvalido);
    	});
    	
    	boton.setGraphic(Icono.save16());
    	
    	itemEliminar.setGraphic(Icono.clear16());
    	itemDeseleccionar.setGraphic(Icono.remove16());
    	
    	cargarClientes();
    	
    }
    
    @FXML
    private void onBoton(ActionEvent event) {
    	manager.cliente().ifPresent(dao -> {
    		Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
        	String nit = fieldNit.getText().trim();
    		String nombre = fieldNombre.getText().trim().toUpperCase();
    		String apellido = fieldApellido.getText().trim().toUpperCase();
        	String telefono = fieldTelefono.getText().trim();
        	if(cliente == null) {
        		cliente = new Cliente(nit,nombre,apellido,telefono);
        		try {
    				dao.insert(cliente);
    				clientes.add(cliente);
    				Main.notificar("Se registró el cliente \"" + nombre + "\"");
    				fieldNit.clear();
    				fieldNombre.clear();
    				fieldApellido.clear();
    				fieldTelefono.clear();
    				tablaClientes.requestFocus();
    			} catch (SQLException e) {
    				Main.notificar(e.getMessage());
    				fieldNit.selectAll();
    				fieldNit.requestFocus();
    				e.printStackTrace();
    			}
        	} else {
        		int indice = clientes.indexOf(cliente);
        		cliente.setNombre(nombre);
        		cliente.setApellido(apellido);
        		cliente.setTelefono(telefono);
        		
    			dao.update(cliente);
    			clientes.set(indice, cliente);
    			tablaClientes.getSelectionModel().select(indice);
    			Main.notificar("Se actualizó un cliente");
    			tablaClientes.requestFocus();
        	}
    	});
    }
    
    @FXML
    private void onTablaClientesContextMenuShown(WindowEvent event) {
    	boolean sinSeleccion = tablaClientes.getSelectionModel().getSelectedItem() == null;
    	itemEliminar.setDisable(sinSeleccion);
    	itemDeseleccionar.setDisable(sinSeleccion);
    }
    
    @FXML
    private void onEliminarCliente(ActionEvent event) {
    	manager.cliente().ifPresent(dao -> {
    		final Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
    		tablaClientes.getSelectionModel().clearSelection();
    		try {
				dao.delete(cliente.getNit());
				clientes.remove(cliente);
				Main.notificar("Se eliminó un cliente");
    			tablaClientes.requestFocus();
			} catch (SQLException e) {
				Main.notificar(e.getMessage());
    			e.printStackTrace();
			}
    	});
    }
    
    @FXML
    private void onDeseleccionarCliente(ActionEvent event) {
    	tablaClientes.getSelectionModel().clearSelection();
    }
	
}
