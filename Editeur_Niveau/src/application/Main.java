package application;

import java.net.URL;
import java.nio.CharBuffer;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javafx.stage.FileChooser;

import controlleurs.ClavierControleur;
import controlleurs.DragAndDropControleur;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import models.Mur;
import utils.Contexte;
import utils.RestAccess;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
	private TextField nomTextField;
	@FXML
	private TextField prezTextField;
	
	@FXML
	private Button openButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button saveAsButton;
	@FXML
	private Button exportButton;
	
	@FXML
	private Pane pane;
	@FXML
	private Pane root;
	
	
    private static Scene scene;
        
    private ClavierControleur clavierControleur;
    private DragAndDropControleur dragAndDropControleur;
    
    private File file;
    
    private List<Rectangle> murs;
    
	
	@Override
	public void start(Stage primaryStage) {

         try {		
			scene = new Scene((Parent) JfxUtils.loadFxml("Editeur_niveau_v3.fxml"), 1366, 760);
			
			primaryStage.setScene(scene);
			primaryStage.setWidth(1366);
			primaryStage.setHeight(760);
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
		
		murs = new ArrayList<>();
		 
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
	
		openButton.setOnAction(a -> on_select_file_button("open"));
		saveButton.setOnAction(a -> on_select_file_button("save"));
		saveAsButton.setOnAction(a -> on_select_file_button("save as"));
		exportButton.setOnAction(a -> export());

	}
	
    protected File chooseExport(){
    	
    	if (file != null){
    		return file;
    	}
    	else {
    		return chooseExportAs();
    	}		
	}
    
    protected File chooseExportAs(){
		
		Stage newStage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à exporter");
		fileChooser.setInitialFileName(nomTextField.getText());
		fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("Shape In The Mazes", "*.sitm"));
		File selectedFile = fileChooser.showSaveDialog(newStage);
		
		return selectedFile;		
	}
    
    protected File chooseImport(){
		
		Stage newStage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Fichier à importer");
		fileChooser.getExtensionFilters().addAll(
		         new FileChooser.ExtensionFilter("Shape In The Mazes", "*.sitm"));
		File selectedFile = fileChooser.showOpenDialog(newStage);
		if (selectedFile != null) {
			 return selectedFile;
		}
		else {
			 return (File) null;
		}
		
	}
    
    protected void ajoutMurDepuisLignesauvegarde(String l){
    		
    	if (l.split(",")[0].split("=")[1].trim().equals("VERTICAL")){
    		Rectangle temp = new Rectangle(Double.parseDouble(l.split(",")[1].trim()),
    									   Double.parseDouble(l.split(",")[4].trim()) - Double.parseDouble(l.split(",")[3].trim()));
    		temp.setLayoutX(Double.parseDouble(l.split(",")[2].trim()));
    		temp.setLayoutY(Double.parseDouble(l.split(",")[3].trim()));
    		temp.setFill(Color.DARKCYAN);
    		
			murs.add(temp);				
		}
    	else if (l.split(",")[0].split("=")[1].trim().equals("HORIZONTAL")){
    		Rectangle temp = new Rectangle(Double.parseDouble(l.split(",")[4].trim()) - Double.parseDouble(l.split(",")[3].trim()),
    									   Double.parseDouble(l.split(",")[1].trim()));
			temp.setLayoutX(Double.parseDouble(l.split(",")[3].trim()));
    		temp.setLayoutY(Double.parseDouble(l.split(",")[2].trim()));
    		temp.setFill(Color.DARKCYAN);
    		
    		murs.add(temp);
        } 	
    }
    
    protected void ajoutPersoDepuisLignesauvegarde(String l){
    	
    	Rectangle temp = new Rectangle(Double.parseDouble(l.split(",")[3].trim()),
				                       Double.parseDouble(l.split(",")[4].trim()));
    	temp.setLayoutX(Double.parseDouble(l.split(",")[1].trim()));
    	temp.setLayoutY(Double.parseDouble(l.split(",")[2].trim()));
    	temp.setFill(Color.DARKGOLDENROD);  

    	pane.getChildren().add(temp);
    	
    	dragAndDropControleur = new DragAndDropControleur();
    	temp.setOnDragDetected(a -> dragAndDropControleur.controleMouvementDetected(temp, a, "image_p_"));
    	temp.setOnDragDone(a -> dragAndDropControleur.controleMouvementDone(temp, a));
    }
    
    protected void ajoutGoalDepuisLignesauvegarde(String l){
    	
    	ImageView temp = new ImageView("goal.png");
    	temp.setLayoutX(Double.parseDouble(l.split(",")[0].split("=")[1].trim()));
    	temp.setLayoutY(Double.parseDouble(l.split(",")[1].trim()));
    	
    	pane.getChildren().add(temp);
    	
    	dragAndDropControleur = new DragAndDropControleur();
    	temp.setOnDragDetected(a -> dragAndDropControleur.controleMouvementDetected(temp, a, "image_"));
    	temp.setOnDragDone(a -> dragAndDropControleur.controleMouvementDone(temp, a));
    }
    
	public void on_select_file_button(String effet){
		
		switch (effet) {
		case "open" : file = chooseImport();
		                murs.clear();
		                pane.getChildren().clear();
		                
		                Rectangle fond = new Rectangle(1000, 600);
		                fond.setFill(Color.WHITE);

		                pane.getChildren().add(fond);
		                
		                try (Stream<String> stream = Files.lines(file.toPath())) {	
							stream.filter(a -> a.startsWith("Perso"))
							      .forEach(b -> ajoutPersoDepuisLignesauvegarde(b));
						} catch (IOException e) {
							e.printStackTrace();
						}
		                
		                try (Stream<String> stream = Files.lines(file.toPath())) {	
							stream.filter(a -> a.startsWith("Goal"))
							      .forEach(b -> ajoutGoalDepuisLignesauvegarde(b));
						} catch (IOException e) {
							e.printStackTrace();
						}
		                
						try (Stream<String> stream = Files.lines(file.toPath())) {	
							stream.filter(a -> a.startsWith("Mur") && Double.parseDouble(a.split(",")[1].trim()) > 5)
							      .forEach(b -> ajoutMurDepuisLignesauvegarde(b));
						} catch (IOException e) {
							e.printStackTrace();
						}
						DragAndDropControleur dd = new DragAndDropControleur();
						for (Rectangle rectangle : murs) {
							pane.getChildren().add(rectangle);
							pane.setOnDragOver(b -> dd.controleMouvementOver(rectangle, b));
							pane.setOnDragDropped(b -> dd.controleMouvementDrop(rectangle, b, "mur"));
							 
							rectangle.setOnDragDetected(a -> dd.controleMouvementDetected((Rectangle) a.getSource(), a, "mur_"));
							rectangle.setOnDragDone(a -> dd.controleMouvementDone((Rectangle) a.getSource(), a));
							rectangle.setOnMouseClicked(a -> dd.controleSelection((Rectangle) a.getSource(), "mur"));
							rectangle.setOnKeyPressed(a -> getClavierControleur().controleClavier((Rectangle) a.getSource(), a));
						}
						
		              break;
		case "save" : file = chooseExport();
		              ecrireFichier();
        			  break;  
		case "save as" : file = chooseExportAs();
		              ecrireFichier();
		              break; 
		}
		
		System.out.println(file.getName());
	}
	
	protected void ecrireFichier(){
		
		if (! file.getName().endsWith(".sitm")){
			if (file.getName().contains(".")){
				file = new File(file.getPath().split("\\.")[0] + ".sitm");
			}
			else {
				file = new File(file.getPath() + ".sitm");
			}
		}
		
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(nouveauNiveau());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String nouveauNiveau(){
		
		String res = "";
		
		res += String.format("\n\n[%s]\n", nomTextField.getText().trim().length() > 0 ? nomTextField.getText().trim().replace(" ", "_") : "test" );
		res += "\n";
		res += "Goal = 900, 300\n";
		res += String.format("Infos = \"%s\"; 500; 300; 300\n", prezTextField.getText());
		res += "\n";
		res += "Mur = VERTICAL,    5,       0,        	0,   600\n";
		res += "Mur = VERTICAL,    5,     995,        	0,   600\n";
		res += "Mur = HORIZONTAL,  5,       0,        	0,  1000\n";
		res += "Mur = HORIZONTAL,  5,     595,        	0,  1000\n";
		res += "\n";
		
		for (Rectangle r : murs){
			if (r.getHeight() > r.getWidth()){
				res += String.format("Mur = VERTICAL,   %d,     %d,          %d,   %d\n", Math.round(r.getWidth()), Math.round(r.getLayoutX()), Math.round(r.getLayoutY()), Math.round(r.getLayoutY() + r.getHeight()));					}
			
			else {
				res += String.format("Mur = HORIZONTAL,   %d,     %d,          %d,   %d\n", Math.round(r.getHeight()), Math.round(r.getLayoutY()), Math.round(r.getLayoutX()), Math.round(r.getLayoutX() + r.getWidth()));
			}
		}
		
		res += "\n";
		res += String.format("Perso = BLACK, %d, %d, %d, %d", Math.round(personnage.getLayoutX()), Math.round(personnage.getLayoutY()), Math.round(personnage.getFitWidth()), Math.round(personnage.getFitHeight()));
		
		return res;
		
	}
	
	private void export(){
		
		RestAccess.connect();
		
		try {
			String home =  System.getProperty("user.home");
			SnapshotParameters sp = new SnapshotParameters();
			WritableImage image = pane.snapshot(sp, null);
			File file = new File(home, "tmp_0001.png");
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println(RestAccess.ajouter(nomTextField.getText().trim().length() > 0 ? nomTextField.getText().trim().replace(" ", "_") : "test", file));
            System.out.println(RestAccess.ajouter(nouveauNiveau()));
        } catch (IOException e) {
            // TODO: handle exception here
        }
		
		
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
	
	public void ajoutMur(Rectangle m){
		
		boolean nouveau = false;
		
		if (murs.isEmpty()){
			murs.add(m);
		}
		else {
			for (Rectangle r : murs){
				if (m.getLayoutX() == r.getLayoutX()
				&&  m.getLayoutY() == r.getLayoutY()
				&&  m.getWidth() == r.getWidth()
				&&  m.getHeight() == r.getHeight()){
					r.setWidth(m.getWidth());
					r.setHeight(m.getHeight());
					r.setLayoutX(m.getLayoutX());
					r.setLayoutY(m.getLayoutY());
					nouveau = false;
					break;
				}
				else {
					nouveau = true;
				}
			}
			
			if (nouveau) {
				murs.add(m);
			}
		}		
	}
}
