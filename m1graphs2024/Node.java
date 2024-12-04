package m1graphs2024;

import java.util.*;

/**
 * Represents a node in a graph.
 */
public class Node implements Comparable<Node>{

    private int id;
    private String name;
    private Graph graph;

    /**
     * Constructs a node with the specified ID and graph
     * @param id the ID of the node
     * @param graph the graph to which this node belongs
     */
    public Node(int id, Graph graph){
        this(id, "", graph);
    }

    /**
     * Constructs a node with the specified ID, name, and graph
     * @param id the ID of the node
     * @param name the name of the node
     * @param graph the graph to which this node belongs
     */
    public Node(int id, String name, Graph graph){
        this.id = id;
        this.name = name;
        this.graph = graph;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node){
            Node n = (Node) obj;
            return n.id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Node o) {
        return this.id - o.id;
    }

    @Override
    public String toString() {
        return ""+getId();
    }

    /**
     * Returns the ID of the node
     * @return the ID of the node
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the graph to which this node belongs
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Returns the name of the node
     * @return the name of the node
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the successors of this node
     * @return the list of successor nodes
     */
    public List<Node> getSuccessors() {
        return graph.getSuccessors(this);
    }

    /**
     * Returns the successors of this node in a multigraph
     * @return the list of successor nodes in a multigraph
     */
    public List<Node> getSuccessorsMulti(){
        return graph.getSuccessorsMulti(this);
    }

    /**
     * Checks if this node is adjacent to the specified node
     * @param n the node to check adjacency with
     * @return true if the nodes are adjacent, false otherwise
     */
    public boolean adjacent(Node n){
        return graph.adjacent(this, n);
    }

    /**
     * Checks if this node is adjacent to the node with the specified ID
     * @param id the ID of the node to check adjacency with
     * @return true if the nodes are adjacent, false otherwise
     */
    public boolean ajdacent(int id){
        return graph.adjacent(this, new Node(id, graph));
    }

    /**
     * Returns the in-degree of this node
     * @return the in-degree of this node
     */
    public int inDegree(){
        return graph.inDegree(this);
    }

    /**
     * Returns the out-degree of this node
     * @return the out-degree of this node
     */
    public int outDegree(){
        return graph.outDegree(this);
    }

    /**
     * Returns the degree of this node
     * @return the degree of this node
     */
    public int degree(){
        return graph.degree(this);
    }

    /**
     * Returns the outgoing edges of this node
     * @return the list of outgoing edges
     */
    public List<Edge> getOutEdges(){
        return graph.getOutEdges(this);
    }

    /**
     * Returns the incoming edges of this node
     * @return the list of incoming edges
     */
    public List<Edge> getInEdges(){
        return graph.getInEdges(this);
    }

    /**
     * Returns the incident edges of this node
     * @return the list of incident edges
     */
    public List<Edge> getIncidentEdges(){
        return graph.getIncidentEdges(this);
    }

    /**
     * Returns the edges from this node to the specified node
     * @param n the target node
     * @return the list of edges to the target node
     */
    public List<Edge> getEdgesTo(Node n){
        return graph.getEdges(this, n);
    }

    /**
     * Returns the edges from this node to the node with the specified ID
     * @param id the ID of the target node
     * @return the list of edges to the target node
     */
    public List<Edge> getEdgesTo(int id){
        return graph.getEdges(this, new Node(id, graph));
    }
}
