package m1graphs2024;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a graph with nodes and edges
 */
public class Graph {

    protected Map<Node, List<Edge>> adjEdList;

    /**
     * COnstructs an empty graph
     */
    public Graph(){
        adjEdList = new HashMap<>();
    }

    /**
     * Constructs a graph with the specified nodes
     * @param nodes the nodes to be added to the graph
     */
    public Graph(int ... nodes){
        adjEdList = new HashMap<>();
        int i = 1;
        for(int n : nodes){
            if(n == 0){
                if(!adjEdList.containsKey(new Node(i, this))) {
                    adjEdList.put(new Node(i, this), new ArrayList<>());
                }
                i++;
            }else{
                if(!adjEdList.containsKey(new Node(i, this))){
                    adjEdList.put(new Node(i, this), new ArrayList<>());
                }
                adjEdList.get(new Node(i, this)).add(new Edge(new Node(i, this), new Node(n, this), this, null));
            }
        }
    }

    /**
     * Creates a graph from a DOT file with specified filename
     * @param filename the name of the DOT file
     * @return the created graph
     */
    public static Graph fromDotFile(String filename){
        return Graph.fromDotFile(filename, "gv");
    }

    /**
     * Creates a graph from a DOT file with specified filename and extension
     * @param filename the name of the DOT file
     * @param extension the extension of the DOT file
     * @return the created graph
     */
    public static Graph fromDotFile(String filename, String extension){
        Graph g = new Graph();


        Path startPath = Paths.get(System.getProperty("user.dir"));
        String pattern = filename+"."+extension;
        final String[] src = {""};

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().equals(pattern)) {
                        if(!file.toAbsolutePath().toString().contains("out")){
                            src[0] = file.toAbsolutePath().toString();
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(BufferedReader br = new BufferedReader(new FileReader(src[0]))){
            String line;
            while((line = br.readLine()) != null){
                if(line.contains("->")){
                    Integer weight = null;
                    String[] nodes = line.split("->");
                    Node from = new Node(Integer.parseInt(nodes[0].trim()), g);
                    if(nodes[1].contains("[")){

                        Pattern pattern_ = Pattern.compile("label=(\\d+)");
                        Matcher matcher = pattern_.matcher(nodes[1]);
                        if(matcher.find()){
                            weight = Integer.parseInt(matcher.group(1));
                        }
                        nodes[1] = nodes[1].substring(0, nodes[1].indexOf("["));
                    }
                    Node to = new Node(Integer.parseInt(nodes[1].trim()), g);

                    if(!g.adjEdList.containsKey(from)){
                        g.adjEdList.put(from, new ArrayList<>());
                    }

                    Edge e = new Edge(from, to, g, weight);
                    g.addEdge(e);
                }else if(!line.contains("digraph") && !line.contains("rankdir") && !line.contains("}")){
                    g.adjEdList.put(new Node(Integer.parseInt(line.trim()), g), new ArrayList<>());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return g;


    }


    /**
     * Converts the graph to a DOT string representation
     * @return the DOT string representation of the graph
     */
    public String toDotString(){
        String res = "digraph {\n\trankdir=LR\n";
        for(Node n : adjEdList.keySet()){
            if(adjEdList.get(n).isEmpty() && !usesNode(n)){
                res += "\t"+n.getId()+"\n";
            }
            for(Edge e : adjEdList.get(n)){
                res += "\t"+n.getId()+" -> "+e.to().getId();
                if(e.isWeighted()){
                    res += " [label="+e.getWeight()+", len="+e.getWeight()+"]";
                }
                res += "\n";
            }
        }
        return res+"}\n";
    }

    /**
     * Returns the number of nodes in the graph
     * @return the number of nodes
     */
    public int nbNodes(){
        return adjEdList.size();
    }

    /**
     * Checks if the graph uses the specified node
     * @param n the node to check
     * @return true if the graph uses the node, false otherwise
     */
    public boolean usesNode(Node n){
        if(!adjEdList.containsKey(n)){
            return false;
        }
        for(Node node : adjEdList.keySet()){
            for(Edge e : adjEdList.get(node)){
                if(e.to().equals(n) || e.from().equals(n)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the graph uses the node with the specified id
     * @param id the ID of the node to check
     * @return true if the graph uses the node, false otherwise
     */
    public boolean usesNode(int id){
        return usesNode(new Node(id, this));
    }

    /**
     * Checks if the graph holds the specified node
     * @param n the node to check
     * @return true if the graph holds the node, false otherwise
     */
    public boolean holdsNode(Node n){
        return adjEdList.containsKey(n);
    }

    /**
     * Checks if the graph holds the node with the specified id
     * @param id the ID of the node to check
     * @return true if the graph holds the node, false otherwise
     */
    public boolean holdsNode(int id){
        return holdsNode(new Node(id, this));
    }

    /**
     * Returns the node with the specified ID
     * @param id the ID of the node to return
     * @return the node with the specified ID, or null if not found
     */
    public Node getNode(int id){
        return adjEdList.keySet().stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }

    /**
     * Adds the specified node to the graph
     * @param n the node to add
     * @return true if the node was added, false otherwise
     */
    public boolean addNode(Node n){
        if(adjEdList.containsKey(n)){
            return false;
        }
        adjEdList.put(n, new ArrayList<>());
        return true;
    }

    /**
     * Adds a node with the specified ID to the graph
     * @param id the ID of the node to add
     * @return true if the node was added, false otherwise
     */
    public boolean addNode(int id){
        return addNode(new Node(id, this));
    }

    /**
     * Removes the specified node from the graph
     * @param n the node to remove
     * @return true if the node was removed, false otherwise
     */
    public boolean removeNode(Node n){
        if(!holdsNode(n)){
            return false;
        }
        adjEdList.remove(n);
        for(Node node : adjEdList.keySet()){
            adjEdList.get(node).removeIf(e -> e.to().equals(n));
            adjEdList.get(node).removeIf(e -> e.from().equals(n));
        }
        return true;
    }

    /**
     * Removes the node with the specified ID from the graph
     * @param n the ID of the node to remove
     * @return true if the node was removed, false otherwise
     */
    public boolean removeNode(int n){
        return removeNode(new Node(n, this));
    }

    /**
     * Returns a list of all nodes in the graph
     * @return a list of all nodes
     */
    public List<Node> getAllNodes(){
        return new ArrayList<>(adjEdList.keySet());
    }

    /**
     * Returns the largest node ID in the graph
     * @return the largest node ID or 0 if the graph is empty
     */
    public int largestNodeId(){
        return adjEdList.keySet().stream().mapToInt(Node::getId).max().orElse(0);
    }

    /**
     * Returns the smallest node ID in the graph
     * @return the smallest node ID or 0 if the graph is empty
     */
    public int smallestNodeId(){
        return adjEdList.keySet().stream().mapToInt(Node::getId).min().orElse(0);
    }

    /**
     * Returns a list of successors of the specified node
     * @param n the node whose successors are to be returned
     * @return a list of successors
     */
    public List<Node> getSuccessors(Node n){
        List<Node> successors = new ArrayList<>();
        if(!holdsNode(n)) {
            return successors;
        }
        List<Edge> edges = adjEdList.get(n);
        for(Edge e : edges){
            if(!successors.contains(e.to())) {
                successors.add(e.to());
            }
        }
        return successors;
    }

    /**
     * Returns a list of successors of the node with the specified ID
     * @param id the ID of the node whose successors are to be returned
     * @return a list of successors
     */
    public List<Node> getSuccessors(int id){
        return getSuccessors(new Node(id, this));
    }

    /**
     * Returns a list of successors of the specified node, including multiple edges
     * @param n the node whose successors are to be returned
     * @return a list of successors
     */
    public List<Node> getSuccessorsMulti(Node n){
        List<Node> successors = new ArrayList<>();
        if(!holdsNode(n)){
            return successors;
        }
        adjEdList.get(n).forEach(e -> successors.add(e.to()));
        return successors;
    }

    /**
     * Returns a list of successors of the node with the specified ID, including multiple edges
     * @param id the ID of the node whose successors are to be returned
     * @return a list of successors
     */
    public List<Node> getSuccessorsMulti(int id){
        return getSuccessorsMulti(new Node(id, this));
    }

    /**
     * Checks if the specified nodes are adjacent
     * @param u the first node
     * @param v the second node
     * @return true if the nodes are adjacent, false otherwise
     */
    public boolean adjacent(Node u, Node v){
        if(!holdsNode(u) || !holdsNode(v)){
            return false;
        }
        return adjEdList.get(u).stream().anyMatch(e -> e.to().equals(v)) || adjEdList.get(v).stream().anyMatch(e -> e.to().equals(u));
    }

    /**
     * Checks if the nodes with the specified IDs are adjacent
     * @param u the ID of the first node
     * @param v the ID of the second node
     * @return true if the nodes are adjacent, false otherwise
     */
    public boolean adjacent(int u, int v){
        return adjacent(new Node(u, this), new Node(v, this));
    }

    /**
     * Returns the in-degree of the specified node
     * @param n the node whose in-degree is to be returned
     * @return the in-degree of the node
     */
    public int inDegree(Node n){
        int inDegree = 0;
        if(!holdsNode(n)){
            return inDegree;
        }
        for(Node node : adjEdList.keySet()){
            for(Edge e : adjEdList.get(node)){
                if(e.to().equals(n)){
                    inDegree++;
                }
            }
        }
        return inDegree;
    }

    /**
     * Returns the in-degree of the node with the specified ID
     * @param id the ID of the node whose in-degree is to be returned
     * @return the in-degree of the node
     */
    public int inDegree(int id){
        return inDegree(new Node(id, this));
    }

    /**
     * Returns the out-degree of the specified node
     * @param n the node whose out-degree is to be returned
     * @return the out-degree of the node
     */
    public int outDegree(Node n){
        if(!holdsNode(n)){
            return 0;
        }
        return adjEdList.get(n).size();
    }

    /**
     * Returns the out-degree of the node with the specified ID
     * @param id the ID of the node whose out-degree is to be returned
     * @return the out-degree of the node
     */
    public int outDegree(int id){
        return outDegree(new Node(id, this));
    }

    /**
     * Returns the degree of the specified node
     * @param n the node whose degree is to be returned
     * @return the degree of the node
     */
    public int degree(Node n){
        return inDegree(n) + outDegree(n);
    }

    /**
     * Returns the degree of the node with the specified ID
     * @param id the ID of the node whose degree is to be returned
     * @return the degree of the node
     */
    public int degree(int id){
        return degree(new Node(id, this));
    }

    /**
     * Returns the number of edges in the graph
     * @return the number of edges
     */
    public int nbEdges(){
        return adjEdList.values().stream().mapToInt(List::size).sum();
    }

    /**
     * Checks if an edge exists between the specified nodes
     * @param u the first node
     * @param v the second node
     * @return true if an edge exists between the nodes, false otherwise
     */
    public boolean existsEdge(Node u, Node v){
        return existsEdge(new Edge(u, v, this, null));
    }

    /**
     * Checks if an edge exists between the nodes with the specified IDs
     * @param u the ID of the first node
     * @param v the ID of the second node
     * @return true if an edge exists between the nodes, false otherwise
     */
    public boolean existsEdge(int u, int v){
        return existsEdge(new Node(u, this), new Node(v, this));
    }

    /**
     * Checks if the specified edge exists in the graph
     * @param e the edge to check
     * @return true if the edge exists, false otherwise
     */
    public boolean existsEdge(Edge e){
        if(!holdsNode(e.to()) || !holdsNode(e.from())){
            return false;
        }
        for(Edge ed : adjEdList.get(e.from())){
            if(e.equals(ed)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an edge between the specified nodes
     * @param from the starting node
     * @param to the ending node
     */
    public void addEdge(Node from, Node to){
        addEdge(from, to, null);
    }

    /**
     * Adds an edge between the specified nodes with the specified weight
     * @param from the starting node
     * @param to the ending node
     * @param weight the weight of the edge
     */
    public void addEdge(Node from, Node to, Integer weight){
        if(!holdsNode(from)){
            addNode(from);
        }
        if(!holdsNode(to)){
            addNode(to);
        }
        adjEdList.get(from).add(new Edge(from, to, this, weight));
        Collections.sort(adjEdList.get(from));
    }

    /**
     * Adds an edge between the nodes with the specified IDs
     * @param from the ID of the starting node
     * @param to the ID of the ending node
     */
    public void addEdge(int from, int to){
        addEdge(new Node(from, this), new Node(to, this), null);
    }

    /**
     * Adds an edge between the nodes with the specified IDs and the specified weight
     * @param from the ID of the starting node
     * @param to the ID of the ending node
     * @param weight the weight of the edge
     */
    public void addEdge(int from, int to, int weight){
        addEdge(new Node(from, this), new Node(to, this), weight);
    }

    /**
     * Adds the specified edge to the graph
     * @param e the edge to add
     */
    public void addEdge(Edge e){
        addEdge(e.from(), e.to(), e.getWeight());
    }

    /**
     * Removes the edge between the specified nodes
     * @param from the starting node
     * @param to the ending node
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(Node from, Node to){
        if(!holdsNode(from) || !holdsNode(to)){
            return false;
        }
        return adjEdList.get(from).removeIf(e -> e.to().equals(to));
    }

    /**
     * Removes the edge between the nodes with the specified IDs
     * @param from the ID of the starting node
     * @param to the ID of the ending node
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(int from, int to){
        return removeEdge(new Node(from, this), new Node(to, this));
    }

    /**
     * Removes the specified edge from the graph
     * @param e the edge to remove
     * @return true if the edge was removed, false otherwise
     */
    public boolean removeEdge(Edge e){
        return removeEdge(e.from(), e.to());
    }

    /**
     * Returns a list of outgoing edges from the specified node
     * @param n the node whose outgoing edges are to be returned
     * @return a list of outgoing edges
     */
    public List<Edge> getOutEdges(Node n){
        if(!holdsNode(n)){
            return new ArrayList<>();
        }
        return adjEdList.get(n);
    }

    /**
     * Returns a list of outgoing edges from the node with the specified ID
     * @param id the ID of the node whose outgoing edges are to be returned
     * @return a list of outgoing edges
     */
    public List<Edge> getOutEdges(int id){
        return getOutEdges(new Node(id, this));
    }

    /**
     * Returns a list of incoming edges to the specified node
     * @param n the node whose incoming edges are to be returned
     * @return a list of incoming edges
     */
    public List<Edge> getInEdges(Node n){
        List<Edge> inEdges = new ArrayList<>();
        if(!holdsNode(n)){
            return inEdges;
        }
        for(Node node : adjEdList.keySet()){
            for(Edge e : adjEdList.get(node)){
                if(e.to().equals(n)){
                    inEdges.add(e);
                }
            }
        }
        return inEdges;
    }

    /**
     * Returns a list of incoming edges to the node with the specified ID
     * @param id the ID of the node whose incoming edges are to be returned
     * @return a list of incoming edges
     */
    public List<Edge> getInEdges(int id){
        return getInEdges(new Node(id, this));
    }

    /**
     * Returns a list of incident edges for the specified node
     * @param n the node whose incident edges are to be returned
     * @return a list of incident edges
     */
    public List<Edge> getIncidentEdges(Node n){
        if(!holdsNode(n)){
            return new ArrayList<>();
        }
        List<Edge> edges = new ArrayList<>();
        edges.addAll(getInEdges(n));
        edges.addAll(getOutEdges(n));
        return edges;
    }

    /**
     * Returns a list of incident edges for the node with the specified ID
     * @param id the ID of the node whose incident edges are to be returned
     * @return a list of incident edges
     */
    public List<Edge> getIncidentEdges(int id){
        return getIncidentEdges(new Node(id, this));
    }

    /**
     * Returns a list of edges between the specified nodes
     * @param u the first node
     * @param v the second node
     * @return a list of edges between the nodes
     */
    public List<Edge> getEdges(Node u, Node v){
        if(!holdsNode(u) || !holdsNode(v)){
            return new ArrayList<>();
        }
        List<Edge> edges = new ArrayList<>();
        for(Edge e : adjEdList.get(u)){
            if(e.to().equals(v)){
                edges.add(e);
            }
        }
        for(Edge e : adjEdList.get(v)){
            if(e.to().equals(u)){
                edges.add(e);
            }
        }
        return edges;
    }

    /**
     * Returns a list of edges between the nodes with the specified IDs
     * @param u the ID of the first node
     * @param v the ID of the second node
     * @return a list of edges between the nodes
     */
    public List<Edge> getEdges(int u, int v){
        return getEdges(new Node(u, this), new Node(v, this));
    }

    /**
     * Returns a list of all edges in the graph
     * @return a list of all edges
     */
    public List<Edge> getAllEdges(){
        List<Edge> edges = new ArrayList<>();
        adjEdList.values().forEach(edges::addAll);
        return edges;
    }

    /**
     * Returns the successor array representation of the graph
     * @return the successor array representation
     */
    public int[] toSuccessorArray(){
        int[] succArray = new int[nbNodes()+nbEdges()];
        int i = 0;
        for(Node n : adjEdList.keySet()){
            for(Edge e : adjEdList.get(n)){
                succArray[i] = e.to().getId();
                i++;
            }
            succArray[i] = 0;
            i++;
        }
        return succArray;
    }

    /**
     * Returns the adjacency matrix representation of the graph
     * @return the adjacency matrix representation
     */
    public int[][] toAdjMatrix(){
        int[][] adjMatrix = new int[nbNodes()][nbNodes()];
        for(Node n : adjEdList.keySet()){
            for(Edge e : adjEdList.get(n)){
                adjMatrix[n.getId()-1][e.to().getId()-1] += 1;
            }
        }
        return adjMatrix;
    }

    /**
     * Returns the reverse of the graph
     * @return the reverse of the graph
     */
    public Graph getReverse(){
        Graph reverse = new Graph();
        for(Node n : adjEdList.keySet()){
            reverse.addNode(n);
            for(Edge e : adjEdList.get(n)){
                reverse.addEdge(e.getSymmetric());
            }
        }
        return reverse;
    }

    /**
     * Returns the transitive closure of the graph
     * @return the transitive closure of the graph
     */
    public Graph getTransitiveClosure(){
        Graph transitiveClosure = new Graph();
        for(Node n : adjEdList.keySet()){
            transitiveClosure.addNode(n);
            List<Node> reachable = getReachable(n, new ArrayList<>());
            reachable.remove(0);
            for(Node r : reachable){
                transitiveClosure.addEdge(n, r);
            }
        }
        return transitiveClosure;
    }

    /**
     * Returns a list of nodes reachable from the specified node
     * @param n the starting node
     * @param reachable the list of reachable nodes
     * @return a list of reachable nodes
     */
    private List<Node> getReachable(Node n, List<Node> reachable){
        if(!reachable.contains(n)){
            reachable.add(n);
            for(Node s : getSuccessors(n)){
                getReachable(s, reachable);
            }
        }
        return reachable;

    }

    /**
     * Checks if the graph is a multigraph
     * @return true if the graph is a multigraph, false otherwise
     */
    public boolean isMultiGraph(){
        for(Node n : adjEdList.keySet()){
            List<Edge> edges = adjEdList.get(n);
            for(int i = 0; i < edges.size(); i++){
                for(int j = i + 1; j < edges.size(); j++){
                    if(edges.get(i).to().equals(edges.get(j).to())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the graph has self-loops
     * @return true if the graph has self-loops, false otherwise
     */
    public boolean hasSelfLoops(){
        for(Node n : adjEdList.keySet()){
            for(Edge e : adjEdList.get(n)){
                if(e.isSelfLoop()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a simple graph (no multiedges or self-loops) equivalent to this graph.
     *
     * @return the simple graph
     */
    public Graph toSimpleGraph(){
        if(!isMultiGraph() && !hasSelfLoops()){
            return this;
        }
        Graph simpleGraph = new Graph();
        for(Node n : adjEdList.keySet()){
            simpleGraph.addNode(n);
            for(Edge e : adjEdList.get(n)){
                if(!simpleGraph.existsEdge(e.from(), e.to())){
                    simpleGraph.addEdge(e);
                }
            }
        }

        return simpleGraph;
    }

    /**
     * Returns a copy of the graph
     * @return a copy of the graph
     */
    public Graph copy(){
        Graph copy = new Graph();
        for(Node n : adjEdList.keySet()){
            copy.addNode(n);
            for(Edge e : adjEdList.get(n)){
                copy.addEdge(e);
            }
        }
        return copy;
    }

    /**
     * Returns the depth-first search (DFS) traversal of the graph from the node with the smallest ID
     * @return the DFS traversal list
     */
    public List<Node> getDFS() {
        Node start = new Node(smallestNodeId(), this);
        return getDFS(start);
    }

    /**
     * Returns the depth-first search (DFS) traversal of the graph starting from the specified node
     * @param n the starting node
     * @return the DFS traversal list
     */
    public List<Node> getDFS(Node n) {
        List<Node> dfs = new ArrayList<>();
        getDFS(n, dfs);
        if(dfs.size() < nbNodes()){
            for(Node node : adjEdList.keySet()){
                if(!dfs.contains(node) && usesNode(node)){
                    getDFS(node, dfs);
                }
            }
        }
        return dfs;
    }

    /**
     * Returns the depth-first search (DFS) traversal of the graph starting from the node with the specified ID
     * @param id the ID of the starting node
     * @return the DFS traversal list
     */
    public List<Node> getDFS(int id){
        return getDFS(new Node(id, this));
    }

    /**
     * Performs the depth-first search (DFS) traversal of the graph starting from the specified node and updates the DFS list
     * @param n the starting node
     * @param dfs the DFS list
     * @return the updated DFS list
     */
    private List<Node> getDFS(Node n, List<Node> dfs){
        if(!dfs.contains(n)){
            dfs.add(n);
            for(Node s : getSuccessors(n)){
                getDFS(s, dfs);
            }
        }

        return dfs;
    }

    /**
     * Returns the breadth-first search (BFS) traversal of the graph starting from the node with the smallest ID
     * @return the BFS traversal list
     */
    public List<Node> getBFS() {
        Node start = new Node(smallestNodeId(), this);
        return getBFS(start);
    }

    /**
     * Returns the breadth-first search (BFS) traversal of the graph starting from the specified node
     * @param n the starting node
     * @return the BFS traversal list
     */
    public List<Node> getBFS(Node n) {
        List<Node> bfs = new ArrayList<>();
        return getBFS(n, bfs);
    }

    /**
     * Returns the breadth-first search (BFS) traversal of the graph starting from the node with the specified ID
     * @param id the ID of the starting node
     * @return the BFS traversal list
     */
    public List<Node> getBFS(int id){
        return getBFS(new Node(id, this));
    }

    /**
     * Performs the breadth-first search (BFS) traversal of the graph starting from the specified node and updates the BFS list
     * @param n the starting node
     * @param bfs the BFS list
     * @return the updated BFS list
     */
    private List<Node> getBFS(Node n, List<Node> bfs){
        Queue<Node> queue = new LinkedList<>();
        queue.add(n);
        while(!queue.isEmpty()){
            Node node = queue.poll();
            if(!bfs.contains(node)){
                bfs.add(node);
                for(Node s : getSuccessors(node)){
                    if(!bfs.contains(s)){
                        queue.add(s);
                    }
                }
            }
        }
        if(bfs.size() < nbNodes()){
            for(Node node : adjEdList.keySet()){
                if(!bfs.contains(node) && usesNode(node)){
                    getBFS(node, bfs);
                }
            }
        }
        return bfs;
    }

    /**
     * Returns the depth-first search (DFS) traversal of the graph with information starting from the node with the smallest ID
     * @param nodeVisit the map to store node visit information
     * @param edgeVisit the map to store edge visit information
     * @return the DFS traversal list
     */
    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
        return getDFSWithVisitInfo(new Node(smallestNodeId(), this), nodeVisit, edgeVisit);
    }

    /**
     * Returns the depth-first search (DFS) traversal of the graph with information starting from the specified node
     * @param n the starting node
     * @param nodeVisit the map to store node visit information
     * @param edgeVisit the map to store edge visit information
     * @return the DFS traversal list
     */
    public List<Node> getDFSWithVisitInfo(Node n, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
        List<Node> dfs = new ArrayList<>();
        for(Node node : adjEdList.keySet()){
            nodeVisit.put(node, new NodeVisitInfo());
        }
        MutableInteger time = new MutableInteger(0);
        getDFSWithVisitInfoRec(n, nodeVisit, edgeVisit, dfs, time);
        if(dfs.size() < nbNodes()){
            for(Node node : adjEdList.keySet()){
                if(nodeVisit.get(node).getColour() == NodeColour.WHITE && usesNode(node)){
                    getDFSWithVisitInfoRec(node, nodeVisit, edgeVisit, dfs, time);
                }
            }
        }

        return dfs;
    }

    /**
     * Returns the depth-first search (DFS) traversal of the graph with information starting from the node with the specified ID
     * @param id the ID of the starting node
     * @param nodeVisit the map to store node visit information
     * @param edgeVisit the map to store edge visit information
     * @return the DFS traversal list
     */
    public List<Node> getDFSWithVisitInfo(int id, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
        return getDFSWithVisitInfo(new Node(id, this), nodeVisit, edgeVisit);
    }

    /**
     * Performs the depth-first search (DFS) traversal of the graph with visit information starting from the specified node and updates the DFS list
     * @param n the starting node
     * @param nodeVisit the map to store node visit information
     * @param edgeVisit the map to store edge visit information
     * @param dfs the DFS list
     * @param time the mutable integer to track the discovery and finish times
     */
    private void getDFSWithVisitInfoRec(Node n, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit, List<Node> dfs, MutableInteger time){
        time.increment();
        nodeVisit.get(n).setDiscovery(time.getValue());
        dfs.add(n);
        nodeVisit.get(n).setColour(NodeColour.GRAY);

        for(Node s : getSuccessors(n)){
            if(nodeVisit.get(s).getColour() == NodeColour.WHITE){
                nodeVisit.get(s).setPredeccessor(n);
                edgeVisit.put(new Edge(n, s, this), EdgeVisitType.TREE);
                getDFSWithVisitInfoRec(s, nodeVisit, edgeVisit, dfs, time);
            }
        }
        for(Node s : getSuccessors(n)){
            if(nodeVisit.get(s).getPredeccessor() != n){
                if(nodeVisit.get(s).getColour() == NodeColour.BLACK){
                    Set<Node> pr1 = new HashSet<>();
                    Set<Node> pr2 = new HashSet<>();
                    getAllPredecessor(pr1, nodeVisit, n);
                    getAllPredecessor(pr2, nodeVisit, s);
                    pr1.retainAll(pr2);
                    if(pr1.isEmpty()) {
                        edgeVisit.put(new Edge(n, s, this), EdgeVisitType.FORWARD);
                    }else{
                        edgeVisit.put(new Edge(n, s, this), EdgeVisitType.CROSS);
                    }
                }else if(nodeVisit.get(s).getColour() == NodeColour.GRAY){
                    edgeVisit.put(new Edge(n, s, this), EdgeVisitType.BACKWARD);
                }
            }
        }
        nodeVisit.get(n).setColour(NodeColour.BLACK);
        time.increment();
        nodeVisit.get(n).setFinished(time.getValue());
    }

    /**
     * Recursively collects all predecessors of the specified node
     * @param predecessor the set to store the predecessors
     * @param nodeVisit the map to store node visit information
     * @param n the node whose predecessors are to be collected
     */
    private void getAllPredecessor(Set<Node> predecessor, Map<Node, NodeVisitInfo> nodeVisit, Node n){
        if(nodeVisit.get(n).getPredeccessor() != null){
            predecessor.add(nodeVisit.get(n).getPredeccessor());
            getAllPredecessor(predecessor, nodeVisit, nodeVisit.get(n).getPredeccessor());
        }
    }
}
