package m1graphs2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Represents a directed graph data structure, supporting multigraphs (multiple edges between nodes)
 * and self-loops. This class provides functionality for adding, removing, and accessing nodes and edges,
 * as well as methods for graph traversal, transformation, and representation.
 * @author Johan Barçon
 */
public class Graph {
    private Map<Node, List<Edge>> adjEgList;
    private String name;

    // Constructors
    /**
     * Default constructor that creates an empty graph.
     */
    public Graph() {
        adjEgList = new HashMap<Node, List<Edge>>(0);
    }

    /**
     * Constructor that creates an empty graph with a specified name.
     * @param name the name of the graph
     */
    public Graph(String name) {
        adjEgList = new HashMap<Node, List<Edge>>(0);
        this.name = name;
    }

    /**
     * Constructor that creates a graph based on an array of node integers.
     * Each integer represents a node ID, with `0` indicating separation between nodes.
     * @param nodes array of integers representing the graph's structure
     */
    public Graph(int... nodes){
        this();
        int nbNodes = 1;
        for (int node : nodes) if (node == 0) addNode(nbNodes++);
        int currNodeId = 1;
        List<Edge> edges = new ArrayList<Edge>();
        Node currNode = getNode(currNodeId);
        for (int node : nodes) {
            if (node ==  0) {
                currNodeId++;
            }
            else {
                addEdge(new Edge(getNode(currNodeId), getNode(node)));
            }
        }
    }

    /**
     * Constructor that creates a graph with a specified name and based on an array of node integers.
     * @param name the name of the graph
     * @param nodes array of integers representing the graph's structure
     */
    public Graph(String name, int... nodes){
        this(nodes);
        this.name = name;
    }

    /**
     * Retrieves the adjacency list representation of the graph.
     * @return a map of nodes to their respective edge lists
     */
    public Map<Node, List<Edge>> getAdjEgList() {
        return adjEgList;
    }

    /**
     * Retrieves the name of the graph.
     * @return the name of the graph, or an empty string if the name is null
     */
    public String getName() {
        return name == null ? "" : name;
    }

    /**
     * Sets the name of the graph.
     * @param name the new name for the graph
     */
    public void setName(String name) {
        this.name = name;
    }

    // Node management methods
    /**
     * Returns the number of nodes in the graph.
     * @return the number of nodes in the graph
     */
    public int nbNodes() {
        return adjEgList.keySet().size();
    }

    /**
     * Checks if a specified node is used in the graph.
     * @param n the node to check
     * @return true if the node exists in the graph, false otherwise
     */
    public boolean usesNode(Node n) {
        return getNode(n.getId()) != null;
    }

    /**
     * Checks if a node with the given ID exists in the graph.
     * @param nId the ID of the node to check
     * @return true if a node with the specified ID exists, false otherwise
     */
    public boolean usesNode(int nId) {
        return getNode(nId) != null;
    }

    /**
     * Checks if the specified node is present in the graph.
     * @param n the node to check
     * @return true if the node is present, false otherwise
     */
    public boolean holdsNode(Node n) {
        return adjEgList.containsKey(n);
    }

    /**
     * Checks if a node with the given ID is present in the graph.
     * @param nId the ID of the node to check
     * @return true if a node with the specified ID is present, false otherwise
     */
    public boolean holdsNode(int nId) {
        return adjEgList.containsKey(getNode(nId));
    }

    /**
     * Retrieves a node in the graph by its unique ID.
     * @param id the ID of the node to retrieve
     * @return the Node with the specified ID, or null if no such node exists
     */
    public Node getNode(int id) {
        for (Node n : adjEgList.keySet()) {
            if (n.getId() == id) return n;
        }
        return null;
    }

    /**
     * Adds a node to the graph.
     * @param n the node to add
     * @return true if the node was added, false if it already exists
     */
    public boolean addNode(Node n) {
        if (holdsNode(n)) return false;
        adjEgList.put(n, new ArrayList<Edge>());
        return holdsNode(n);
    }

    /**
     * Adds a node to the graph by its ID.
     * @param nId the ID of the node to add
     * @return true if the node was added, false if it already exists
     */
    public boolean addNode(int nId) {
        if (holdsNode(nId)) return false;
        addNode(new Node(this, nId));
        return holdsNode(nId);
    }

    /**
     * Removes a specified node from the graph and all edges incident to it.
     * @param n the node to remove
     * @return true if the node was successfully removed, false otherwise
     */
    public boolean removeNode(Node n) {
        if (!(holdsNode(n))) return false;
        List<Edge> in = getInEdges(n);
        for (Edge e : in) {
            removeEdge(e.from(), e.to());
            if (in.isEmpty()) break;
        }
        adjEgList.remove(n);
        return true;
    }

    /**
     * Removes a node from the graph by its ID.
     * @param nId the ID of the node to remove
     * @return true if the node was successfully removed, false otherwise
     */
    public boolean removeNode(int nId) {
        return removeNode(getNode(nId));
    }

    /**
     * Retrieves a list of all nodes in the graph.
     * @return a sorted list of all nodes in the graph
     */
    public List<Node> getAllNodes() {
        List<Node> res = new ArrayList<Node>(adjEgList.keySet());
        Collections.sort(res);
        return res;
    }

    /**
     * Retrieves the largest node ID in the graph.
     * @return the largest node ID, or 0 if the graph is empty
     */
    public int largestNodeId() {
        List<Node> nodes = getAllNodes();
        int largestId = 0;
        for (Node n : nodes) {
            if (largestId < n.getId()) largestId = n.getId();
        }
        return largestId;
    }

    /**
     * Retrieves the smallest node ID in the graph.
     * @return the smallest node ID, or 0 if the graph is empty
     */
    public int smallestNodeId() {
        List<Node> nodes = getAllNodes();
        return nodes.isEmpty() ? 0 : nodes.get(0).getId();
    }

    /**
     * Retrieves the successors of a given node, without duplicates.
     * @param n the node for which to retrieve successors
     * @return a sorted list of successor nodes
     */
    public List<Node> getSuccessors(Node n) {
        List<Edge> edges = getOutEdges(n.getId());
        Set<Node> successors = new HashSet<>();
        for (Edge e : edges) {
            successors.add(e.to());
        }
        List<Node> res = new ArrayList<>(successors);
        Collections.sort(res);
        return res;
    }

    /**
     * Retrieves the successors of a node by its ID, without duplicates.
     * @param nId the ID of the node for which to retrieve successors
     * @return a sorted list of successor nodes
     */
    public List<Node> getSuccessors(int nId) {
        return getSuccessors(getNode(nId));
    }

    /**
     * Retrieves the successors of a given node, allowing duplicates.
     * @param n the node for which to retrieve successors
     * @return a list of successor nodes, with possible duplicates
     */
    public List<Node> getSuccessorsMulti(Node n) {
        List<Edge> edges = getOutEdges(n);
        List<Node> successors = new ArrayList<>();
        for (Edge e : edges) {
            successors.add(e.to());
        }
        return successors;
    }

    /**
     * Retrieves the successors of a node by its ID, allowing duplicates.
     * @param nId the ID of the node for which to retrieve successors
     * @return a list of successor nodes, with possible duplicates
     */
    public List<Node> getSuccessorsMulti(int nId) {
        return getSuccessorsMulti(getNode(nId));
    }

    /**
     * Retrieves all the descendents of a node
     * @param n the source node
     * @return a list of all the descendents nodes
     */
    public List<Node> getDescendents(Node n) {
        Set<Node> descendents = new HashSet<>(getSuccessors(n));
        descendents.remove(n);
        descendents.addAll(getDescendents(descendents));
        return new ArrayList<Node>(descendents);
    }

    /**
     * Retrieves all the descendents of a list of nodes
     * @param n a list of source nodes
     * @return a list of all the descendents of the node list
     */
    public Set<Node> getDescendents(Set<Node> n) {
        Set<Node> descendents = new HashSet<>(n);
        Set<Node> curr = new HashSet<>();
        for (Node node : n) {
            curr.addAll(getSuccessors(node));
        }
        if (descendents.containsAll(curr)) return descendents;
        descendents.addAll(curr);
        return getDescendents(descendents);
    }

    /**
     * Retrieves all the descendents of a node id
     * @param nId the source node's id
     * @return a list of all the descendents nodes
     */
    public List<Node> getDescendents(int nId) { return getDescendents(getNode(nId));}

    /**
     * Checks if two nodes are adjacent (connected by an edge) in the graph.
     * @param u the first node
     * @param v the second node
     * @return true if nodes u and v are adjacent, false otherwise
     */
    public boolean adjacent(Node u, Node v) {
        List<Edge> edges = getIncidentEdges(u);
        for (Edge e : edges) {
            if (e.to().equals(v) || e.from().equals(v)) return true;
        }
        return false;
    }

    /**
     * Checks if two nodes are adjacent (connected by an edge) in the graph using their IDs.
     * @param uId the ID of the first node
     * @param vId the ID of the second node
     * @return true if nodes with IDs uId and vId are adjacent, false otherwise
     */
    public boolean adjacent(int uId, int vId) {
        return adjacent(getNode(uId), getNode(vId));
    }

    /**
     * Retrieves the in-degree of a specified node (number of incoming edges).
     * @param n the node for which to calculate the in-degree
     * @return the in-degree of the node
     */
    public int inDegree(Node n) {
        return getInEdges(n).size();
    }

    /**
     * Retrieves the in-degree of a node by its ID (number of incoming edges).
     * @param nId the ID of the node
     * @return the in-degree of the node
     */
    public int inDegree(int nId) {
        return inDegree(getNode(nId));
    }
    /**
     * Retrieves the out-degree of a specified node (number of outgoing edges).
     * @param n the node for which to calculate the out-degree
     * @return the out-degree of the node
     */
    public int outDegree(Node n) {
        return getOutEdges(n).size();
    }

    /**
     * Retrieves the out-degree of a node by its ID (number of outgoing edges).
     * @param nId the ID of the node
     * @return the out-degree of the node
     */
    public int outDegree(int nId) {
        return outDegree(getNode(nId));
    }

    /**
     * Retrieves the total degree (sum of in-degree and out-degree) of a specified node.
     * @param n the node for which to calculate the degree
     * @return the total degree of the node
     */
    public int degree(Node n) {
        return getIncidentEdges(n).size();
    }

    /**
     * Retrieves the total degree (sum of in-degree and out-degree) of a node by its ID.
     * @param nId the ID of the node
     * @return the total degree of the node
     */
    public int degree(int nId) {
        return degree(getNode(nId));
    }

    /**
     * Retrieves the total number of edges in the graph.
     * @return the total number of edges
     */
    public int nbEdges() {
        int nbEdges = 0;
        for (List<Edge> edges : adjEgList.values()) {
            nbEdges += edges.size();
        }
        return nbEdges;
    }

    /**
     * Checks if an edge exists between two nodes in the graph.
     * @param u the source node
     * @param v the target node
     * @return true if an edge exists from u to v, false otherwise
     */
    public boolean existsEdge(Node u, Node v) {
        return holdsNode(u) && holdsNode(v) && !getEdges(u, v).isEmpty();
    }

    /**
     * Checks if an edge exists between two nodes in the graph by their IDs.
     * @param uId the ID of the source node
     * @param vId the ID of the target node
     * @return true if an edge exists from uId to vId, false otherwise
     */
    public boolean existsEdge(int uId, int vId) {
        return holdsNode(uId) && holdsNode(vId) && !getEdges(uId, vId).isEmpty();
    }

    /**
     * Checks if an edge exists in the graph.
     * @param e the edge to verify
     * @return true if the edge exists, false otherwise
     */
    public boolean existsEdge(Edge e) { return holdsNode(e.from()) && holdsNode(e.to()) && !getEdges(e.from(), e.to()).isEmpty();}

    public boolean isMultiEdge(Node u, Node v) {
        int countEdges = 0;
        List<Node> multiSuccessors = getSuccessorsMulti(u);
        for (Node m : multiSuccessors) {
            if (m.equals(v)) countEdges++;
        }
        return countEdges > 1;
    }

    public boolean isMultiEdge(int uId, int vId) { return isMultiEdge(getNode(uId), getNode(vId));}

    public boolean isMultiEdge(Edge e) { return e.isMultiEdge();}

    /**
     * Adds an edge between two nodes in the graph.
     * @param from the source node
     * @param to the target node
     */
    public void addEdge(Node from, Node to) {
        if (from == null || to == null) return;
        if (!adjEgList.containsKey(from)) adjEgList.put(from, new ArrayList<>());
        if (!adjEgList.containsKey(to)) adjEgList.put(to, new ArrayList<>());
        addEdge(new Edge(from, to));
    }

    /**
     * Adds an edge between two nodes in the graph with a specified weight.
     * @param from the source node
     * @param to the target node
     * @param weight the weight of the edge
     */
    public void addEdge(Node from, Node to, int weight) {
        if (from == null || to == null) return;
        if (!adjEgList.containsKey(from)) adjEgList.put(from, new ArrayList<>());
        if (!adjEgList.containsKey(to)) adjEgList.put(to, new ArrayList<>());
        addEdge(new Edge(from, to, weight));
    }

    /**
     * Adds an edge between two nodes ids in the graph.
     * @param fromId the source node's id
     * @param toId the target node's id
     */
    public void addEdge(int fromId, int toId) {
        if (fromId <= 0 || toId <= 0) return;
        addEdge(new Edge(fromId, toId, this));
    }

    /**
     * Adds an edge between two nodes ids in the graph with a specified weight.
     * @param fromId the source node's id
     * @param toId the target node's id
     * @param weight the weight of the edge
     */
    public void addEdge(int fromId, int toId, int weight) {
        if (fromId <= 0 || toId <= 0) return;
        addEdge(new Edge(fromId, toId, weight, this));
    }

    /**
     * Adds an edge in the graph.
     * @param e the edge to add
     */
    public void addEdge(Edge e) {
        if (!adjEgList.containsKey(e.from())) adjEgList.put(e.from(), new ArrayList<>());
        if (!adjEgList.containsKey(e.to())) adjEgList.put(e.to(), new ArrayList<>());
        adjEgList.get(e.from()).add(e);
    }

    /**
     * Removes an edge between two nodes in the graph.
     * @param from the source node
     * @param to the target node
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(Node from, Node to) {
        if (!holdsNode(from) || !holdsNode(to)) return false;
        for (Edge e : adjEgList.get(from)) {
            if (e.to().equals(to)) {
                return removeEdge(e);
            }
        }
        return false;
    }

    /**
     * Removes an edge between two nodes with th corresponding weight in the graph.
     * @param from the source node
     * @param to the target node
     * @param weight the edge's weight
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(Node from, Node to, int weight) {
        if (!holdsNode(from) || !holdsNode(to)) return false;
        for (Edge e : adjEgList.get(from)) {
            if (e.to().equals(to) && e.getWeight() == weight) {
                return removeEdge(e);
            }
        }
        return false;
    }

    /**
     * Removes an edge between two nodes ids in the graph.
     * @param fromId the source node's id
     * @param toId the target node's id
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(int fromId, int toId) { return removeEdge(getNode(fromId), getNode(toId));}

    /**
     * Removes an edge between two nodes ids with th corresponding weight in the graph.
     * @param fromId the source node's id
     * @param toId the target node's id
     * @param weight the edge's weight
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(int fromId, int toId, int weight) { return removeEdge(getNode(fromId), getNode(toId), weight);}

    /**
     * Removes an edge in the graph.
     * @param e the edge to remove
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(Edge e) { return adjEgList.get(e.from()).remove(e);}

    /**
     * Retrieves a list of all outgoing edges from a specified node.
     * @param n the node for which to retrieve outgoing edges
     * @return a list of outgoing edges, or an empty list if no edges exist
     */
    public List<Edge> getOutEdges(Node n) {
        return adjEgList.get(n) == null ? new ArrayList<Edge>() : adjEgList.get(n);
    }

    /**
     * Retrieves a list of all outgoing edges from a specified node id.
     * @param nId the node's id for which to retrieve outgoing edges
     * @return a list of outgoing edges, or an empty list if no edges exist
     */
    public List<Edge> getOutEdges(int nId) { return getOutEdges(getNode(nId));}

    /**
     * Retrieves a list of all incoming edges to a specified node.
     * @param n the node for which to retrieve incoming edges
     * @return a list of incoming edges
     */
    public List<Edge> getInEdges(Node n) {
        List<Edge> inEdges = new ArrayList<>();
        for (Edge edge : getAllEdges()) {
            if (edge.to().equals(n)) inEdges.add(edge);
        }
        return inEdges;
    }

    /**
     * Retrieves a list of all incoming edges to a specified node id.
     * @param nId the node's id for which to retrieve incoming edges
     * @return a list of incoming edges
     */
    public List<Edge> getInEdges(int nId) { return getInEdges(getNode(nId));}

    /**
     * Retrieves a list of all edges incident to a specified node.
     * @param n the node for which to retrieve incident edges
     * @return a list of incident edges (both incoming and outgoing)
     */
    public List<Edge> getIncidentEdges(Node n) {
        List<Edge> incidentEdges = new ArrayList<>();
        for (Edge edge : getAllEdges()) {
            if (edge.from().equals(n) || edge.to().equals(n)) incidentEdges.add(edge);
        }
        return incidentEdges;
    }

    /**
     * Retrieves a list of all edges incident to a specified node id.
     * @param nId the node's id for which to retrieve incident edges
     * @return a list of incident edges (both incoming and outgoing)
     */
    public List<Edge> getIncidentEdges(int nId) { return getIncidentEdges(getNode(nId));}

    /**
     * Retrieves a list of all edges between two specified nodes.
     * @param u the source node
     * @param v the target node
     * @return a list of edges from u to v, or an empty list if no edges exist
     */
    public List<Edge> getEdges(Node u, Node v) {
        List<Edge> edges = new ArrayList<>();
        List<Edge> edges1 = getOutEdges(u);
        if (edges1 == null) return edges;
        for (Edge edge : edges1) {
            if (edge.to().equals(v)) edges.add(edge);
        }
        return edges;
    }

    /**
     * Retrieves a list of all edges between two specified nodes ids.
     * @param uId the source node's id
     * @param vId the target node's id
     * @return a list of edges from u to v, or an empty list if no edges exist
     */
    public List<Edge> getEdges(int uId, int vId) { return getEdges(getNode(uId), getNode(vId));}

    /**
     * Retrieves a sorted list of all edges in the graph.
     * @return a list of all edges, sorted by source and target nodes
     */
    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adjEgList.values()) {
            allEdges.addAll(edges);
        }
        Collections.sort(allEdges);
        return allEdges;
    }

    /**
     * Retrieves all nodes concerned into a edge list 
     * @param edges a list of edges
     * @return a list of nodes concerned by the edges
     */
    public List<Node> getEdgesNodes(List<Edge> edges) {
        Set<Node> nodes = new HashSet<Node>();
        for (Edge e: edges) {
            nodes.add(e.from());
            nodes.add(e.to());
        }
        return new ArrayList<Node>(nodes);
    }

//  FOR REPRESENTATION & TRANSFORMATION
    /**
     * Converts the graph to a successor array representation.
     * @return an integer array representing the graph's successor relationships
     */
    public int[] toSuccessorArray() {
        int index = 0;
        List<Edge> edges = getAllEdges();
        int[] result = new int[getAllNodes().size() + edges.size()];
        for (int i = 1; i <= nbNodes(); i++) {
            for (Edge edge : getOutEdges(i)) {
                if (edges.contains(edge)) result[index++] = edge.to().getId();
            }
            index++;
        }
        return result;
    }

    /**
     * Converts the graph to an adjacency matrix representation.
     * @return a 2D integer array representing the adjacency matrix, with elements
     * indicating the number of edges between nodes
     */
    public int[][] toAdjMatrix() {
        int countNodes = nbNodes();
        int[][] matrix = new int[countNodes][countNodes];
        for (Edge e : getAllEdges()) {
            matrix[e.from().getId() - 1][e.to().getId() - 1]++;
        }
        return matrix;
    }

    /**
     * Computes the reverse of the current graph (with all edge directions flipped).
     * @return a new Graph instance representing the reverse of this graph
     */
    public Graph getReverse() {
        Graph reverse = new Graph();
        for (Node n : adjEgList.keySet()) {
            reverse.addNode(n);
        }
        for (Edge e : getAllEdges()) {
            reverse.addEdge(e.getSymmetric());
        }
        return reverse;
    }

    /**
     * Computes the transitive closure of the current graph, which adds edges
     * to ensure that there is a direct path between any reachable nodes.
     * @return a new Graph instance representing the transitive closure of this graph
     */
    public Graph getTransitiveClosure() {
        Graph trans = toSimpleGraph();
        Edge newEdge;
        for (Node n : trans.getAllNodes()) {
            for (Edge p : trans.getInEdges(n)) {
                for (Edge s : trans.getOutEdges(n)) {
                    newEdge = new Edge(p.from().getId(), s.to().getId(), trans);
                    if (newEdge.isSelfLoop() || trans.existsEdge(newEdge)) continue;
                    trans.addEdge(newEdge);
                }
            }
        }
        return trans;
    }

    /**
     * Checks if the graph contains at least one multi-edge (multiple edges between any two nodes).
     * @return true if the graph is a multigraph, false otherwise
     */
    public boolean isMultiGraph() {
        for (List<Edge> edges: adjEgList.values()) {
            for (Edge edge : edges) {
                if (isMultiEdge(edge)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the graph is simple (contains no self-loops or multi-edges).
     * @return true if the graph is simple, false otherwise
     */
    public boolean isSimpleGraph() {
        for (List<Edge> edges: adjEgList.values()) {
            for (Edge edge : edges) {
                if (isMultiEdge(edge) || edge.isSelfLoop()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the graph contains any self-loops.
     * @return true if the graph contains self-loops, false otherwise
     */
    public boolean hasSelfLoops() {
        for (List<Edge> edges: adjEgList.values()) {
            for (Edge edge : edges) {
                if (edge.isSelfLoop()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Transforms the graph into a simple graph by removing self-loops and multi-edges.
     * @return a new Graph instance representing the simple version of this graph
     */
    public Graph toSimpleGraph() {
        Graph simple = new Graph();
        Edge newEdge;
        for (Edge e: getAllEdges()) {
            newEdge = new Edge(e.from().getId(), e.to().getId(), e.getWeight(), simple);
            if (newEdge.isSelfLoop() || simple.existsEdge(newEdge)) continue;
            simple.addEdge(newEdge);
        }
        return simple;
    }

    /**
     * Creates a deep copy of the graph.
     * @return a new Graph instance representing a copy of this graph
     */
    public Graph copy() {
        Graph copy = new Graph();
        for (Node n: getAllNodes()) {
            copy.addNode(new Node(copy, n.getId(), n.getName()));
        }
        for (Edge e: getAllEdges()) {
            copy.addEdge(new Edge(e.from().getId(), e.to().getId(), e.getWeight(), copy));
        }
        return copy;
    }

    //  FOR REPRESENTATION & TRANSFORMATION Graph Traversal
    /**
     * Performs a depth-first search (DFS) traversal starting from a node with the given ID.
     * @param uId the ID of the node to start DFS from
     * @return a list of nodes in DFS traversal order
     */
    public List<Node> getDFS(int uId) {
        return getDFS(getNode(uId));
    }

    /**
     * Performs a depth-first search (DFS) traversal starting from the node with the smallest ID.
     * @return a list of nodes in DFS traversal order
     */
    public List<Node> getDFS() {
        return getDFS(smallestNodeId());
    }

    /**
     * Performs a depth-first search (DFS) traversal starting from a specified node.
     * @param u the node to start DFS from
     * @return a list of nodes in DFS traversal order
     */
    public List<Node> getDFS(Node u) {
        List<Node> dfsNodes = new ArrayList<Node>();
        Map<Integer, NodeColour> nodeColour = new HashMap<>();
        Map<Integer, Integer> d = new HashMap<>();
        Map<Integer, Integer> f = new HashMap<>();
        Map<Integer, Node> pi = new HashMap<>();
        List<Node> nodes = getAllNodes();

        for (Node n: nodes) {
            nodeColour.put(n.getId(), NodeColour.WHITE);
            pi.put(n.getId(), null);
        }
        int time = 0;

            if (nodeColour.get(u.getId()) == NodeColour.WHITE) {
                if (!(dfsNodes.contains(u))) dfsNodes.add(u);
                dfsVisit(u, time, nodeColour, d, f, pi, dfsNodes);
            }
        for (Node n: nodes) {
            if (nodeColour.get(n.getId()) == NodeColour.WHITE) {
                if (!(dfsNodes.contains(n))) dfsNodes.add(n);
                dfsVisit(n, time, nodeColour, d, f, pi, dfsNodes);
            }
        }
        return dfsNodes;
    }

    /**
     * Helper method for depth-first search (DFS). Visits nodes recursively, marking discovery and finish times.
     * @param u the current node being visited
     * @param time the current timestamp in the DFS traversal
     * @param nodeColour a map tracking the color (visit status) of each node
     * @param d a map storing the discovery time of each node
     * @param f a map storing the finish time of each node
     * @param pi a map storing the predecessor of each node
     * @param dfsNodes the list of nodes visited in DFS order
     * @return a list of nodes in DFS traversal order
     */
    public List<Node> dfsVisit(Node u, int time, Map<Integer, NodeColour> nodeColour, Map<Integer, Integer> d, Map<Integer, Integer> f, Map<Integer, Node> pi, List<Node> dfsNodes) {
        time++;
        d.put(u.getId(), time);
        nodeColour.put(u.getId(), NodeColour.GREY);
        List<Node> successors = u.getSuccessors();
        Collections.sort(successors);
        for (Node v: successors) {
            if (nodeColour.get(v.getId()) == NodeColour.WHITE) {
                if (!(dfsNodes.contains(v))) dfsNodes.add(v);
                pi.put(v.getId(), u);
                dfsVisit(v, time, nodeColour, d, f, pi, dfsNodes);
            }
        }
        nodeColour.put(u.getId(), NodeColour.BLACK);
        time++;
        f.put(u.getId(), time);
        return dfsNodes;
    }

    /**
     * Performs a breadth-first search (BFS) traversal starting from a specified node id.
     * @param uId the node's id to start BFS from
     * @return a list of nodes in BFS traversal order
     */
    public List<Node> getBFS(int uId) { return getBFS(getNode(uId));}
    
    /**
     * Performs a breadth-first search (BFS) traversal starting from the node with the smallest ID.
     * @return a list of nodes in BFS traversal order
     */
    public List<Node> getBFS() {
        return getBFS(smallestNodeId());
    }

    /**
     * Performs a breadth-first search (BFS) traversal starting from a specified node.
     * @param u the node to start BFS from
     * @return a list of nodes in BFS traversal order
     */
    public List<Node> getBFS(Node u) {
        List<Node> bfsNodes = new ArrayList<>();
        Map<Integer, NodeColour> nodeColour = new HashMap<>();
        Map<Integer, Integer> d = new HashMap<>();
        Map<Integer, Node> parent = new HashMap<>();
        for (Node n: getAllNodes()) {
            d.put(n.getId(), -1);
            parent.put(n.getId(), null);
            nodeColour.put(n.getId(), NodeColour.WHITE);
        }
        getBFSVisit(u, nodeColour, d, parent, bfsNodes);
        for (Integer iColor : nodeColour.keySet()){
            if (nodeColour.get(iColor) == NodeColour.WHITE){
                getBFSVisit(getNode(iColor), nodeColour, d, parent, bfsNodes);
            }
        }
        return bfsNodes;
    }

    /**
     * Helper method for breadth-first search (BFS) traversal, visiting nodes level by level.
     * @param u the node to start BFS from
     * @param nodeColour a map tracking the color (visit status) of each node
     * @param d a map storing the distance of each node from the start node
     * @param parent a map storing the parent of each node
     * @param bfsNodes the list of nodes visited in BFS order
     */
    public void getBFSVisit(Node u, Map<Integer, NodeColour> nodeColour, Map<Integer, Integer> d, Map<Integer, Node> parent, List<Node> bfsNodes) {
        nodeColour.put(u.getId(), NodeColour.GREY);
        bfsNodes.add(u);
        d.put(u.getId(), -1);
        parent.put(u.getId(), null);
        Queue<Node> toDo = new LinkedList<Node>();
        toDo.add(u);
        while (!toDo.isEmpty()) {
            Node curr = toDo.poll();
            for (Node n: curr.getSuccessors()) {
                if (nodeColour.get(n.getId()) == NodeColour.WHITE) {
                    toDo.add(n);
                    if (!(bfsNodes.contains(n))) bfsNodes.add(n);
                    nodeColour.put(n.getId(), NodeColour.GREY);
                    d.put(n.getId(), d.get(curr.getId()) + 1);
                    parent.put(n.getId(), curr);
                }
            }
            nodeColour.put(curr.getId(), NodeColour.BLACK);
        }
    }

    /**
     * Performs a depth-first search with additional visit information, starting from the smallest node ID.
     * @param nodeVisit a map to store visit information for each node
     * @param edgeVisit a map to store edge visit types
     * @return a list of nodes in DFS order with visit info recorded
     */
    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) { return getDFSWithVisitInfo(smallestNodeId(), nodeVisit, edgeVisit);}

    /**
     * Performs a depth-first search with additional visit information, starting from a specified node.
     * @param u the node to start DFS from
     * @param nodeVisit a map to store visit information for each node
     * @param edgeVisit a map to store edge visit types
     * @return a list of nodes in DFS order with visit info recorded
     */
    public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
        List<Node> nodes = getAllNodes();
        if (nodeVisit.isEmpty()) for (Node node : nodes) nodeVisit.put(node, new NodeVisitInfo());
        if (edgeVisit.isEmpty()) for (Edge edge : getAllEdges()) edgeVisit.put(edge, EdgeVisitType.NULL);
        int time = 0;
        List<Node> dfsNodes = new ArrayList<Node>();
        dfsNodes.add(u);

        dfsVisitWithVisitInfo(u, time, nodeVisit, edgeVisit, dfsNodes);

        for (Node n: nodes) {
            if (nodeVisit.get(n).getColor() == NodeColour.WHITE) {
                if (!(dfsNodes.contains(n))) dfsNodes.add(n);
                time = 0;
                dfsVisitWithVisitInfo(n, time, nodeVisit, edgeVisit, dfsNodes);
            }
        }
        return dfsNodes;
    }

    /**
     * Helper method for depth-first search with visit information, categorizing edges as tree, back, forward, or cross.
     * @param u the current node being visited
     * @param time the current timestamp in the DFS traversal
     * @param nodeVisit a map storing visit information for each node
     * @param edgeVisit a map storing edge visit types
     * @param dfsNodes the list of nodes visited in DFS order
     * @return a list of nodes in DFS traversal order with visit information
     */
    public List<Node> dfsVisitWithVisitInfo(Node u, int time, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit, List<Node> dfsNodes) {
        time++;
        NodeVisitInfo nodeVisitInfo = nodeVisit.get(u);
        nodeVisitInfo.setDiscovery(time);
        nodeVisitInfo.setColor(NodeColour.GREY);
        for (Edge e: u.getOutEdges()) {
            List<Node> descendents = getDescendents(e.from());
            descendents.remove(e.from());
            descendents.removeAll(getSuccessors(e.from()));
            switch (nodeVisit.get(e.to()).getColor()) {
                case WHITE:
                    edgeVisit.replace(e, descendents.contains(e.to())? EdgeVisitType.FORWARD : EdgeVisitType.TREE);
                    break;
                case GREY:
                    edgeVisit.replace(e, EdgeVisitType.BACKWARD);
                    break;
                case BLACK:
                    edgeVisit.replace(e, descendents.contains(e.to())? EdgeVisitType.FORWARD : EdgeVisitType.CROSS);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + nodeVisit.get(e.to()).getColor());
            }
        }
        for (Node v: u.getSuccessors()) {
            if (nodeVisit.get(v).getColor() == NodeColour.WHITE) {
                if (!(dfsNodes.contains(v))) dfsNodes.add(v);
                nodeVisit.get(v).setPredecessor(u);
                dfsVisitWithVisitInfo(v, time, nodeVisit, edgeVisit, dfsNodes);
            }
        }
        nodeVisitInfo.setColor(NodeColour.BLACK);
        time++;
        nodeVisitInfo.setFinished(time);
        return dfsNodes;
    }

    /**
     * Performs a depth-first search with additional visit information, starting from a specified node id.
     * @param uId the node´s id to start DFS from
     * @param nodeVisit a map to store visit information for each node
     * @param edgeVisit a map to store edge visit types
     * @return a list of nodes in DFS order with visit info recorded
     */
    public List<Node> getDFSWithVisitInfo(int uId, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) { return getDFSWithVisitInfo(getNode(uId), nodeVisit, edgeVisit);}

    // FOR DOT
     /**
     * Loads a graph from a DOT file with the default .gv extension.
     * @param filename the name of the file (without extension) to load
     * @return a Graph instance created from the file, or null if the file cannot be read
     */
    public static Graph fromDotFile(String filename) {
        return fromDotFile(filename, ".gv");
    }

    /**
     * Loads a graph from a DOT file with the specified extension (.gv or .dot).
     * @param filename the name of the file (without extension) to load
     * @param extension the file extension (.gv or .dot)
     * @return a Graph instance created from the file, or null if the file cannot be read
     */
    public static Graph fromDotFile(String filename, String extension) {
        Graph graph = null;
        if (!(extension.equals(".gv")) && !(extension.equals(".dot"))) return graph;
        File file = new File("./ressources/" + filename + extension);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.charAt(0) == '#' || line.trim().isEmpty()) continue;
                String[] tokens = line.split("\\s+");
                if (line.contains("{")){
                    if (tokens.length == 3) {
                        if (Objects.equals(tokens[2], "{")) {
                            if (tokens[0].equals("digraph")) {
                                graph = new Graph(tokens[1]);
                            } else {
                                return null;
                            }
                        }
                    }else{
                        if (tokens[0].equals("digraph")) {
                            graph = new Graph();
                        } else {
                            return null;
                        }
                    }
                }
                if (tokens[tokens.length - 1].equals("}")) {
                    return graph;
                }
                if (graph != null){
                    if (tokens.length >= 3) {
                        if (tokens[1].equals("->")) {
                            int nodeId1 = Integer.parseInt(tokens[0]);
                            int nodeId2 = Integer.parseInt(tokens[2]);
                            graph.addNode(nodeId1);
                            graph.addNode(nodeId2);
                            if (tokens.length > 3) {
                                graph.addEdge(nodeId1, nodeId2, Integer.parseInt(tokens[tokens.length - 1].split("=")[1].replace("]", "")));
                            } else {
                                graph.addEdge(nodeId1, nodeId2);
                            }
                        }
                    }else if (tokens.length == 1 && tokens[0].matches("[0-9]+")) graph.addNode(Integer.parseInt(tokens[0]));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return graph;
    }

    /**
     * Generates a DOT format string representation of the graph.
     * @return a String in DOT format representing the graph
     */
    public String toDotString() {
        StringBuilder res = new StringBuilder("# DOT string generated by the 'toDotString' function\n");
        res.append("digraph ").append(getName()).append("{\n\trankdir=LR");
        List<Node> edgeNodes = getEdgesNodes(getAllEdges());
        List<Edge> edges;
        for (Node n : getAllNodes()){
            if (!edgeNodes.contains(n)){
                res.append("\n\t").append(n.getId());
                continue;
            }
            edges = adjEgList.get(n);
            Collections.sort(edges);
            for (Edge e : edges){
                res.append("\n\t").append(e.toString());
            }
        }
        return res.append("\n}").toString();
    }

    /**
     * Exports the graph to a DOT file with the default .gv extension.
     * @param fileName the name of the file (without extension) to export to
     * @throws IOException if an I/O error occurs during file creation
     */
    public void toDotFile(String fileName) throws IOException {
        toDotFile(fileName, ".gv");
    }

    /**
     * Exports the graph to a DOT file with the specified extension.
     * @param fileName the name of the file (without extension) to export to
     * @param extension the file extension (.gv or other)
     * @throws IOException if an I/O error occurs during file creation
     */
    public void toDotFile(String fileName, String extension) throws IOException {
        File targetFile = new File("./ressources/" + fileName + extension);
        targetFile.delete();
        File newFile = new File("./ressources/" + fileName + extension);
        boolean success = newFile.createNewFile();
        if (!success) throw new IOException("Could not create file " + targetFile);
        else {
            Path filePath = Paths.get("./ressources/" + fileName + extension);
            Files.writeString(filePath, toDotString());
        }
    }
}
