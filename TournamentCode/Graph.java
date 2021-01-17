import java.util.ArrayList;
public class Graph {
  public int[][] adj; //The graph's initial adjacency matrix
  public ArrayList<Vertex> vertices; //The graph's set of vertices
  public int edgeNumber; //The number of edges in the graph
  public int vertexNumber; //The number of vertices in the graph
  /**
  This class is used to create a Graph object which is needed to initialize its set of vertices as
  an ArrayList containing Vertex objects and to initialize its adjacency matrix.
  @param colEdges An array containing ColEdge objects resembling the graphs edges
  @param m The number of edges in the graph
  @param n The number of vertices in the graph
  */
  public Graph(ColEdge[] colEdges, int m, int n) {
    //Get the adjacency matrix
    adj = HelperFunctions.getAdjacencyMatrix(colEdges, m, n);
    vertices = new ArrayList<Vertex>();
    edgeNumber = m;
    vertexNumber = adj.length;
    //Create the set of vertices as an ArrayList containing Vertex objects and initialize them
    for(int i = 0; i < adj.length; i++) {
      vertices.add(new Vertex(i));
    }
    for(int i = 0; i < adj.length; i++) {
      vertices.get(i).setNeighbours(findNeighbours(i));
      vertices.get(i).setConflictingNodes(HelperFunctions.getSetMinus(vertices, vertices.get(i).getNeighbours()));
    }
  }
  /**
  This method is used to add all neighbours of a vertex to its ArrayList of neighbours
  @param currNode An <i>int</i> resembling the id of the vertexy to look for neighbours
  @return An ArrayList containing Vertex objects that are all neighbours of the Vertex corresponding to the given id (currNode)
  */
  private ArrayList<Vertex> findNeighbours(int currNode) {
    ArrayList<Vertex> neighbours = new ArrayList<Vertex>();
    for(int i = 0; i < adj.length; i++) {
      if(adj[currNode][i] == 1) {
        neighbours.add(vertices.get(i));
      }
    }
    return neighbours;
  }
  /**
  This method functions as a getter method for the graph's vertices
  @return All of the graph's vertices as an ArrayList containing Vertex objects
  */
  public ArrayList<Vertex> getVertices() {
    return vertices;
  }
}
