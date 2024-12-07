import m1graphs2024.Edge;
import m1graphs2024.UndirectedGraph;
import m1graphs2024.Node;

import java.util.*;

public class ChinesePostman {

    private final Integer INFINITY = Integer.MAX_VALUE;

    UndirectedGraph graph;

    public ChinesePostman(UndirectedGraph graph){
        this.graph = graph;
    }

    private Integer weight(Node x, Node y){
        List<Edge> edges = graph.getEdges(x, y);
        if(edges.isEmpty()){
            return null;
        }
        return edges.get(0).getWeight();
    }

    public List<Node> findEulerianWay(){
        List<Node> nodes = graph.getAllNodes();
        if(graph.getDFS().size() == nodes.size()){
            if(isEulerian()){
                System.out.println("Eulerian");
                return eulerianTrail(new Node(graph, graph.smallestNodeId()));
            }else if(isSemiEulerian()) {
                System.out.println("Semi-Eulerian");
                Node start = graph.getAllNodes().stream().filter(node -> graph.degree(node) % 2 != 0).findFirst().get();
                return eulerianTrail(new Node(graph, start.getId()));
            }
        }
        return new ArrayList<>();
    }

    private boolean isConnected(){
        return graph.getDFS().size() == graph.getAllNodes().size();
    }

    public boolean isEulerian(){
        return graph.getAllNodes().stream().allMatch(node -> graph.degree(node) % 2 == 0);
    }

    public boolean isSemiEulerian(){
        if(!isConnected()){
            return false;
        }
        return graph.getAllNodes().stream().filter(node -> graph.degree(node) % 2 != 0).count() == 2;
    }

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

    public List<Node> eulerianTrail(Node start){
        UndirectedGraph g = graph.copy();
        return eulerianTrail(g, g.getNode(start.getId()));
    }

    /**
     * Floyd-Warshall algorithm
     * @return a map with the shortest path between each pair of nodes where the key is a pair of nodes and the value
     * is a pair with the length of the shortest path and the predecessor node
     */
    public Map<Pair<Node, Node>, Pair<Integer, Node>> floydWarshall(){
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
                    System.out.println("edges "+x+" "+y+": "+edges);
                    int weight = INFINITY;
                    for(Edge edge : edges){
                        if(edge.getWeight() < weight){
                            weight = edge.getWeight();
                        }
                    }
                    res.put(pair, new Pair<>(weight, y));
                }else{
                    System.out.println("edges "+x+" "+y+": "+edges);
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

    public Pair<List<Pair<Node, Node>>, Integer> lengthPairwiseMatching(List<Node> v){
        v.sort(Comparator.comparing(Node::getId));
        List<Pair<Node, Node>> bestMatching = new ArrayList<>();
        Integer bestMatchingWeight = Integer.MAX_VALUE;
        List<List<Pair<Node, Node>>> listPairwiseMatching = listPairsOddNodes(new HashSet<>(v), new ArrayList<>(), new ArrayList<>());
        for(List<Pair<Node, Node>> pairs : listPairwiseMatching){
            Integer weight = 0;
            for(Pair<Node, Node> pair : pairs){
                weight += floydWarshall().get(pair).getFirst();
            }
            if(weight < bestMatchingWeight){
                bestMatching = pairs;
                bestMatchingWeight = weight;
            }
        }
        return new Pair<>(bestMatching, bestMatchingWeight);
    }

    public List<List<Pair<Node, Node>>> listPairsOddNodes(Set<Node> v, List<Pair<Node, Node>> currentListOfPairs,
                                                  List<List<Pair<Node, Node>>> listsOfPairs) {
        if (v.isEmpty()) {
            listsOfPairs.add(new ArrayList<>(currentListOfPairs));
        } else {

            Node x = v.stream().min(Node::compareTo).get();
            System.out.println(x);
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

    public List<Edge> computeEulerianCircuit(){
        return computeEulerianCircuit(graph.getNode(graph.smallestNodeId()));
    }

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

    /*public static ChinesePostman fromDotFile(String filename){
        return fromDotFile(filename, "gv");
    }

    public static ChinesePostman fromDotFile(String filename, String extension){
        UndirectedGraph g = new UndirectedGraph();
        String path = filename + "." + extension;

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
        StringBuilder dot = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(src[0]))){

            String line;
            while((line = br.readLine()) != null){
                if(line.contains("--")){
                    //System.out.println(line);
                    List<String> split = List.of(line.split("\\["));
                    Pattern p = Pattern.compile("\\d+");
                    Matcher matcher = p.matcher(split.get(1));
                    int weight = 0;
                    if (matcher.find()) {
                        weight = Integer.parseInt(matcher.group());

                        dot.append(split.get(0)).append(" [len=").append(matcher.group()).append(", label=").append(matcher.group()).append("]\n");
                    } else {
                        System.out.println("No number found in the string.");
                    }

                    List<String> nodes = List.of(split.get(0).split("--"));
                    g.addEdge(Integer.parseInt(nodes.get(0).trim()), Integer.parseInt(nodes.get(1).trim()), weight);


                }else{
                    dot.append(line).append("\n");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ChinesePostman(g);
    }



    public String toDotString(){
        return null;
    }*/
}
