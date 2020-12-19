# Directed Weighted Graph - And the Pokemon game
### Project Overview

- This project was built as part of the OOP Course of the Computer Science Department at Ariel University.

- This project is a Two part project, one is implementing directed weighted graph

- Part 2 : is implemantion UI and Agent logic solving a Pokemon game, where an agent must collect pokemons n a graph, while doing it as fastest and with minimum moves.

- **Project contributors**: Michael trushkin

### Interface Summary

![Image of class and interface summary](https://github.com/miko-t/OOEx2/blob/main/res/interfaces.png?raw=true)

## 

### Part 1 Overview
- We implement the interfaces above and the fucntions

- we get functionality of loading saving, creating graphs

- we also have Algorithems like check if the graph is fully connected and find the fastest path between any two points.

### Algorithms
- The methods: `isConnected`, `shortestPath` and `shortestPathDist` are based on two well known algorithms:

  - **BFS - Breadth-first search:** This algorithm is used by `isConnected` method for traversing the graph, we use it twice once to see if Node is connected
  to all, and then too see if everything connected to node we do it by "reversing" the graph edges.
  - **Dijkstra:** This algorithm is used by `shortestPath` and `shortestPathDist` methods to find the shortest path between two vertices.

### Class Overview
![Image of graph ds constructors](https://github.com/miko-t/OOEx2/blob/main/res/Part1_IMP.png?raw=true)

- We are using a heap implementation to increase the search speed, for the path we can "save" the index of any node as tag, thus we can get O(1) search function on our heap, 
and O(log n) update key.

### Heap Overview
![Image of graph ds methods](https://github.com/miko-t/OOEx2/blob/main/res/Heap.png?raw=true)

- We use Jsons as load save and copy, methods

### Json classes
![Image of graph ds methods](https://github.com/miko-t/OOEx2/blob/main/res/Part1_Utilities.png?raw=true)

## Part 2 Overview

- in this part we are implementing our UI that will draw graphs, Pokemons and Agents,

- we implement a GameData structure that will communicate with the server, and make updates to the game.

- we also impelment Agent behaviour each Agent has it's own thread and will decide when to call "Move", each agent will also decide what his next action should be.

### Ingame footage
  ![Image of graph ds methods](https://media0.giphy.com/media/MgY5wtOTRZDscUiLdJ/giphy.gif)
  

### UI Classes
![Image of graph algo constructors](https://github.com/miko-t/OOEx2/blob/main/res/Part2_Graphics.png?raw=true)
### Game Classes
![Image of graph ds methods](https://github.com/miko-t/OOEx2/blob/main/res/Part2_Game.png?raw=true)
### Utilites
![Image of graph ds methods](https://github.com/miko-t/OOEx2/blob/main/res/Part2_Utilities.png?raw=true)

## Unit Tests
- This project was tested using JUnit 5 (Jupiter) unit tests.
- Inside the tests folder you can find two JUnit test classes:
  - **DWGraph_AlgoTest:** this class was used to test the DWGraph_Algo class.
  - **DWGraph_DSTest:** this class was used to test the DWGraph_DS class. 
  - **JsonGraoh_Test:** this class was used to test the Json Graph loader class.
  - **invertedGraoh_Test:** this class was used to test the Inverted Graph class.
  - **testsPart2:** this class was used to test the Part2, honestly the game and the ui is a test so its useless

## Importing and Using the Project
- In order to be able to use this project, you should have JDK 11 or above (not tetsted on older versions).

- Simply clone this project to you computer and then import it to your favorite IDE (Eclipse, IntelliJ, etc..).

- to run the game you can simply Click on the Ex2.jar, or run the Ex2.java file, either way, it depends on outside Server with would probably be dead in a month.
  
