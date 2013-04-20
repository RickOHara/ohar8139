package ohar8139;

import java.awt.Color;

import spacewar2.shadows.LineShadow;
import spacewar2.shadows.Shadow;
import spacewar2.utilities.Vector2D;

/**
 * Generates the edge for the graph
 * 
 * @author Dr. McGovern
 *
 */
public class Edge {
	LineShadow shadow;
	Vertex vertex1, vertex2;
	double pathCost;
	boolean isSolution;
	
	public Edge(Vertex vertex1, Vertex vertex2, Vector2D lineVec) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		vertex1.addEdge(this);
		vertex2.addEdge(this);
		pathCost = lineVec.getMagnitude();
		shadow = new LineShadow(vertex1.getPosition(), vertex2.getPosition(), lineVec); 
		shadow.setLineColor(Color.RED);
	}
	
	public Shadow getShadow() {
		return shadow;
	}
	
	public double getPathCost() {
		return pathCost;
	}
	
	public Vertex getVertex1() {
		return vertex1;
	}
	
	public Vertex getVertex2() {
		return vertex2;
	}
	
	public void setSolution() {
		isSolution = true;
		shadow.setLineColor(Color.YELLOW);
		shadow.setStrokeWidth(4);
	}
}
