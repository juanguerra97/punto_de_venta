package jguerra.punto_de_venta.gui.productos;

import java.sql.SQLException;
import java.util.Optional;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Existencia;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Sucursal;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Main;

public class NuevaExistenciaController {

	@FXML
    private CustomTextField fieldCantidad;

    @FXML
    private ChoiceBox<Sucursal> choiceBoxSucursal;

    @FXML
    private Button btnGuardar;
    
    private DAOManager manager;
    
    private Presentacion presentacion = null;
    private Existencia existencia = null;
    
    public void reset() {
    	
    	presentacion = null;
    	fieldCantidad.clear();
    	choiceBoxSucursal.getSelectionModel().clearSelection();
    	fieldCantidad.requestFocus();
    	
    }
    
    public Optional<Existencia> getExistencia(){
    	if(existencia == null)
    		return Optional.empty();
    	Optional<Existencia> opt = Optional.of(existencia);
    	existencia = null;
    	return opt;
    }
    
    public void setPresentacion(final Presentacion presentacion) {
    	assert presentacion != null;
    	this.presentacion = presentacion;
    }
    
    public void cargarSucursales() {
    	manager.sucursal().ifPresent(dao -> {
    		choiceBoxSucursal.getItems().setAll(dao.selectAll());
    	});
    }
    
    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	Fields.setupClearButtonField(fieldCantidad);
    	
    	fieldCantidad.textProperty().addListener((observable,oldText,newText) -> {
    		boolean cantidadInvalida = false;
    		try {
    			int cantidad = Integer.parseInt(newText.trim());
    			cantidadInvalida = cantidad < 0;
    		}catch(NumberFormatException ex) {
    			cantidadInvalida = true;
    		}
    		btnGuardar.setDisable(cantidadInvalida || 
    				choiceBoxSucursal.getSelectionModel().getSelectedItem() == null);
    	});
    	
    	choiceBoxSucursal.getSelectionModel().selectedItemProperty().addListener(
    			(observable,oldSelection,newSelection)->{
    		boolean cantidadInvalida = false;
    	    try {
    	    	int cantidad = Integer.parseInt(fieldCantidad.getText().trim());
    	    	cantidadInvalida = cantidad < 0;
    	    }catch(NumberFormatException ex) {
    	    	cantidadInvalida = true;
    	    }
    	    btnGuardar.setDisable(cantidadInvalida || newSelection == null);
    	});
    }
    
    @FXML
    private void onGuardar(ActionEvent event) {
    	if(presentacion == null)
    		return;
    	manager.existencia().ifPresent(dao -> {
    		
    		existencia = new Existencia(presentacion.getId(), 
    				choiceBoxSucursal.getSelectionModel().getSelectedItem().getId(), 
    				Integer.parseInt(fieldCantidad.getText().trim()));
    		
    		try {
				dao.insert(existencia);
				btnGuardar.getScene().getWindow().hide();
			} catch (SQLException e) {
				existencia = null;
				Main.alertError("ERROR", "Ocurrió un error al guardar la existencia", e.getMessage());
				choiceBoxSucursal.requestFocus();
				e.printStackTrace();
			}
    		
    	});
    }
    
    @FXML
    private void onCancelar(ActionEvent event) {
    	btnGuardar.getScene().getWindow().hide();
    }
	
}
