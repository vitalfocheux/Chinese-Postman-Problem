import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UndirectedGraph g = UndirectedGraph.fromDotFile("eulerian");
        ChinesePostman c = new ChinesePostman(g);
        System.out.println(c.euler2(new Node(g.smallestNodeId(), g)));
//        System.out.println(c.floydWarshall());
//
//
//        System.out.println("Eulerian: "+c.isEulerian());
//        System.out.println("Semi-Eulerian: "+c.isSemiEulerian());

        //System.out.println(c.euler2(g, new Node(g.smallestNodeId(), g)));

    }
}