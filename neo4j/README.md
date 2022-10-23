# Neo4J

## It is a graph database with Lucene index 
## A graph database is an abstract representation of set of objects and their relationships 
## Neo4J is a High available, ACID complaint database 
## Cypher is the query language that is used



## Graph Database 
```xml
Object or entities are called as vertex(s) or node(s), node represents entities or actors
Relationships are called as Edge(s) and denotes connection between nodes or actors
Undirected graphs have relationship that do not have a direction. 
Directed graphs have relationship with direction.
When nodes has path to every other node, its a connected graph 
When one or more nodes does not have path to other nodes then it is a disconnected graph 
Cyclic graph contains atleast one node that has a path to itself 
Ascyclic graph does not have any node that has a path to itself 
Weighted graph is the one where the edges in your graph have weights, if the edges do not have weights, it is unweighted. 
Property graph is where the relationship have properties associated with them 
Graphs that are classified with labels are labelled graphs 
If nodes have more than one relationship then they are parallel relationships
Graph density is the no. of edges relative to the no. of vertices - In Parse graph has this ratio is small and large in dense graph 
Mono partite graph - One type of label, Bi partite graph - Two types of label, N partite graph - N types of label 
Subgraph - A graph within a graph 
Multi graph - allows self loop, parallel edges, cyclic and ascyclic, directed and undirected, connected or disconnected and subgraphs

```


## Installation 
```xml
Sandbox - Aura 
---------------
This can be launched from here -> https://neo4j.com/developer/
Signup and proceed 
Out of the recommendation -> select realtime recommendation based on movie review 
Launch will launch the sandbox browser - And for the first time a user id and pwd will be downloaded 
Select your instanance and click on Query to connect with the same user id and password 


Standalone mode
---------------
Go the download center -> https://neo4j.com/download-center/
Select the community version and download it 
Extract and move to your softwares folder 
Go to this folder and run the command -> bin/neo4j start 
Once started the server can be viewed from http://localhost:7474
Login first time with userid/pwd -> neo4j/neo4j (change it after login)

I can also connect to the running server using command -> bin/cypher-shell

```

## Cypher 
```xml
It is a graph database query language
Cypher is made up of clauses, statements, functions and expressions

() -> All nodes
(:Movie) -> Movie label node
(m:Movie) -> m is a variable assigned to the Movie label node
(m:Movie {key:value}) -> having key-value pair assigned 
 



Refer to the Cypher cheat sheet from the below link:
https://neo4j.com/docs/cypher-refcard/current/

```




References:
```xml
https://www.udemy.com/course/sql-nosql-big-data-hadoop/ 
https://grouplens.org/datasets/movielens/
https://en.wikipedia.org/wiki/Graph_theory
```