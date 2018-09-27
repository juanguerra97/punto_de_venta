package jguerra.punto_de_venta.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

public class InicioController {

	@FXML
    private Tab tabProductos;

    @FXML
    private Tab tabSucursales;

    @FXML
    private Tab tabClientes;

    @FXML
    private Tab tabProveedores;

    @FXML
    private Tab tabCompras;

    @FXML
    private Tab tabVentas;
    
    @FXML
    private void initialize() {
    	
    	try {
			tabSucursales.setContent(FXMLLoader.load(getClass().getResource("/fxml/sucursales/Sucursales.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
	
}
