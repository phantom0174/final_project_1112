package base;

/*

一些數學工具函數

*/


public class Utils {
	public static double easeInOut(double t) {
	    return t * t * (3 - 2 * t);
	}
	
	public static short sign(short n) {
		if (n > 0) return 1;
		else if (n == 0) return 0;
		return -1;
	}
}
