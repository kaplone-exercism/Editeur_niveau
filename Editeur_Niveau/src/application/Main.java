package application;

import java.net.URL;
import java.util.ResourceBundle;

import java.io.File;
import javafx.stage.FileChooser;

import controlleurs.ClavierControleur;
import controlleurs.DragAndDropControleur;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import utils.Contexte;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Main extends Application implements Initializable{
	
	@FXML
	private Rectangle mur_vertical;
	@FXML
	private Rectangle mur_horizontal;
	@FXML
	private Rectangle background;
	
	@FXML
	private ImageView personnage;
	@FXML
	private ImageView goal;
	
	@FXML
	private TextField id_textField;
	@FXML
	private TextField x_textField;
	@FXML
	private TextField y_textField;
	@FXML
	private TextField h_textField;
	@FXML
	private TextField l_textField;
	
	@FXML
	private Button openButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button saveAsButton;
	
	@FXML
	private Pane pane;
	@FXML
	private Pane root;
	
	
    private static Scene scene;
        
    private ClavierControleur clavierControleur;
    private DragAndDropControleur dragAndDropControleur;
    
    private File file;
    
	
	@Override
	public void start(Stage primaryStage) {
		
		System.out.println("start");

         try {		
			scene = new Scene((Parent) JfxUtils.loadFxml("Editeur_niveau_v3.fxml"), 1366, 720);
			
			primaryStage.setScene(scene);
			primaryStage.setWidth(1366);
			primaryStage.setHeight(720);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		} 
         
        Contexte.setStage(primaryStage);
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("initialize");
		
		 
        Contexte.setMain(this);

        clavierControleur = new ClavierControleur();
        dragAndDropControleur = new DragAndDropControleur();
		
		background.setOnMouseClicked(a -> dragAndDropControleur.controleSelection(null, ""));
		
		mur_vertical.setOnDragDetected(a -> dragAndDropControleur.controleMouvementDetected(mur_vertical, a, "mur"));
		mur_vertical.setOnDragDone(a -> dragAndDropControleur.controleMouvementDone(mur_vertical, a));
		
		mur_horizontal.setOnDragDetected(a -> dragAndDropControleur.controleMouvementDetected(mur_horizontal, a, "mur"));
		mur_horizontal.setOnDragDone(a -> dragAndDropControleur.controleMouvementDone(mur_horizontal, a));
		
		personnage.setOnDragDetected(a -> dragAndDropControleur.controleMouvementDetected(personnage, a, "image_p"));
		personnage.setOnDragDone(a -> dragAndDropControleur.controleMouvementDone(personnage, a));
		
		goal.setOnDragDetected(a -> dragAndDropControleur.controleMouvementDetected(goal, a, "image"));
		goal.setOnDragDone(a -> dragAndDropControleur.controleMouvementDone(goal, a));
		
		h_textField.textProperty().addListener((other, old_value, new_value ) -> {
			if (Contexte.getElement() != null && h_textField.isFocused()){
				Contexte.getElement().setHeight(Double.parseDouble(new_value));
			}
		});
		
		l_textField.textProperty().addListener((other, old_value, new_value ) -> {
			if (Contexte.getElement() != null && l_textField.isFocused()){
				Contexte.getElement().setWidth(Double.parseDouble(new_value));
			}
		});
		
		x_textField.textProperty().addListener((other, old_value, new_value ) -> {
			if (Contexte.getElement() != null && x_textField.isFocused()){
				Contexte.getElement().setLayoutX(Double.parseDouble(new_value));
			}
		});
		
		y_textField.textProperty().addListener((other, old_value, new_value ) -> {
			if (Contexte.getElement() != null && y_textField.isFocused()){
				Contexte.getElement().setLayoutY(Double.parseDouble(new_value));
			}
		});
	
		openButton.setOnAction(a -> on_select_file_button());
		saveButton.setOnAction(a -> System.out.println("save"));
		saveAsButton.setOnAction(a -> System.out.println("save as"));

	}
	
    protected File chooseExport(){
		
		Stage newStage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à importer");
		fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("conf Shape In The Mazes", "*.sitm"));
		File selectedFile = fileChooser.showOpenDialog(newStage);
		if (selectedFile != null) {
			 return selectedFile;
		}
		else {
			 return (File) null;
		}
		
	}
    
	public void on_select_file_button(){
		
		file = chooseExport();
		System.out.println(file.getName());
	}
	
	public void setX_textFieldText(String s){
		x_textField.setText(s);
	}
	public void setY_textFieldText(String s){
		y_textField.setText(s);
	}
	public void setH_textFieldText(String s){
		h_textField.setText(s);
	}
	public void setL_textFieldText(String s){
		l_textField.setText(s);
	}

	public Pane getPane() {
		return pane;
	}

	public ClavierControleur getClavierControleur() {
		return clavierControleur;
	}

	public DragAndDropControleur getDragAndDropControleur() {
		return dragAndDropControleur;
	}

	public Rectangle getBackground() {
		return background;
	}

	public TextField getX_textField() {
		return x_textField;
	}

	public TextField getY_textField() {
		return y_textField;
	}

	public TextField getH_textField() {
		return h_textField;
	}

	public TextField getL_textField() {
		return l_textField;
	}
	
	
	
	
}
