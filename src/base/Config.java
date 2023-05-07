package base;

/*

遊戲全域設定，包括在選單中設定的數值與遊戲中動態變化的數值。

被用來作為各個 class 之間共同同步的變數區域。

*/


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Config {
	// 視窗解析度
	public static int width = 1080, height = 600;
	
	// 遊戲中背景透明度
	// 調太高會讓遊戲中個體對比度變低
	public static double backgroundOpacity = 0.7;
	
	public static DoubleProperty snakeSpeed = new SimpleDoubleProperty(2);
	
	// 遊戲中所有音效的音量大小，音效應要正規化到 -3 dB
	public static DoubleProperty volume = new SimpleDoubleProperty(0.5);
	
	// 單一比高分只能上傳一次的監控變數
	public static boolean scoreUploadCoda = true;
	
	// 遊戲難度的變數與分數加乘
	public static GameDiff difficulty = GameDiff.EASY;
	public static double scoreMultiplier = 1;
}
