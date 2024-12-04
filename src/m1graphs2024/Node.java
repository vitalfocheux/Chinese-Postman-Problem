package m1graphs2024;

import java.util.List;
import java.util.Objects;

/**
 * Represents a node in a graph. Each node has a unique identifier (id), an optional name,
 * and a reference to the graph it belongs to.
 * Provides methods for accessing node properties, successors, and edges.
 * @author Johan Barçon
 */
public class Node implements Comparable<Node> {
    private int id;
    private String name;
    private Graph graph_holder;

    /**
     * Constructs a Node that belongs to the specified graph, with an id automatically
     * assigned as one more than the largest current id in the graph.
     *
     * @param graph_holder the Graph to which this node belongs
     */
    public Node(Graph graph_holder) {
        this.graph_holder = graph_holder;
        this.id = graph_holder.largestNodeId() + 1;
    }

    /**
     * Constructs a Node with a specified id and assigns it to a graph.
     *
     * @param graph_holder the Graph to which this node belongs
     * @param id           the unique identifier of this node
     */
    public Node(Graph graph_holder, int id) {
        this.graph_holder = graph_holder;
        this.id = id;
    }

    /**
     * Constructs a Node with a specified id and assigns it to a graph.
     *
     * @param graph_holder the Graph to which this node belongs
     * @param id           the unique identifier of this node
     */
    public Node(int id, Graph graph_holder) {
        this.graph_holder = graph_holder;
        this.id = id;
    }

    /**
     * Constructs a Node with a specified id and name, and assigns it to a graph.
     *
     * @param graph_holder the Graph to which this node belongs
     * @param id           the unique identifier of this node
     * @param name         the name of this node
     */
    public Node(Graph graph_holder, int id, String name) {
        this(graph_holder, id);
        this.name = name;
    }

    /**
     * Returns the unique identifier of this node.
     *
     * @return the id of this node
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the graph that holds this node.
     *
     * @return the graph this node belongs to
     */
    public Graph getGraph() {
        return graph_holder;
    }

    /**
     * Returns the name of this node, if any.
     *
     * @return the name of this node, or null if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a list of successor nodes of this node without duplicates.
     *
     * @return a list of unique successor nodes
     */
    public List<Node> getSuccessors() {
        return graph_holder.getSuccessors(this);
    }

    /**
     * Returns a list of successor nodes of this node with possible duplicates in case of multigraphs.
     *
     * @return a list of successor nodes, possibly containing duplicates
     */
    public List<Node> getSuccessorsMulti() {
        return graph_holder.getSuccessorsMulti(this);
    }

    /**
     * Checks if this node is adjacent to another node.
     *
     * @param u the node to check adjacency with
     * @return true if the nodes are adjacent, false otherwise
     */
    public boolean adjacent(Node u) {
        return graph_holder.adjacent(this, u);
    }

    /**
     * Checks if this node is adjacent to a node with the specified id.
     *
     * @param id the id of the node to check adjacency with
     * @return true if the nodes are adjacent, false otherwise
     */
    public boolean adjacent(int id) {
        return graph_holder.adjacent(this.id, id);
    }

    /**
     * Returns the in-degree of this node (number of incoming edges).
     *
     * @return the in-degree of this node
     */
    public int inDegree() {
        return graph_holder.inDegree(this);
    }

    /**
     * Returns the out-degree of this node (number of outgoing edges).
     *
     * @return the out-degree of this node
     */
    public int outDegree() {
        return graph_holder.outDegree(this);
    }

    /**
     * Returns the degree of this node (sum of in-degree and out-degree).
     *
     * @return the degree of this node
     */
    public int degree() {
        return inDegree() + outDegree();
    }

    /**
     * Returns the list of edges entering this node.
     *
     * @return the list of incoming edges
     */
    public List<Edge> getInEdges() {
        return graph_holder.getInEdges(this);
    }

    /**
     * Returns the list of edges leaving this node.
     *
     * @return the list of outgoing edges
     */
    public List<Edge> getOutEdges() {
        return graph_holder.getOutEdges(this);
    }

    /**
     * Returns the list of all edges incident to this node.
     *
     * @return the list of incident edges
     */
    public List<Edge> getIncidentEdges() {
        return graph_holder.getIncidentEdges(this);
    }

    /**
     * Returns the list of edges from this node to another specified node.
     *
     * @param u the destination node
     * @return the list of edges from this node to node u
     */
    public List<Edge> getEdgesTo(Node u) {
        return graph_holder.getEdges(this, u);
    }

    /**
     * Checks if this node is equal to another object. Two nodes are equal if they have the same id
     * and belong to the same graph.
     *
     * @param o the object to compare
     * @return true if the nodes are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return (getId() == node.getId()) && Objects.equals(getGraph(), node.getGraph());
    }

    /**
     * Returns a hash code for this node.
     *
     * @return the hash code for this node
     */
    @Override
    public int hashCode() {
        int result = 17;
        result *= 31 + getId();
        if (graph_holder != null) {
            result *= 31 + graph_holder.hashCode();
        }
        if (name != null) {
            result *= 31 + name.hashCode();
        }
        return result;
    }

    /**
     * Returns a string representation of this node, which is its id.
     *
     * @return the string representation of this node
     */
    @Override
    public String toString() {
        return id + "";
    }

    /**
     * Compares this node to another node based on their ids.
     *
     * @param o the node to compare to
     * @return a negative integer, zero, or a positive integer as this node's id is less than, equal to,
     * or greater than the specified node's id
     */
    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.getId(), o.getId());
    }
}
