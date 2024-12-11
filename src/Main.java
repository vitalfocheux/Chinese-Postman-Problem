import m1graphs2024.Node;
import m1graphs2024.UndirectedGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the name of the file (precise the extension format): ");
            String filename = scanner.nextLine();
            String path = "./ressources/"+filename;
            File file = new File(path);
            if(file.exists()){
                String[] split = filename.split("\\.");
                UndirectedGraph g = UndirectedGraph.fromDotFile(split[0], ("."+split[1]));
                ChinesePostman c = new ChinesePostman(g);
                boolean random = false;
                if(g.isDisconnectedGraph()) {
                    System.out.println("The graph is disconnected, it is impossible to compute the Chinese Circuit");
                    break;
                }
                List<Node> findEulerianWay = new ArrayList<>();
                if(!c.isEulerian() && !c.isSemiEulerian()){
                    System.out.print("Choose the method to compute the Chinese Circuit (1: Normal method, 2: Random): ");
                    random = (scanner.nextInt() == 2);
                    List<Node> oddNodes = new ArrayList<>();
                    g.getAllNodes().stream().filter(node -> g.degree(node) % 2 != 0).forEach(oddNodes::add);
                    Pair<List<Pair<Node, Node>>, Integer> lengthPairwiseMatching = (random) ? c.lengthPairwiseMatchingRandom(oddNodes) : c.lengthPairwiseMatching(oddNodes);
                    System.out.println("Length of the pairwise matching: " + lengthPairwiseMatching);
                    System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
                    findEulerianWay = c.findEulerianWay(random);
                    g.setLabel(c.createLabel(findEulerianWay));
                    System.out.println(g.toDotString());
                }else{
                    System.out.println("Floyd-Warshall Matrix: " + c.floydWarshall());
                    findEulerianWay = c.findEulerianWay();
                    g.setLabel(c.createLabel(findEulerianWay));
                    System.out.println(g.toDotString());
                }
            }else{
                System.out.println("The file "+filename+" does not exist in the directory ressources");
            }
            System.out.print("Do you want to continue (y/n): ");
            if(scanner.nextLine().equals("n")){
                break;
            }
        }
    }
}
