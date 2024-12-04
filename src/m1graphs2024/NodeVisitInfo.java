package m1graphs2024;

import java.util.Objects;

/**
 * Represents the visitation information of a node during graph traversal, such as DFS or BFS.
 * Tracks the node's color state, discovery and finish times, and predecessor node.
 * @author Johan Barçon
 */
public class NodeVisitInfo {
    private NodeColour color;
    private Node predecessor;
    private Integer discovery;
    private Integer finished;

    /**
     * Constructs a NodeVisitInfo object with a specified color, predecessor, and discovery time.
     * The finish time is initially set to null.
     *
     * @param color       the color representing the visitation state of the node
     * @param predecessor the predecessor node in the traversal
     * @param discovery   the discovery time of the node during traversal
     */
    public NodeVisitInfo(NodeColour color, Node predecessor, Integer discovery) {
        this.color = color;
        this.predecessor = predecessor;
        this.discovery = discovery;
        this.finished = null;
    }

    /**
     * Constructs a NodeVisitInfo object with default values:
     * - color is set to WHITE (unvisited)
     * - predecessor is set to null
     * - discovery and finish times are set to null
     */
    public NodeVisitInfo() {
        this.color = NodeColour.WHITE;
        this.predecessor = null;
        this.discovery = null;
        this.finished = null;
    }

    /**
     * Returns the color representing the visitation state of the node.
     *
     * @return the color of the node
     */
    public NodeColour getColor() {
        return color;
    }

    /**
     * Sets the color representing the visitation state of the node.
     *
     * @param color the color to set for the node
     */
    public void setColor(NodeColour color) {
        this.color = color;
    }

    /**
     * Returns the predecessor node in the traversal, if any.
     *
     * @return the predecessor node, or null if there is no predecessor
     */
    public Node getPredecessor() {
        return predecessor;
    }

    /**
     * Sets the predecessor node in the traversal.
     *
     * @param predecessor the node to set as predecessor
     */
    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    /**
     * Returns the discovery time of the node during traversal.
     *
     * @return the discovery time of the node, or null if not yet discovered
     */
    public Integer getDiscovery() {
        return discovery;
    }

    /**
     * Sets the discovery time of the node during traversal.
     *
     * @param discovery the discovery time to set
     */
    public void setDiscovery(Integer discovery) {
        this.discovery = discovery;
    }

    /**
     * Returns the finish time of the node during traversal.
     *
     * @return the finish time of the node, or null if not yet finished
     */
    public Integer getFinished() {
        return finished;
    }

    /**
     * Sets the finish time of the node during traversal.
     *
     * @param finished the finish time to set
     */
    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    /**
     * Returns a string representation of the NodeVisitInfo object, including color, predecessor, discovery, and finish times.
     *
     * @return a string representation of the visitation information
     */
    @Override
    public String toString() {
        return "{" +
                "color=" + color +
                ", predecessor=" + predecessor +
                ", discovery=" + discovery +
                ", finished=" + finished +
                "}";
    }
}
