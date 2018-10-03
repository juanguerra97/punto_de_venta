package jguerra.punto_de_venta.gui;

import org.controlsfx.control.textfield.CustomTextField;

import javafx.animation.FadeTransition;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Fields {
	
	private static final Duration FADE_DURATION = Duration.millis(350);
	
	public static void setupClearButtonField(CustomTextField inputField) {
		
		ObjectProperty<Node> rightProperty = inputField.rightProperty();
        inputField.getStyleClass().add("clearable-field"); //$NON-NLS-1$

        Region clearButton = new Region();
        clearButton.getStyleClass().addAll("graphic"); //$NON-NLS-1$
        StackPane clearButtonPane = new StackPane(clearButton);
        clearButtonPane.getStyleClass().addAll("clear-button"); //$NON-NLS-1$
        clearButtonPane.setOpacity(0.0);
        clearButtonPane.setCursor(Cursor.DEFAULT);
        clearButtonPane.setOnMouseReleased(e -> inputField.clear());
        clearButtonPane.managedProperty().bind(inputField.editableProperty());
        clearButtonPane.visibleProperty().bind(inputField.editableProperty());

        rightProperty.set(clearButtonPane);

        final FadeTransition fader = new FadeTransition(FADE_DURATION, clearButtonPane);
        fader.setCycleCount(1);

        inputField.textProperty().addListener((o) -> {
                String text = inputField.getText();
                boolean isTextEmpty = text == null || text.isEmpty();
                boolean isButtonVisible = fader.getNode().getOpacity() > 0;

                if (isTextEmpty && isButtonVisible) {
                	fader.setFromValue(1.0);
                    fader.setToValue(0.0);
                    fader.play();
                } else if (!isTextEmpty && !isButtonVisible) {
                	fader.setFromValue(0.0);
                    fader.setToValue(1.0);
                    fader.play();
                }
        });
        
    }

}
