import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) {

        // Eulerian graph

        System.out.println("\nEulerian graph:\n");
        UndirectedGraph g = UndirectedGraph.fromDotFile("eulerian");
        ChinesePostman c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel()+"\n\n\n");


        System.out.println("\nEulerian multi graph:\n");
        g = UndirectedGraph.fromDotFile("eulerianMulti");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel()+"\n\n\n");


        System.out.println("Eulerian multi graph with self-loop:\n");
        g = UndirectedGraph.fromDotFile("eulerianMultiWithSelfLoop");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel()+"\n\n\n");



        // Semi-Eulerian graph

        System.out.println("Semi-Eulerian graph:\n");
        g = UndirectedGraph.fromDotFile("semiEulerian");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel()+"\n\n\n");


        System.out.println("Semi-Eulerian multi graph:\n");
        g = UndirectedGraph.fromDotFile("semiEulerianMulti");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel()+"\n\n\n");


        System.out.println("Semi-Eulerian graph with self-loop:\n");
        g = UndirectedGraph.fromDotFile("semiEulerianWithSelfLoop");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel()+"\n\n\n");





        // tc1 example

        System.out.println("tc1 example:\n");
        g = UndirectedGraph.fromDotFile("tc1");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel());
        System.out.println("tc1 after modifications:\n"+g.toDotString());





        //debug phase
        UndirectedGraph g1 = UndirectedGraph.fromDotFile("semiEulerianWithSelfLoop");

        List<Node> v = new ArrayList<>();
        for(Node n : g1.getAllNodes()){
            System.out.println(g1.degree(n));
            if(g1.degree(n) % 2 != 0){
                v.add(n);
            }
        }
    }
}
