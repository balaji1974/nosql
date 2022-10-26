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
(m:Movie {key:value}) -> having key-value pair assigned, we can have one or more key-value pairs
m=() -> asign variable to an empty node
m=(:Movie {}) -> assign varibale to a movie having a key-value pair 


[] -> All relationship 
[:LIKE] -> all like relationship
[r:LIKE] -> assign the relationship to a varibale r

(emy) <- [KNOWS] - (jim) - [KNOWS] -> (jean) - [KNOWS] -> (emy) 
(m) - [r] -> (m1), where m is the subject, r is the action and m1 is the object of this relationship

Refer to the Cypher cheat sheet from the below link:
https://neo4j.com/docs/cypher-refcard/current/

```

## Cypher clauses 
```xml
CREATE -> Creates nodes and relationship
RETURN -> returns projected nodes, relationship or their properties to the user
MATCH -> Provides a predicate that Neo4J should evaluate as true. It is mostly used to retrieve a starting node or nodes in a query
SET -> writes or updates the property of a node or relationship
REMOVE -> removes a node or relationship property 
MERGE -> Provides a construct to create a node or relationship if it does not exists or do something else if it does
WITH -> Provides a way to pipeline resultes from one part of a cypher query to another part. It is a projection without sending the results to the end user 
WHERE -> filters records that has been returned from a prior pattern matching 
FOREACH -> iterates and executes an operation on each item in a collection 
DELETE -> deletes a node or relationship 

```

## Cypher queries
```xml

//create a business unique id for a friend referral system
CREATE CONSTRAINT ON (p:Person) ASSERT p.id IS UNIQUE;

//create nodes
CREATE (p: Person {id: "di@zetemhas.vg"}) SET p.firstname="Jack", p.lastname="Singleton", p.city="Johannesburg", p.age=36;
CREATE (p: Person {id: "uwa@tod.ky"}) SET p.firstname="Patrick", p.lastname="McGee", p.city="Auckland", p.age=62;
CREATE (p: Person {id: "so@jejap.gg"}) SET p.firstname="Roger", p.lastname="Palmer", p.city="Tokyo", p.age=26;
CREATE (p: Person {id: "ibe@ari.tc"}) SET p.firstname="Mildred", p.lastname="Joshua", p.city="New York", p.age=63;
CREATE (p: Person {id: "sac@nirwa.ge"}) SET p.firstname="Glen", p.lastname="Warren", p.city="St. Petersburg", p.age=56;
CREATE (p: Person {id: "unki@guwul.pt"}) SET p.firstname="Eric", p.lastname="Owen", p.city="Amsterdam", p.age=18;
CREATE (p: Person {id: "mekwa@jeh.mz"}) SET p.firstname="Callie", p.lastname="Graves", p.city="Lagos", p.age=60;
CREATE (p: Person {id: "gohaeta@unowoet.ee"}) SET p.firstname="Shane", p.lastname="Wise", p.city="Berlin", p.age=34;
CREATE (p: Person {id: "zo@umahulog.am"}) SET p.firstname="Emily", p.lastname="Johnston", p.city="London", p.age=65;
CREATE (p: Person {id: "lo@ro.ga"}) SET p.firstname="Eleanor", p.lastname="Mack", p.city="Los Angeles", p.age=28;

//create relationships
MATCH (jack: Person {id: "di@zetemhas.vg"})
MATCH (pat: Person {id: "uwa@tod.ky"})
MATCH (rog: Person {id: "so@jejap.gg"})
MATCH (josh: Person {id: "ibe@ari.tc"})
MATCH (glen: Person {id: "sac@nirwa.ge"})
MATCH (eric: Person {id: "unki@guwul.pt"})
MATCH (cal: Person {id: "mekwa@jeh.mz"})
MATCH (shane: Person {id: "gohaeta@unowoet.ee"})
MATCH (emy: Person {id: "zo@umahulog.am"})
MATCH (mac: Person {id: "lo@ro.ga"})
MERGE (jack)-[:REFERS]->(pat)
MERGE (jack)-[:REFERS]->(rog)
MERGE (pat)-[:REFERS]->(cal)
MERGE (cal)-[:REFERS]->(eric)
MERGE (cal)-[:REFERS]->(jack)
MERGE (cal)-[:REFERS]->(rog)
MERGE (rog)-[:REFERS]->(jack)
MERGE (rog)-[:REFERS]->(pat)
MERGE (glen)-[:REFERS]->(eric)
MERGE (mac)-[:REFERS]->(eric)
MERGE (cal)-[:REFERS]->(glen)
MERGE (shane)-[:REFERS]->(mac)
MERGE (josh)-[:REFERS]->(josh);

//write queries

//find all
MATCH (p:Person) return p;

//find those who referred themselves
MATCH (p:Person)-[:REFERS]-> (p1)
WHERE p.id = p1.id
return p.firstname, p.lastname, p.id AS email, p.age;

//find all that referred eric
MATCH (p:Person {id: "unki@guwul.pt"}) <- [:REFERS] - (p1) return p1; 

//find all that eric referred
MATCH (p:Person {id: "unki@guwul.pt"}) - [:REFERS] -> (p1) return p1; 

//find all that Patrick referred or was referred by him
MATCH (p:Person {id: "uwa@tod.ky"}) - [:REFERS] - (p1) return p1; 

//generate a password for everyone in our network
MATCH (p:Person)
SET p.passwd = apoc.text.random(16, "A-Za-z0-9")
return p.passwd;

//remove the password property 
MATCH (p:Person)
REMOVE p.passwd
return p;

MATCH (p:Person)-[r:REFERS]->()
DELETE r
DELETE p;

//another way to do it
MATCH p=()-->() DELETE p;

//or
MATCH p=() DELETE p;


```




References:
```xml
https://www.udemy.com/course/sql-nosql-big-data-hadoop/ 
https://grouplens.org/datasets/movielens/
https://en.wikipedia.org/wiki/Graph_theory
```