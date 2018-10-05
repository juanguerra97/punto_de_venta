package jguerra.punto_de_venta.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class Icono {
	
//	public static final Image NEW = new Image("icons/file-new-icon.png");
//	
//	public static final ImageView ADD_16 = new ImageView("icons/add_black_48dp.png");
//	public static final ImageView CLEAR_16 = new ImageView("icons/clear_black_48dp.png");
//	public static final ImageView REMOVE_16 = new ImageView("icons/remove_black_48dp.png");
//	public static final ImageView FILTER_12 = new ImageView("icons/filter_black_16px.png");
//	public static final ImageView EDIT_16 = new ImageView("icons/edit_black_48dp.png");
//	public static final ImageView SAVE_16 = new ImageView("icons/save_black_48dp.png");
//	public static final ImageView ANNOUNCEMENT_20 = new ImageView("icons/announcement_black_48dp.png");
//	
//	static {
//		ADD_16.setFitWidth(16);
//		ADD_16.setFitHeight(16);
//		CLEAR_16.setFitWidth(16);
//		CLEAR_16.setFitHeight(16);
//		REMOVE_16.setFitWidth(16);
//		REMOVE_16.setFitHeight(16);
//		FILTER_12.setFitWidth(12);
//		FILTER_12.setFitHeight(12);
//		EDIT_16.setFitWidth(16);
//		EDIT_16.setFitHeight(16);
//		SAVE_16.setFitWidth(16);
//		SAVE_16.setFitHeight(16);
//		ANNOUNCEMENT_20.setFitWidth(20);
//		ANNOUNCEMENT_20.setFitHeight(20);
//	}
	
	public static Image new128() {
		return new Image("/icons/file-new-icon.png");
	}
	
	public static Image product128() {
		return new Image("/icons/product-documentation-icon.png");
	}
	
	public static ImageView add16() {
		ImageView imgView = new ImageView("/icons/add_black_48dp.png");
		imgView.setFitWidth(16);
		imgView.setFitHeight(16);
		return imgView;
	}
	
	public static ImageView clear16() {
		ImageView imgView = new ImageView("/icons/clear_black_48dp.png");
		imgView.setFitWidth(16);
		imgView.setFitHeight(16);
		return imgView;
	}
	
	public static ImageView remove16() {
		ImageView imgView = new ImageView("/icons/remove_black_48dp.png");
		imgView.setFitWidth(16);
		imgView.setFitHeight(16);
		return imgView;
	}
	
	public static ImageView filter12() {
		ImageView imgView = new ImageView("/icons/filter_black_16px.png");
		imgView.setFitWidth(12);
		imgView.setFitHeight(12);
		return imgView;
	}
	
	public static ImageView edit16() {
		ImageView imgView = new ImageView("/icons/edit_black_48dp.png");
		imgView.setFitWidth(16);
		imgView.setFitHeight(16);
		return imgView;
	}
	
	public static ImageView save16() {
		ImageView imgView = new ImageView("/icons/save_black_48dp.png");
		imgView.setFitWidth(16);
		imgView.setFitHeight(16);
		return imgView;
	}
	
	public static ImageView announcement20() {
		ImageView imgView = new ImageView("/icons/announcement_black_48dp.png");
		imgView.setFitWidth(20);
		imgView.setFitHeight(20);
		return imgView;
	}

}
