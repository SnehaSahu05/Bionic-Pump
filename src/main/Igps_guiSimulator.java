package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author Winxberry
 * 
 */
public class Igps_guiSimulator extends Application {

	public static void main(String[] args) {
		launch(args);
		// Application.launch(Igps_guiSimulator.class,
		// (java.lang.String[])null);
	}

	@Override
	public void start(Stage stage) throws Exception {
//		try {
			AnchorPane page = (AnchorPane) FXMLLoader.load(getClass().getResource("igpsGui.fxml"));
			
			Scene scene = new Scene(page);
			stage.setTitle("Simple IGPS");
			stage.setResizable(false);
			
			stage.setScene(scene);
			stage.show();
//		} catch (Exception ex) {
			/*
			 * FutureWork: use log file
			 * Logger.getLogger(Igps_guiSimulator.class.
			 * getName()).log(Level.SEVERE, null, ex);
			 */
//			System.out.println("Found Null Exception. \nCould not load FXML Scene for Insulin Glucagon Pump Simulator.\nException Handled. Good Luck for next Try!");
//		}

	}
	
	

}
