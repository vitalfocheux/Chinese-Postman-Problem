# Chinese-Postman-Problem

API Multipgrah :
Changement
- Modification dans UndirectedGraph du fromDotFile(String filename, String extension)
  - Passer de la condition par rapport au label de ça 
    ```txt
    Mettre le contenu de l'ancienne condition
    ```
    à ça :  
    ```java
        if(nodes[1].contains("[")){
          List<String> sp = List.of(nodes[1].split("\\["));
          Pattern pattern_ = Pattern.compile("\\d+");
          Matcher matcher = pattern_.matcher(sp.get(1));
          if(matcher.find()){
              weight = Integer.parseInt(matcher.group().trim());
          }
          nodes[1] = nodes[1].substring(0, nodes[1].indexOf("["));
        }
    ```
    
- Ajout de la méthode existsEdge(Node u, Node v, Integer weight)
