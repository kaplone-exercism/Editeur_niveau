package controlleurs;

import application.Main;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import utils.Contexte;

public class ClavierControleur {
	
	private Main main;
	
	public ClavierControleur(){
		main = Contexte.getMain();
	}
	
    public void controleClavier(Rectangle r, KeyEvent e){
    	
    	switch (e.getCode()){
    		case RIGHT : r.setLayoutX(r.getLayoutX() + 1);
        		         break;
    		case LEFT : r.setLayoutX(r.getLayoutX() - 1);
				        break;
    		case UP : r.setLayoutY(r.getLayoutY() - 1);
        		         break;
    		case DOWN : r.setLayoutY(r.getLayoutY() + 1);
				        break;
    		case ADD :  if (r.getWidth() > r.getHeight()){
    			r.setWidth(r.getWidth() + 1);
    		}
    		else {
    			r.setHeight(r.getHeight() + 1);
    		}
	                    break;
            case SUBTRACT :if (r.getWidth() > r.getHeight()){
    			r.setWidth(Math.max(14, r.getWidth() - 1));
    		}
    		else {
    			r.setHeight(Math.max(14, r.getHeight() - 1));
    		}
	                    break;
		    default :
    	}
    	e.consume();
    	
    	main.setX_textFieldText("" + r.getLayoutX());
    	main.setY_textFieldText("" + r.getLayoutY());
    	main.setH_textFieldText("" + r.getHeight());
    	main.setL_textFieldText("" + r.getWidth());
    	
	}
}
