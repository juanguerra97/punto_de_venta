package jguerra.punto_de_venta.gui.productos;

import java.sql.SQLException;
import java.util.Optional;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.gui.Main;

public class NuevoProductoController {

	 @FXML
	 private CustomTextField fieldNombre;

	 @FXML
	 private CustomTextField fieldMarca;

	 @FXML
	 private Button btnCancelar;

	 @FXML
	 private Button btnGuardar;
	 
	 private DAOManager manager;
	 
	 private Producto producto = null;
	 
	 public void reset() {
		 
		 fieldNombre.clear();
		 fieldMarca.clear();
		 fieldNombre.requestFocus();
		 
	 }
	 
	 public Optional<Producto> getProducto(){
		 if(producto == null)
			 return Optional.empty();
		 Optional<Producto> opt = Optional.of(producto);
		 producto = null;
		 return opt;
	 }
	 
	 @FXML
	 private void initialize() {
		 
		 manager = DAOManager.instance();
		 
		 fieldNombre.textProperty().addListener((observable,oldText,newText)->{
			 btnGuardar.setDisable(newText.trim().isEmpty() || 
					 fieldMarca.getText().trim().isEmpty());
		 });
		 
		 fieldMarca.textProperty().addListener((observable,oldText,newText)->{
			 btnGuardar.setDisable(newText.trim().isEmpty() || 
					 fieldNombre.getText().trim().isEmpty());
		 });
		 
	 }
	 
	 @FXML
	 private void onGuardar(){
		 manager.producto().ifPresent(dao -> {
			 producto = new Producto(fieldNombre.getText().trim().toUpperCase(), 
					 fieldMarca.getText().trim().toUpperCase());
			 try {
				int id = dao.insert(producto);
				producto.setId(id);
				//producto = new Producto(id,producto.getNombre(),producto.getMarca());
				btnGuardar.getScene().getWindow().hide();
			} catch (SQLException e) {
				producto = null;
				Main.alertError("ERROR", "Ocurrió un error al guardar el producto", e.getMessage());
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