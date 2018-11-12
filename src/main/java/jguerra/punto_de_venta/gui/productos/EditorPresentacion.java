package jguerra.punto_de_venta.gui.productos;


import java.math.BigDecimal;

import org.controlsfx.control.textfield.TextFields;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jguerra.punto_de_venta.datos.modelo.Presentacion;
import jguerra.punto_de_venta.datos.validacion.Validacion;

public class EditorPresentacion extends GridPane {
	
	private Button btnActualizar;
	
	private TextField fieldNombre;
	private TextField fieldCosto;
	private TextField fieldPrecio;
	
	private boolean nombreInvalido = true;
	private boolean costoInvalido = true;
	private boolean precioInvalido = true;
	
	public EditorPresentacion(final EventHandler<ActionEvent> onActualizar,
			final EventHandler<ActionEvent> onCancelar) {
		assert onActualizar != null;
		assert onCancelar != null;
		
		fieldNombre = TextFields.createClearableTextField();
		fieldCosto = TextFields.createClearableTextField();
		fieldPrecio = TextFields.createClearableTextField();
		
		btnActualizar = new Button("Actualizar");
		btnActualizar.setOnAction(onActualizar);
		btnActualizar.setDisable(true);
		
		Button btnCancelar = new Button("Cancelar");
		btnCancelar.setOnAction(onCancelar);
		
		fieldNombre.textProperty().addListener((ob,oldText,newText)->{
			nombreInvalido = newText.trim().isEmpty();
			btnActualizar.setDisable(nombreInvalido || costoInvalido 
					|| precioInvalido || costoGtPrecio());
		});
		
		fieldCosto.textProperty().addListener((ob,oldText,newText)->{
			costoInvalido = !Validacion.validarMoneda(newText.trim());
			btnActualizar.setDisable(costoInvalido || nombreInvalido 
					|| precioInvalido || costoGtPrecio());
		});
		
		fieldPrecio.textProperty().addListener((ob,oldText,newText)->{
			precioInvalido = !Validacion.validarMoneda(newText.trim());
			btnActualizar.setDisable(precioInvalido || nombreInvalido 
					|| costoInvalido || costoGtPrecio());
		});
		
		setPadding(new Insets(3, 3, 3, 3));
		setHgap(3);
		setVgap(2);
		
		addRow(0, new Label("Nombre"), fieldNombre);
        addRow(1, new Label("Costo"), fieldCosto);
        addRow(2, new Label("Precio"), fieldPrecio);
		addRow(3,btnActualizar,btnCancelar);
		
	}
	
	public void setPresentacion(final Presentacion presentacion) {
		assert presentacion != null;
		fieldNombre.setText(presentacion.getNombre());
		fieldCosto.setText(presentacion.getCosto().toPlainString());
		fieldPrecio.setText(presentacion.getPrecio().toPlainString());
		btnActualizar.setDisable(true);
	}
	
	public String getNombre() {
		return fieldNombre.getText();
	}
	
	public String getCosto() {
		return fieldCosto.getText();
	}
	
	public String getPrecio() {
		return fieldPrecio.getText();
	}
	
	public void focusNombre() {
		fieldNombre.selectAll();
		fieldNombre.requestFocus();
	}
	
	public void clear() {
		fieldNombre.clear();
		fieldCosto.clear();
		fieldPrecio.clear();
	}
	
	private boolean costoGtPrecio() {
		try {
			BigDecimal costo = new BigDecimal(fieldCosto.getText().trim());
			BigDecimal precio = new BigDecimal(fieldPrecio.getText().trim());
			return costo.compareTo(precio) > 0;
		}catch(NumberFormatException e) {
			return true;
		}
	}

}
