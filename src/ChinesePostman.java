import m1graphs2024.Edge;
import m1graphs2024.UndirectedGraph;
import m1graphs2024.Node;

import java.util.*;

public class ChinesePostman {

    private final Integer INFINITY = Integer.MIN_VALUE;

    UndirectedGraph graph;

    public ChinesePostman(UndirectedGraph graph){
        this.graph = graph;
    }

    private Integer weight(Node x, Node y){
        return graph.getEdges(x, y).get(0).getWeight();
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
                if(x.equals(y)){
                    res.put(new Pair<>(x, y), new Pair<>(0, x));
                }else{
                    if(graph.existsEdge(x, y)) {
                        res.put(new Pair<>(x, y), new Pair<>(weight(x, y), x));
                    }else{
                        res.put(new Pair<>(x, y), new Pair<>(INFINITY, null));
                    }
                }
            }
        }

        for(Node z : nodes){
            for(Node x : nodes){
                for(Node y : nodes){
                    //Pair<Node, Node> xz = new Pair<>(x, z);
                    Pair<Node, Node> zy = new Pair<>(z, y);
                    Pair<Node, Node> xy = new Pair<>(x, y);
                    Integer Mxz = res.get(new Pair<>(x, z)).getFirst();
                    Integer Mzy = res.get(zy).getFirst();
                    Integer Mxy = res.get(xy).getFirst();
                    if(!Objects.equals(Mxz, INFINITY) && !Objects.equals(Mzy, INFINITY) && Mxz + Mzy < Mxy){
                        res.put(xy, new Pair<>(Mxz + Mzy, res.get(zy).getSecond()));
                    }
                }
            }
        }
        return res;
    }

    public Pair<Integer, Integer> lengthPairwiseMatching(List<Node> v){
        return null;
    }

    public List<Pair<Node, Node>> listPairs(Set<Node> v, List<Pair<Node, Node>> currentListsOfPairs, List<Pair<Node, Node>> listsOfPairs){
        if(v.isEmpty()){
            listsOfPairs.addAll(currentListsOfPairs);
        }else{
            Node x = Collections.min(v);
            for(Node y : v){
                if(x.getId() < y.getId()){
                    v.remove(x);
                    v.remove(y);
                    currentListsOfPairs.add(new Pair<>(x, y));
                    listPairs(v, currentListsOfPairs, listsOfPairs);
                }
            }
        }
        return listsOfPairs;
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
