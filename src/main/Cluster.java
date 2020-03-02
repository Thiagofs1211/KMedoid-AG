package main;

import java.util.ArrayList;
import java.util.List;
 
public class Cluster {
	
	private List<Point> points;
	private Point medoid;
	private int posMedoid;
	private int id;
	private int classe;
	
	//Creates a new Cluster
	public Cluster(int id) {
		this.id = id;
		this.points = new ArrayList<Point>();
	}
	
	public int getClasse() {
		return this.classe;
	}
	
	public void setClasse(int classe) {
		this.classe = classe;
	}
 
	public List<Point> getPoints() {
		return points;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
 
	public void setPoints(List<Point> points) {
		this.points = points;
	}
 
	public Point getMedoid() {
		return medoid;
	}
 
	public void setMedoid(Point centroid) {
		this.medoid = centroid;
	}
 
	public int getId() {
		return id;
	}
	
	public void clear() {
		points.clear();
	}
	
	public void plotCluster() {
		System.out.println("[Cluster: " + id+"]");
		System.out.println("[Medoid: " + medoid + "]");
		System.out.println("[Points: \n");
		for(Point p : points) {
			for(int i = 0;i < 58; i++) {
				System.out.print(p.getPoint()[i]+",");
			}
			System.out.println("");
		}
		System.out.println("]");
	}

	public int getPosMedoid() {
		return posMedoid;
	}

	public void setPosMedoid(int posMedoid) {
		this.posMedoid = posMedoid;
	}
 
}
