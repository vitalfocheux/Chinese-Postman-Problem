import m1graphs2024.UndirectedGraph;


public class Main {
    public static void main(String[] args) {

        ChinesePostman c = new ChinesePostman(UndirectedGraph.fromDotFile("eulerian"));
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());

        c = new ChinesePostman(UndirectedGraph.fromDotFile("semiEulerian"));
        System.out.println("Eulerian Trail: " + c.findEulerianWay());
        System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
    }
}
