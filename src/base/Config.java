package base;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Config {
	public static int width = 1080, height = 600;
	public static double backgroundOpacity = 0.7;
	public static DoubleProperty snakeSpeed = new SimpleDoubleProperty(2);
	
	public static DoubleProperty volume = new SimpleDoubleProperty(0.5);
}
