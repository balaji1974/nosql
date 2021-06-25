# MongoDB


Records are stored as documents (xml, json, yaml etc) -> Its an equivalant of a table in RDBMS   
No schema is followed for storing of documents and hence they are schema less   

When collections become too big they can be sharded (distributed) into the cluster -> Divide the data in the single large collection to mulitple instances of the server   
Shards can be replicated (master-slave strategy) -> Multiple copies of the shards can be stored across cluster for redudency   

Documents are RDBMS equivalant of a record within the table   
For storing documents more than 16M (mongodb limit) in size we need to use MongoDb's GridFS

## Features of MongoDB
Multi-document ACID transactions   
Type conversions   
The improved shard balancer   
The aggregation pipeline builder    
MongoDB Charts (beta)    

## Installing mongodb by 3 different ways on MAC 
1. Direct download of binary and install it   
2. brew install mongodb 
3. Docker container install  (my favourite) - [ docker run -it -p 27017:27017 --name mymongodb -d mongo:latest  ]

## Commad to enter the mongo shell using docker 
docker exec -it mymongodb mongo   
or Connect client to MongoDB - Use MongoDB Compass   


### Important elements of mongo config file (mongo.config)
```xml
bind_ip = 127.0.0.1
port = 27017
quiet = true
dbpath=D:\mongodb\data\db
logpath=D:\mongodb\log\mongo.log
logappend = true
diaglog=3
journal = true
```

### Tables are Collections and Rows are Documents in MongoDB 

## Step by Step - Commands 
```xml
show dbs -> used for listing all the databases available 
db -> will display the current database 
use myrecords -> this will create a reference to database called myrecords. Actual database is created only when we create collections in it 
db.dropDatabase() -> This will drop the current database we are in 

show collections -> This will display all the collections in the current database 
db.createCollection('students') -> this will create a new collection called students
db.createCollection('students', {
    capped : true, 
    size : 2000000, 
    max : 5000,
    autoIndexId:true
}); -> This will create a capped collection called students that will hold a maximum of 5000 documents and will rewrite once this limit has been reached 
db.students.drop() -> This will drop the collection named students
```

### Insert 
```xml
db.students.insert({name:"Balaji", rollno:14, status:"present"}) -> This will insert one document into the collection and return the count of inserted records
db.students.insert({_id: 1, name:"Thiagarajan", rollno:1, status:"present"}) -> This will insert document with our own id, will throw write errror if the id exist 
db.students.insert([{name:"Havisha", rollno:2, status:"present"}, {name:"Haasya", rollno:3, status:"absent"}]) -> This is a bulk insert of more than one document
db.students.insertOne({name:"Balaji", rollno:14, status:"present"}) -> This will insert one document into the collection 'students' and will return a boolean of status and also the unique key of inserted record 
db.students.insertMany([{name:"Havisha", rollno:15, status:"present"}, {name:"Haasya", rollno:16, status:"absent"}]) -> This will insert more than one document into the collection
```

### Find
```xml
db.students.find() -> This will read all the documents in the collection
db.students.find().pretty() -> this will display all documents in the collection in a pretty format 
db.students.find({"status":"present", rollno:14}) -> This is a find documents with filter condition 
db.students.find({rollno:{$gte:15}}) -> This will find documents with filter condition of greater than or equal to ($gte) - for other criterias use the following $lte, $lt, $gt, $eq, $ne, $in, $nin, $exists
db.students.find({"rollno":14},{name:1, _id:0}) -> This will find all documents with rollno=14 and will display only the name field from the document 
db.students.find({},{name:0}) -> This will display all documents in the collection but will not display the name field 
```

### Update 
```xml
db.students.updateOne({"rollno":15},{$set:{"name":"Havisha Balaji"}}) -> This will update the document name to Havisha Balaji where rollno is 15 (only the first match would be updated)
db.students.updateMany({"rollno":{$gte:15}},{$set:{"status":"Present Y"}}) -> This will update more than one document in the collection if the match is found
db.students.updateMany({"rollno":{$gte:10}},{$set:{"game":"Cricket"}}) -> This will add one more new column to the document in the collection if the match is found
```

### Find and modify (will return back the found document before the update)
```xml
db.students.findAndModify({query:{name:"Balaji"}, update:{$inc:{rollno:3}}}) -> This will find the name-Balaji and increment the rollno by 3 
```

### Delete (will return boolen ack along with count)
```xml
db.students.deleteOne({"rollno":15}) -> This will detele the first matching document where the rollno is 15
db.students.updateMany({"rollno":{$gte:15}}) -> This will delete all documents that have roll no. greater or equal to 15
```

### Remove (will return count of removed documents)
```xml
db.students.remove({"rollno":15}) -> This will detele the document where the rollno is 15
db.students.remove({"rollno":15}, 1) -> This will delete the first document where the rollno is 15
db.students.remove({rollno:{$gt:15}}) -> This will remove all documents that have age greater than 15 
db.students.remove({}) -> This will remove all documents from the collection
```

### Renaming a collection
```xml
db.students.renameCollection("student") -> This will rename a collection 
```



### Other general syntax
```xml
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


// This will create an index on the column counter in ascending order which is given as 1 , -1 is for descending order 
db.clicks.createIndex({'counter',1})

// This will create indexes on multiple fields that are unique 
db.clicks.createIndexes([ {'counter',1}, {'field',1}], {unique,'true'})


//get all movies released in the year 2000
db.movies.find({'release_year': 1995})

//get any ten female users who are teens
db.users.find({$and : [{'age_id' : 1}, {'gender' : 'F'}]}).limit(10)

//get any ten lawyers or female users who are teens 
db.users.find({$or : [
        {'occupation' : 'Lawyer'}, 
        {$and : [{'age_id' : 1}, {'gender' : 'F'}]
    }]
}).limit(10)

//get the count of teenage female users
db.users.count({$and : [{'age_id' : 1}, {'gender' : 'F'}]})

//insert a new movie
db.movies.insertOne({
    "title" : "Avenger Endgame (2019)",
    "release_year" : 2019,
    "genres" : [
            "Fantasy",
            "Sci-Fi"
    ]
});

// insert many movies at the same time
db.movies.insertMany([
{"title" : "Glass", "release_year" : 2019, "genres" : [ "Drama", "Sci-Fi", "Thriller" ] },
{"title" : "Fighting with My Family", "release_year" : 2019, "genres" : [ "Biography", "Comedy", "Drama" ] },
{"title" : "Black Panther", "release_year" : 2018, "genres" : [ "Action", "Adventure", "Sci-Fi" ] }])

//search
db.movies.findOne()
//find functions generally take two documents
//the first part is the query predicate definition, 
//the second part controls the projection
db.movies.find({'title' : "Black Panther"}, {}) //.pretty()
//now project only the id and title
db.movies.find({'title' : "Black Panther"}, {'title' : 1, '_id' : 0})

//examples of operations
//search for the 10 Crime and Action movies
db.movies.find({$and : [{'genres' : 'Crime'}, {'genres' : 'Action'}]}).limit(10)
//and some pagnation
db.movies.find({$and : [{'genres' : 'Crime'}, {'genres' : 'Action'}]}).skip(2).limit(10)

//update 
//has to parts: query part and the updating document part
db.movies.update({'title' : "Black Panther"}, {$set : {'release_year' : 2019}})

//delete will delete all documents in the collection
db.movies.delete({})

//get the max id
db.movies.find().sort({"_id": -1}).limit(1)


//explain
db.users.find().explain()
db.users.find({'age_id' : 1}).explain()
db.users.find({$and : [{'age_id' : 1}, {'gender' : 'F'}]}).limit(10).explain()

//add executionStats to explain output
db.users.find({'age_id' : 1}).explain("executionStats")

//get indexes on the users collection
db.users.getIndexes()

//to understand the impact of index on a query, it is important to use the explain function 
db.ratings.find({'genres' : 'Action'}).explain()

//create indexes on the rating collection
db.users.createIndex({'age_id' : 1})
db.ratings.createIndexes([{'movie_id' : 1}, {'user_id': 1}], {unique: true})  //with options
//sort the ratings in descending order or time
db.ratings.createIndex({'rated_at' : -1})
//assumming a unique index on user c
db.users.createIndex({'ssn' : 1}, {unique: true}) //many other options. Check the documentaion


//create full text indexes on the tags collection and embedded title in the movie object
//weight signifies preference that should be givin for similar matching text 
db.tags.createIndex({
        'tag' : 'text',
        'movie.title' : 'text'
    }, 
    {
        'weights' : {
            'tag' : 5,
            'movie.title' : 10
        },
        'name' : 'tag_text_idx'
})

//search using text index
db.tags.find( { $text: { $search: "bitter" } }, {'tag': 1, 'movie.title' : 1} )

//add the score of the relevance to the search
db.tags.find( 
    { $text: { $search: "awesome romance" }},
    { score: { $meta: "textScore" } } 
)

//sort by the score of the relevance to the search
db.tags.find( 
    { $text: { $search: "awesome romance" }},
    { score: { $meta: "textScore" } } 
).sort({score: { $meta: "textScore"}})

//drop movie_id and user_id indexes on the rating collection
db.ratings.dropIndex('idx44')

//joining ratings and user table

//get all ratings where the user gave a 5 star review
db.ratings.aggregate([
    {$match : {"rating" : 5}},
    {$lookup : {
            from : 'users',
            localField: 'user_id',
            foreignField: '_id',
            as: 'user'
        }
    },
    {$unwind : '$user'}
])


//fetch a frequency of genre of movies per year for all years
var mapFn = function(){
    var genres = this.genres;
    var year = this.release_year;
    //emit each genre as count 1
    for (var i = 0; i < genres.length; i++)
        emit({'year': year, 'genre': genres[i]}, 1);
}

var reduceFn = function(key, values) {
    return Array.sum(values);
}

db.movies.mapReduce(mapFn, reduceFn, {out: {inline: 1}, query: {}}) //outputs to screen

db.movies.mapReduce(mapFn, reduceFn, {out: 'genre_yearly_hist', query: {}}) //outputs to a collection

//we will now query the genre_yearly_hist table
//get the frequency distribution of genres in a particular year. Order by frequency in descending order
db.genre_yearly_hist.find({'_id.year' : 1999}, {}).sort({'value': -1})

//get the number of movies for a particular genre through the years
db.genre_yearly_hist.find({'_id.genre' : 'Action'}, {}).sort({'_id.year': -1})

// sql equivalent
//select genre, release_year as year, count(1) from v_movie_genre group by genre, release_year;


//fetch a histogram of genre of movies per year for all years
db.movies.aggregate([
    {$project : {'release_year' : 1, 'genres' : 1, '_id' : 0}},
    {$unwind : '$genres'},
    {$group : {
        _id : { 'year' : '$release_year', 'genre' : '$genres'},
        value : {$sum : 1}
    }},
    // {$match : {'_id.year' : 2000}}
])

// sql equivalent
//select genre, release_year as year, count(1) from v_movie_genre group by genre, release_year;

//fetch the summary ratings for all movies by demographics of viewers
db.ratings.aggregate([
    {$lookup : {
            from : 'movies',
            localField: 'movie_id',
            foreignField: '_id',
            as: 'movie'
        }
    },
    {$unwind : '$movie'},
    {$project : {'rating' : 1, 'movie.title' : 1, 'movie_id' : 1, 'user_id' : 1, '_id': 0}},
    {$lookup : {
            from : 'users',
            localField: 'user_id',
            foreignField: '_id',
            as: 'user'
        }
    },
    {$unwind : '$user'},
    {$project : {'rating' : 1, 'movie.title' : 1, 'movie_id' : 1, 'user_id' : 1, 'user.age_group': 1, 'user.occupation': 1}},
        {$group: {
                '_id' : {'age' : 'movie_id'},
                'k' : {$sum : 1}
            }
        }
])



//select age_group, occupation, count(1) no_movies, sum(rating) total_ratings, 
//avg(rating) average_rating, variance(rating) from v_rating 
//group by age_group, occupation;


// fetch the summary ratings for all movies by demographics of viewers by genre by yearMonth
db.ratings_v2.aggregate([
    {$project : {'rating' : 1, 'ryear' : {'$year' : '$rated_at'} ,  'rmonth' : {'$month' : '$rated_at'} , 'movie.title' : 1, 'movie_id' : 1, 'user.age_group' : 1, 'user.occupation' : 1, 'movie.genres':1, '_id' : 0}},
    {$unwind : '$movie.genres'},
    {$group : {
                '_id' : {'year' : '$ryear', 'month' : '$rmonth', 'title' : '$movie.title', 'age_group' : '$user.age_group', 'occupation' : '$user.occupation', 'genre' : '$movie.genres'},
                'no_rating' : {$sum : 1},
                'average_rating' : {$avg : '$rating'},
                'var_rating' : { $pow: [ { '$stdDevPop' : '$rating' }, 2 ] } ,
        }
    }
])


// select age_group, occupation, gm.genre, monthname(r.rated_at) monthname, count(1) no_movies from v_rating r
//  join v_movie_genre gm on gm.movie_id = r.movie_id
// group by age_group, occupation, gm.genre, monthname;

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

## MongoDB CRUD



References:   
https://spring.io/guides/gs/accessing-data-mongodb/    
https://docs.mongodb.com/manual/reference   





