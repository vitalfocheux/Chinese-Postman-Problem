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
            System.out.print("Enter the name of the file : ");
            String filename = scanner.nextLine();
            System.out.print("Choose the extension of the file "+filename+": ");
            String extension = "."+scanner.nextLine();
            String path = "./ressources/"+filename+extension;
            File file = new File(path);
            if(file.exists()){
                UndirectedGraph g = UndirectedGraph.fromDotFile(filename, extension);
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
            String answer = scanner.next();
            if(answer.equals("n")){
                break;
            }
        }
    }
}
