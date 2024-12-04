package m1graphs2024;

/**
 * Represents the color state of a node during graph traversal, typically used in depth-first search (DFS) and breadth-first search (BFS) algorithms.
 * Each color signifies a different stage in the traversal process:
 * <ul>
 *     <li>WHITE - The node has not been visited yet.</li>
 *     <li>GREY - The node is currently being visited (in progress).</li>
 *     <li>BLACK - The node and all its adjacent nodes have been fully explored.</li>
 * </ul>
 * @author Johan Barçon
 */
public enum NodeColour {
    WHITE,  // Unvisited node
    GREY,   // Node currently being visited
    BLACK   // Node fully visited and processed
}
