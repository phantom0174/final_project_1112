package base;

import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.Node;

public class Grid2D<T> {
	public double spaceWidth;
	public double dimension; // initial: 300, need to be larger than the largest radius of planet
	public ArrayList[][] grid;
	
	public int maxIndex;
	public double offset;
	
	public Grid2D(double dim, double spaceWidth, double offset) {
		this.dimension = dim;
		this.spaceWidth = spaceWidth;
		this.offset = offset;
		
		this.maxIndex = (int) (spaceWidth / dim) - 1;
		this.grid = new ArrayList[maxIndex + 1][maxIndex + 1];
		
		initializeGrid();
	}
	
	private void initializeGrid() {
		for (int i = 0; i <= maxIndex; i++) {
			for (int j = 0; j <= maxIndex; j++) {
				grid[i][j] = new ArrayList<T>();
			}
		}
	}
	
	public boolean insert(Node n) {
		double x = n.getTranslateX() + offset,
				z = n.getTranslateZ() + offset;
		
		if (x < 0 || z < 0 || x >= spaceWidth || z >= spaceWidth) return false;
		
		int xIndex = (int) (x / dimension),
			zIndex = (int) (z / dimension);
		
		grid[xIndex][zIndex].add(n);
		return true;
	}
	
	public ArrayList<ArrayList<T>> query(Point3D p) {
		double x = p.getX() + offset,
				z = p.getZ() + offset;
		
		int centXInd = (int) (x / dimension),
			centZInd = (int) (z / dimension);
		
		int startXInd = Math.max(centXInd - 1, 0),
			endXInd = Math.min(centXInd + 1, maxIndex),
			startZInd = Math.max(centZInd - 1, 0),
			endZInd = Math.min(centZInd + 1, maxIndex);
		
		ArrayList<ArrayList<T>> targets = new ArrayList<>();
		for (int i = startXInd; i <= endXInd; i++) {
			for (int j = startZInd; j <= endZInd; j++) {
				targets.add(grid[i][j]);
			}
		}
		return targets;
	}
}
