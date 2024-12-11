import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        // Eulerian graph

        System.out.println("\nEulerian graph:\n");
        UndirectedGraph g = UndirectedGraph.fromDotFile("graph");
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


        System.out.println("\nSecond eulerian multi graph:\n");
        g = UndirectedGraph.fromDotFile("eulerianMulti2");
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


        // tc1 example

        System.out.println("tc1 example:\n");
        g = UndirectedGraph.fromDotFile("tc1");
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        List<Node> findEulerianWay = c.findEulerianWay();
        System.out.println("Eulerian Trail: " + findEulerianWay);
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        g.setLabel(c.createLabel());
        System.out.println("\ntc1 after modifications:\n"+g.toDotString());

        System.out.println("disjoiny example:\n");
        g = UndirectedGraph.fromDotFile("graph");
//        for(Node n : g.getAllNodes()){
//            System.out.println(g.getSuccessors(n));
//        }
        c = new ChinesePostman(g);
        System.out.println(g.toDotString());
        findEulerianWay = c.findEulerianWay();
        System.out.println("Eulerian Trail: " + findEulerianWay);
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
        System.out.println("\t"+c.createLabel(findEulerianWay));
        System.out.println("tc1 after modifications:\n"+g.toDotString());
    }
}
