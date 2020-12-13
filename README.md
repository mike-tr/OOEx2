# Directed Weighted Graph - And the Pokemon game
### Project Overview

- This project was built as part of the OOP Course of the Computer Science Department at Ariel University.

- Inside the src folder you can find an implementation of the Graph Theory data structure and algorithms.

- **Project contributors**: Michael trushkin

- Below is an illustration of the Agent position calculation ( at initial prototypes ) as well as the graph drawer and basic functionallity,
![Alt Text](https://im6.ezgif.com/tmp/ezgif-6-de44b015a0e9.gif)

### Class and Interface Summary

![Image of class and interface summary](https://github.com/yevgenyl/ex1/blob/master/res/package_ex1_content.png?raw=true)

## WGraph_DS Class

### Class Overview
- This class represents an undirected weighted graph **G(V,E)**

- It supports a large number of nodes (over 10^6, with average degree of 10).

- The implementation is based on a compact and efficient representation using HashMap data structure:

  - **V** - The collection of vertices is implemented by using `HashMap<Integer,node_info>`.
  - **E** - The collection of edges is implemented by using `HashMap<Integer, HashMap<Integer, Double>>`.

- Most of the basic operations (add node, get node, connect nodes.. etc) are running in a constant time O(1).

### Constructors
![Image of graph ds constructors](https://github.com/yevgenyl/ex1/blob/master/res/WGraph_DS_Constructors.png?raw=true)
### Methods
![Image of graph ds methods](https://github.com/yevgenyl/ex1/blob/master/res/WGraph_DS_Methods.png?raw=true)

## WGraph_Algo Class

### Class Overview
- This class represents an Undirected (positive) Weighted Graph Theory algorithms including:

  - clone(); (copy)
  - init(graph);
  - isConnected();
  - double shortestPathDist(int src, int dest);
  - List<node_data> shortestPath(int src, int dest);
  - Save(file);
  - Load(file);

### Algorithms
- The methods: `isConnected`, `shortestPath` and `shortestPathDist` are based on two well known algorithms:

  - **BFS - Breadth-first search:** This algorithm is used by `isConnected` method for traversing the graph.
  - **Dijkstra:** This algorithm is used by `shortestPath` and `shortestPathDist` methods to find the shortest path between two vertices.

### Constructors
![Image of graph algo constructors](https://github.com/yevgenyl/ex1/blob/master/res/WGraph_Algo_Constructors.png?raw=true)
### Methods
![Image of graph ds methods](https://github.com/yevgenyl/ex1/blob/master/res/WGraph_Algo_Methods.png?raw=true)

## Unit Tests
- This project was tested using JUnit 5 (Jupiter) unit tests.
- Inside the tests folder you can find two JUnit test classes:
  - **WGraph_DSTest:** this class was used to test the WGraph_DS class.
  - **WGraph_AlgoTest:** this class was used to test the WGraph_Algo class. 

## Importing and Using the Project
- In order to be able to use this project, you should have JDK 11 or above (not tetsted on older versions).

- Simply clone this project to you computer and then import it to your favorite IDE (Eclipse, IntelliJ, etc..).

- Then you should create a new class with `main()` method or use it in any other way that you prefer.

- Below is an example of creating a new weighted graph and adding a vertices to it:
  ```java
  public static void main(String[] args){
    weighted_graph g = new WGraph_DS(); // Creating new weighted graph
    g.addNode(1); // Adding vertices to the graph
    g.addNode(2);
    g.connect(1,2,20); // Connecting node 1 to node 2 and setting the weight to 20.
    g.removeNode(1); // Removing the node which key is 1.
  }
  ```
  
