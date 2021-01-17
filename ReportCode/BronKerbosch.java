import java.util.ArrayList;
public class BronKerbosch {
  public static ArrayList<Vertex> recordClique = new ArrayList<Vertex>();
  public static ArrayList<Vertex> V;


  /**
   *function for running the BK algorithm
   * @param vertices Array List of Vertex objects that form the graph
   * @return maximum maximal clique, the lowerbound
   */
  public static ArrayList<Vertex> run(ArrayList<Vertex> vertices) {

    ArrayList<Vertex> verticesTmp = (ArrayList<Vertex>) vertices.clone();
    recursivePart(new ArrayList<Vertex>(), verticesTmp, new ArrayList<Vertex>());
    return recordClique;
  }

  /**
   * function finds maximal cliques, it comapares them s.t we have maximum one
   * @param R Arraylist of vertices, currently growing clique
   * @param P Arraylist of vertices, prospective nodes which are connected to ones in R
   * @param X Arraylist of vertices, contains nodes which are already processed
   */
  public static void recursivePart(ArrayList<Vertex> R, ArrayList<Vertex> P, ArrayList<Vertex> X) {
    if(P.size() == 0 && X.size() == 0) {
      if(R.size() > recordClique.size()) {
        recordClique = R;
      }
    }
    ArrayList<Vertex> P1 = (ArrayList<Vertex>) P.clone();
    ArrayList<Vertex> Q = tomitaPivoting(R, P, X);
    for(int i = 0; i < Q.size(); i++) {
      Vertex v = Q.get(i);
      ArrayList<Vertex> RUnionSetOfV = (ArrayList<Vertex>) R.clone();
      RUnionSetOfV.add(v);
      ArrayList<Vertex> neighboursOfV = v.getNeighbours();
      ArrayList<Vertex> PIntersectNeighboursofV = HelperFunctions.getIntersect(P1, neighboursOfV);
      ArrayList<Vertex> XIntersectNeighboursofV = HelperFunctions.getIntersect(X, neighboursOfV);
      recursivePart(RUnionSetOfV, PIntersectNeighboursofV, XIntersectNeighboursofV);
      //P \ {v}
      P1.remove(v);
      //X UNION {v}
      X.add(v);
    }
  }

  /**
   *function finds best pivot node
   * @param R Arraylist of vertices, currently growing clique
   * @param P Arraylist of vertices, prospective nodes which are connected to ones in R
   * @param X Arraylist of vertices, contains nodes which are already processed
   * @return set P minus neighbours of pivot point
   */
  public static ArrayList<Vertex> tomitaPivoting(ArrayList<Vertex> R, ArrayList<Vertex> P, ArrayList<Vertex> X) {
    if(P.size() != 0) {
      ArrayList<Vertex> PUnionX = HelperFunctions.getUnion(P, X);
      Vertex recordVertex = null;
      int record = -1;
      for(int i = 0; i < PUnionX.size(); i++) {
        Vertex q = PUnionX.get(i);
        ArrayList<Vertex> PInterSectNeighboursOfQ = HelperFunctions.getIntersect(P, q.getNeighbours());
        if(PInterSectNeighboursOfQ.size() > record) {
          recordVertex = q;
          record = PInterSectNeighboursOfQ.size();
        }
      }
      return HelperFunctions.getSetMinus(P, recordVertex.getNeighbours());
    }
    else {
      return new ArrayList<Vertex>();
    }
  }
}
