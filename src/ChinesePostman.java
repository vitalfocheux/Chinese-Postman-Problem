import m1graphs2024.Edge;
import m1graphs2024.UndirectedGraph;
import m1graphs2024.Node;

import java.util.*;

public class ChinesePostman {

    private final Integer INFINITY = Integer.MAX_VALUE;
    private String type = "";
    private int extraCost = 0;

    UndirectedGraph graph;

    public ChinesePostman(UndirectedGraph graph){
        this.graph = graph;
    }

    /**
     * The function `findEulerianWay` checks if the graph is a disconnected graph, and then 
     * determines if it is Eulerian, Semi-Eulerian, or Non-Eulerian. 
     * Based on the type of graph, it returns either an Eulerian trail, a Chinese circuit,
     * or an empty list otherwise.
     * 
     * @param random boolean
     * @return A List of Node objects is being returned.
     */
    public List<Node> findEulerianWay(boolean random){
        if(!graph.isDisconnectedGraph()){
            if(isEulerian()){
                type ="Eulerian";
                return eulerianTrail(new Node(graph, graph.smallestNodeId()));
            }
            if(isSemiEulerian()) {
                type = "Semi-Eulerian";
                Node start = graph.getAllNodes().stream().filter(node -> graph.degree(node) % 2 != 0).findFirst().get();
                return eulerianTrail(new Node(graph, start.getId()));
            }
            type = "Non Eulerian";
            return chineseCircuit(new Node(graph, graph.smallestNodeId()), random);
        }
        type = "Non connect graph";
        return new ArrayList<>();
    }

    /**
     * The function `findEulerianWay` returns a list of nodes representing an Eulerian way in a graph.
     * 
     * @return List<Node>
     */
    public List<Node> findEulerianWay(){
        return findEulerianWay(false);
    }

    /**
     * The function checks if all nodes in a graph have even degrees, indicating that the graph is
     * Eulerian.
     * 
     * @return boolean
     */
    public boolean isEulerian(){
        return graph.getAllNodes().stream().allMatch(node -> graph.degree(node) % 2 == 0);
    }

    /**
     * The function checks if a graph is semi-Eulerian by verifying if there are exactly two nodes with
     * odd degrees.
     * 
     * @return boolean
     */
    public boolean isSemiEulerian(){
        return graph.getAllNodes().stream().filter(node -> graph.degree(node) % 2 != 0).count() == 2;
    }

    /**
     * The function `createLabel` generates a label for a circuit based on the Eulerian circuit or trail given.
     * 
     * @param circuit List<Node>
     * @return String
     */
    public String createLabel(List<Node> circuit){
        String label = "Type: "+type+"\n\t";
        int totalCost = 0;
        switch (type){
            case "Eulerian":
                label += "Eulerian Circuit: ";
                break;
            case "Semi-Eulerian":
                label += "Semi-Eulerian Trail: ";
                break;
            case "Non Eulerian":
                label += "Chinese Circuit: ";
                break;
        }
        label += "[";
        List<Edge> visited = new ArrayList<>();
        for(int i = 0; i < circuit.size()-1; ++i){
            boolean reversed = false;
            List<Edge> edges = graph.getEdges(circuit.get(i).getId(), circuit.get(i+1).getId());
            if(edges.isEmpty()){
                reversed = true;
                edges = graph.getEdges(circuit.get(i+1).getId(), circuit.get(i).getId());
            }
            Edge edge = edges.get(0);
            if(reversed){
                edge = edge.getSymmetric();
            }
            if(!visited.contains(edge)){
                label += edge.toCircuitString()+", ";
                totalCost += edge.getWeight();
                visited.add(edge);
            }
        }
        label = label.substring(0, label.length()-2)+"]\n\t";
        label += "Total Cost: "+totalCost;
        label += "\n\tExtra Cost: "+extraCost;
        return label;
    }

    /**
     * The function createLabel() returns a label based on a Eulerian way.
     * 
     * @return String
     */
    public String createLabel(){
        return createLabel(findEulerianWay());
    }

    /**
     * The function `eulerianTrail` finds an Eulerian trail in an undirected graph starting from a
     * given node.
     * 
     * @param g UndirectedGraph
     * @param x Node
     * @return List<Node>
     */
    public List<Node> eulerianTrail(UndirectedGraph g, Node x){
        List<Node> trail = new ArrayList<>();
        trail.add(x);
        List<Edge> edges = g.getOutEdges(x);
        if(edges.isEmpty()){
            return trail;
        }
        while(!edges.isEmpty()){
            Edge e = edges.get(0);
            if(trail.get(trail.size()-1).equals(e.to())){
                x = e.from();
            }else{
                x = e.to();
            }
            g.removeEdge(e.from(),e.to());
            trail.add(x);
            edges = g.getOutEdges(x);
        }
        List<Node> trail_prime = new ArrayList<>();
        for(int i = 0; i < trail.size(); ++i){
            trail_prime.addAll(eulerianTrail(g, trail.get(i)));
        }
        return trail_prime;
    }

    /**
     * The function `eulerianTrail` returns an Eulerian trail starting from a specified node in an undirected graph.
     * 
     * @param start Node
     * @return List<Node>
     */
    public List<Node> eulerianTrail(Node start){
        UndirectedGraph g = graph.copy();
        return eulerianTrail(g, g.getNode(start.getId()));
    }

    /**
     * The function `chineseCircuit` finds a Chinese postman circuit in a graph starting from a given
     * node, using the Floyd-Warshall algorithm and pairwise matching of odd-degree nodes.
     * 
     * @param start Node
     * @param random boolean
     * @return List<Node>
     */
    public List<Node> chineseCircuit(Node start, boolean random){
        Map<Pair<Node, Node>, Pair<Integer, Node>> floyd_warshall = floydWarshall();
        List<Node> oddNodes = new ArrayList<>();
        graph.getAllNodes().stream().filter(node -> graph.degree(node) % 2 != 0).forEach(oddNodes::add);

        Pair<List<Pair<Node, Node>>, Integer> lengthPairwiseMatching = null;
        if(random){
            lengthPairwiseMatching = lengthPairwiseMatchingRandom(oddNodes);
        }else{
            lengthPairwiseMatching = lengthPairwiseMatching(oddNodes);
        }
        List<Pair<Node, Node>> bestMatching = lengthPairwiseMatching.getFirst();
        int extraCost = lengthPairwiseMatching.getSecond();
        for(Pair<Node, Node> pair : bestMatching){
            Node from = pair.getFirst();
            Node curr = from;
            while(!curr.equals(pair.getSecond())){
                Node next = floyd_warshall.get(new Pair<>(curr, pair.getSecond())).getSecond();
                Node futur_curr = floyd_warshall.get(new Pair<>(curr, next)).getSecond();
                while(!futur_curr.equals(floyd_warshall.get(new Pair<>(curr, futur_curr)).getSecond())){
                    futur_curr = floyd_warshall.get(new Pair<>(curr, futur_curr)).getSecond();
                }
                Integer weight = floyd_warshall.get(new Pair<>(curr, futur_curr)).getFirst();
                graph.addEdge(curr.getId(), futur_curr.getId(), weight, "red");
                curr = futur_curr;
            }
        }
        this.extraCost = extraCost;
        return eulerianTrail(start);
    }

    /**
     * The function implements the Floyd-Warshall algorithm to find the shortest paths between all
     * pairs of nodes in a graph.
     * 
     * @return a `Map` where the key is a `Pair` of Nodes representing the source and destination nodes, 
     * and the value is a `Pair` containing an integer representing the shortest distance between the nodes 
     * and a Node representing the intermediate node on the shortest path.
     */
    public Map<Pair<Node, Node>, Pair<Integer, Node>> floydWarshall(){
        if(graph.isDisconnectedGraph()){
            return null;
        }
        List<Node> nodes = graph.getAllNodes();
        Map<Pair<Node, Node>, Pair<Integer, Node>> res = new HashMap<>();
        for(Node x : nodes){
            for(Node y : nodes){
                Pair<Node, Node> pair = new Pair<>(x, y);
                List<Edge> edges = graph.getEdges(x, y);
                edges.addAll(graph.getEdges(y, x));
                if(x.equals(y)){
                    res.put(pair, new Pair<>(0, x));
                }else if(!edges.isEmpty()){
                    int weight = INFINITY;
                    for(Edge edge : edges){
                        if(edge.getWeight() < weight){
                            weight = edge.getWeight();
                        }
                    }
                    res.put(pair, new Pair<>(weight, y));
                }else{
                    res.put(pair, new Pair<>(INFINITY, null));
                }
            }
        }
        for(Node x : nodes){
            for(Node y : nodes){
                for(Node z : nodes){
                    Pair<Node, Node> xz = new Pair<>(x, z);
                    Pair<Node, Node> zy = new Pair<>(z, y);
                    Pair<Node, Node> xy = new Pair<>(x, y);

                    Integer Mxz = res.get(xz).getFirst();
                    Integer Mzy = res.get(zy).getFirst();
                    Integer Mxy = res.get(xy).getFirst();

                    if(!Objects.equals(Mxz, INFINITY) && !Objects.equals(Mzy, INFINITY) && Mxz + Mzy < Mxy){
                        res.put(xy, new Pair<>(res.get(xz).getFirst() + res.get(zy).getFirst(), z));
                    }
                }
            }
        }
        return res;
    }

    /**
     * The function `lengthPairwiseMatching` finds the best pairwise matching of nodes in a list based
     * on a given weight function.
     * 
     * @param v List<Node>
     * @return a Pair containing a list of pairs of nodes representing the best match, 
     * and an integer representing the total weight of the best matching.
     */
    public Pair<List<Pair<Node, Node>>, Integer> lengthPairwiseMatching(List<Node> v){
        v.sort(Comparator.comparing(Node::getId));
        List<Pair<Node, Node>> bestMatching = new ArrayList<>();
        Integer bestMatchingWeight = Integer.MAX_VALUE;
        Map<Pair<Node, Node>, Pair<Integer, Node>> floyd_warshall = floydWarshall();
        List<List<Pair<Node, Node>>> listPairwiseMatching = listPairsOddNodes(new HashSet<>(v), new ArrayList<>(), new ArrayList<>());
        for(List<Pair<Node, Node>> pairs : listPairwiseMatching){
            Integer weight = 0;
            for(Pair<Node, Node> pair : pairs){
                weight += floyd_warshall.get(pair).getFirst();
            }
            if(weight < bestMatchingWeight){
                bestMatching = pairs;
                bestMatchingWeight = weight;
            }
        }
        return new Pair<>(bestMatching, bestMatchingWeight);
    }

    /**
     * The function `lengthPairwiseMatchingRandom` randomly selects pairs of nodes from a list,
     * calculates the weight using Floyd-Warshall algorithm, and returns the matching pairs along with
     * the total weight.
     * 
     * @param v List<Node>
     * @return A Pair containing a List of Pair objects representing pairwise matchings between
     * nodes, and an Integer representing the total weight of the pairwise matchings.
     */
    public Pair<List<Pair<Node, Node>>, Integer> lengthPairwiseMatchingRandom(List<Node> v){
        List<Pair<Node, Node>> matching = new ArrayList<>();
        Integer weight = 0;
        Map<Pair<Node, Node>, Pair<Integer, Node>> floyd_warshall = floydWarshall();
        Random random = new Random();
        while(!v.isEmpty()){
            Node x = v.get(random.nextInt(v.size()));
            v.remove(x);
            Node y = v.get(random.nextInt(v.size()));
            v.remove(y);
            Pair<Node, Node> pair = new Pair<>(x, y);
            matching.add(pair);
            Node curr = pair.getFirst();
            while(!curr.equals(pair.getSecond())){
                Node next = floyd_warshall.get(new Pair<>(curr, pair.getSecond())).getSecond();
                Node futur_curr = floyd_warshall.get(new Pair<>(curr, next)).getSecond();
                while(!futur_curr.equals(floyd_warshall.get(new Pair<>(curr, futur_curr)).getSecond())){
                    futur_curr = floyd_warshall.get(new Pair<>(curr, futur_curr)).getSecond();
                }
                weight += floyd_warshall.get(new Pair<>(curr, futur_curr)).getFirst();
                curr = futur_curr;
            }
        }
        return new Pair<>(matching, weight);
    }

    /**
     * The function recursively generates all possible pairs of odd nodes from a given set of nodes.
     * 
     * @param v Set<Node>
     * @param currentListOfPairs List<Pair<Node, Node>>
     * @param listsOfPairs List<List<Pair<Node, Node>>>
     * @return The method `listPairsOddNodes` returns a `List<List<Pair<Node, Node>>>`, which contains
     * a list of lists of pairs of nodes.
     */
    public List<List<Pair<Node, Node>>> listPairsOddNodes(Set<Node> v, List<Pair<Node, Node>> currentListOfPairs,
                                                  List<List<Pair<Node, Node>>> listsOfPairs) {
        if (v.isEmpty()) {
            listsOfPairs.add(new ArrayList<>(currentListOfPairs));
        } else {

            Node x = v.stream().min(Node::compareTo).get();
            v.remove(x);
            Set<Node> setNextNodes = new HashSet<>(v);
            for (Node y : setNextNodes) {
                if (x.getId() < y.getId()) {
                    v.remove(y);
                    currentListOfPairs.add(new Pair<>(x, y));

                    listPairsOddNodes(v, currentListOfPairs, listsOfPairs);
                    currentListOfPairs.remove(currentListOfPairs.size() - 1);
                    v.add(y);
                }
            }
            v.add(x);
        }
        return listsOfPairs;
    }

    /**
     * The function `computeEulerianCircuit` returns a list of edges representing an Eulerian circuit
     * starting from the node with the smallest ID in the graph.
     * 
     * @return List<Edge>
     */
    public List<Edge> computeEulerianCircuit(){
        return computeEulerianCircuit(graph.getNode(graph.smallestNodeId()));
    }

    /**
     * The function `computeEulerianCircuit` returns a list of edges representing an Eulerian circuit
     * starting from the node with the smallest ID in the graph.
     * 
     * @param start Node
     * @return List<Edge>
     */
    public List<Edge> computeEulerianCircuit(Node start){
        List<Edge> circuit = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        stack.push(start);
        while(!stack.isEmpty()){
            Node curr = stack.peek();
            List<Edge> edges = graph.getOutEdges(curr);
            if(!edges.isEmpty()){
                for(Edge edge : edges){
                    Node nextNode = edge.to();
                    stack.push(nextNode);
                    circuit.add(edge);
                }
            }else{
                stack.pop();
            }
        }
        return circuit;
    }

}
