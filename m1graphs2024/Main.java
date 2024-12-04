package m1graphs2024;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Graph g = new Graph(2,4,0,0,6,0,2,3,5,8,0,0,4,7,0,3,0,7,0);

//        System.out.println(g);
//        System.out.println(g.getDFS(new Node(2, g)));
//        System.out.println(g.getBFS(new Node(2, g)));

        UndirectedGraph ug = new UndirectedGraph(2,4,0,0,6,0,2,3,5,8,0,0,4,7,0,3,0,7,0);
        System.out.println(ug);

    }
}