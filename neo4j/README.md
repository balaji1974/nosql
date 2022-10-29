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
To restart the server -> bin/neo4j restart 
Once started the server can be viewed from http://localhost:7474
Login first time with userid/pwd -> neo4j/neo4j (change it after login)

I can also connect to the running server using command -> bin/cypher-shell
To exit the shell use the command -> :exit 

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

## Using plugins 
```xml

The below link contains all function extensions of neo4j
https://github.com/neo4j-contrib
Go to releases and download the latest in my case graph-algorithms-algo-3.5.4.0.jar  (not working currently and wait for 4.x version to be released)
Go to releases and  download the lateest in my case apoc-4.4.0.10-all.jar
Then restat neo4j with the command bin/neo4j restart
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

// Delete every node that refer to other nodes 
MATCH (p:Person)-[r:REFERS]->()
DELETE r
DELETE p;

//another way to do it
MATCH p=()-->() DELETE p;

//or
MATCH p=() DELETE p;

```

## Movielens database queries 
```xml

CREATE CONSTRAINT ON (u:User) ASSERT u.userId IS UNIQUE;
CREATE CONSTRAINT ON (m:Movie) ASSERT m.movieId IS UNIQUE;
CREATE CONSTRAINT ON (g:Genre) ASSERT g.name IS UNIQUE;

CREATE INDEX ON :Rating(userId);
CREATE INDEX ON :Rating(movieId);

//load predefined genres
CREATE (: Genre {name: "Action"}),
		(: Genre {name: "Adventure"}),
		(: Genre {name: "Animation"}),
		(: Genre {name: "Children's"}),
		(: Genre {name: "Comedy"}),
		(: Genre {name: "Crime"}),
		(: Genre {name: "Documentary"}),
		(: Genre {name: "Drama"}),
		(: Genre {name: "Fantasy"}),
		(: Genre {name: "Film-Noir"}),
		(: Genre {name: "Horror"}),
		(: Genre {name: "Musical"}),
		(: Genre {name: "Mystery"}),
		(: Genre {name: "Romance"}),
		(: Genre {name: "Sci-Fi"}),
		(: Genre {name: "Thriller"}),
		(: Genre {name: "War"}),
		(: Genre {name: "Western"});
		

// Data sets can be taken from the uploaded dataset folder 
// Data sets must reside in the import folder of neo4j 

//load movies
USING PERIODIC COMMIT 100
LOAD CSV WITH HEADERS FROM 'file:///movies.csv' AS line
CREATE (m:Movie {movieId: toInteger(line.id)})
SET m.title=line.title
SET m.genres=line.genres;


//load users
USING PERIODIC COMMIT 1000
LOAD CSV WITH HEADERS FROM 'file:///users.csv' AS line
CREATE (u:User {userId: toInteger(line.id), age: line.age_group, gender: line.gender, occupation: line.occupation, zipCode: line.zip_code});


//load rating and rating relationship
USING PERIODIC COMMIT 10000 //without this line, we get an outofmemory exception
LOAD CSV WITH HEADERS FROM 'file:///ratings.csv' AS line
MATCH (m:Movie {movieId : toInteger(line.mId)})
MATCH (u:User {userId: toInteger(line.uId)})
CREATE (r:Rating {rate: toFloat(line.rate), ts: line.ts}),
	   (u)-[:GAVE]->(r),
       (r)-[:TO]->(m);


//evolve the data to have a genre with relationship to the movie
MATCH (movie:Movie)
WHERE coalesce(movie.genres, "-") <> "-"
WITH SPLIT(movie.genres, "|") as parts, movie as m
UNWIND parts as x
MATCH (g: Genre {name: x})
MERGE (m)-[:IS_A]->(g)
REMOVE m.genres;


// Return the top x most rated movies
MATCH (u:User)-[:GAVE]->(r:Rating)-[t:TO]->(m:Movie)
WITH m.movieId as movieId, m.title as title, count(r) as count, AVG(r.rate) as avg_rate
WHERE count > 2000
RETURN movieId, title, count, avg_rate
ORDER BY avg_rate DESC //which should we use? count or rating
LIMIT 15;

// Return other movies that belong to the same genre as movie x
MATCH (m:Movie {movieId : 5})-[:IS_A]-(g)-[:IS_A]-(otherMovies)
return m, g, otherMovies;


// Return 10 most rated movies that were rated by other people who rated movie X positively
MATCH (x:Movie {movieId : 122})<-[:TO]-(r)<-[:GAVE]-(u:User)
WHERE r.rate > 3 
WITH u as users
MATCH (users)-[:GAVE]-(r)-[:TO]-(m:Movie)
WITH m, AVG(r.rate) AS score, COUNT(r) AS scount
ORDER BY score DESC, scount DESC
RETURN m, score , scount
LIMIT 10;

//similar to the query above. We only removed the first WITH conjunction
MATCH (x:Movie {movieId : 122})<-[:TO]-(xr:Rating)<-[:GAVE]-(us:User)-[:GAVE]->(r2:Rating)-[:TO]->(m:Movie)
WHERE xr.rate > 3 
WITH m, AVG(r2.rate) AS score, COUNT(r2) AS scount
ORDER BY score DESC, scount DESC
RETURN m, score, scount
LIMIT 10;


// Return 10 most rated movies by people like our user x
MATCH (u:User {userId : 700})-[:GAVE]->(r)-[:TO]->(m:Movie)<-[:TO]-(r2)-[:GAVE]-(others:User)-[:GAVE]->(r3)-[:TO]->(m2:Movie)
WHERE r.rate >= 3 AND r2.rate >=3 AND r3.rate >= 3 AND u.gender = others.gender and u.age=others.age 
WITH m2 AS movie, AVG(r3.rate) AS score, count(r3)  AS ratings
RETURN movie
ORDER BY ratings DESC, score DESC
LIMIT 10;


// get the schema in your neo4j instance
call db.schema(); -> Deprecated now with call db.schema.visualization;

//use the apoc help function to find functions or procedures
call apoc.help('path');



//generate page rank score for all nodes in the person network
CALL algo.pageRank.stream('Person', 'REFERS', {iterations:50, dampingFactor:0.95})
YIELD nodeId, score
CALL apoc.nodes.get(nodeId) YIELD node
SET node.rankscore = score;

//generate centrality score for all nodes in the person network
CALL algo.closeness.stream('Person', 'REFERS')
YIELD nodeId, centrality
WITH algo.asNode(nodeId) AS node, centrality
SET node.closecenterscore = centrality;


//let us checkout our new state of network
MATCH (p:Person) 
RETURN p.id, p.firstname, p.lastame, p.gender, p.age, p.rankscore, p.closecenterscore;



///////////////////////////////////////////////////
////////////////  MOVIELENS
//////////////////////////////////////////////////
//what is common between two users.
MATCH (u:User {userId: 2334}) 
MATCH (v:User {userId: 1})
CALL apoc.algo.dijkstra(u, v, 'GAVE|TO', 'weight', 1.0, 5) YIELD path, weight
return path, weight;

//similarity calculation using jaccard similarity function
MATCH (u:User {userId: 2334})-[:GAVE]->()-[:TO]->(movies)
WITH u, collect(id(movies)) AS user1Movies
MATCH (v:User)-[:GAVE]->()-[:TO]->(movies2) WHERE v <> u
WITH u, user1Movies, v, collect(id(movies2)) AS user2Movies2
RETURN u.userId AS from,
       v.userId AS to,
       algo.similarity.jaccard(user1Movies, user2Movies2) AS similarity
ORDER BY similarity DESC
LIMIT 20;


```

## Springboot project 
```xml
Check the appplication in folder neo4j-dataaccess
Change the database access in application.properties file and run the springboot project
Check the swagger url at http://localhost:8080/
Or the swaggger url at http://localhost:8080/swagger-ui.html

A more latest version of the project can be found in the following link:
https://neo4j.com/developer/spring-data-neo4j/


```


## Graph Analytics
```xml
Search 
Path-finding
Community detection
Propagation
Centrality 
Similarity 

```

## Recommended resource
```xml
Check the recommened resource to understard graph algorithm from the recommended resource section
Title Graph Algorithms: Practical Examples in Apache Spark and Neo4j
Authors: Mark Needham, Amy Hodler
```



References:
```xml
https://www.udemy.com/course/sql-nosql-big-data-hadoop/ 
https://grouplens.org/datasets/movielens/
https://en.wikipedia.org/wiki/Graph_theory
```