package main;

public class Point {

	private double[] point;
    private int cluster_number = 0;
    
    public void setPoint(double[] point) {
    	this.point = point;
    }
    
    public double[] getPoint() {
    	return this.point;
    }
    
    public void setCluster(int n) {
        this.cluster_number = n;
    }
    
    public int getCluster() {
        return this.cluster_number;
    }
    
    //Calculates the distance between two points.
    protected static double distance(Point p, Point centroid) {
    	double distancia = 0;
    	for(int i=0; i < 57; i++) {
    		distancia = distancia + Math.pow((centroid.getPoint()[i] - p.getPoint()[i]), 2);
    	}
        return Math.sqrt(distancia);
    }
}
