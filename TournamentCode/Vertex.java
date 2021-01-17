import java.util.ArrayList;
public class Vertex implements Comparable{

  int id; //Vertex's position in the adjacency matrix
  public ArrayList<Vertex> neighbours; //ArrayList of the neighbor nodes of a vertex
  public ArrayList<Vertex> conflictingNodes; //ArrayList of the nodes that conflict with a vertex
  public static Vertex recordHolder = null; //Vertex with the highest degree
  public int degree; //Degree of a vertex

  /** Constructor to create a BankAccount object with a given balance
   * @param id vertex's position in the adjacency matrix
   */
  public Vertex(int id) {
    this.id = id;
    if(recordHolder == null) {
      recordHolder = this;
    }
  }

  /** Method to get the neighbors nodes of a vertex
   * @return ArrayList of the neighbours
   */
  public ArrayList<Vertex> getNeighbours() {
    return neighbours;
  }

   /** Method to assign the neighbor nodes to a vertex
   * @param neighbourList ArrayList of the neighbor nodes
   */
  public void setNeighbours(ArrayList<Vertex> neighbourList) {
    neighbours = neighbourList;
    if(neighbours.size() > recordHolder.getNeighbours().size()) {
      recordHolder = this;
    }
    degree = neighbours.size();
  }

   /** Method to assign conflicting nodes to a vertex
   * @param conflictingNodesList ArrayList of conflicting nodes
   */
  public void setConflictingNodes(ArrayList<Vertex> conflictingNodesList) {
    conflictingNodes = conflictingNodesList;
  }

   /** Method to get the conflicting nodes of a vertex
   * @return ArrayList of the conflicting nodes
   */
  public ArrayList<Vertex> getConflictingNodes() {
     return conflictingNodes;
  }

   /** Method to get the vertex with the highest degree
   * @return vertex with the highest degree
   */
  public Vertex getRecordHolder() {
    return recordHolder;
  }

   /** Method to get the degree of a vertex
   * @return degree
   */
  public int getDegree() {
    return degree;
  }

   /** Method to remove the one neighbor node from a vertex's neighbors nodes list
   * @param v Neighbor node to remove
   */
  public void removeNeighbour(Vertex v) {
    neighbours.remove(v);
    degree--;
  }

   /** Method to get the id of a vertex
   * @return id
   */
  public int getId() {
    return id;
  }

   /** Method to assign an id to a vertex
   * @param newId Id to assign
   */
  public void setId(int newId) {
    id = newId;
  }

   /** Method to compare the degrees of two vertices
   * @return the difference between the degrees of the two vertices
   */
  @Override
  public int compareTo(Object v) {
    int vDegree = ((Vertex) v).getDegree();
    return degree-vDegree;
  }
}
