package utils;

import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.Commun;
import models.Goal;
import models.Mur;
import models.Perso;

public class DragControl {

	private Mur copie;
	private Goal copie_g;
	private Perso copie_p;

	private Pane pane;
	private TextField id_textField, x_textField, y_textField, h_textField, l_textField;
	
	private Dragboard dragBoard;

	public DragControl() {
		super();
	}

	public DragControl(Pane pane, TextField id_textField, TextField x_textField, TextField y_textField, TextField h_textField,
			TextField l_textField) {
		super();
		this.pane = pane;
		this.id_textField = id_textField;
		this.x_textField = x_textField;
		this.y_textField = y_textField;
		this.h_textField = h_textField;
		this.l_textField = l_textField;
	}

	public void controleMouvementDetected(Commun n, MouseEvent e, String classe) {

		
		ClipboardContent content = new ClipboardContent();
		content.putString(n.toString());
		dragBoard.setContent(content);

		e.consume();

		switch (classe) {

		case "mur":
			Mur n_ = (Mur) n;
			copie = new Mur(n_);
			
			n_.setOpacity(0.5);
			n_.toFront();
			n_.setVisible(true);
			n_.relocate((int) (e.getSceneX() - n_.getBoundsInLocal().getWidth() / 2),
					(int) (e.getSceneY() - n_.getBoundsInLocal().getHeight() / 2));

			dragBoard = n_.startDragAndDrop(TransferMode.ANY);
			
			h_textField.setText("" + copie.getHeight());
			l_textField.setText("" + copie.getWidth());
			
			pane.setOnDragOver(b -> controleMouvementOver(copie, b));
			pane.setOnDragDropped(b -> controleMouvementDrop(copie, b, "mur"));

			copie.setOnDragDetected(a -> controleMouvementDetected((Mur) a.getSource(), a, "mur_"));
			copie.setOnDragDone(a -> controleMouvementDone((Mur) a.getSource(), a));
			copie.setOnMouseClicked(a -> controleSelection((Mur) a.getSource()));
			break;

		case "mur_":
			Mur mur_ = (Mur) n;
			
			
			
			h_textField.setText("" + mur_.getHeight());
			l_textField.setText("" + mur_.getWidth());
			pane.setOnDragOver(b -> controleMouvementOver(mur_, b));
			pane.setOnDragDropped(b -> controleMouvementDrop(mur_, b, "mur"));
			break;

		case "image_g":
			Goal imv = (Goal) n;
			copie_g = new Goal(imv);
			
			pane.setOnDragOver(b -> controleMouvementOver(copie_g, b));
			pane.setOnDragDropped(b -> controleMouvementDrop(copie_g, b, "image"));

			copie_g.setOnDragDetected(a -> controleMouvementDetected((Goal) a.getSource(), a, "image_"));
			copie_g.setOnDragDone(a -> controleMouvementDone((Goal) a.getSource(), a));
			break;

		case "image_g_":
			Goal imv_ = (Goal) n;
			pane.setOnDragOver(b -> controleMouvementOver(imv_, b));
			pane.setOnDragDropped(b -> controleMouvementDrop(imv_, b, "image"));
			break;
		}

	}

	public void controleSelection(Mur r) {

		pane.getChildren().stream()
				.filter(a -> a instanceof Mur && !((Mur) a).getFill().equals(Color.valueOf("#d6ebff")))
				.forEach(a -> ((Mur) a).setFill(Color.BLACK));
		
		

		if (r != null) {
			r.setFill(Color.CORAL);
			id_textField.setText(r.getNom());
			x_textField.setText("" + r.getPosition().getX());
			y_textField.setText("" + r.getPosition().getY());
			h_textField.setText("" + r.getCommonHeight());
			l_textField.setText("" + r.getCommonWidth());
		} else {
			id_textField.setText("");
			x_textField.setText("");
			y_textField.setText("");
			h_textField.setText("");
			l_textField.setText("");
		}
	}

	public void controleMouvementOver(Node n, DragEvent e) {
		if (!pane.getChildren().contains(n)) {
			pane.getChildren().add(n);
		}

		n.relocate((int) (e.getX() - n.getBoundsInLocal().getWidth() / 2),
				(int) (e.getY() - n.getBoundsInLocal().getHeight() / 2));

		e.acceptTransferModes(TransferMode.ANY);

		x_textField.setText("" + e.getX());
		y_textField.setText("" + e.getY());

		e.consume();
	}

	public void controleMouvementDrop(Commun n, DragEvent e, String classe) {

		/* data dropped */
		/* if there is a string data on dragboard, read it and use it */
		Dragboard db = e.getDragboard();
		boolean success = false;
		if (db.hasString()) {
			
			switch (classe){
			case "mur" : Mur mur = (Mur) n;
			             mur.setPosition(new Point2D(Double.parseDouble(x_textField.getText()),
			            		 					 Double.parseDouble(y_textField.getText())));
			             mur.setNom(id_textField.getText());
			             mur.setHeight(Double.parseDouble(h_textField.getText()));
			             mur.setWidth(Double.parseDouble(l_textField.getText()));
			}

			success = true;
		}
		/*
		 * let the source know whether the string was successfully transferred
		 * and used
		 */
		e.setDropCompleted(success);

		e.consume();
	}

	public void controleMouvementDone(Node n, Event e) {
		System.out.println("dragControl");
		n.setOpacity(1);
		e.consume();
	}

}
