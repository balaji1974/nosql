# Redis 
Remote Directory Service -> It is a key-value in-memory data store used mainly in caching on clustered environments. (It follows strict access pattern)     

Redis can act as a cache between the primary db and the application or it can itself act as the primary db or it can be a mix of both.     

Data structures - Keys, Strings, Hashes, List, Set, SortedSet, HyperLog, Geo, PubSub, Transactions     


## Start redis - Easy way is to the use a docker container image 
Download Redis -> docker pull redis    
Connect to Redis -> Open the CLI from docker desktop after starting the redis container and type the following command:      
redis-cli (or)    
redis-cli -h host -p port -a password (if host is different, port is different and password is set)   
or from docker image start redis in a docker container using command    
docker run -p 6379:6379 redis    

## movielens-redis-ui 
Upload the files in the dataset using the commands given in the run-bulk-uploader-command.sh file into redis     
We have used the Jedis java client to upload data into redis    
As specired in the commands after the upload start the application using java -jar target/movielens-redis-ui-1.0-SNAPSHOT.jar     

## Spring boot redis sample using jedis (spring-boot-sample)   
The configuration package is the starting point which defines all the configruation needed for connection to the redis server.     
We also have defned an entity (key for redis) and the jpa respository class for our JPA opertations.    
We have a RedisOperations class which does sample opertations on redis.    

## Spring boot cache using redis (spring-boot-cache)
The configuration package is the starting point which defines all the configruation needed for the redis cache    
Two custom cache with timeout have been defined that can be tested out    


 
## Redis Command Reference 
```xml
Redis command reference: https://redis.io/commands
KEYS * -> display all keys
KEYS test.* -> display keys starting with test.

The following 7 example commands can be used to set key-values: 
SET foos bar -> String  key 
SET demo.redis.cli.hello world -> String key 
SET demo.redis.cli.keys 12999030039 -> String key 
SADD demo.redis.cli.set0001 apple -> Set key 
HSET demo.redis.cli.hash001 movieId 3 title "Shawshank Redemption" genres "Comedy|Adventure" year 1998 -> Hash Key
ZADD demo.redis.cli.10202-scoreboard 6 James 2 Shane 9 Ezekiel 1 Darwin -> ZSet
PFADD demo.redis.cli.hll001 -> String key 

EXISTS demo.redis.cli.hll001 -> Command to check the existence of a key (returns 1 for true and 0 for false) 

TTL demo.redis.cli.set0001 -> Returns the length of time in seconds that is remaining before the key automatically expires 
PTTL demo.redis.cli.10202-scoreboard -> Same as above but in milliseconds 


EXPIRE foos 5 -> Set the length of time in seconds before the key expires
PEXPIRE mylist 3600000 -> Same as above but in milliseconds

DEL demo.redis.cli.keys -> Delete a key 

TYPE demo.redis.cli.set0001 -> Display the type of the value that the key is pointing to 
OBJECT ENCODING demo.webapp.visitor_count -> This shows the type of the value that is in specified in the key 

SCAN 0 -> to incrementally iterate over a collection of elements

SET foos bars EX 21600 XX -> Create a key foos with value bars and expires in 21800 seconds where XX means “if already exist”. So this will not work if the key foos does not exist

SET foos bars EX 21600 NX -> Same as above but here NX means “if not exist”. So this will not work if the key already exist

GET demo.webapp.visitor_count -> Get the value of the key 

INCR demo.webapp.visitor_count  -> Increments the value of the key by 1
DECR demo.webapp.visitor_count -> Decrements the value of the key by 1

INCRBY demo.ml.movie.2 2.1 -> Same as above but we can specify the increment number 
DECRBY demo.webapp.visitor_count 2 -> Same as above but we can specify the increment number 

STRLEN foos -> Returns the length of a string value 

Hash Data structure (Key -> Value (key,value)) 

Eg. 
HSET demo.ml.movie#1 title "Toy Story (1995)"
HSET demo.ml.movie#1 year 1995
HSET demo.ml.movie#1 genres "Animation|Children's|Comedy"
HSET demo.ml.movie#1 id 1

MSET demo.ml.movie#3952 title "Contender, The (2000)" year 2000  genres "Thriller" id 3952 -> This will inset all of the above in a single command 

HGETALL demo.ml.movie#1 -> will get all values of the key 
HGET demo.ml.movie#1 id -> will get the id value of the key 

HKEYS demo.ml.movie#3 -> List all keys in this hash key 
HVALS demo.ml.movie#2  -> List all values in this hash key 
HLEN demo.ml.movie#3 -> Find the length of the key value pairs inside the hash key 

HEXISTS demo.ml.movie#3927 id  -> Check if the inner key exist for this hash key

TYPE demo.ml.movie#3952 -> Display the type of the key 
OBJECT ENCODING demo.ml.movie#3952 -> Display the type of the value inside the key 

HMGET demo.ml.movie#1 id title year genres field5 -> Returns the value of the inner keys that are specified 

HINCRBY demo.ml.movie#1 year -1 -> Increments/Decrements the value of the inner key specified by the hash key 

List Data structure (Key -> Value (key,key, key etc)) -> order of inserts is preserved and duplicate keys are allowed as values 

LPUSH demo.ml.ratings "1::1193::5::978300760" "1::661::3::978302109" -> Push data to the left of the list (head) 
RPUSH demo.ml.ratings "1::914::3::978301968" "1::3408::4::978300275" -> Push data to the right of the list (tail)

LRANGE demo.ml.ratings 0 -1 -> Display items from the given key starting from 0 index to end of the list (-1) 

LINDEX demo.ml.ratings 1 -> Return the element of the list starting from the left and with the given index (-1 will give the last element of the list) 

LPOP demo.ml.ratings -> Return and Remove an item from the left 
RPOP demo.ml.ratings -> Return and Remove an item from the right 

LTRIM demo.ml.ratings 3 -1 -> Return all items from start index 3 until stop index -1 -> all items from 3 onwards will be returned 
LTRIM demo.ml.ratings 0 0 -> Return all items from start index 0 until stop index 0, only 1 item will be returned 

BLPOP demo.ml.ratings 0 -> use blocking pop to remove an item from the list with 0 timeout

Set Data structure (Key -> Value (key,key, key etc)) -> order of inserts are not preserved and duplicate keys cannot be inserted as values 

SADD demo.ml.genres Action Adventure Animation Children Comedy Crime Drama Fantasy ->  Insert values against the key 
SCARD demo.ml.genres -> Displays the count of all the values in the key (Cardinality) 

TYPE demo.ml.genres -> Displays the type of the object
OBJECT ENCODING demo.ml.genres -> Displays the type of the value of the key 

SMEMBERS demo.ml.genres -> Displays all members of the key 
SISMEMBER demo.ml.genres Action -> Display 1 if the member is present and 0 if not
SRANDMEMBER demo.ml.genres 2 -> Randomly return 2 members of the set 



SINTER demo.ml.genres demo.ml.genres.viewed -> Find common items between the 2 given sets 
SDIFF demo.ml.genres demo.ml.genres.viewed -> Find difference between the 2 sets returning values in the first set not present in the second set

SPOP demo.ml.genres.viewed -> Remove and return a value from this set, since it is not ordered we cannot guarantee which item will be removed 

SREM demo.ml.genres.viewed Drama -> Remove a named value from the set 

SortedSet Data structure (Key -> Value (score value,score value, score value etc)) -> ordered based on a score and duplicates with same sore are sorted based on lexical order 

ZADD demo.ml.genre_hist 20 Action 34 Adventure 10 Animation 16 Children 55 Comedy 19 Crime 39 Drama 11 Fantasy -> Insert data with a score 
ZADD demo.ml.genre_hist NX 22 War 8 Western -> Inset if not exist 
ZADD demo.ml.genre_hist CH 8.9 Horror 8 Sci-Fi 3 Mystery 24 Romance 10 Thriller 3.11 Documentary 1 Film-Noir -> Update the already existing score (CH - change) 

ZCARD demo.ml.genre_hist -> Get the size (cardinality) 

ZRANGE demo.ml.genre_hist 0 -1 -> All items will be displayed starting from 0 index
ZRANGE demo.ml.genre_hist 0 -1 WITHSCORES -> Same as above with scores 

ZREVRANGE demo.ml.genre_hist 0 2 -> Reverse range starting from 0 to 2 index 
ZREVRANGE demo.ml.genre_hist 0 2 WITHSCORES -> Same as above with scores 

TYPE demo.ml.genre_hist -> Display the type of the data
OBJECT ENCODING demo.ml.genre_hist -> Display the type of the data 

ZRANK demo.ml.genre_hist Action -> Displays the rank of the item in ascending order 
ZREVRANK demo.ml.genre_hist Action -> Same as above but in descending order 

ZSCORE demo.ml.genre_hist Action -> Return the score of an item 

ZINCRBY demo.ml.genre_hist 3 Action -> Increase the score of an item 
ZINCRBY demo.ml.genre_hist -3 Action -> Decrease the score of an item (negative value) 

ZPOPMIN demo.ml.genre_hist -> Removing an item with min score 
ZPOPMAX demo.ml.genre_hist -> Removing an item with max score 

ZREM demo.ml.genre_hist XXXXXX -> Will remove an item 

Geospatial Data structure 

GEOADD demo.cities -118.243685  34.052234 "Los Angeles" -> Add lat and long and assign a city to it. The key here is demo.cities 

GEOADD demo.cities -0.127758 51.507351 London 139.650311 35.676192 Tokyo 172.636225 -43.532054 Christchurch  -> Adding more than one data 
               
ZRANGE demo.cities 0 -1 WITHSCORES -> Return all the values within a range and return their scores 

GEODIST demo.cities Dubai Berlin km -> will find the distance between two cities in kms

GEOPOS demo.cities Christchurch -> Display the geo position of the city (lat & long) 

TYPE demo.cities -> Display the type of the key 
OBJECT ENCODING demo.cities -> Display the type of the value 

GEOHASH demo.cities "Los Angeles" -> Display the hash of a city 

GEORADIUSBYMEMBER demo.cities Berlin 2000 km -> Cities within 2000 kms radius of the given city Berlin

Hyperlog log Data structure (HyperLogLog keeps a counter of items that is incremented when new items are added that have not been previously added. It keeps track of the no. of unique values that are added)

PFADD demo.ml.rating.users 1 1 3 4 12 3 4 6 3 5 7 65 5 33 2 213 3 7 4 -> Add items to the hyperlog log

TYPE demo.ml.rating.users -> display the type of key 
OBJECT ENCODING demo.ml.rating.users -> display the type of value 

PFCOUNT demo.ml.rating.users -> Will display the count of unique values that were added 

PubSub Data structure  (Way to communicate between two systems) 
SUBSCRIBE channel_test_1 -> subscribe to channel 





PUBLISH channel_test_1 "this is my hello world" -> publish message to the channel 

Transaction Data structure 
SET foos 230 -> Set initial value for the key 

MULTI -> Start a transaction 
INCRBY foos 11 -> do operations 
DECRBY foos 6 -> do operations 
EXEC -> commit the transaction 

GET foos

MULTI -> Start a transaction 
INCRBY foos 11 -> do operations 
DECRBY foos 6 -> do operations 
DISCARD -> discard the transaction 

GET foos


```

References: 
https://www.udemy.com/course/sql-nosql-big-data-hadoop/    
https://redis.io/commands    
https://www.baeldung.com/spring-data-redis-tutorial    
https://www.baeldung.com/spring-boot-redis-cache    





