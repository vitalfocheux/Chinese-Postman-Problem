package m1graphs2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an undirected graph.
 */
public class UndirectedGraph extends Graph{

    /**
     * Constructs an empty undirected graph.
     */
    public UndirectedGraph(){
        super();
    }

    /**
     * Constructs an undirected graph with the specified nodes
     * @param nodes the nodes of the undirected graph
     */
    public UndirectedGraph(int... nodes){
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
                if(!adjEdList.containsKey(new Node(n, this))){
                    adjEdList.put(new Node(n, this), new ArrayList<>());
                }
                adjEdList.get(new Node(i, this)).add(new Edge(new Node(i, this), new Node(n, this), this, null));
            }
        }

    }

    /**
     * Returns all nodes in the graph.
     * @return a list of all nodes in the graph
     */
    public List<Node> getAllNodes(){
        List<Node> nodes = new ArrayList<>();
        adjEdList.keySet().forEach(node -> {
            if(usesNode(node)){
                nodes.add(node);
            }
        });
        return nodes;
    }

    /**
     * Returns the successors of the specified node
     * @param n the node whose successors are to be returned
     * @return the list of successors node
     */
    public List<Node> getSuccessors(Node n){
        if(!holdsNode(n)){
            return new ArrayList<>();
        }
        List<Node> successors = new ArrayList<>();
        List<Edge> edges = getAllEdges();
        for(Edge e : edges){
            if(e.from().equals(n) && !successors.contains(e.to())){
                successors.add(e.to());
            }else if(e.to().equals(n) && !successors.contains(e.from())){
                successors.add(e.from());
            }
        }
        return successors;
    }

    /**
     * Returns the successors of the node with the specified ID
     * @param id the ID of the node whose successors are to be returned
     * @return the list of successors node
     */
    public List<Node> getSuccessors(int id){
        return getSuccessors(new Node(id, this));
    }

    /**
     * Returns the successors of the specified node in a undirected multigraph
     * @param n the node whose successors are to be returned
     * @return the list of successors node in an undirected multigraph
     */
    public List<Node> getSuccessorsMulti(Node n){
        if(!holdsNode(n)){
            return new ArrayList<>();
        }
        List<Node> successors = new ArrayList<>();
        List<Edge> edges = getAllEdges();
        for(Edge e : edges){
            if(e.from().equals(n)){
                successors.add(e.to());
            }else if(e.to().equals(n)){
                successors.add(e.from());
            }
        }
        return successors;
    }

    /**
     * Returns the successors of the node with the specified ID in a undirected multigraph
     * @param id the ID of the node whose successors are to be returned
     * @return the list of successors node in an undirected multigraph
     */
    public List<Node> getSuccesorsMulti(int id){
        return getSuccessorsMulti(new Node(id, this));
    }

    /**
     * Returns the degree of the specified node
     * @param n the node whose degree is to be returned
     * @return the degree of the node
     */
    public int degree(Node n){
        return super.inDegree(n) + super.outDegree(n);
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
     * Returns the out-degree of the specified node
     * @param n the node whose out-degree is to be returned
     * @return the out-degree of the node
     */
    public int outDegree(Node n){
        return degree(n);
    }

    /**
     * Returns the out-degree of the node with the specified ID
     * @param id the ID of the node whose out-degree is to be returned
     * @return the out-degree of the node
     */
    public int outDegree(int id){
        return degree(new Node(id, this));
    }

    /**
     * Returns the in-degree of the specified node
     * @param n the node whose in-degree is to be returned
     * @return the in-degree of the node
     */
    public int inDegree(Node n){
        return degree(n);
    }

    /**
     * Returns the in-degree of the node with the specified ID
     * @param id the ID of the node whose in-degree is to be returned
     * @return the in-degree of the node
     */
    public int inDegree(int id){
        return degree(new Node(id, this));
    }

    /**
     * Returns the outgoing edges of the specified node
     * @param n the node whose outgoing edges are to be returned
     * @return the list of outgoing edges
     */
    public List<Edge> getOutEdges(Node n){
        List<Edge> outEdges = new ArrayList<>();
        List<Edge> edges = getAllEdges();
        for(Edge e : edges){
            if(e.from().equals(n)){
                outEdges.add(e);
            }else if(e.to().equals(n)){
                outEdges.add(new Edge(e.to(), e.from(), this, e.getWeight()));
            }
        }
        return outEdges;
    }

    /**
     * Returns the outgoing edges of the node with the specified ID
     * @param id the ID of the node whose outgoing edges are to be returned
     * @return the list of outgoing edges
     */
    public List<Edge> getOutEdges(int id){
        return getOutEdges(new Node(id, this));
    }

    /**
     * Returns the incoming edges of the specified node
     * @param n the node whose incoming edges are to be returned
     * @return the list of incoming edges
     */
    public List<Edge> getInEdges(Node n){
        return getOutEdges(n);
    }

    /**
     * Returns the incoming edges of the node with the specified ID
     * @param id the ID of the node whose incoming edges are to be returned
     * @return the list of incoming edges
     */
    public List<Edge> getInEdges(int id){
        return getOutEdges(new Node(id, this));
    }

    /**
     * Returns the incident edges of the specified node
     * @param n the node whose incident edges are to be returned
     * @return the list of incident edges
     */
    public List<Edge> getIncidentEdges(Node n){
        return getOutEdges(n);
    }

    /**
     * Returns the incident edges of the node with the specified ID
     * @param id the ID of the node whose incident edges are to be returned
     * @return the list of incident edges
     */
    public List<Edge> getIncidentEdges(int id){
        return getOutEdges(new Node(id, this));
    }

    /**
     * Returns the edge between the specified nodes
     * @param u the first node
     * @param v the second node
     * @return the list of edges between the two nodes
     */
    public List<Edge> getEdges(Node u, Node v){
        if(!holdsNode(u) || !holdsNode(v)){
            return new ArrayList<>();
        }
        List<Edge> edges = new ArrayList<>();
        adjEdList.get(u).forEach(e -> {
            if(e.to().equals(v)){
                edges.add(e);
            }
        });
        return edges;
    }

    /**
     * Returns the edge between the nodes with the specified IDs
     * @param u the ID of the first node
     * @param v the ID of the second node
     * @return the list of edges between the two nodes
     */
    public List<Edge> getEdges(int u, int v){
        return getEdges(new Node(u, this), new Node(v, this));
    }

    /**
     * Returns the adjacency matrix of the graph
     * @return the adjacency matrix of the graph
     */
    public int[][] toAdjMatrix(){
        int[][] adjMatrix = new int[nbNodes()][nbNodes()];
        for(Node n : adjEdList.keySet()){
            for(Edge e : adjEdList.get(n)){
                adjMatrix[e.from().getId() - 1][e.to().getId() - 1] += 1;
                Edge sym = e.getSymmetric();
                if(!existsEdge(sym)){
                    adjMatrix[sym.from().getId() - 1][sym.to().getId() - 1] += 1;
                }
            }
        }
        return adjMatrix;
    }

    /**
     * Returns the reverse of the graph
     * @return the reverse of the graph
     */
    public UndirectedGraph getReverse(){
        return copy();
    }

    /**
     * Returns the transitive closure of the graph
     * @return the transitive closure of the graph
     */
    public UndirectedGraph getTransitiveClosure(){
        UndirectedGraph transitiveClosure = new UndirectedGraph();
        for(Node n : adjEdList.keySet()){
            transitiveClosure.addNode(n);
            List<Node> reachable = getReachable(n, new ArrayList<>());
            reachable.remove(0);
            for(Node r : reachable){
                if(!transitiveClosure.existsEdge(n, r) && !transitiveClosure.existsEdge(r, n)){
                    transitiveClosure.addEdge(n, r);
                }
            }
        }
        return transitiveClosure;
    }

    /**
     * Returns the reachable nodes from the specified node
     * @param n the node from which the reachable nodes are to be returned
     * @param reachable the list of reachable nodes
     * @return the list of reachable nodes
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
     * Returns a simple graph without multi-edges and self-loops
     * @return a simple graph
     */
    public UndirectedGraph toSimpleGraph(){
        if(!isMultiGraph() && !hasSelfLoops()){
            return this;
        }
        UndirectedGraph simple = new UndirectedGraph();
        for(Node n : adjEdList.keySet()){
            simple.addNode(n);
            for(Edge e : adjEdList.get(n)){
                if(!simple.existsEdge(e.from(), e.to())){
                    simple.addEdge(e);
                }
            }
        }
        return simple;
    }

    /**
     * Returns a copy of the graph
     * @return a copy of the graph
     */
    public UndirectedGraph copy(){
        UndirectedGraph copy = new UndirectedGraph();
        for(Node n : adjEdList.keySet()){
            copy.addNode(n);
            for(Edge e : adjEdList.get(n)){
                copy.addEdge(e);
            }
        }
        return copy;
    }

    /**
     * Creates an undirected graph from a DOT file
     * @param filename the name of the DOT file
     * @return the undirected graph created from the DOT file
     */
    public static UndirectedGraph fromDotFile(String filename){
        return fromDotFile(filename, "gv");
    }

    /**
     * Creates an undirected graph from a DOT file with the specified extension
     * @param filename the name of the DOT file
     * @param extension the extension of the DOT file
     * @return the undirected graph created from the DOT file
     */
    public static UndirectedGraph fromDotFile(String filename, String extension){

        UndirectedGraph g = new UndirectedGraph();

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
                if(line.contains("--")){
                    Integer weight = null;
                    String[] nodes = line.split("--");
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
                }else if(!line.contains("graph") && !line.contains("rankdir") && !line.contains("}")){
                    g.adjEdList.put(new Node(Integer.parseInt(line.trim()), g), new ArrayList<>());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return g;
    }

    /**
     * Returns the DOT representation of the graph
     * @return the DOT representation of the graph
     */
    public String toDotString(){
        String res = "graph {\n\trankdir=LR\n";
        for(Node n : adjEdList.keySet()){
            if(adjEdList.get(n).isEmpty() && !holdsNode(n)){
                res += "\t"+n.getId()+"\n";
            }
            for(Edge e : adjEdList.get(n)){
                res += "\t"+n.getId()+" -- "+e.to().getId();
                if(e.isWeighted()){
                    res += " [label="+e.getWeight()+", len="+e.getWeight()+"]";
                }
                res += "\n";
            }
        }
        res += "}\n";
        return res;
    }
}
