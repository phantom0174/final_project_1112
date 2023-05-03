package base;

public class ScoreDatum {
	public String name;
	public String score;
	
	public ScoreDatum(String n, String s) {
		this.name = n;
		this.score = s;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getScore() {
		return this.score;
	}
}
