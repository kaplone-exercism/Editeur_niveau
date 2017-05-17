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
    private Rectangle copie_p;
    
    private Rectangle murSelectionne;
    
    private Color colorRemplissage;
    private Color colorRemplissage_alt;
    private Color colorBordure;
    
    private final Color colorBordure_mur = Color.BLACK;
    private final Color colorRemplissage_mur = Color.DARKCYAN;
    private final Color colorRemplissage_mur_alt = Color.LIGHTCYAN;
    
    private final Color colorBordure_perso = Color.BLACK;
    private final Color colorRemplissage_perso = Color.DARKGOLDENROD;
    private final Color colorRemplissage_perso_alt = Color.LIGHTGOLDENRODYELLOW;
	
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
					 main.getPane().setOnDragDropped(b -> controleMouvementDrop(copie, b, "mur"));
					 
					 copie.setOnDragDetected(a -> controleMouvementDetected((Rectangle) a.getSource(), a, "mur_"));
					 copie.setOnDragDone(a -> controleMouvementDone((Rectangle) a.getSource(), a));
					 copie.setOnMouseClicked(a -> controleSelection((Rectangle) a.getSource(), "mur"));
					 copie.setOnKeyPressed(a -> main.getClavierControleur().controleClavier((Rectangle) a.getSource(), a));
		             break;
		             
		case "mur_" : Rectangle mur_ = (Rectangle) n;
        			 main.setH_textFieldText("" + mur_.getHeight());
        			 main.setL_textFieldText("" + mur_.getWidth());	
        			 main.getPane().setOnDragOver(b -> controleMouvementOver(mur_, b));
        			 main.getPane().setOnDragDropped(b -> controleMouvementDrop(mur_, b, "mur"));
        			 break;
        			 
		case "image_p" : ImageView imv_p = (ImageView) n;
		        copie_p = new Rectangle();
		        copie_p.setX(imv_p.getX());
		        copie_p.setY(imv_p.getY());
		        copie_p.setHeight(imv_p.getFitHeight());
		        copie_p.setWidth(imv_p.getFitWidth());
		        copie_p.setFill(Color.SALMON);
		        main.setH_textFieldText("" + copie_p.getHeight());
				main.setL_textFieldText("" + copie_p.getWidth());
		        main.getPane().setOnDragOver(b -> controleMouvementOver(copie_p, b));
				main.getPane().setOnDragDropped(b -> controleMouvementDrop(copie_p, b, "perso"));
				   
				copie_p.setOnDragDetected(a -> controleMouvementDetected((Rectangle) a.getSource(), a, "image_p_"));
				copie_p.setOnDragDone(a -> controleMouvementDone((Rectangle) a.getSource(), a));
				copie_p.setOnMouseClicked(a -> controleSelection((Rectangle) a.getSource(), "perso"));
				copie_p.setOnKeyPressed(a -> main.getClavierControleur().controleClavier((Rectangle) a.getSource(), a));
		        break;
		        
		case "image_p_" : Rectangle perso_ = (Rectangle) n;
		 				  main.setH_textFieldText("" + perso_.getHeight());
		 				  main.setL_textFieldText("" + perso_.getWidth());	
		 				  main.getPane().setOnDragOver(b -> controleMouvementOver(perso_, b));
		 				  main.getPane().setOnDragDropped(b -> controleMouvementDrop(perso_, b, "perso"));
		 				  break;
      
		             
		case "image" : ImageView imv = (ImageView) n;
                       copie_i = new ImageView();
		               copie_i.setX(imv.getX());
		               copie_i.setY(imv.getY());
		               copie_i.setFitHeight(imv.getFitHeight());
		               copie_i.setFitWidth(imv.getFitWidth());
		               copie_i.setImage(imv.getImage());
		               main.getPane().setOnDragOver(b -> controleMouvementOver(copie_i, b));
					   main.getPane().setOnDragDropped(b -> controleMouvementDrop(copie_i, b, "image"));
					   
					   copie_i.setOnDragDetected(a -> controleMouvementDetected((ImageView) a.getSource(), a, "image_"));
					   copie_i.setOnDragDone(a -> controleMouvementDone((ImageView) a.getSource(), a));
		               break;
		               
  		case "image_" : ImageView imv_ = (ImageView) n;
  		               main.getPane().setOnDragOver(b -> controleMouvementOver(imv_, b));
  					   main.getPane().setOnDragDropped(b -> controleMouvementDrop(imv_, b, "image"));
  		               break;
  		}	
	}

	public void controleSelection(Rectangle r, String type){
		
		if (type == null){
			type = "";
		}
		
		main.getBackground().requestFocus();
		
		main.setX_textFieldText("");
		main.setY_textFieldText("");
		main.setH_textFieldText("");
		main.setL_textFieldText("");	
		
		main.getX_textField().setVisible(false);
		main.getY_textField().setVisible(false);
		main.getH_textField().setVisible(false);
		main.getL_textField().setVisible(false);	
		
		murSelectionne = r;
		
		switch (type){
		case "mur" : colorRemplissage = colorRemplissage_mur;
		             colorRemplissage_alt = colorRemplissage_mur_alt;
		             colorBordure = colorBordure_mur;
		             break;
		case "perso" : colorRemplissage = colorRemplissage_perso;
		               colorRemplissage_alt = colorRemplissage_perso_alt;
		               colorBordure = colorBordure_perso;
		               break;
		default : murSelectionne = null;	
		}

		main.getPane().getChildren().forEach(a -> {
			if(a instanceof Rectangle){
				
				Rectangle a_ = (Rectangle) a;
				a_.setStrokeWidth(0);
				
				switch (a_.getFill().toString()) {
				case "0xffffffff" : a_.setStrokeWidth(1);
				    break;
				case "0xe0ffffff" : a_.setFill(colorRemplissage_mur);
				    break;
				case "0xfafad2ff" : a_.setFill(colorRemplissage_perso);
				    break;
				default:
					break;
				}
			}
			
		});
	
		if(r != null && ! r.getFill().equals(Color.WHITE)){
			r.setFill(colorRemplissage_alt);
			r.setStrokeWidth(1);
			r.setStroke(colorBordure);
			
			main.getX_textField().setVisible(true);
			main.getY_textField().setVisible(true);
			main.getH_textField().setVisible(true);
			main.getL_textField().setVisible(true);	
	
			main.setX_textFieldText("" + r.getLayoutX());
			main.setY_textFieldText("" + r.getLayoutY());
			main.setH_textFieldText("" + r.getHeight());
			main.setL_textFieldText("" + r.getWidth());	
			
			r.requestFocus();
		}
		else {
			murSelectionne = null;
		}
		
		Contexte.setElement(murSelectionne);
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

    public void controleMouvementDrop(Node n, DragEvent e, String type){
    	
    	if(n instanceof Rectangle){
     	   controleSelection((Rectangle) n, type);
     	   main.ajoutMur((Rectangle) n);
        }
    	else {
    		controleSelection(null, type);
    	}
    	
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
		//System.out.println("drag&dropControleur");
		n.setOpacity(1);
	    e.consume();
	}

	public Rectangle getMurSelectionne() {
		return murSelectionne;
	}
	
}
