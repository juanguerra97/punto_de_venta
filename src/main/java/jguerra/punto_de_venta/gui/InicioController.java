package jguerra.punto_de_venta.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import jguerra.punto_de_venta.gui.productos.ProductosController;

public class InicioController {
	
	@FXML
    private TabPane tabPane;

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
			
			tabClientes.setContent(FXMLLoader.load(getClass()
					.getResource("/fxml/clientes/Clientes.fxml")));
			
			tabProveedores.setContent(FXMLLoader.load(getClass()
					.getResource("/fxml/proveedores/Proveedores.fxml")));
			
			tabCompras.setContent(FXMLLoader.load(getClass()
					.getResource("/fxml/compras/Compras.fxml")));
			
			tabVentas.setContent(FXMLLoader.load(getClass()
					.getResource("/fxml/ventas/Ventas.fxml")));
			
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	tabPane.getSelectionModel().selectedItemProperty().addListener(
    			(ob,oldTab,newTab)->{
    		if(newTab != null) {
    			if(newTab.equals(tabProductos))
    				controllerProductos.updateExistencias();
    		}
    	});
    	
    }
    
	
}
