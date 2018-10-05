package jguerra.punto_de_venta.gui.productos;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.validacion.Validacion;
import jguerra.punto_de_venta.gui.Fields;
import jguerra.punto_de_venta.gui.Main;

public class NuevaPresentacionController {
	
	@FXML
    private CustomTextField fieldNombre;

    @FXML
    private CustomTextField fieldCosto;

    @FXML
    private CustomTextField fieldPrecio;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;
    
    private DAOManager manager;
    
    private Producto producto = null;
    private Presentacion presentacion = null;
    
    private boolean nombreInvalido = true;
    private boolean costoInvalido = true;
    private boolean precioInvalido = true;
    
    public void reset() {
    	
    	producto = null;
    	fieldNombre.clear();
    	fieldCosto.clear();
    	fieldPrecio.clear();
    	fieldNombre.requestFocus();
    	
    }
    
    public Optional<Presentacion> getPresentacion() {
    	if(presentacion == null)
    		return Optional.empty();
    	Optional<Presentacion> opt = Optional.of(presentacion);
    	presentacion = null;
    	return opt;
    }
    
    public void setProducto(final Producto producto) {
    	assert producto != null;
    	this.producto = producto;
    }
    
    private boolean valoresCostoPrecioInvalidos() {
    	boolean invalidos = false;
    	
    	BigDecimal costo = null;
    	BigDecimal precio = null;
    	
    	try {
    		costo = new BigDecimal(fieldCosto.getText().trim());
    	}catch(NumberFormatException ex) {
    		costoInvalido = true;
    	}
    	
    	try {
    		precio = new BigDecimal(fieldPrecio.getText().trim());
    	}catch(NumberFormatException ex) {
    		precioInvalido = true;
    	}
    	invalidos = (costo != null && precio != null && costo.compareTo(precio) > 0);
    	return invalidos;
    }
    
    @FXML
    private void initialize() {
    	
    	manager = DAOManager.instance();
    	
    	Fields.setupClearButtonField(fieldNombre);
    	Fields.setupClearButtonField(fieldCosto);
    	Fields.setupClearButtonField(fieldPrecio);
    	
    	fieldNombre.textProperty().addListener((observable,oldText,newText)->{
    		nombreInvalido = newText.trim().isEmpty();
    		btnGuardar.setDisable(nombreInvalido || costoInvalido || precioInvalido ||
    				valoresCostoPrecioInvalidos());
    	});
    	
    	fieldCosto.textProperty().addListener((observable,oldText,newText)->{
    		costoInvalido = !Validacion.validarMoneda(newText.trim());
    		btnGuardar.setDisable(costoInvalido || nombreInvalido || precioInvalido ||
    				valoresCostoPrecioInvalidos());
    	});
    	
    	fieldPrecio.textProperty().addListener((observable,oldText,newText)->{
    		precioInvalido = !Validacion.validarMoneda(newText.trim());
    		btnGuardar.setDisable(precioInvalido || nombreInvalido || costoInvalido ||
    				valoresCostoPrecioInvalidos());
    	});
    	
    }
    
    @FXML
    private void onGuardar(ActionEvent event) {
    	if(producto == null)
    		return;
    	manager.presentacion().ifPresent(dao -> {
    		presentacion = new Presentacion(producto.getId(), 
    				fieldNombre.getText().trim().toUpperCase(), 
    				new BigDecimal(fieldPrecio.getText().trim()), 
    				new BigDecimal(fieldCosto.getText().trim()));
    		try {
				int id = dao.insert(presentacion);
				presentacion.setId(id);
				btnGuardar.getScene().getWindow().hide();
			} catch (SQLException e) {
				presentacion = null;
				Main.alertError("ERROR", "Ocurrió un error al guardar la presentación", e.getMessage());
				fieldNombre.selectAll();
				fieldNombre.requestFocus();
				e.printStackTrace();
			}
    	});
    	
    }
    
    @FXML
    private void onCancelar(ActionEvent event) {
    	btnGuardar.getScene().getWindow().hide();
    }

}
