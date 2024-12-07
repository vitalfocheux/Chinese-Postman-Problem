package m1graphs2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an edge in a graph, connecting two nodes. Supports both directed and undirected graphs.
 * Edges can be weighted, have self-loops, and can participate in multigraphs (multiple edges between the same nodes).
 * @author Johan Barçon
 */
public class Edge implements Comparable<Edge> {
    private Node from;
    private Node to;
    private Integer weight = null;

    /**
     * Constructs an unweighted edge between two nodes in the same graph.
     *
     * @param from the source node
     * @param to   the destination node
     * @throws IllegalArgumentException if the nodes are null or do not belong to the same graph
     */
    public Edge(Node from, Node to) throws IllegalArgumentException {
        if (from == null || to == null || from.getGraph() != to.getGraph()) {
            throw new IllegalArgumentException("Both nodes must exist and belong to the same graph.");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Constructs a weighted edge between two nodes in the same graph.
     *
     * @param from   the source node
     * @param to     the destination node
     * @param weight the weight of the edge
     */
    public Edge(Node from, Node to, Integer weight) {
        this(from, to);
        this.weight = weight;
    }

    /**
     * Constructs an unweighted edge between two nodes in the same graph, identified by their IDs.
     *
     * @param from         the ID of the source node
     * @param to           the ID of the destination node
     * @param graphHolder  the graph containing the nodes
     * @throws IllegalArgumentException if the IDs are less than or equal to 0 or the graph is null
     */
    public Edge(int from, int to, Graph graphHolder) {
        if (from <= 0 || to <= 0 || graphHolder == null) {
            throw new IllegalArgumentException("Node IDs must be greater than 0 and the graph must be non-null.");
        }
        this.from = graphHolder.getNode(from) == null ? new Node(graphHolder, from) : graphHolder.getNode(from);
        this.to = graphHolder.getNode(to) == null ? new Node(graphHolder, to) : graphHolder.getNode(to);
    }

    /**
     * Constructs a weighted edge between two nodes in the same graph, identified by their IDs.
     *
     * @param from         the ID of the source node
     * @param to           the ID of the destination node
     * @param weight       the weight of the edge
     * @param graphHolder  the graph containing the nodes
     */
    public Edge(int from, int to, Integer weight, Graph graphHolder) {
        this(from, to, graphHolder);
        this.weight = weight;
    }

    /**
     * Returns the source node of the edge.
     *
     * @return the source node
     */
    public Node from() {
        return from;
    }

    /**
     * Returns the destination node of the edge.
     *
     * @return the destination node
     */
    public Node to() {
        return to;
    }

    /**
     * Returns the symmetric edge with the same weight, reversing the direction.
     *
     * @return a new edge with the source and destination nodes reversed
     */
    public Edge getSymmetric() {
        return new Edge(to, from, weight);
    }

    /**
     * Checks if the edge is a self-loop (i.e., the source and destination nodes are the same).
     *
     * @return true if this edge is a self-loop, false otherwise
     */
    public boolean isSelfLoop() {
        return from.equals(to);
    }

    /**
     * Checks if there is another edge with the same source and destination nodes in the graph.
     *
     * @return true if there is another edge with the same endpoints, false otherwise
     */
    public boolean isMultiEdge() {
        Graph curGraph = from.getGraph();
        for (Edge e : curGraph.getOutEdges(from)) {
            if (e != this && e.to().equals(to)) return true;
        }
        return false;
    }

    /**
     * Returns all edges in the graph that have the same source and destination nodes as this edge.
     *
     * @return a list of edges with the same source and destination, or null if none exist
     */
    public List<Edge> getMultiEdges() {
        List<Edge> edges = new ArrayList<>();
        if (!isMultiEdge()) return null;
        Graph curGraph = from.getGraph();
        for (Edge e : curGraph.getOutEdges(from)) {
            if (!e.equals(this) && e.to().equals(to)) edges.add(e);
        }
        return edges;
    }

    /**
     * Checks if the edge is weighted.
     *
     * @return true if the edge has a non-null weight, false otherwise
     */
    public boolean isWeighted() {
        return weight != null;
    }

    /**
     * Returns the weight of the edge.
     *
     * @return the weight of the edge, or null if unweighted
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Compares this edge with another object for equality. Edges are considered equal if they
     * have the same source, destination, and weight (if weighted).
     *
     * @param o the object to compare
     * @return true if the edges are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        if (isWeighted() && edge.isWeighted()) {
            return weight.equals(edge.getWeight()) && Objects.equals(from(), edge.from()) && Objects.equals(to(), edge.to());
        }
        return Objects.equals(from(), edge.from()) && Objects.equals(to(), edge.to());
    }

    /**
     * Computes the hash code for this edge, based on its source, destination, and weight (if present).
     *
     * @return the hash code for this edge
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        if (isWeighted()) {
            result = 31 * result + weight;
        }
        return result;
    }

    /**
     * Compares this edge to another edge for ordering purposes.
     * Compares by source node, then by destination node if sources are the same.
     * Weight is not considered in the comparison.
     *
     * @param o the edge to compare to
     * @return a negative integer, zero, or a positive integer if this edge is less than, equal to,
     * or greater than the specified edge
     */
    @Override
    public int compareTo(Edge o) {
        if (this.from.getId() != o.from.getId()) return this.from.getId() - o.from.getId();
        return this.to.getId() - o.to.getId();
    }

    /**
     * Returns a string representation of this edge, formatted according to graph type
     * (directed or undirected) and showing the weight if present.
     *
     * @return a string representation of this edge
     */
    @Override
    public String toString() {
        return from + ((from.getGraph() instanceof UndirectedGraph) ? " -- " : " -> ") + to +
                (isWeighted() ? " [label=" + weight + ", len=" + weight + "]" : "");
    }

    /**
     * Returns a string representation of this edge, formatted according to graph type
     * in a circuit and showing the weight if present.
     *
     * @return a string representation of this edge
     */
    public String toCircuitString() {
        return from + "-" + (isWeighted() ? "(" + weight + ")" : "") + "-" + to;
    }
}
