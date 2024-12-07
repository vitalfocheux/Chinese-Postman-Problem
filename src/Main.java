import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) {

        UndirectedGraph g = UndirectedGraph.fromDotFile("tc1");
        ChinesePostman c = new ChinesePostman(g);
//        System.out.println("Eulerian Trail: " + c.findEulerianWay());
//        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());

        List<Node> v = new ArrayList<>();
        for(Node n : g.getAllNodes()){
            if(g.degree(n) % 2 != 0){
                v.add(n);
            }
        }
        Set<Node> nodes = new HashSet<>(v);
        System.out.println(c.listPairsOddNodes(nodes, new ArrayList<>(), new ArrayList<>()));
        System.out.println(c.lengthPairwiseMatching(v));
    }
}
