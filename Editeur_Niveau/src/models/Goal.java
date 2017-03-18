package models;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class Goal extends ImageView implements Commun{
	
	private String nom;
	private Point2D position;
	private double hauteur;
	private double largeur;

	public Goal() {
		super();
	}

	public Goal(Goal g) {
		super();
		this.nom = g.getNom();
		this.position = g.getPosition();
		this.hauteur = g.getCommonHeight();
		this.largeur = g.getCommonWidth();
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public void setNom(String nom) {
		this.nom = nom;
		
	}

	@Override
	public void setPosition(Point2D position) {
		this.position = position;
		
	}

	@Override
	public void setCommonHeight(double hauteur) {
	    setFitHeight(hauteur);
		
	}

	@Override
	public void setCommonWidth(double largeur) {
		setFitWidth(largeur);
		
	}

	@Override
	public double getCommonHeight() {
		return super.getFitHeight();
	}

	@Override
	public double getCommonWidth() {
		return super.getFitWidth();
	}


}
