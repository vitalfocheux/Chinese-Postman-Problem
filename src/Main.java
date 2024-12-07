import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UndirectedGraph g = UndirectedGraph.fromDotFile("semiEulerian");
        UndirectedGraph g2 = new UndirectedGraph();
        g2.addEdge(1,2, 3);
        g2.addEdge(1,3, 4);
        g2.addEdge(1,4,7);
        g2.addEdge(4,3,5);
        g2.addEdge(2,4,6);
        //System.out.println(g2.getIncidentEdges(2));
        //System.out.println(g2.copy().getIncidentEdges(2));

        ChinesePostman c = new ChinesePostman(g);
        //System.out.println(c.solve());
        System.out.println(c.euler2(g.getNode(3)));

        System.out.println(c.floydWarshall());
    }
}
