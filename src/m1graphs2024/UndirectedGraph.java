package m1graphs2024;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<Node> getSuccessors(Node n) {
        List<Edge> edges = getIncidentEdges(n.getId());
        //System.out.println("SUCC EDGES of "+n+": "+edges);
        Set<Node> successors = new HashSet<>();
        for (Edge e : edges) {
            successors.add(e.to());
            successors.add(e.from());
        }
        List<Node> res = new ArrayList<>(successors);
        Collections.sort(res);
        return res;
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
        UndirectedGraph graph = new UndirectedGraph();
        String path = "./ressources/" + filename + extension;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean inGraph = false;
            Pattern nodePattern = Pattern.compile("^\\s*(\\d+)\\s*(?:\\[.*\\])?\\s*$");
            Pattern edgePattern = Pattern.compile("^\\s*(\\d+)\\s*(--{1,2})\\s*(\\d+)(?:\\s*\\[(.*)\\])?\\s*$");
            Pattern weightPattern = Pattern.compile("label=(\\d+),\\s*len=(\\d+)");
            Pattern weight2Pattern = Pattern.compile("len=(\\d+)");
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("digraph") || line.startsWith("graph")) {
                    inGraph = true;
                    continue;
                }
                if (line.equals("}")) {
                    inGraph = false;
                    continue;
                }
                if (!inGraph) {
                    continue;
                }
                if (line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                }
                Matcher nodeMatcher = nodePattern.matcher(line);
                if (nodeMatcher.matches()) {
                    int nodeId = Integer.parseInt(nodeMatcher.group(1));
                    graph.addNode(nodeId);
                    continue;
                }
                Matcher edgeMatcher = edgePattern.matcher(line);
                if (edgeMatcher.matches()) {
                    int fromId = Integer.parseInt(edgeMatcher.group(1));
                    int toId = Integer.parseInt(edgeMatcher.group(3));
                    Node from = graph.getNode(fromId);
                    if (from == null) {
                        from = new Node(graph, fromId);
                        graph.addNode(from);
                    }
                    Node to = graph.getNode(toId);
                    if (to == null) {
                        to = new Node(graph, toId);
                        graph.addNode(to);
                    }
                    Integer weight = null;
                    String attributes = edgeMatcher.group(4);
                    if (attributes != null) {
                        Matcher weightMatcher = weightPattern.matcher(attributes);
                        Matcher weight2Matcher = weight2Pattern.matcher(attributes);
                        if (weightMatcher.matches()) {
                            int labelWeight = Integer.parseInt(weightMatcher.group(1));
                            int lenWeight = Integer.parseInt(weightMatcher.group(2));
                            if (labelWeight == lenWeight) {
                                weight = labelWeight;
                            }
                        }
                        if (weight2Matcher.matches()) {
                            weight = Integer.parseInt(weight2Matcher.group(1));
                        }
                    }
                    graph.addEdge(new Edge(from, to, weight));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading DOT file: " + e.getMessage());
        }
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
