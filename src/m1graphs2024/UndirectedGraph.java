package m1graphs2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Represents an undirected graph data structure, supporting multigraphs (multiple edges between nodes)
 * and self-loops. This class provides functionality for adding, removing, and accessing nodes and edges,
 * as well as methods for graph traversal, transformation, and representation.
 * This class extends the class Graph.
 * @author Johan Barçon
 */
public class UndirectedGraph extends Graph{

    /**
     * Default constructor that creates an empty graph.
     */
    public UndirectedGraph() {
        super();
    }

    /**
     * Constructor that creates an empty graph with a specified name.
     * @param name the name of the graph
     */
    public UndirectedGraph(String name) {
        super(name);
    }

    /**
     * Constructor that creates a graph based on an array of node integers.
     * Each integer represents a node ID, with `0` indicating separation between nodes.
     * @param nodes array of integers representing the graph's structure
     */
    public UndirectedGraph(int... nodes) {
        super(nodes);
    }

    @Override
    public void addEdge(Node from, Node to) {
        super.addEdge(from, to);
        super.addEdge(to, from);
    }

    @Override
    public void addEdge(Node from, Node to, int weight) {
        super.addEdge(from, to, weight);
        super.addEdge(to, from, weight);
    }

    @Override
    public void addEdge(int fromId, int toId) {
        super.addEdge(fromId, toId);
        super.addEdge(toId, fromId);
    }

    @Override
    public void addEdge(int fromId, int toId, int weight) {
        super.addEdge(fromId, toId, weight);
        super.addEdge(toId, fromId, weight);
    }

    public void addEdge(int fromId, int toId, int weight, String color){
        super.addEdge(fromId, toId, weight, color);
        super.addEdge(toId, fromId, weight, color);
    }

    @Override
    public int nbEdges() {
        return super.nbEdges()/2;
    }

    @Override
    public void addEdge(Edge e) {
        super.addEdge(e);
    }

    @Override
    public boolean removeEdge(Node from, Node to) { return super.removeEdge(from, to) && super.removeEdge(to, from);}

    @Override
    public boolean removeEdge(Node from, Node to, int weight) { return super.removeEdge(from, to, weight) && super.removeEdge(to, from, weight);}

    @Override
    public boolean removeEdge(int fromId, int toId) { return super.removeEdge(getNode(fromId), getNode(toId)) && super.removeEdge(getNode(toId), getNode(fromId));}

    @Override
    public boolean removeEdge(int fromId, int toId, int weight) { return super.removeEdge(getNode(fromId), getNode(toId), weight) && super.removeEdge(getNode(toId), getNode(fromId), weight);}

    @Override
    public boolean removeEdge(Edge e) { return super.removeEdge(e); }


    @Override
    public List<Edge> getOutEdges(int nId) { return getOutEdges(getNode(nId)); }

    @Override
    public List<Edge> getOutEdges(Node n) {
        List<Edge> out = super.getIncidentEdges(n);
        List<Edge> res = new ArrayList<>();
        boolean oneOnTwo = true;
        for (Edge e : out) {
            if (e.from().equals(e.to())){
                if (oneOnTwo){
                    res.add(e);
                }
                oneOnTwo = !oneOnTwo;
            }else {
                res.add(e);
            }
        }

        return res;
    }

    @Override
    public List<Edge> getInEdges(int nId) {
        return getOutEdges(nId);
    }

    @Override
    public List<Edge> getInEdges(Node n) {
        return getOutEdges(n);
    }

    @Override
    public List<Edge> getIncidentEdges(Node n) {
        return getOutEdges(n);
    }

    @Override
    public List<Edge> getIncidentEdges(int nId) {
        return super.getOutEdges(nId);
    }

    @Override
    public List<Node> getSuccessorsMulti(Node n) {
        List<Edge> edges = getOutEdges(n);
        List<Node> successors = new ArrayList<Node>();
        boolean oneOnTwo = true;
        for (Edge e : edges) {
            if (e.isSelfLoop()){

                if (oneOnTwo) successors.add(e.to());
                oneOnTwo = !oneOnTwo;
            }
            else successors.add(e.to());
        }
        return successors;
    }

    @Override
    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        List<Edge> res = new ArrayList<>();
        boolean oneOnTwo = true;
        for (Node n : getAllNodes()){
            for (Edge e : getAdjEgList().get(n)){
                if (!edges.contains(e)){
                    if (e.isSelfLoop()){
                        if (oneOnTwo) {
                            res.add(e);
                        }
                        oneOnTwo = !oneOnTwo;
                    }else res.add(e);
                }

                if (!e.isSelfLoop())edges.add(e.getSymmetric());
            }
            oneOnTwo = true;
        }
        return res;
    }

    @Override
    public int[][] toAdjMatrix() {
        int countNodes = nbNodes();
        int line = 0;
        int column = 0;
        int [][] matrix = new int[countNodes][countNodes];
        for (Edge e: getAllEdges()) {
            matrix[e.from().getId() - 1][e.to().getId() - 1]++;
            if (!e.isSelfLoop()) matrix[e.to().getId() - 1][e.from().getId() - 1]++;
        }
        return matrix;
    }

    @Override
    public UndirectedGraph getReverse() {
        return copy();
    }

    @Override
    public UndirectedGraph toSimpleGraph() {
        UndirectedGraph simple = new UndirectedGraph();
        Edge newEdge;
        for (Node n : getAllNodes()){
            simple.addNode(n);
        }
        for (Edge e: getAllEdges()) {
            newEdge = new Edge(e.from().getId(), e.to().getId(), e.getWeight(), simple);
            if (newEdge.isSelfLoop() || (simple.existsEdge(newEdge) && simple.existsEdge(newEdge.getSymmetric()))) continue;
            simple.addEdge(newEdge);
        }
        return simple;
    }

    @Override
    public UndirectedGraph getTransitiveClosure() {
        UndirectedGraph trans = copy().toSimpleGraph();
        Edge newEdge;
        List<Node> descendents;
        for (Node n: trans.getAllNodes()) {
            descendents = getDescendents(n);
            for (Node descendent : descendents) {
                newEdge = new Edge(n.getId(), descendent.getId(), trans);
                if (!newEdge.isSelfLoop() && !trans.existsEdge(newEdge) && !trans.existsEdge(newEdge.getSymmetric())) {
                    trans.addEdge(newEdge);
                }
            }
        }
        return trans;
    }


    @Override
    public UndirectedGraph copy() {
        UndirectedGraph copy = new UndirectedGraph();
        for (Edge e: getAllEdges()) {
            copy.addEdge(new Edge(e.from().getId(), e.to().getId(), e.getWeight(), copy));
            copy.addEdge(new Edge(e.to().getId(),e.from().getId(), e.getWeight(), copy));
        }
        return copy;
    }


    /**
     * Loads an undirected graph from a DOT file with the default .gv extension.
     * @param filename the name of the file (without extension) to load
     * @return a Graph instance created from the file, or null if the file cannot be read
     */
    public static UndirectedGraph fromDotFile(String filename) { return fromDotFile(filename, ".gv");}


    /**
     * Loads an undirected graph from a DOT file with the specified extension (.gv or .dot).
     * @param filename the name of the file (without extension) to load
     * @param extension the file extension (.gv or .dot)
     * @return a Graph instance created from the file, or null if the file cannot be read
     */
    public static UndirectedGraph fromDotFile(String filename, String extension) {
        UndirectedGraph graph = null;
        if (!(extension.equals(".gv")) && !(extension.equals(".dot"))) return graph;
        File file = new File("./ressources/" + filename + extension);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        graph = new UndirectedGraph(); // Initialize an empty graph

        while (scanner.hasNext()) {
            String token = scanner.next();

            if (token.equals("graph") || token.equals("{") || token.equals("rankdir=LR")) {
                continue;
            }else if (token.equals("}")) {
                break;
            }

            // Parse nodes and edges
            try {
                int from = Integer.parseInt(token); // Read the first node
                scanner.next(); // Skip the "--"
                int to = scanner.nextInt(); // Read the second node

                // Check for attributes (label, len)
                String attributes = scanner.findInLine("\\[.*?\\]");
                Integer weight = null;
                if (attributes != null) {
                    // Parse the "label" attribute if present
                    String[] parts = attributes.replace("[", "").replace("]", "").split(",");
                    for (String part : parts) {
                        String[] keyValue = part.split("=");
                        if (keyValue[0].trim().equals("label") || keyValue[0].trim().equals("len")) {
                            weight = Integer.parseInt(keyValue[1].trim());
                        }
                    }
                }

                // Add the nodes and edge to the graph
                graph.addNode(from);
                graph.addNode(to);
                if (weight != null) {
                    graph.addEdge(from, to, weight);
                } else {
                    graph.addEdge(from, to);
                }
            } catch (NumberFormatException | IllegalStateException e) {
                // Handle potential parsing issues
                System.err.println("Error parsing line: " + token);
            }
        }
        scanner.close();
        return graph;
    }


    @Override
    public String toDotString() {
        StringBuilder res = new StringBuilder("# DOT string generated by the 'toDotString' function\n");
        res.append("graph ").append(getName()).append("{\n\trankdir=LR");
        List<Node> edgeNodes = getEdgesNodes(getAllEdges());
        List<Edge> edges = new ArrayList<>();
        List<Edge> edges1;
        boolean oneOnTwo = true;

        for (Node n : getAllNodes()){
            if (!edgeNodes.contains(n)){
                res.append("\n\t").append(n.getId());
                continue;
            }
            edges1 = getAdjEgList().get(n);
            Collections.sort(edges1);
            for (Edge e : edges1){
                if (!edges.contains(e)){
                    if (e.isSelfLoop()){
                        if (oneOnTwo) {
                            res.append("\n\t").append(e.toString());
                        }
                        oneOnTwo = !oneOnTwo;
                    }else res.append("\n\t").append(e.toString());
                }

                if (!e.isSelfLoop())edges.add(e.getSymmetric());
            }
            oneOnTwo = true;
        }
        if (label != null)
            res.append("\n\tlabel=\"").append(label).append("\"");
        return res.append("\n}").toString();
    }

}
