package controlleurs;

import application.Main;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.Contexte;

public class DragAndDropControleur {
	
	private Main main;
	private Rectangle copie;
    private ImageView copie_i;
    
    private Rectangle murSelectionne;
	
	public DragAndDropControleur(){
		main = Contexte.getMain();
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
					 main.setH_textFieldText("" + copie.getHeight());
					 main.setL_textFieldText("" + copie.getWidth());	
					 main.getPane().setOnDragOver(b -> controleMouvementOver(copie, b));
					 main.getPane().setOnDragDropped(b -> controleMouvementDrop(copie, b));
					 
					 copie.setOnDragDetected(a -> controleMouvementDetected((Rectangle) a.getSource(), a, "mur_"));
					 copie.setOnDragDone(a -> controleMouvementDone((Rectangle) a.getSource(), a));
					 copie.setOnMouseClicked(a -> controleSelection((Rectangle) a.getSource()));
					 copie.setOnKeyPressed(a -> main.getClavierControleur().controleClavier((Rectangle) a.getSource(), a));
		             break;
		             
		case "mur_" : Rectangle mur_ = (Rectangle) n;
        			 main.setH_textFieldText("" + mur_.getHeight());
        			 main.setL_textFieldText("" + mur_.getWidth());	
        			 main.getPane().setOnDragOver(b -> controleMouvementOver(mur_, b));
        			 main.getPane().setOnDragDropped(b -> controleMouvementDrop(mur_, b));
        			 break;
		             
		case "image" : ImageView imv = (ImageView) n;
                       copie_i = new ImageView();
		               copie_i.setX(imv.getX());
		               copie_i.setY(imv.getY());
		               copie_i.setFitHeight(imv.getFitHeight());
		               copie_i.setFitWidth(imv.getFitWidth());
		               copie_i.setImage(imv.getImage());
		               main.getPane().setOnDragOver(b -> controleMouvementOver(copie_i, b));
					   main.getPane().setOnDragDropped(b -> controleMouvementDrop(copie_i, b));
					   
					   copie_i.setOnDragDetected(a -> controleMouvementDetected((ImageView) a.getSource(), a, "image_"));
					   copie_i.setOnDragDone(a -> controleMouvementDone((ImageView) a.getSource(), a));
		               break;
		               
  		case "image_" : ImageView imv_ = (ImageView) n;
  		               main.getPane().setOnDragOver(b -> controleMouvementOver(imv_, b));
  					   main.getPane().setOnDragDropped(b -> controleMouvementDrop(imv_, b));
  		               break;
  		}	
	}

	public void controleSelection(Rectangle r){
		
		main.getBackground().requestFocus();
		main.setX_textFieldText("");
		main.setY_textFieldText("");
		main.setH_textFieldText("");
		main.setL_textFieldText("");	
		
		murSelectionne = r;
		
		main.getPane().getChildren().stream()
        							.filter(a -> a instanceof Rectangle && ! ((Rectangle) a).getFill().equals(Color.valueOf("#d6ebff")))
        							.forEach(a -> ((Rectangle)a).setFill(Color.BLACK));
		
		if(r != null && ! r.getFill().equals(Color.valueOf("#d6ebff"))){

			r.setFill(Color.CORAL);
			main.setX_textFieldText("" + r.getLayoutX());
			main.setY_textFieldText("" + r.getLayoutY());
			main.setH_textFieldText("" + r.getHeight());
			main.setL_textFieldText("" + r.getWidth());	
			
			r.requestFocus();
		}
		else {
			r = null;
			murSelectionne = r;
		}
		
		Contexte.setMur(murSelectionne);
	}
	
    public void controleMouvementOver(Node n, DragEvent e){
    	if (! main.getPane().getChildren().contains(n)){
			main.getPane().getChildren().add(n);
		}
		
		n.relocate(
			(int) (e.getX() - n.getBoundsInLocal().getWidth() / 2),
			(int) (e.getY() - n.getBoundsInLocal().getHeight() / 2));
		
		e.acceptTransferModes(TransferMode.ANY);

		main.setX_textFieldText("" + e.getX());
		main.setY_textFieldText("" + e.getY());
		
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

	public Rectangle getMurSelectionne() {
		return murSelectionne;
	}
	
	

}
