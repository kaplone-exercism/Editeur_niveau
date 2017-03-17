package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
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
	private AnchorPane pane;
	@FXML
	private Pane root;
	
	
    private static Scene scene;
    private Rectangle copie;
    private ImageView copie_i;
    
	
	@Override
	public void start(Stage primaryStage) {

         try {		
			scene = new Scene((Parent) JfxUtils.loadFxml("Editeur_niveau_v2.fxml"), 1366, 720);
			
			primaryStage.setScene(scene);
			primaryStage.setWidth(1366);
			primaryStage.setHeight(720);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void controleMouvementDetected(Node n, MouseEvent e, String classe){
		
		n.setOpacity(0.5);
        n.toFront();
        n.setVisible(true);
        n.relocate(
                (int) (e.getSceneX() - n.getBoundsInLocal().getWidth() / 2),
                (int) (e.getSceneY() - n.getBoundsInLocal().getHeight() / 2));
        
        Dragboard dragBoard = n.startDragAndDrop(TransferMode.ANY);			
		ClipboardContent content = new ClipboardContent();	
		content.putString(n.toString());
		dragBoard.setContent(content);
        
		e.consume();
		
		switch (classe){
		
		case "mur" : Rectangle mur = (Rectangle) n;
		             copie = new Rectangle();
					 copie.setX(mur.getX());
					 copie.setY(mur.getY());
					 copie.setHeight(mur.getHeight());
					 copie.setWidth(mur.getWidth());
					 h_textField.setText("" + copie.getHeight());
					 l_textField.setText("" + copie.getWidth());	
					 pane.setOnDragOver(b -> controleMouvementOver(copie, b));
					 pane.setOnDragDropped(b -> controleMouvementDrop(copie, b));
					 
					 copie.setOnDragDetected(a -> controleMouvementDetected((Rectangle) a.getSource(), a, "mur_"));
					 copie.setOnDragDone(a -> controleMouvementDone((Rectangle) a.getSource(), a));
					 copie.setOnMouseClicked(a -> controleSelection((Rectangle) a.getSource()));
		             break;
		             
		case "mur_" : Rectangle mur_ = (Rectangle) n;
        			 h_textField.setText("" + mur_.getHeight());
        			 l_textField.setText("" + mur_.getWidth());	
        			 pane.setOnDragOver(b -> controleMouvementOver(mur_, b));
        			 pane.setOnDragDropped(b -> controleMouvementDrop(mur_, b));
        			 break;
		             
		case "image" : ImageView imv = (ImageView) n;
                       copie_i = new ImageView();
		               copie_i.setX(imv.getX());
		               copie_i.setY(imv.getY());
		               copie_i.setFitHeight(imv.getFitHeight());
		               copie_i.setFitWidth(imv.getFitWidth());
		               copie_i.setImage(imv.getImage());
		               pane.setOnDragOver(b -> controleMouvementOver(copie_i, b));
					   pane.setOnDragDropped(b -> controleMouvementDrop(copie_i, b));
					   
					   copie_i.setOnDragDetected(a -> controleMouvementDetected((ImageView) a.getSource(), a, "image_"));
					   copie_i.setOnDragDone(a -> controleMouvementDone((ImageView) a.getSource(), a));
		               break;
		               
  		case "image_" : ImageView imv_ = (ImageView) n;
  		               pane.setOnDragOver(b -> controleMouvementOver(imv_, b));
  					   pane.setOnDragDropped(b -> controleMouvementDrop(imv_, b));
  		               break;
  		}
		
		
		
	}
	
	public void controleSelection(Rectangle r){
		
		pane.getChildren().stream()
		                  .filter(a -> a instanceof Rectangle && ! ((Rectangle) a).getFill().equals(Color.valueOf("#d6ebff")))
		                  .forEach(a -> ((Rectangle)a).setFill(Color.BLACK));
		
		r.setFill(Color.CORAL);

		x_textField.setText("" + r.getX());
		y_textField.setText("" + r.getY());
		h_textField.setText("" + r.getHeight());
		l_textField.setText("" + r.getWidth());	
		
		
	}
	
    public void controleMouvementOver(Node n, DragEvent e){
    	if (! pane.getChildren().contains(n)){
			pane.getChildren().add(n);
		}
		
		n.relocate(
			(int) (e.getX() - n.getBoundsInLocal().getWidth() / 2),
			(int) (e.getY() - n.getBoundsInLocal().getHeight() / 2));
		
		e.acceptTransferModes(TransferMode.ANY);

		x_textField.setText("" + e.getX());
		y_textField.setText("" + e.getY());
		
		e.consume();
	}

    public void controleMouvementDrop(Node n, DragEvent e){
    	
    	 /* data dropped */
	        /* if there is a string data on dragboard, read it and use it */
	        Dragboard db = e.getDragboard();
	        boolean success = false;
	        if (db.hasString()) {
	           success = true;
	        }
	        /* let the source know whether the string was successfully 
	         * transferred and used */
	        e.setDropCompleted(success);
	        
	        e.consume();
    }
	
	public void controleMouvementDone(Node n, Event e){
		n.setOpacity(1);
	    e.consume();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		background.setOnMouseClicked(a -> controleSelection(null));
		
		mur_vertical.setOnDragDetected(a -> controleMouvementDetected(mur_vertical, a, "mur"));
		mur_vertical.setOnDragDone(a -> controleMouvementDone(mur_vertical, a));
		
		mur_horizontal.setOnDragDetected(a -> controleMouvementDetected(mur_horizontal, a, "mur"));
		mur_horizontal.setOnDragDone(a -> controleMouvementDone(mur_horizontal, a));
		
		personnage.setOnDragDetected(a -> controleMouvementDetected(personnage, a, "image"));
		personnage.setOnDragDone(a -> controleMouvementDone(personnage, a));
		
		goal.setOnDragDetected(a -> controleMouvementDetected(goal, a, "image"));
		goal.setOnDragDone(a -> controleMouvementDone(goal, a));

	}
}
