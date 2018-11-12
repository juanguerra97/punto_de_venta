package jguerra.punto_de_venta.gui.productos;

import org.controlsfx.control.textfield.TextFields;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jguerra.punto_de_venta.datos.modelo.Producto;

public class EditorProducto extends GridPane {

	private Button btnActualizar;
	
	private TextField fieldNombre;
	private TextField fieldMarca;
	
	private boolean nombreInvalido = true;
	private boolean marcaInvalida = true;
	
	public EditorProducto(final EventHandler<ActionEvent> onActualizar,
			final EventHandler<ActionEvent> onCancelar) {
		assert onActualizar != null;
		assert onCancelar != null;
		
		fieldNombre = TextFields.createClearableTextField();
		fieldMarca = TextFields.createClearableTextField();
		
		fieldNombre.setPrefColumnCount(20);
		
		btnActualizar = new Button("Actualizar");
		btnActualizar.setOnAction(onActualizar);
		btnActualizar.setDisable(true);
		
		Button btnCancelar = new Button("Cancelar");
		btnCancelar.setOnAction(onCancelar);
		
		fieldNombre.textProperty().addListener((ob,oldText,newText)->{
			nombreInvalido = newText.trim().isEmpty();
			btnActualizar.setDisable(nombreInvalido || marcaInvalida);
		});
		
		fieldMarca.textProperty().addListener((ob,oldText,newText)->{
			marcaInvalida = newText.trim().isEmpty();
			btnActualizar.setDisable(marcaInvalida || nombreInvalido);
		});
		
		setPadding(new Insets(3, 3, 3, 3));
		setHgap(3);
		setVgap(2);
		
		addRow(0, new Label("Nombre"), fieldNombre);
        addRow(1, new Label("Marca"), fieldMarca);
		addRow(2,btnActualizar,btnCancelar);
		
	}
	
	public void setProducto(final Producto producto) {
		assert producto != null;
		fieldNombre.setText(producto.getNombre());
		fieldMarca.setText(producto.getMarca());
		btnActualizar.setDisable(true);
	}
	
	public String getNombre() {
		return fieldNombre.getText();
	}
	
	public String getMarca() {
		return fieldMarca.getText();
	}
	
	public void focusNombre() {
		fieldNombre.selectAll();
		fieldNombre.requestFocus();
	}
	
	public void clear() {
		fieldNombre.clear();
		fieldMarca.clear();
	}
	
}
