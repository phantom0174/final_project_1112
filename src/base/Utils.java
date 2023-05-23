package base;

/*

一些數學工具函數

*/


public class Utils {
	public static double easeInOut(double t) {
	    return t*(3 - t*t) / 2;
	}
	
	public static short sign(short n) {
		if (n > 0) return 1;
		else if (n == 0) return 0;
		return -1;
	}
	
	public static double rand(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}
}
