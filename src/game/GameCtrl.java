package game;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;


/*
 * Game scene controller class
 * 
 * Params: its loader (is it needed?)
 * the primary stage -> needed for calculations and handlers
 * local properties (same as options)
 * global properties (?)
 */
public class GameCtrl implements Initializable {
	private FXMLLoader loader;
	private Stage primaryStage;
	private Properties localProp;
	private Properties globalProp;
	
	public Scenario scenario;
	
	
	private void initHandlers() {
		
	}
	
	private void initAnimations() {
		
	}
	
	private void initValues() {
		
	}
	
	public void startup(FXMLLoader loader, Stage stage, Properties l, Properties g) {
		this.loader = loader;
		primaryStage = stage;
		localProp = l;
		globalProp = g;
		
		initHandlers();
		initAnimations();
		initValues();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
}
