package jguerra.punto_de_venta.gui;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.controlsfx.control.NotificationPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jguerra.punto_de_venta.db.oracle.Conexion;

public class Main extends Application {
	
	private static final NotificationPane root = new NotificationPane();
	private static final Alert ERROR_ALERT = new Alert(AlertType.ERROR,"",new ButtonType("Entendido")); 
	
	@Override
	public void start(Stage stage) throws Exception {
		
		ERROR_ALERT.initModality(Modality.APPLICATION_MODAL);
		root.setGraphic(Icono.announcement20());
		
		try {
			Conexion.get();
			// conectado!
		}catch(SQLException ex) {
			ex.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR	, "", new ButtonType("Salir"));
			alert.setTitle("Error de conexión");
			alert.setHeaderText("No se pudo conectar a la base de datos");
			alert.setContentText("Es posible que el servidor no se encuentre funcionando");
			alert.showAndWait();
			Platform.exit();
		}
		
		root.setContent(FXMLLoader.load(getClass().getResource("/fxml/Inicio.fxml")));
		Scene scene = new Scene(root,800,500);
		stage.setScene(scene);
		stage.setTitle("Punto de venta");
		stage.getIcons().add(Icono.product128());
		stage.setMinWidth(750);
		stage.setMinHeight(450);
		stage.setOnCloseRequest(w->{
			try {
				Conexion.get().close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static synchronized void notificar(String msg) {
		Timer timer = new Timer();
		root.show(msg);
		timer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                	root.hide();
	                	timer.cancel();
	                }
	            });
	        }
	    }, 3000, 3000);
	}
	
	public static void alertError(final String title,final String header,final String message) {
		ERROR_ALERT.setTitle(title);
		ERROR_ALERT.setHeaderText(header);
		ERROR_ALERT.setContentText(message);
		ERROR_ALERT.showAndWait();
	}

}
