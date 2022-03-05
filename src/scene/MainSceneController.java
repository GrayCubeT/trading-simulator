package scene;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Main scene controller class
 * Main menu stuff and controls for it are here
 * 
 * Params: its loader (is it needed?)
 * the primary stage -> needed for calculations and handlers
 * local properties (same as options)
 * global properties (?)
 */
public class MainSceneController implements Initializable{
	@SuppressWarnings("unused")
	private FXMLLoader loader;
	private Stage primaryStage;
	private Properties localProp;
	@SuppressWarnings("unused")
	private Properties globalProp;
	
	@FXML
	private Text tipsText;
    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private TabPane MainTabPane;
    @FXML
    private AnchorPane PlayPane;
    @FXML
    private AnchorPane NewsPane;
    @FXML
    private AnchorPane ProfilePane;
    @FXML
    private AnchorPane LearnPane;
    @FXML
    private AnchorPane OptionsPane;
    @FXML
    private Label option1_label;
    @FXML
    private Slider option1_control;
    @FXML
    private Button btnExit;
    
    
    /*
     * Initialize function works before everything is even set on a scene
     * Online loading of stuff may be here (?)
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	/*
	 * Initialize options screen, animations and handlers
	 */
	public void initStuff(FXMLLoader loader, Stage stage, Properties l, Properties g) {
		this.loader = loader;
		primaryStage = stage;
		localProp = l;
		globalProp = g;
		
		initHandlers();
		initAnimations();
		initValues();
	}
	
	private void initValues() {
		// set correct option values
		option1_control.valueProperty().set(Double.valueOf(localProp.getProperty("option1")));
		option1_label.textProperty().setValue(String.valueOf(option1_control.valueProperty().intValue()));
	}
	
	/*
	 * Initialize animations
	 * 
	 * They need to be recalculated for each resize of the window
	 */
	private void initAnimations() {
		// running text on top of the window -> news or tips
		// TODO online loading of that text
		tipsText.setText("TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS TIPS");
		double sceneWidth = primaryStage.getScene().getWidth();
	    double msgWidth = tipsText.getLayoutBounds().getWidth();
	    
	    
	    final Timeline timeline = new Timeline();
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    
	    KeyValue initKeyValue = new KeyValue(tipsText.translateXProperty(), sceneWidth);
	    KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
	    
	    final KeyValue kv = new KeyValue(tipsText.translateXProperty(), -sceneWidth);
	    final KeyFrame kf = new KeyFrame(Duration.seconds((sceneWidth + msgWidth) / 50), kv);
	    
	    timeline.getKeyFrames().add(initFrame);
	    timeline.getKeyFrames().add(kf);
	    timeline.play();
	}
	
	/* 
	 * Initialize handlers for all the stuff the user might do
	 */
	private void initHandlers() {
		// keypress 
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,  (event) -> {
		    System.out.println("Key pressed: " + event.getCode());
		});
		
		// options change handler
		// TODO other options
		option1_control.valueProperty().addListener((observable, oldValue, newValue) -> {
			option1_label.textProperty().setValue(String.valueOf(newValue.intValue()));
			localProp.setProperty("option1", String.valueOf(newValue.intValue()));
		});
		
		// options save
		MainTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldv, newv) -> {
			if (oldv.intValue() == 4) {
				try {
					localProp.store(new FileOutputStream(new File("config.ini")), null);					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// exit button handler
		btnExit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				primaryStage.close();
			}
		});
		
		// TODO window resize handler
		
		
		
	}
	
}

