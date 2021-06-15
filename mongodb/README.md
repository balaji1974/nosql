# MongoDB


Records are stored as documents (xml, json, yaml etc) -> Its an equivalant of a table in RDBMS   
No schema is followed for storing of documents and hence they are schema less   

When collections become too big they can be sharded (distributed) into the cluster -> Divide the data in the single large collection to mulitple instances of the server   
Shards can be replicated (master-slave strategy) -> Multiple copies of the shards can be stored across cluster for redudency   

Documents are RDBMS equivalant of a record within the table   
For storing documents more than 16M (mongodb limit) in size we need to use MongoDb's GridFS

## Commands
Installation of MongoDB - docker pull mongo  
Connect client to MongoDB - Use MongoDB Compass   

show dbs -> used for listing all the databases available    
use test -> will switch to the test database and will create one if this does not exist. It will not show up until we add collection to this db.   
db -> will display the current database   

```xml
This will create a collection called clicks that will hold a maximum of 200 documents 
db.createCollection('clicks', {
    capped : true, 
    size : 2000000, 
    max : 200
});

//load 200 documents into the collection
// Here id field is not specified and hence mongodb generates an imutable id field automatically.
// This id field can be of any data type except arrays 
for (var i = 1; i <= 200; i++){
    db.clicks.save({'field' : 'x', 'counter' : i})
}

//count the documents 
db.clicks.count()

//count the documents with  counter less than 200
db.clicks.count({'counter' : {$lt : 200}})

// Find will display the documents page by page
db.clicks.find()


```

## Simple Springboot application to connect to MongoDB and perform CRUD operations
### Application name: simple-crud-samples
In pom.xml add the following dependencies:   
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```
Create an Entity, a repository interface that extends MongoRepository and start using the repository for all the basic operations just like any regular Spring Data JPA / RDBMS samples.   


References:   
https://spring.io/guides/gs/accessing-data-mongodb/    




