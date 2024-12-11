###### Vital FOCHEUX 
###### Emma MANGE

# ProjetGraph2
## m1graphs2024 package Modifications
### class Edge
- isMultiEdge() 1.117: le.equals(this) => e != this
- Added toCircuitString()
- Added attribut color for the label
- Added the parameters String color in Edge constructor l.74
### class Graph
- Added the function isDisconnectedGraph()
- Changed adjEgList.values() by getAllEdges() 1.735 & 1.748
- Changed fromDotFile to use the nextInt() method
- Added label attribut
- Added setLabel method
- Added getLabel method
### class Undirected Graph
- Modification of the function copy
- Changed fromDotFile to the one from Emma's API
- Added the override of the getSuccessors method

## Main Menu guide

First, the programme will ask you to enter the DOT file you would like to try, 
in the `ressources` folder.

Then you will be able to choose the extension.

If the graph is non-Eulerian, you will be asked to choose the way the nodes with odd
degree should be paired.

Finally, the program will ask if you would like to quit or compute another file.
