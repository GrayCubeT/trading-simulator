package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import scene.MainSceneController;
import javafx.scene.Parent;
import javafx.scene.Scene;

/*
 * Main class that opens up the application
 * Also starts up the main menu scene
 */
public class Main extends Application {
	
	private FXMLLoader loader;
	private MainSceneController controller;
	private Properties localProp;
	private Properties globalProp;
	
	/*
	 * Startup, application initialization
	 * Settings parse, stage initialization
	 * Parse fxml and css files
	 * Show everything
	 * 
	 * The game itself runs in the control classes of the stage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			// load persistent settings
			loadSettings();
			
			// setting global properties of stage
			String ss = globalProp.getProperty("stageTitle", "BRUH");
			primaryStage.setTitle(ss);
			
			ss = globalProp.getProperty("stageStyle", "DECORATED");
			primaryStage.initStyle(StageStyle.valueOf(ss));
			
			
			// configure root scene
			Scene scene = new Scene(initFXMLStuff(MainSceneController.class.getResource("MainScene.fxml")), 
					Integer.valueOf(localProp.getProperty("clientWidth")), 
					Integer.valueOf(localProp.getProperty("clientHeight")));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// set scene
			primaryStage.setScene(scene);
			
			// initialize correct values on controller
			controller.initStuff(loader, primaryStage, localProp, globalProp);
			
			// display
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Load global properties and local machine settings
	 * 
	 * Also create nessesary files if run for the first time
	 * in a new directory
	 */
	private void loadSettings() {
		globalProp = new Properties();
		localProp = new Properties();
		
		// load global dev settings
		try {
			globalProp.load(Main.class.getClassLoader().getResourceAsStream("GlobalConfig.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// load local machine settings 
		try {
			boolean newFile = false;
			// check existence
			File f = new File("config.ini");
			if (!f.exists()) {
				f.createNewFile();
				newFile = true;
			}
			
			// parse .properties
			try {
				localProp = new Properties();
				if (newFile) {
					localProp.setProperty("clientWidth", "800");
					
					localProp.setProperty("clientHeight", "600");
					localProp.setProperty("option1", "0");
					localProp.store(new FileOutputStream(f), null);
				} else {
					localProp.load(new FileInputStream(f));
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}	
	}
	
	/*
	 * Load FXML file
	 */
	private Parent initFXMLStuff(URL loc) {
		Parent root = null;
		try {
			loader = new FXMLLoader(loc);
			root = loader.load();
			controller = loader.<MainSceneController>getController();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return root;
	}
	
	
	public static void run(String[] args) {
		launch(args);
	}
}
