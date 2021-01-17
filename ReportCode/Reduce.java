import java.util.ArrayList;
import java.util.Collections;
/**
This class is used to run the reduction proceduresmentioned in the project report
*/
public class Reduce {
  public static ArrayList<Vertex> vertices; //The set of vertices in the graph
  public static boolean debug;
  /**
  This method is used to run the reduction procedures
  @param clique An ArrayList containing all vertices that contribute to the maximal clique found by Bron-Kerbosch
  @param vertexList An ArrayList containing all vertices in the graph to be reduced
  @param debugSwitch A boolean acting as a switch to whether verbose output should be enabled or not
  @return The adjacency matrix of the reduced graph as a 2-dimensional integer array
  */
  public static int[][] run(ArrayList<Vertex> clique, ArrayList<Vertex> vertexList, boolean debugSwitch) {
    vertices = (ArrayList<Vertex>) vertexList.clone();
    debug = debugSwitch;
    removeDisconnectedVertices();
    ArrayList<Vertex> toRemove = new ArrayList<Vertex>();
    ArrayList<Vertex> VMinusK = HelperFunctions.getSetMinus(vertices, clique);
    if(VMinusK.size() != 0) {
      for(Vertex v : VMinusK) {
        for(Vertex w : clique) {
          if(!toRemove.contains(v) && v.getConflictingNodes().contains(w) && HelperFunctions.isSubset(v.getNeighbours(), w.getNeighbours())) {
            toRemove.add(v);
            if(debug) {
              System.out.println("Vertex " + v.getId() + " will be removed");
              System.out.print("Vertex " + v.getId() + "s neighbours: ");
              for(Vertex j : v.getNeighbours()) {
                System.out.print(j.getId() + " ");
              }
              System.out.println();
            }
          }
        }
      }
      Collections.sort(toRemove);
      while(toRemove.size() != 0) {
        Vertex v = toRemove.get(0);
        toRemove.remove(0);
        for(Vertex w : v.getNeighbours()) {
          w.removeNeighbour(v);
        }
        vertices.remove(v);
        Collections.sort(toRemove);
        if(debug) {
          System.out.println("Removing vertex " + v.getId() + "...");
        }
        removeVerticesRecursively(VMinusK, clique.size());
      }
      updateIds();
    }
    return HelperFunctions.getAdjacencyMatrix(vertices);
  }
  /**
  This method is used to remove vertices recursively based on the rules shown in procedure 2 mentioned in the project report
  @param VMinusK An ArrayList containing Vertex objects resembling the set minus of V (the set of all vertices in a graph G) and K (the set of vertices in the maximal clique)
  @param cliqueSize An <i>int</i> resembling the number of nodes in the maximal clique
  */
  private static void removeVerticesRecursively(ArrayList<Vertex> VMinusK, int cliqueSize) {
    if(VMinusK.size() != 0) {
      Vertex v = VMinusK.get(0);
      VMinusK.remove(0);
      if(v.getDegree() < cliqueSize - 1) {
        if(debug) {
          System.out.println("Removing vertex " + v.getId() + "...");
        }
        for(Vertex w : v.getNeighbours()) {
          w.removeNeighbour(v);
        }
        vertices.remove(v);
        removeVerticesRecursively(VMinusK, cliqueSize);
      }
    }
  }
  /**
  This method is used to update the vertices' ids after reduction to be their corresponding indices in the the reduced graph's adjacency matrix
  */
  private static void updateIds() {
    for(int i = 0; i < vertices.size(); i++) {
      vertices.get(i).setId(i);
    }
  }
  /**
  This method is used to remove all disconnected from every other vertex
  @return The adjacency matrix of the graph without completely disconnected vertices as a 2-dimensional array of integers
  */
  public static int[][] removeDisconnectedVertices() {
    ArrayList<Vertex> verticesTmp = (ArrayList<Vertex>) vertices.clone();
    for(Vertex v : verticesTmp) {
      if(v.getDegree() == 0) {
        vertices.remove(v);
      }
    }
    updateIds();
    return HelperFunctions.getAdjacencyMatrix(vertices);
  }
}
