package utils;

import application.Main;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Contexte {
	
	private static Main main;
	private static Stage stage;
	private static Rectangle mur;
	
	public static Main getMain() {
		return main;
	}
	public static void setMain(Main main) {
		Contexte.main = main;
	}
	public static Stage getStage() {
		return stage;
	}
	public static void setStage(Stage stage) {
		Contexte.stage = stage;
	}
	public static Rectangle getMur() {
		return mur;
	}
	public static void setMur(Rectangle mur) {
		Contexte.mur = mur;
	}
	
}
