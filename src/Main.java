import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UndirectedGraph g = UndirectedGraph.fromDotFile("eulerian");
        UndirectedGraph g2 = new UndirectedGraph();
        g2.addEdge(1,2);
        g2.addEdge(1,3);
        g2.addEdge(2,3);
        System.out.println(g2.getIncidentEdges(3));
        System.out.println(g2.copy().getIncidentEdges(3));
        ChinesePostman c = new ChinesePostman(g2);
        System.out.println(c.euler2(new Node(g2, g2.smallestNodeId())));
//        System.out.println(c.floydWarshall());
//
//
//        System.out.println("Eulerian: "+c.isEulerian());
//        System.out.println("Semi-Eulerian: "+c.isSemiEulerian());

        //System.out.println(c.euler2(g, new Node(g.smallestNodeId(), g)));

    }
}