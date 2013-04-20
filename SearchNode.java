package ohar8139;

/**
 * Search node
 * 
 * Generates a search node
 * 
 * @author Dr. McGovern
 *
 */

public class SearchNode {
	Vertex vertex;
	Edge edge;
	
	//Create a search node
	public SearchNode(Vertex v, Edge e) {
		vertex = v;
		edge = e;
	}

	/**
	 * Get the edges
	 * @return
	 */
	public Edge getEdge() {
		return edge;
	}

	/**
	 * Get the vertex
	 * @return
	 */
	public Vertex getVertex() {
		return vertex;
	}
	
	

}
