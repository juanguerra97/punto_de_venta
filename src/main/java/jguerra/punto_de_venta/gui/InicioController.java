package jguerra.punto_de_venta.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import jguerra.punto_de_venta.gui.productos.ProductosController;

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
    
    private ProductosController controllerProductos;
    
    @FXML
    private void initialize() {
    	
    	try {
			tabSucursales.setContent(FXMLLoader.load(getClass()
					.getResource("/fxml/sucursales/Sucursales.fxml")));
			
			FXMLLoader loaderProd = new FXMLLoader(getClass()
					.getResource("/fxml/productos/Productos.fxml"));
			tabProductos.setContent(loaderProd.load());
			controllerProductos = loaderProd.getController();
			
			tabProveedores.setContent(FXMLLoader.load(getClass()
					.getResource("/fxml/proveedores/Proveedores.fxml")));
			
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
	
}
