package models;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Mur extends Rectangle implements Commun{
	
	private String nom;
	private Point2D position;
	private double hauteur;
	private double largeur;

	public Mur() {
		super();
	}

	public Mur(Mur m) {
		
		super();
		this.nom = m.getNom();
		this.position = m.getPosition();
		this.hauteur = m.getCommonHeight();
		this.largeur = m.getCommonWidth();
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
		this.hauteur = hauteur;
		
	}

	@Override
	public void setCommonWidth(double largeur) {
		this.largeur = largeur;
		
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
