package models;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Perso extends Rectangle implements Commun{
	
	private String nom;
	private Point2D position;

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
	    setHeight(hauteur);
		
	}

	@Override
	public void setCommonWidth(double largeur) {
		setWidth(largeur);
		
	}

	@Override
	public double getCommonHeight() {
		return super.getHeight();
	}

	@Override
	public double getCommonWidth() {
		return super.getWidth();
	}

}
