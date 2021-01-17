import java.util.*;
public class DSatur {
  public static HelperFunctions lib = new HelperFunctions();
  public static int[][] adj;

  /**
  This function returns a 2-dimensional sorted array where the original indices are preserved
  @param deg int[] containing the different degrees of all vertices
  @return 2-dimensional sorted array, sorted by degrees
  */
  public static int[][] getOrderedVertices(int[] deg) {
    return lib.TwoDSort(deg);
  }

  /**
  This is the main function to run the DSatur algorithm. Descriptions of how it works in detail
  can be found in the comments
  @param deg int[] containing the different degrees of all vertices
  @param adj The adjacency matrix
  @return The estimated upper bound for the given graph
  */
  public static int[] run(int[] deg, int[][] adj_matrix) {
    adj = adj_matrix;
    int[][] orderedVertices;
    int[] colors = new int[deg.length];
    for(int i = 0; i < colors.length; i++) {
      //Initialize colors as -1 to determine uncolored vertices from colored ones
      colors[i] = -1;
    }
    //Get vertices ordered by degree
    orderedVertices = getOrderedVertices(deg);

    //Select a vertex of maximal degree and colour it with the first colour.
    colors[orderedVertices[orderedVertices.length-1][1]] = 0;
    //Remove it from the uncolored array
    int[] uncoloredVertices = new int[orderedVertices.length-1];
    for(int i = 0; i < uncoloredVertices.length; i++) {
      uncoloredVertices[i] = orderedVertices[i][1];
    }
    return recursivePart(colors, uncoloredVertices, adj);
  }

  /**
  This is the recursive part of the DSatur algorithm that colors in all vertices according to their degree of saturation
  @param c int[] containing the current coloring of all vertices
  @param v int[] containing all yet uncolored vertices
  @param adj The adjacency matrix
  @return the estimated upper bound for the given graph
  */
  public static int[] recursivePart(int[] c, int[] v, int[][] adj) {
    //Base case: no uncolored vertices left
    if(v.length == 0) {
      return c;
    }
    else{
      //Choose the vertex with the highest degree of saturation
      int nextVertex = getHighestSaturation(c, v);
      //initialize possible colors...
      boolean[] possibleColors = new boolean[adj.length];
      for(int i = 0; i < possibleColors.length; i++) {
        possibleColors[i] = true;
      }
      //...and determine which are not possible for this vertex
      for(int i = 0; i < c.length; i++) {
        if(c[i] != -1) {
          if(adj[i][nextVertex] == 1) {
            possibleColors[c[i]] = false;
          }
        }
      }
      int selectedColor = 0;
      //Check which color can be selected...
      for(int i = 0; i < possibleColors.length; i++) {
        if(possibleColors[i]) {
          selectedColor = i;
          break;
        }
      }
      //...and assign this color to the position of the current vertex in the color array
      c[nextVertex] = selectedColor;
      ArrayList<Integer> vertices = new ArrayList<>();
      for(int i = 0; i < v.length; i++) {
        vertices.add(v[i]);
      }
      //Remove the already processed vertex from the array containing the uncolored vertices
      for(int i = 0; i < v.length; i++) {
        if(v[i] == nextVertex) {
          vertices.remove(i);
          break;
        }
      }
      int[] newV = new int[vertices.size()];
      for(int i = 0; i < vertices.size(); i++) {
        newV[i] = vertices.get(i);
      }
      //Recursive call: Check for the next uncolored vertex with highest degree of saturation
      return recursivePart(c, newV, adj);
    }
  }

  /**
  This function returns the vertex with the highest degree pf saturation.
  @param c int[] containing the current colors of all vertices (-1 if not yet colored)
  @param adj The adjacency matrix
  @param v int[] containing the uncolored vertices
  @return vertex with highest degree of saturation
  */
  public static int getHighestSaturation(int[]c, int[] v) {
    ArrayList<Integer> AdjacentNodes = new ArrayList<>();
    int[] SatScores = new int[adj.length];
    for(int i = 0; i < SatScores.length; i++) {
      SatScores[i] = -1;
    }
    for(int i = 0; i < v.length; i++) {
      AdjacentNodes.clear();
      int SatScore = 0;
      for(int j = 0; j < adj[0].length; j++) {
        if(adj[v[i]][j] == 1) {
          AdjacentNodes.add(j);
        }
      }
      for(int j = 0; j < AdjacentNodes.size(); j++) {
        if(c[AdjacentNodes.get(j)] != -1) {
          SatScore++;
        }
      }
      SatScores[v[i]] = SatScore;
      boolean debug = false;
      if(debug) {
        System.out.println("Node: " + v[i]);
        System.out.println("Score " + SatScores[v[i]]);
        System.out.println(AdjacentNodes);
      }
    }
    return lib.getMaxIndex(SatScores);
  }
}
