package m1graphs2024;

/**
 * Enum representing the different types of edges encountered during a depth-first search (DFS) traversal.
 * <p>Each type is defined as follows:</p>
 * <ul>
 *     <li><b>TREE</b>: An edge that is part of the DFS tree.</li>
 *     <li><b>BACKWARD</b>: An edge that points back to an ancestor in the DFS tree.</li>
 *     <li><b>FORWARD</b>: An edge that points to a descendant in the DFS tree.</li>
 *     <li><b>CROSS</b>: An edge that connects two nodes in different DFS trees or components.</li>
 *     <li><b>NULL</b>: A placeholder type for unvisited or unclassified edges.</li>
 * </ul>
 * @author Johan Barçon
 */
public enum EdgeVisitType {
    TREE, BACKWARD, FORWARD, CROSS, NULL
}
