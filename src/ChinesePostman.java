import m1graphs2024.Edge;
import m1graphs2024.UndirectedGraph;
import m1graphs2024.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChinesePostman {

    UndirectedGraph graph;

    public ChinesePostman(UndirectedGraph graph){
        this.graph = graph;
    }

    public Map<Pair<Node, Node>, Pair<Integer, Node>> floydWarshall(){
        return null;
    }

    public Pair<Integer, Integer> lengthPairwiseMatching(){
        return null;
    }

    public List<Pair<Node, Node>> listPairs(Set<Node> v, List<Pair<Node, Node>> currentListsOfPairs, List<Pair<Node, Node>> listsOfPairs){
        return listsOfPairs;
    }

    public static ChinesePostman fromDotFile(String filename){
        return fromDotFile(filename, "gv");
    }

    public static ChinesePostman fromDotFile(String filename, String extension){
        UndirectedGraph g = new UndirectedGraph();
        String path = filename + "." + extension;

        Path startPath = Paths.get(System.getProperty("user.dir"));
        String pattern = filename+"."+extension;
        final String[] src = {""};

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().equals(pattern)) {
                        if(!file.toAbsolutePath().toString().contains("out")){
                            src[0] = file.toAbsolutePath().toString();
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder dot = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(src[0]))){

            String line;
            while((line = br.readLine()) != null){
                if(line.contains("--")){
                    //System.out.println(line);
                    List<String> split = List.of(line.split("\\["));
                    Pattern p = Pattern.compile("\\d+");
                    Matcher matcher = p.matcher(split.get(1));
                    int weight = 0;
                    if (matcher.find()) {
                        weight = Integer.parseInt(matcher.group());

                        dot.append(split.get(0)).append(" [len=").append(matcher.group()).append(", label=").append(matcher.group()).append("]\n");
                    } else {
                        System.out.println("No number found in the string.");
                    }

                    List<String> nodes = List.of(split.get(0).split("--"));
                    g.addEdge(Integer.parseInt(nodes.get(0).trim()), Integer.parseInt(nodes.get(1).trim()), weight);


                }else{
                    dot.append(line).append("\n");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ChinesePostman(g);
    }



    public String toDotString(){
        return null;
    }
}
