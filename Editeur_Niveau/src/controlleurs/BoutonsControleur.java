package controlleurs;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import utils.Contexte;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class BoutonsControleur implements Initializable {
	
	@FXML
	private Button openButton;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Button saveAsButton;
	
	private Main main;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		main = Contexte.getMain();	
		
		openButton.setOnAction(a -> System.out.println("open"));
		saveButton.setOnAction(a -> System.out.println("save"));
		saveAsButton.setOnAction(a -> System.out.println("save as"));
	}
}
