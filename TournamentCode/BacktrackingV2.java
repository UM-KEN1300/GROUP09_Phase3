public class BacktrackingV2 {
  public static int[][] adj;

  /**
   * this function calls the BackTracking algorithm in main method
   * @param lowerbound the best lowerbound found from previous algorithms
   * @param adj_matrix 2d array representative of graph
   * @param debug
   * @return the chromatic number
   */
  public static int run(int lowerbound, int[][] adj_matrix, boolean debug) {
    boolean done = false;
    adj = adj_matrix;
    int chromaticNumber = lowerbound;
    while(!done) {
      if(debug) {
        System.out.println("Currently trying " + chromaticNumber + "-coloring");
      }
      int[] vertices = new int[adj.length];
      done = recursivePart(vertices, chromaticNumber, 0, 1);
      chromaticNumber++;
    }
    chromaticNumber--;
    return chromaticNumber;
  }

  /**
   * this function tries to color the graph in assigned nr of colors
   * @param vertices array contains respective colors
   * @param colors upper bound of number of colors attempted
   * @param currV the current vertice
   * @param starting amount of colors with which coloring attempted
   * @return true or false whever algorithm is done
   */
  private static boolean recursivePart(int[] vertices, int colors, int currV, int colorStart) {
    if(colorStart > colors) {
      boolean allChecked = true;
      for(int i = 0; i < currV; i++) {
        if(vertices[i] != colors) {
          allChecked = false;
        }
      }
      return false;
    }
    boolean done = true;
    for(int i = 0; i < vertices.length; i++) {
      if(vertices[i] == 0) {
        done = false;
      }
    }
    if(done) {
      return true;
    }
    for(int i = colorStart; i <= colors; i++) {
      if(validColor(currV, i, vertices)) {
        vertices[currV] = i;
        return recursivePart(vertices, colors, currV+1, 1);
      }
    }
    //Backtrackingstep
    return recursivePart(vertices, colors, currV-1, vertices[currV-1]+1);
  }

  /**
   * this function check if a certain coloring is legal
   * @param v the current vertex
   * @param c color with which vertex is to be colored
   * @param vertices array of vertix colors
   * @return true if coloring valid, false if not
   */
  private static boolean validColor(int v, int c, int[] vertices) {
    for(int i = 0; i < adj.length; i++) {
      if(adj[v][i] == 1 && vertices[i] == c) {
        return false;
      }
    }
    return true;
  }
}
