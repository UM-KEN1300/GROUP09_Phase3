import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
/**
This class is used to run the recursive largest first algorithm
*/
public class RLF {
  /**
  This method is used to run the algorithm.
  @param vertices An ArrayList containing all Vertex objects in the graoph and therefore representing it
  @return The algorithm's result (the number of color classes used, which is equivalent to an upper bound for the graph)
  */
  public static int run(ArrayList<Vertex> vertices) {
    ArrayList<Vertex> G = (ArrayList<Vertex>) vertices.clone();
    int k = 0;
    while(G.size() != 0) {
      k++;
      ArrayList<Integer> orderedSet = chooseVertex(G, G);
      Vertex v = getVertexById(G, orderedSet.get(orderedSet.size()-1));
      ArrayList<Vertex> C = constructColorClass(G, v);
      G = HelperFunctions.getSetMinus(G, C);
    }
    return k;
  }
  /**
  This method is used to choose vertices or rather sort the set of vertices in U corresponding to their degree with respect to vertices in W in ascending order
  @param U An ArrayList containing Vertex objects for which to evaluate their degrees with respect to W
  @param W An ArrayList containing Vertex objects resembling the set of possible neighboursto count for vertices in U
  @return An ArrayList of Integer objects containing the vertices' ids ordered by their respective degree
  */
  private static ArrayList<Integer> chooseVertex(ArrayList<Vertex> U, ArrayList<Vertex> W) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    int[][] relativeDegrees = new int[U.size()][2];
    for(int i = 0; i < U.size(); i++) {
      relativeDegrees[i][0] = countNeighboursIn(W, U.get(i));
      relativeDegrees[i][1] = U.get(i).getId();
    }
    HelperFunctions.recursiveTwoDSortHelper(relativeDegrees);
    for(int i = 0; i < relativeDegrees.length; i++) {
      result.add(relativeDegrees[i][1]);
    }
    return result;
  }
  /**
  This method is used to construct a color class
  @param U An ArrayList containing uncolored vertices in the Graph G
  @param v A vertex to start constructing the color class C from.
  @return The color class C as an <i>ArrayList<Vertex</i>
  */
  private static ArrayList<Vertex> constructColorClass(ArrayList<Vertex> U, Vertex v) {
    ArrayList<Vertex> W = (ArrayList<Vertex>) v.getNeighbours().clone();
    ArrayList<Vertex> C = new ArrayList<Vertex>();
    C.add(v);
    U.remove(v);
    U = HelperFunctions.getSetMinus(U, W);
    while(U.size() != 0) {
      ArrayList<Integer> orderedSet = chooseVertex(U, W);
      Vertex u = null;
      if(U.size() > 1) {
        if(orderedSet.get(orderedSet.size()-1) == orderedSet.get(orderedSet.size()-2)) {
          orderedSet = chooseVertex(U, U);
          u = getVertexById(U, orderedSet.get(0));
        }
      }
      if(u == null) {
        u = getVertexById(U, orderedSet.get(orderedSet.size()-1));
      }
      C.add(u);
      ArrayList<Vertex> neighboursOfU = u.getNeighbours();
      for(Vertex j : neighboursOfU) {
        if(U.contains(j)) {
          W.add(j);
        }
      }
      U.remove(u);
      U = HelperFunctions.getSetMinus(U, W);
    }
    return C;
  }
  /**
  This method is used to count the neighbours of a vertex v in a given set of vertices W
  @param W An ArrayList containing Vertex objects in which to look for neighbours of v
  @param v A Vertex object for which the neighbours in W should be counted
  @return The number of v's neighbours in W as an <i>int</i>
  */
  private static int countNeighboursIn(ArrayList<Vertex> W, Vertex v) {
    int result = 0;
    for(Vertex u : W) {
      if(u.getNeighbours().contains(v)) {
        result++;
      }
    }
    return result;
  }
  /**
  This method is used to retrieve vertices from a given set based on their ids
  @param set An ArrayList containing Vertex objects resembling the set of vertices in which to look for
  @param id An <i>int</i> resembling the vertex's id
  @return The Vertex in the set corresponding to the given id (null if the set does not contain the vertex with the given id)
  */
  private static Vertex getVertexById(ArrayList<Vertex> set, int id) {
    for(Vertex v : set) {
      if(v.getId() == id) {
        return v;
      }
    }
    return null;
  }
}
