package m1graphs2024;

import java.util.Objects;

/**
 * Represents an edge in a graph., connecting two nodes.
 */
public class Edge implements Comparable<Edge> {

    private Node from, to;
    private Graph graph;
    private Integer weight;

    /**
     * Constructs an edge with the specified nodes and graph.
     * @param from the starting node
     * @param to the ending node
     * @param graph the graph to which this edge belongs
     */
    public Edge(Node from, Node to, Graph graph){
        this(from, to, graph, null);
    }

    /**
     * Constructs an edge with the specified nodes IDs and graph.
     * @param from the starting node ID
     * @param to the ending node ID
     * @param graph the graph to which this edge belongs
     */
    public Edge(int from, int to, Graph graph){
        this(new Node(from, graph), new Node(to, graph), graph, null);
    }

    /**
     * Constructs an edge with the specified nodes, graph and weight.
     * @param from the starting node
     * @param to the ending node
     * @param graph the graph to which this edge belongs
     * @param weight the weight of the edge
     */
    public Edge(Node from, Node to, Graph graph, Integer weight){
        this.from = from;
        this.to = to;
        this.graph = graph;
        this.weight = weight;
    }

    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof Edge){
            Edge e = (Edge) obj;
            return e.from.equals(this.from) && e.to.equals(this.to) && Objects.equals(e.weight, this.weight);
        }
        return false;
    }


    @Override
    public int compareTo(Edge o) {
        if(this.from.equals(o.from)){
            return this.to.compareTo(o.to);
        }
        return this.from.compareTo(o.from);
    }

    @Override
    public String toString() {
        return from+"->"+to;
    }

    /**
     * Returns the starting node of this edge.
     * @return the starting node
     */
    public Node from(){
        return from;
    }

    /**
     * Returns the ending node of this edge.
     * @return the ending node
     */
    public Node to(){
        return to;
    }

    /**
     * Returns a symmetric edge with reversed direction.
     * @return the symmetric edge
     */
    public Edge getSymmetric(){
        return new Edge(to, from, graph, weight);
    }

    /**
     * Checks if this edge is a self-loop.
     * @return true if the edge is a self-loop, false otherwise
     */
    public boolean isSelfLoop(){
        return from.equals(to);
    }

    /**
     * Checks if this edge is a multi-edge.
     * @return true if the edge is a multi-edge, false otherwise
     */
    public boolean isMultiEdge(){
        return graph.getEdges(from, to).size() > 1;
    }

    /**
     * Checks if this edge is weighted.
     * @return true if the edge is weighted, false otherwise
     */
    public boolean isWeighted(){
        return Objects.nonNull(weight);
    }

    /**
     * Returns the weight of this edge.
     * @return the weight of the edge
     */
    public Integer getWeight(){
        return weight;
    }
}
