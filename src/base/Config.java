package base;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Config {
	public static int width = 1080, height = 600;
	public static double backgroundOpacity = 0.9;
	public static double snakeSpeed = 5;
	
	public static DoubleProperty volume = new SimpleDoubleProperty(2);
}
