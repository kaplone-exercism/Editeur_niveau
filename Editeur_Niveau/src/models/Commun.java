package models;

import javafx.geometry.Point2D;

public interface Commun{
	
	public String getNom();
	public Point2D getPosition();
	public double getCommonHeight();
	public double getCommonWidth();
	
	public void setNom(String nom);
	public void setPosition(Point2D position);
	public void setCommonHeight(double hauteur);
	public void setCommonWidth(double largeur);

}
