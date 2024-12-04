package m1graphs2024;

/**
 * Represents the visit information of a node during graph traversal
 */
public class NodeVisitInfo {

    private Node predeccessor;
    private NodeColour colour;
    private int discovery, finished;

    /**
     * Constructs a NodeVisitInfo object with default values
     * The predecessor is set to null, the colour to white, the discovery time to 0 and the finished time to -1
     */
    public NodeVisitInfo() {
        this.predeccessor = null;
        this.colour = NodeColour.WHITE;
        this.discovery = 0;
        this.finished = -1;
    }

    /**
     * Returns the predecessor of this node
     * @return the predecessor of this node
     */
    public Node getPredeccessor() {
        return predeccessor;
    }

    /**
     * Returns the discovery time of this node
     * @return the discovery time of this node
     */
    public int getDiscovery() {
        return discovery;
    }

    /**
     * Returns the finished time of this node
     * @return the finished time of this node
     */
    public int getFinished() {
        return finished;
    }

    /**
     * Returns the colour of this node
     * @return the colour of this node
     */
    public NodeColour getColour() {
        return colour;
    }

    /**
     * Sets the colour of this node
     * @param colour the new colour of this node
     */
    public void setColour(NodeColour colour){
        this.colour = colour;
    }

    /**
     * Sets the predecessor of this node
     * @param predeccessor the new predecessor of this node
     */
    public void setPredeccessor(Node predeccessor){
        this.predeccessor = predeccessor;
    }

    /**
     * Sets the discovery time of this node
     * @param discovery the new discovery time of this node
     */
    public void setDiscovery(int discovery){
        this.discovery = discovery;
    }

    /**
     * Sets the finished time of this node
     * @param finished the new finished time of this node
     */
    public void setFinished(int finished){
        this.finished = finished;
    }
}
