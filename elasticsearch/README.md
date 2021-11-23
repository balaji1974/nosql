# ElasticSearch

## Installation option 1
```xml
Download and install Elasticsearch from the below URL: 
https://www.elastic.co/downloads/elasticsearch

Uzip the zip file and move it to the location of your choice. 
Now navigate to the root of the extracted elasticsearch folder. 
From this folder start the server by issuing the following command: 

bin/elasticsearch

Note: 9200 is the default port for Elasticsearch which can be changed in elasticsearch.yml file located in the config folder.

We can configure many more things like apart from port settings in this yaml file like: 
cluster name
node name
data file storage path
log file storage path 
host ip 

Next we can set the JVM options in the file called jvm.options located in the config directory. 

For logging properties setting we can configure the file called log4j2.properties file located under the config directory. 

Once the server is started we can query the server status with the following  url: 
GET localhost:9200/ 
or curl http://localhost:9200

```

## Installation option 2
```xml

Directly sign up for a 14 day free trial of elastic cloud and start using it. 

The sign-up URL is given below: 
https://www.elastic.co/cloud/

```

## Installation option 3
```xml
Docker Elastic Search 

It can be installed by following the steps in the following URL:
https://hub.docker.com/_/elasticsearch

Commands that can be run for docker install are: 
docker pull elasticsearch:7.14.2 -> latest is not supported and we need to use specific tag for elastic
docker network create somenetwork -> where somenetwork will be replaced with our own network name
docker run -d --name elasticsearch --net somenetwork -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.14.2


For production grade installation (multi-node) follow the instructions given in the below url:
https://www.elastic.co/guide/en/elasticsearch/reference/7.5/docker.html 

```

### Node, Cluster, Shard & Replica -> Same concept as other databases 
```xml
Node -> Single machine running elastic search 
Cluster -> Set of multiple nodes running together
Shard -> Index horizontally partitioned across multiple nodes
Replica -> Data in a shared being replicated across nodes for fault tolerance  
Documents -> Data is stored in documents as JSON objects
Index -> It is a collection of related documents 
```

## Index 
### An index is like a ‘database' in a relational database. 
```xml
RDBMS => Databases => Tables => Columns/Rows
Elasticsearch => Indices => Types => Documents with Properties
Eg. http://localhost:9200/review/doc 
In the above url the index (database) is 'review' and type (table) is 'doc'

Note: Type concept is now deprecated and we can use the following url instead 
http://localhost:9200/review/_doc
 ```

## Useful commands (run them from Kibana dev tools console)
```xml

Since all the below commands are run from the Kibana dev tools console I will be omitting the http://localhost:9200/ and Kibana will add it to all my requests
But if we use the same commands from postman/curl please be sure to append http://localhost:9200/ to all the below commands 
If we copy the below command from dev tools as a curl command, the full URL is appended with the curl request automatically. 

Here all underscore (_) refers to the APIs that we are calling and the other part refers to the command we are running. 
For eg. In the below request cluster is the API that we are calling and health is the command we are running against this API. 

GET _cluster/health -> This will display the clusters health status 
GET _cat/nodes -> This will list the information of the nodes 
GET _cat/nodes?v -> Same command as above with descriptive header information 
GET _nodes -> Retrieve more complete node information 
GET _cat/indices -> Display all the indices in Elasticsearch

Leading dot (.) in indices will help us to hide the index within the node (eg. .kibana_7.15.1_001)

```
## Sharding 
```xml
This is a process by which an index is split into multiple indices 
The reason for this is to run the search query in parallel - for optimization 
and data storage to be managed in chunks on multiple nodes

A default shard for an index is 1 and a decent number for millions of documents in an index is 5 
A shard can be split or shrink and we have Elasticsearch APIs for each of this operation. 

GET /_cat/shards?v -> This will display all the shards in the cluster

```

## Replication
```xml
Replication works by creating copies of shards
A shard that has been replicated is called a primary shard
Primary and secondary shards are together called as replication groups
No. of replicas can be configured at index creating time
One is the default value for a replica 

No replica shard is stored on the same node as the primary shard

A replica shard increases the query performance where workload is placed on the replica or the primary shard based on the load on the nodes. 

Best practice is to replicate a shard twice atleast and add a minimum of 3 nodes to the elastic cluster. The more the better. 

```

## Snapshots 
```xml
Elasticsearch supports snapshots of data to be taken as backups
While replication is live, snapshots are an instance of time. 

```

## Running multiple elastic nodes from the same machine (for development purpose only)
```xml
First way
---------
Either opy the elastic folder into another folder or make another duplicate elasticsearch.yaml file and change the node.name, path.data and path.logs in the elasticsearch.yaml file. This file is located inside the config folder.
Now start the new node.

Second way (not an ideal way and first approach is better)
----------
Start elastic with the following command:
bin/elasticsearch -Enode.name=node-2 -Epath.data=./node-2/data -Epath.logs=./node-2/log -> This will override the default setting in the elasticsearch.yaml file. 

Third way 
---------
Use cloud solution but needs subscription

```

## Types of nodes (roles that they play)
```xml
Master node: Responsible for creating and deleting index. Dedicated master nodes are useful for large clusters.
Data node: Responsible for storing data and performing search queries on this data. 
Ingest node: Enables nodes to run ingest pipeline which are series of steps that are performed when indexing documents (simplified version of Logstash)
Machine learning node: node.ml if set to true will enable a node to run machine learning jobs, xpack.ml.enabled if set will enable the machine learning API for the node
Coordination node: Responsible for distribution of queries and aggregation of results
Voting-only node: Will process in the voting process of new master node 

Note: Modifying the default roles of a node is done only for large clusters. 

```

## How Elasticsearch reads data (Single document and not how search queries work)
```xml
Steps: 
1. Coordinating node -> Node which receives the initial read request from the client 
2. Routing -> Next routing happens, which is a process where it resolves to a shard that stores the document 
3. Adaptive replication selection -> This is where a shard is selected from the replication group 
4. Request is sent to the selected shard and the response is collected by the coordinating node and sent back to the client. 

```

## How Elasticsearch writes data 
```xml
Steps: 
1. Coordinating node -> Node which receives the initial write request from the client 
2. Routing -> Next routing happens, which is a process where it resolves to the primary shard that needs to store the document 
3. Primary Shard -> This will validate the structure and the field values of the request 
4. Forward the write to replica shards -> Primary shard performs the write operation before forwarding the request to the replication shards

```

## Other concepts: 
```xml
Primary terms: A way to distinguish between old and new primary shards by maintaining a counter 
which holds the value of how many times the primary shard had been changed 
Sequence number: A counter that is incremented for each write operation until the primary shard changes 
Global check point: Sequence number that exist for each replication group 
Local check point: Sequence number that exist for each replica shard 
_version: Elasticsearch maintains the version field (incrementing integer) for every document that changes -> This is internal versioning 
External versioning: This is a manual versioning of documents which is controlled by client
Optimistic concurrency control: Process by which operations are sequenced by the order in which they are sent by the client -> Old approach and not used any more

```

## Bulk API for inserting data
```xml
curl -H "Content-Type: application/x-ndjson" -XPOST http://localhost:9200/products/_bulk --data-binary "@products-bulk.json"
-> Please check the products-bulk.json file for data that is being inserted. 
If curl is not present download and install it from the below url: 
https://curl.se/download.html
```

## Data types:
```xml

Commonly used data types: 
boolean, short, integer, long, float, double, text, date, object (used for storing any JSON object) 

Specialized data types:
geo_point, ip, nested (used for storing nested JSON object), exact (used for exact matching of values - sorting,filtering & aggregations)

Reference: 
https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html

```


## Mapping
### Mapping is the process of defining how a document, and the fields it contains, are stored and indexed, like DDL in RDBMS

```xml
Mapping defines the structure of a document, i.e., their fields and structures - same as Schema in RDBMS 
Explicit mapping -> we define field mappings ourselves.
Dynamic mapping -> ElasticSearch does the field mapping for us. 

GET http://localhost:9200/review 
 -> This will get the mapping of the given index (review in our case)
```

## Create Index without mapping
```xml

Simple index (from Kibana)
PUT /pages

Simple index with 2 shards and 2 replicas
PUT /products
{
  "settings" : {
    "number_of_shards": 2,
    "number_of_replicas": 2
  }

}
```

## Putting document to an Index or 'Indexing a document'
```xml
Simple document - in Kibana console, 
Index will be created automatically if not exist. 
This setting can be overridden by the flag action.auto_create_index=false.  
POST /products/_doc
{
  "name": "coffee maker",
  "price": 47,
  "in_stock": 27
}

Create an document with ID within a index. 
Note the header, it must be PUT instead of POST and the id of the document would be 100
PUT /products/_doc/100
{
  "name": "toaster",
  "price": 15,
  "in_stock": 92
}


Creating a document 
http://localhost:9200/review/_doc
POST
{
    "id": 0,
    "country": "Italy",
    "description": "Aromas include tropical fruit, broom, brimstone and dried herb. The palate isn't overly expressive, offering unripened apple, citrus and dried sage alongside brisk acidity.",
    "designation": "Vulkà Bianco",
    "points": 87,
    "price": 0.0,
    "province": "Sicily & Sardinia",
    "region_1": "Etna",
    "region_2": "",
    "taster_name": "Kerin O’Keefe",
    "taster_twitter_handle": "@kerinokeefe",
    "title": "Nicosia 2013 Vulkà Bianco  (Etna)",
    "variety": "White Blend",
    "winery": "Nicosia",
    "active" : true
}

Creating a document with id where 50 is the id for the document. 
http://localhost:9200/review/_doc/50
POST
{
    "id": 0,
    "country": "Italy",
    "description": "Aromas include tropical fruit, broom, brimstone and dried herb. The palate isn't overly expressive, offering unripened apple, citrus and dried sage alongside brisk acidity.",
    "designation": "Vulkà Bianco",
    "points": 87,
    "price": 0.0,
    "province": "Sicily & Sardinia",
    "region_1": "Etna",
    "region_2": "",
    "taster_name": "Kerin O’Keefe",
    "taster_twitter_handle": "@kerinokeefe",
    "title": "Nicosia 2013 Vulkà Bianco  (Etna)",
    "variety": "White Blend",
    "winery": "Nicosia",
    "active" : true
}


```

## Delete Index

```xml
(from Kibana)
DELETE /pages
```


## Documents 
### These are the actual records that are stored inside an index, they are like rows inside a table in RDBMS

## Elasticsearch field data types
```xml
https://www.elastic.co/guide/en/elasticsearch/reference/7.1/mapping-types.html#_core_datatypes
```

## Create a index from a mapping file (structure of a database table in RDBMS)
```xml
PUT http://localhost:9200/ml-ratings
BODY
{
	"mappings" : {
		"dynamic" : false,
		"properties" : {
			"userId" : {"type": "long", "store": true},
			"movieId" : {"type": "long", "store": true},
			"title" : {"type" : "text", "store" : true, "index" : true, "analyzer" : "snowball"},
			"year" : {"type" : "short", "store" : true},
			"genres" : {"type" : "keyword", "store": true},
			"rating" : {"type" : "float", "store" : true},
			"ts" : {"type" : "date", "store": true, "format":"dd/MM/yyyy HH:mm"}
		}
	},
	"settings" : {
		"number_of_shards" : 5,
		"number_of_replicas" : 1
	}
}

PUT http://localhost:9200/ml-tags
BODY
{
	"mappings" : {
		"dynamic" : false,  //false, true, strict
		"properties" : {
			"userId" : {"type": "long", "store": true},
			"movieId" : {"type": "long", "store": true},
			"title" : {"type" : "text", "store" : true, "index" : true, "analyzer" : "snowball", "copy_to" : "all"},
			"year" : {"type" : "short", "store" : true},
			"genres" : {"type" : "keyword", "store": true, "copy_to" : "all"},
			"tag" : {"type" : "text", "store" : true, "index" : true, "analyzer" : "snowball", "copy_to" : "all"},
			"ts" : {"type" : "date", "store": true, "format":"dd/MM/yyyy HH:mm"},
			"all" : {"type" : "text", "index" : true, "store": false, "analyzer": "standard"} 
		}
	},
	"settings" : {
		"number_of_shards" : 3,
		"number_of_replicas" : 1
	}
}

Here the "all" field will have values from title, genres and tag field because we have specified copy_to: all in these respective fields. Its value will be indexed since we specified index: true but it will not be stored since we specified as store: false
```


### Insert data 
```xml
POST http://localhost:9200/review/_doc
BODY <sample data - in the  body of the request>
{
    "id": 0,
    "country": "Italy",
    "description": "Aromas include tropical fruit, broom, brimstone and dried herb. The palate isn't overly expressive, offering unripened apple, citrus and dried sage alongside brisk acidity.",
    "designation": "Vulkà Bianco",
    "points": 87,
    "price": 0.0,
    "province": "Sicily & Sardinia",
    "region_1": "Etna",
    "region_2": "",
    "taster_name": "Kerin O’Keefe",
    "taster_twitter_handle": "@kerinokeefe",
    "title": "Nicosia 2013 Vulkà Bianco  (Etna)",
    "variety": "White Blend",
    "winery": "Nicosia",
    "active" : true
  }

BODY <sample data - in the  body of the request>
  {
    "id": 1,
    "country": "Portugal",
    "description": "This is ripe and fruity, a wine that is smooth while still structured. Firm tannins are filled out with juicy red berry fruits and freshened with acidity. It's  already drinkable, although it will certainly be better from 2016.",
    "designation": "Avidagos",
    "points": 87,
    "price": 15.12,
    "province": "Douro",
    "region_1": "",
    "region_2": "",
    "taster_name": "Roger Voss",
    "taster_twitter_handle": "@vossroger",
    "title": "Quinta dos Avidagos 2011 Avidagos Red (Douro)",
    "variety": "Portuguese Red",
    "winery": "Quinta dos Avidagos",
    "active" : true
  }
```

### Update data - Elastic search documents are immutable. While updating a new document it is reterived first, fields updated and re-indexed (replaced) with the same id.  
```xml
POST /products/_update/100
{
  "doc" : {
    "in_stock": 92
  }
  
}
-> This updates the in_stock field for document with index 100 

POST /products/_update/100
{
  "doc" : {
    "tags" : ["electronics"]
  }
  
}
-> This adds a new field called tags to the index 'products' with document id 100 

```

### Scripted Update / In-line update  
```xml
POST /products/_update/100
{
  "script": {
    "source": "ctx._source.in_stock--"
  }
}
-> This updates from the present context (ctx) inside the source document (_souce) and reduces the in_stock quantity by 1

POST /products/_update/100
{
  "script": {
    "source":  "ctx._source.in_stock=50"
  }
}
-> Same as above but here the in_stock value has been updated to value 50 

POST /products/_update/100
{
  "script": {
    "source":  "ctx._source.in_stock -= params.quantity",
    "params" : {
      "quantity": 5
    }
  }
}
-> The above reduces the in_stock value by the quantity given in the params (parameter)

POST /products/_update/100
{
  "script": {
    "source":  """
      if (ctx._source.in_stock == 0)  {
        ctx.op='noop';
      }
      ctx._source.in_stock--;
    """
    
  }
}
-> This will not perform anything if the in_stock is 0 but will reduce the in_stock by 1 otherwise. 
Here instead of 'noop' if we use 'delete' then it will delete the document if the matching condition is true.  

```

### Upsert - Insert or update 
```xml
POST /products/_update/101
{
  "script": {
    "source":  "ctx._source.in_stock++"
  },
  "upsert" : {
      "name" : "computer",
      "price" : 900,
      "in_stock" : 105
  }
}

-> This will insert a new document if the document 101 is not present. If present it will update the in_stock and increases it by 1 
```

### Update by query - multiple documents   
```xml
POST /products/_update_by_query 
{
  "script": {
    "source":  "ctx._source.in_stock++"
  },
  "query": {
    "match_all": {}
  }
}
-> This will match all documents and increase the in_stock by 1 
```

### Replicate existing document
```xml
PUT /products/_doc/100
{
  "name": "toaster",
  "price": 49,
  "in_stock": 92
}
-> This will replace the existing document with id 100 with new values given in the body  
```

### Deleting documents
```xml
DELETE /products/_doc/101
-> This will delete the document that has the id 101  
```

### Deleting documents by query 
```xml
POST /products/_delete_by_query 
{
  "query": {
    "match_all": {}
  }
}
-> This will delete all the documents in the given index   
```

## Routing - > This is a process of resolving the shard for a document 
```xml
shard_num = hash(_routing) % num_primary_shards  
```

## Text Analysis
```xml
The process of text analysis: 
Document ----> Analyzer (Character Filter, Tokenizer, Token Filters) ----> Storage  

The standard analyzer of Elasticsearch applies no character filter, applies standard tokenizer, and finally applies lower case tokenizer 

POST /_analyze 
{
  "text" : "this is my Test text.....DUCKS !!!!",
  "char_filter": [], 
  "analyzer": "standard",
  "filter": ["lowercase"]
}

The above is same as specifying
POST /_analyze 
{
  "text" : "this is my Test text.....DUCKS !!!!",
}
since the below are all default values to an analyzer 
"char_filter": [], 
"analyzer": "standard",
"filter": ["lowercase"]

Result would be  
{
  "tokens" : [
    {
      "token" : "this",
      "start_offset" : 0,
      "end_offset" : 4,
      "type" : "<ALPHANUM>",
      "position" : 0
    },
    {
      "token" : "is",
      "start_offset" : 5,
      "end_offset" : 7,
      "type" : "<ALPHANUM>",
      "position" : 1
    },
    {
      "token" : "my",
      "start_offset" : 8,
      "end_offset" : 10,
      "type" : "<ALPHANUM>",
      "position" : 2
    },
    {
      "token" : "test",
      "start_offset" : 11,
      "end_offset" : 15,
      "type" : "<ALPHANUM>",
      "position" : 3
    },
    {
      "token" : "text",
      "start_offset" : 16,
      "end_offset" : 20,
      "type" : "<ALPHANUM>",
      "position" : 4
    },
    {
      "token" : "ducks",
      "start_offset" : 25,
      "end_offset" : 30,
      "type" : "<ALPHANUM>",
      "position" : 5
    }
  ]
}

An Inverted index is a mapping between terms and which document contains them
It is sorted alphabetically and contains other information like relevance score also 
It is created for each text field within an index 
Eg.
            Document #1     Document #2     Document #3
data            X                                X
test            X               X
ducks                           X                X


Keyword data types use the keyword analyzer -> This analyzer is a no op analyzer, i.e., it will not do anything but return the same input value back. 
Eg. 
POST /_analyze 
{
  "text" : "this is my Test text.....DUCKS !!!!",
  "analyzer": "keyword"
}

This will return 
{
  "tokens" : [
    {
      "token" : "this is my Test text.....DUCKS !!!!",
      "start_offset" : 0,
      "end_offset" : 35,
      "type" : "word",
      "position" : 0
    }
  ]
}
Use case example for the keyword type is an email address field where an exact match is required.

Coercion
It is a process where the correct datatype is applied for a string value that is supplied automatically by ElasticSearch  

```


### Search Query DSL 
```xml

GET /products/_doc/100 -> This will retrieve the document in index 'products' with an id whose value is 100 


GET http://localhost:9200/review/_search 
 -> Will result in all records being returned

GET http://localhost:9200/review/_search
BODY 
{
  "query": {
    "match_all": {}
  }
}
	-> Will match all the documents 

BODY 
{
  "query": {
    "match_none": {}
  }
}
	-> Will not match any document

BODY
{
  "query": {
    "match": {
        "description": "This"
    }
  }
}
	-> This will match anything that has "this" in the description field 
	-> the order of display will the in the descending order of relevance score

BODY 
{
  "query": {
    "match": {
      "description": "tropical fruit"
    }
  }
}
 	-> Will result in matching description field containing "tropical" and/or"fruit"  

BODY 
{
  "query": {
    "term": {
        "id": 3
    }
  }
}
	-> The term query will result in the exact match - 
	-> Note: Avoid using terms query for text matches and use match query 

BODY
{
  "query": {
    "range": {
        "price": {
            "gte": 12,
            "lte": 17
        }
    }
  }
} 
	-> This is a range query that matches price between 12 and 17 both inclusive, 
	-> if we need to exclude 12 and 17 use gt and lt instead 

BODY
{
  "query": {
    "match_phrase": {
        "description": "this is"
    }
  }
}
	-> Will match and return the phrase "this is" or "This is"

BODY 
{
  "query": {
    "multi_match": {
        "query": "it Reserve",
        "fields": ["description","designation"]
    }
  }
}
	-> This will match more than one field given inside the "Fields" and match against the data in the "query" tag

BODY
{
  "query": {
    "query_string": {
        "query": "description: is + title: late"
    }
  }
}
	-> This will match the query expression where fields are separated by + 

BODY - Syntax for boolean query
{
	"query": {
		"bool": {
			"must": [],
			"should": [],
			"must_not": [],
			"filter": []
		}
	}
}

	-> this is the syntax of the boolean match query 

Eg. 
{
  "query": {
    "bool": {
      "must": [
      	{
      		"match_all": {}	
      	}
      ],
      "should": [
      	{
      		"match_phrase": {
      			"tag" : "super heroes"
      		}	
      	}
      ],
      "must_not": [
      	{
      		"match_none": {}	
      	}
      ],
      "filter": [
      	{
      		"terms" : {
      			"genres" : ["Action", "Adventure", "Sci-Fi", "Comedy"]
      		}
      	},
      	{
      		"range" : {
      			"year" : {
	      			"gte" : 1990,
	      			"lte" : 2000
	      		}
      		}
      	}
      ]
    }
  }
}

Sort data based on a field
GET http://localhost:9200/review/_search?sort=price
	-> Will sort retreived data based on the field price 

Search based on id
GET http://localhost:9200/review/_doc/<id>
	-> Will retreive the data based on the id field 

Count no. of records in the index
GET http://localhost:9200/review/_count 
	-> Will count the records in the index 'review'

It can be combined with the BODY tag used in the _search end-point to filter data and count

Other queries like exist, prefix, wildcard, regex are also possible. Check Elastic documentation


********************* Version 2 *****************

Few types of search queries are possible: 
GET /products/_search
{
  "query": {
    "match": {
      "name": "Lobster"
    }
  }
}
-> Query DSL query 
or 

GET /products/_search?q=name:Lobster
-> Request Parameter query 
or

GET /products/_search
{
  "query": {
    "query_string": {
      "query": "name:Lobster"
    }
  }
}
-> Query DSL query using query_string


Request UI Query: 
GET /review/_search?q=country:France



```

### Aggregation Query - Used on numeric fields
```xml
Syntax
{
	"query": { … },
	"aggregations" : { … }
}

Types of aggregation are: Bucketing, Metrics, Matrix & Pipeline

Bucketing
{
	"query" : {
		"match_all" : {}
    },
    "size" : 0,
    "aggregations" : {
        "points_counting" : {
            "terms" : { 
                "field" : "points",
                "size"  : 15
            } 
        }
    }
}
	-> This will group the field "points" and will count the results 

Metrics
{
	"query" : {
		"match_all" : {}
    },
    "aggregations" : {
        "points_counting" : {
            "terms" : { 
                "field" : "points",
                "size"  : 20
            }, 
            "aggregations" : {
                "statistics" : {
                    "stats" : { 
                        "field" : "price"
                    } 
                }
            }
        }
    }
}
	-> This will group on "points" and perform std stats operation like count, min, max, avg and sum on "price" 

```

## ELK - Elastic, Logstash, Kibana
### Elastic - Database engine built on top of Apache Lucene
### Logstash - Tool for data collection, transformation and transportation pipeline
### Kibana - Data visualization and analytics platform 

## Kibana
```xml
Download and unzip Kibana from https://www.elastic.co/downloads/kibana or https://www.elastic.co/start
Move it to a folder of your choice

Start Kibana from the root installation folder of kibana by executing the following command:
bin/kibana

This will auto connect to Elasticsearch provide it started in the default port of 9200

Now use the browser to connect to Kibana using the url http://localhost:5601/

Next click on "Add Data" and enter
```

## Logstash
```xml
Download and install logstash from https://www.elastic.co/downloads/logstash

Start Logstash from the folder by running the following sample pipeline:
./bin/logstash -e 'input { stdin { } } output { stdout {} }'

```

## Sample data to work with for practice
```xml
Download the sample file "complete.es.json"
```

## Create a mapping

```xml
PUT /review 
{
  "mappings": {
    "properties": {
      "rating": {"type": "float"},
      "content": { "type": "text"},
      "product_id" :{"type": "integer"},
      "author": {
        "properties": {
          "first_name" : { "type": "text"},
          "last_name" : {"type" : "text"},
          "email" : {"type" : "keyword" }
        }
      }
    }
  }
}

Inserting data into the mapping created above: 
PUT /review/_doc/1
{
  "rating": 5.1,
  "content": "This is a nice data that we checked and verified",
  "product_id" : 129,
  "author": {
    "first_name" : "Balaji",
    "last_name" : "Thiagarajan",
    "email" : "thiagarajan.balaji@gmail.com"
  }
}

Adding a new mapping to the existing index
PUT /review/_mappings
{
  
  "properties": {
    "created_at": {"type": "date"}
  }
}


PUT http://localhost:9200/ufo
{
	"mappings" : {
		"properties" : {
			"datetime" : {"type": "date", "store": true, "format": "M/d/yyyy HH:mm"},
			"city" : {"type":"keyword", "store": true},
			"state" : {"type":"keyword", "store": true},
			"country" : {"type":"keyword", "store": true},
			"shape" : {"type":"keyword", "store": true},
			"duration_in_sec" : {"type" : "integer","store": true},
			"duration_in_hrs_mins" : {"type" : "text", "store" : false, "index":"false"},
			"comments" : {"type":"text", "store": true, "index":"true", "analyzer" : "snowball"},
			"date_posted" : {"type" : "date", "store" : true, "format":"M/d/yyyy", "ignore_malformed": "true"},
			"location" : {"type" : "geo_point", "ignore_malformed" : true}
		}
	},
	"settings" : {
		"number_of_shards" : 2,
		"number_of_replicas" : 1,
		"index" : {
			"analysis" : {
				"analyzer" : {
					"like_keyword_analyzer" : {
						"type" : "custom",
						"tokenizer" : "keyword",
						"filters" :  ["lowercase"]
					}
				}
			}
		}
	}
}

Upload the file into Elasticsearch using the following command:
curl -XPOST 'localhost:9200/_bulk?pretty' -H "Content-Type: application/json" --data-binary @complete.es.json

Count the records that were upload:
GET http://localhost:9200/ufo/_count

```

## Retrieve mapping 
```xml
GET /review/_mapping

```

## Retrieve field mapping 
```xml
GET /review/_mapping/field/content
GET /review/_mapping/field/author.email

```

## Date format 
```xml
Only date
PUT /review/_doc/2
{
  "rating": 4.5,
  "content": "Not bad. Not bad at all!",
  "product_id": 123,
  "created_at": "2015-03-27",
  "author": {
    "first_name": "Average",
    "last_name": "Joe",
    "email": "avgjoe@example.com"
  }
}

Date and Time 
PUT /review/_doc/3
{
  "rating": 3.5,
  "content": "Could be better",
  "product_id": 123,
  "created_at": "2015-04-15T13:07:41Z",
  "author": {
    "first_name": "Spencer",
    "last_name": "Pearson",
    "email": "spearson@example.com"
  }
}

UTC offset
PUT /review/_doc/4
{
  "rating": 5.0,
  "content": "Incredible!",
  "product_id": 123,
  "created_at": "2015-01-28T09:21:51+01:00",
  "author": {
    "first_name": "Adam",
    "last_name": "Jones",
    "email": "adam.jones@example.com"
  }
}

Timestamp since epoc - Long format
PUT /review/_doc/5
{
  "rating": 4.5,
  "content": "Very useful",
  "product_id": 123,
  "created_at": 1436011284000,
  "author": {
    "first_name": "Taylor",
    "last_name": "West",
    "email": "twest@example.com"
  }
}

```

## Parameters in Mapping
```xml
format - Used for formatting the date field
properties - Defines nested fields for objects and nested fields
coerce - used of enable/disable coercion of values - if used in mapping Strings will be coerced to numbers and Floating points will be truncated for integer values.
doc_values - It is an uninverted "inverted" index
norms - normalization factor used for relevance scoring (can be disabled if not needed)
index - field can be disbaled or enabled for indexing
null_value - used for replcating null value with another value 
copy_to - used to copy multiple values into group field 
```

## Reindexing document
```xml
POST /_reindex
{
  "source": {
    "index": "review"
  },
  "dest": {
    "index": "review_new"
  },
  "script": {
    "source": """
      if (ctx._source.product_id != null) {
        ctx._source.product_id = ctx._source.product_id.toString();
      }
    """
  }
}
->Here we re-indexed the document to a new index and we transformed the product id to a string field from an existing integer field.

We can also match some fields in the souce and bring them to the re-indexed document (this is done by _source on the "source" parameter)
POST /_reindex
{
  "source": {
    "index": "review",
    "_source": ["content", "created_at", "rating"]
  },
  "dest": {
    "index": "review_new"
  }
}
```

## Creating field alias
```xml
PUT /review/_mapping
{
  "properties": {
    "comment": {
      "type": "alias",
      "path": "content"
    }
  }
}
-> where path points to the original field and "comment" is the new alias field pointing to content field 
```

## Multifield index
```xml
PUT /multi_field_test
{
  "mappings": {
    "properties": {
      "description": {
        "type": "text"
      },
      "ingredients": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      }
    }
  }
}

Regular query 
GET /multi_field_test/_search
{
  "query": {
    "match": {
      "ingredients": "Spaghetti"
    }
  }
}

And to query using keyword mapping
GET /multi_field_test/_search
{
  "query": {
    "term": {
      "ingredients.keyword": "Spaghetti"
    }
  }
}
```

## Index template
```xml
PUT /_template/access-logs
{
  "index_patterns": ["access-logs-*"],
  "settings": {
    "number_of_shards": 2,
    "index.mapping.coerce": false
  }, 
  "mappings": {
    "properties": {
      "@timestamp": {
        "type": "date"
      },
      "url.original": {
        "type": "keyword"
      },
      "http.request.referrer": {
        "type": "keyword"
      },
      "http.response.status_code": {
        "type": "long"
      }
    }
  }
}
```

## Elastic common schema
```xml
Standard system logs from different webservers, operating system metrics etc can be stored using ECS as it contains readymade data structures for them
```

## Elasticsearch dynamic mapping
```xml

Mapping of Json to Elasticsearch type: 
--------------------------------------
String -> text (with keyword mapping) / date / float or long
integer -> long
float -> float 
boolean -> boolean
object -> object
array -> maps the first non-null values data type


Setting the dynamic mapping to false ignores new fields from being indexed 
So if this is set then new fields must be mapped explicitly for it to be searched 

```

## Recommendations for mapping 
```xml
Do not use dynamic mapping for production 
  -> Always use explicit mapping 
  -> set dynamic to "strict"
Do not always map text field as both text and keyword as this will take more disk space 
  -> If you need full text searches then use 'text mapping'
  -> If you need to perform aggregation/sorting or filter on exact text use 'keyword mapping'
Disable coercion 
  -> Since it forgives for not doing the right thing it is better to disable this in production 
Use Correct numeric data types 
  -> Based on what the field holds use integer or long (this occupies more disk space which might not be needed) 
  -> Same while defining float or double (this occupies more disk space)
Mapping parameters to consider on production system that holds a large no. of data 
  -> Set doc_values to false if you do not need sorting, aggregation and scripting 
  -> Set norms to false if you do not need relevance scoring 
  -> Set index to false if you do not need a field for filtering by its values 
```

## Stemming  - It is a process of reducing the word to its root form 
```xml
Eg. Loved -> Love
Drinking -> Drink 
```

## Stop words - These are words that are filtered during the analysis process 
```xml
Eg. "a", "the", "at", "on" etc 
```

## Analyser: 
```xml
Standard Analyser: 
It splits text into word boundaries, removes puncuations and lower cases the letters. Stop token filter is not enabled by default. 

Simple Analyser: 
Same as standard analyser but splits text into words when it encounters anything other than letters. 

Whitesapce Analyser: 
Spilts text into takens based on white space. Will not lower case letters. 

Keyword Analyser: 
No-op analyser which outputs the input text as is. 

Pattern Analyser: 
A regular expression is used for splitting the input text. It lower cases letter by default.

Custom Analyer: 
Eg.

PUT /analyzer_test
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_custom_analyzer": {
          "type": "custom",
          "char_filter": ["html_strip"],
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "stop",
            "asciifolding"
          ]
        }
      }
    }
  }
}

"char_filter": ["html_strip"] -> this will trup the HTML tags
"filter": [
            "lowercase", -> This will convert text to lower case
            "stop", -> this will remove stop words
            "asciifolding" -> this will convert words like açaí to acai. 
          ]

Dataset to test: 
POST /analyzer_test/_analyze
{
  "analyzer": "my_custom_analyzer", 
  "text": "I&apos;m in a <em>good</em> mood&nbsp;-&nbsp;and I <strong>love</strong> açaí!"
}

Result from the custom analyser: 
{
  "tokens" : [
    {
      "token" : "i'm",
      "start_offset" : 0,
      "end_offset" : 8,
      "type" : "<ALPHANUM>",
      "position" : 0
    },
    {
      "token" : "good",
      "start_offset" : 18,
      "end_offset" : 27,
      "type" : "<ALPHANUM>",
      "position" : 3
    },
    {
      "token" : "mood",
      "start_offset" : 28,
      "end_offset" : 32,
      "type" : "<ALPHANUM>",
      "position" : 4
    },
    {
      "token" : "i",
      "start_offset" : 49,
      "end_offset" : 50,
      "type" : "<ALPHANUM>",
      "position" : 6
    },
    {
      "token" : "love",
      "start_offset" : 59,
      "end_offset" : 72,
      "type" : "<ALPHANUM>",
      "position" : 7
    },
    {
      "token" : "acai",
      "start_offset" : 73,
      "end_offset" : 77,
      "type" : "<ALPHANUM>",
      "position" : 8
    }
  ]
}

Refer:
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-analyzers.html
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html


Adding Analyser to an existing index:
1. Close the existing index
2. Add the new analyser
3. Open the index back

Eg. 
POST /analyzer_test/_close
-> Close the index 

PUT /analyzer_test/_settings
{
  "analysis": {
    "analyzer": {
      "my_second_analyzer": {
        "type": "custom",
        "tokenizer": "standard",
        "char_filter": ["html_strip"],
        "filter": [
          "lowercase",
          "stop",
          "asciifolding"
        ]
      }
    }
  }
}
-> Add the new analyser 

POST /analyzer_test/_open
-> Open the index 

GET /analyzer_test/_settings
-> Retreive the index and check it 
```

## Filter Context vs Query Context 
```xml
In query context the relevance score is calculated where as in filter context is not calculated 
Eg. 
GET /_search
{
  "query": { 
    "bool": { 
      "must": [
        { "match": { "title":   "Search"        }},
        { "match": { "content": "Elasticsearch" }}
      ],
      "filter": [ 
        { "term":  { "status": "published" }},
        { "range": { "publish_date": { "gte": "2015-01-01" }}}
      ]
    }
  }
}

```

## Relevance Score Concepts: 
```xml
Term Frequency: No of times a search term appears within a document. More times means higher the relevance 
Inverse Document Frequency: How often does a term appear within the index across all documents. The more often it appears the lower the relevance score. 
Field length norm: How long is the field. The longer the field the less relevant the term within that field. 

```

## Term Level Queries 
```xml
Term level queries must be used for field like "status" and not for fields like "description" because they find exact matches 

Single field
GET /products/_search
{
  "query": {
    "term": {
      "is_active": true
    }
  }
}
-> will search for the field is_active for the value "true"

Multi field search 
GET /products/_search
{
  "query": {
    "terms": {
      "tags.keyword": [ "Soup", "Cake" ]
    }
  }
}

Retrieving documents based on ids
GET /products/_search
{
  "query": {
    "ids": {
      "values": [ 1, 2, 3 ]
    }
  }
}

Matching range of ids
GET /products/_search
{
  "query": {
    "range": {
      "in_stock": {
        "gte": 1,
        "lte": 5
      }
    }
  }
}

Matching range of dates
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "2010/01/01",
        "lte": "2010/12/31"
      }
    }
  }
}

Matching with custom date format
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "01-01-2010",
        "lte": "31-12-2010",
        "format": "dd-MM-yyyy"
      }
    }
  }
}

Subtracting one year from an anchor date
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "2010/01/01||-1y" 
      }
    }
  }
}
where "gte": "2010/01/01||-1y"  -> is the anchor date and -1y indicates subtracting 1 year

Subtracting one year and 1 day from an anchor date
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "2010/01/01||-1y-1d"
      }
    }
  }
}

Rounding date by 1 month 
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "2010/01/01||-1y/M"
      }
    }
  }
}

Matching documents containing current date 
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "now"
      }
    }
  }
}

Rounding by month before subtracting one year from the current date
GET /products/_search
{
  "query": {
    "range": {
      "created": {
        "gte": "now/M-1y"
      }
    }
  }
}

Matching documents with non-null value
GET /products/_search
{
  "query": {
    "exists": {
      "field": "tags"
    }
  }
}

Matching documents with values of field beginning with some word (here tags field beginning with Vege was matched)
GET /products/_search
{
  "query": {
    "prefix": {
      "tags.keyword": "Vege"
    }
  }
}

Wildcard match for zero to more characters 
GET /products/_search
{
  "query": {
    "wildcard": {
      "tags.keyword": "Veg*ble"
    }
  }
}

Wildcard matching of single character
GET /products/_search
{
  "query": {
    "wildcard": {
      "tags.keyword": "Veg?table"
    }
  }
}

Searching with regular expression 
GET /products/_search
{
  "query": {
    "regexp": {
      "tags.keyword": "Veg[a-zA-Z]+ble"
    }
  }
}
```

## Full text query 
```xml
Full text queries are used where exact match is not required - For eg. blogpost, description field, articles etc

Import the data set from the recipes-bulk.json file using the below command: 
curl -H "Content-Type: application/x-ndjson" -XPOST "http://localhost:9200/recipe/_bulk?pretty" --data-binary "@recipes-bulk.json"

Check if everything is ok after the upload
GET /recipe/_search
GET /recipe/_mapping

Our first full text query - By default all words are matched using the 'or' boolean match 
GET /recipe/_search
{
  "query": {
    "match": {
      "title": "Recipes with pasta or spaghetti"
    }
  }
}

Below query is more of what we want where we want receipe to match pasta and spagehetti 
GET /recipe/_search
{
  "query": {
    "match": {
      "title": {
        "query": "pasta spaghetti",
        "operator": "and"
      }
    }
  }
}

Matching Phrases - Order of the phrase is maintained and important 
GET /recipe/_search
{
  "query": {
    "match_phrase": {
      "title": "spaghetti puttanesca"
    }
  }
}

Matching multiple fields
GET /recipe/_search
{
  "query": {
    "multi_match": {
      "query": "pasta",
      "fields": [ "title", "description" ]
    }
  }
}

```

## Adding boolean logic to search queries 
```xml

Adding boolean logic to a 'must' query 
GET /recipe/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "ingredients.name": "parmesan"
          }
        },
        {
          "range": {
            "preparation_time_minutes": {
              "lte": 15
            }
          }
        }
      ]
    }
  }
}
-> Everything under must array must match 

For faster performance move the range object to the filter object outside of the must object as mentioned below
as we do not have the concept of how well the prepartation time matches and it is not necessary to keep it inside the 'must' object
GET /recipe/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "ingredients.name": "parmesan"
          }
        }
      ],
      "filter": [
        {
          "range": {
            "preparation_time_minutes": {
              "lte": 15
            }
          }
        }
      ]
    }
  }
}
-> Here the match is the same no. of records but the relevane score changees

Must not match query:
GET /recipe/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "ingredients.name": "parmesan"
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "ingredients.name": "tuna"
          }
        }
      ],
      "filter": [
        {
          "range": {
            "preparation_time_minutes": {
              "lte": 15
            }
          }
        }
      ]
    }
  }
}

Should object boost the relevance score if they match but it is not compulsory that it must match 
GET /recipe/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "ingredients.name": "parmesan"
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "ingredients.name": "tuna"
          }
        }
      ],
      "should": [
        {
          "match": {
            "ingredients.name": "parsley"
          }
        }
      ],
      "filter": [
        {
          "range": {
            "preparation_time_minutes": {
              "lte": 15
            }
          }
        }
      ]
    }
  }
}

Named Boolean query for debugging
GET /recipe/_search
{
    "query": {
        "bool": {
          "must": [
            {
              "match": {
                "ingredients.name": {
                  "query": "parmesan",
                  "_name": "parmesan_must"
                }
              }
            }
          ],
          "must_not": [
            {
              "match": {
                "ingredients.name": {
                  "query": "tuna",
                  "_name": "tuna_must_not"
                }
              }
            }
          ],
          "should": [
            {
              "match": {
                "ingredients.name": {
                  "query": "parsley",
                  "_name": "parsley_should"
                }
              }
            }
          ],
          "filter": [
            {
              "range": {
                "preparation_time_minutes": {
                  "lte": 15,
                  "_name": "prep_time_filter"
                }
              }
            }
          ]
        }
    }
}
-> Here we will have an output that will also contain which part of the match query matched. 
Eg. 
"matched_queries" : [
  "prep_time_filter",
  "parmesan_must",
  "parsley_should"
] 

```

## Joining queries 
```xml
Elasticsearch supports simple joins but these queries are expensive and inefficient so always follow denormalization when ever necessary.

Create a new index named department with the following mapping
PUT /department
{
  "mappings": {  
    "properties": {
      "name": {
        "type": "text"
      },
      "employees": {
        "type": "nested"
      }
    }
  }
}

Put documents into this index 
PUT /department/_doc/1
{
  "name": "Development",
  "employees": [
    {
      "name": "Eric Green",
      "age": 39,
      "gender": "M",
      "position": "Big Data Specialist"
    },
    {
      "name": "James Taylor",
      "age": 27,
      "gender": "M",
      "position": "Software Developer"
    },
    {
      "name": "Gary Jenkins",
      "age": 21,
      "gender": "M",
      "position": "Intern"
    },
    {
      "name": "Julie Powell",
      "age": 26,
      "gender": "F",
      "position": "Intern"
    },
    {
      "name": "Benjamin Smith",
      "age": 46,
      "gender": "M",
      "position": "Senior Software Engineer"
    }
  ]
}
PUT /department/_doc/2
{
  "name": "HR & Marketing",
  "employees": [
    {
      "name": "Patricia Lewis",
      "age": 42,
      "gender": "F",
      "position": "Senior Marketing Manager"
    },
    {
      "name": "Maria Anderson",
      "age": 56,
      "gender": "F",
      "position": "Head of HR"
    },
    {
      "name": "Margaret Harris",
      "age": 19,
      "gender": "F",
      "position": "Intern"
    },
    {
      "name": "Ryan Nelson",
      "age": 31,
      "gender": "M",
      "position": "Marketing Manager"
    },
    {
      "name": "Kathy Williams",
      "age": 49,
      "gender": "F",
      "position": "Senior Marketing Manager"
    },
    {
      "name": "Jacqueline Hill",
      "age": 28,
      "gender": "F",
      "position": "Junior Marketing Manager"
    },
    {
      "name": "Donald Morris",
      "age": 39,
      "gender": "M",
      "position": "SEO Specialist"
    },
    {
      "name": "Evelyn Henderson",
      "age": 24,
      "gender": "F",
      "position": "Intern"
    },
    {
      "name": "Earl Moore",
      "age": 21,
      "gender": "M",
      "position": "Junior SEO Specialist"
    },
    {
      "name": "Phillip Sanchez",
      "age": 35,
      "gender": "M",
      "position": "SEM Specialist"
    }
  ]
}

Query the nested field:
GET /department/_search
{
  "query": {
    "nested": {
      "path": "employees",
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "employees.position": "intern"
              }
            },
            {
              "term": {
                "employees.gender.keyword": {
                  "value": "F"
                }
              }
            }
          ]
        }
      }
    }
  }
}
-> Here "nested" and "path" are how nested quries must be used 

The problem with above query is that it will bring all the employees that have the array with the matching values along with also the unmatched arrays. 
To filter this we have the concept of inner hits in Elasticsearch

Inner hits: -To fetch only the matching array values from within the document 
GET /department/_search
{
  "_source": false,
  "query": {
    "nested": {
      "path": "employees",
      "inner_hits": {},
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "employees.position": "intern"
              }
            },
            {
              "term": {
                "employees.gender.keyword": {
                  "value": "F"
                }
              }
            }
          ]
        }
      }
    }
  }
}

-> "_source" is set to false so that the source document is not fetched. 

Mapping document relationships
PUT /department/_mapping
{
  "properties": {
    "join_field": { 
      "type": "join",
      "relations": {
        "department": "employee"
      }
    }
  }
}

Adding documents - Adding departments 
PUT /department/_doc/1
{
  "name": "Development",
  "join_field": "department"
}
PUT /department/_doc/2
{
  "name": "Marketing",
  "join_field": "department"
}

Adding documents - Adding employees into each department
PUT /department/_doc/3?routing=1
{
  "name": "Bo Andersen",
  "age": 28,
  "gender": "M",
  "join_field": {
    "name": "employee",
    "parent": 1
  }
}
PUT /department/_doc/4?routing=2
{
  "name": "John Doe",
  "age": 44,
  "gender": "M",
  "join_field": {
    "name": "employee",
    "parent": 2
  }
}
PUT /department/_doc/5?routing=1
{
  "name": "James Evans",
  "age": 32,
  "gender": "M",
  "join_field": {
    "name": "employee",
    "parent": 1
  }
}
PUT /department/_doc/6?routing=1
{
  "name": "Daniel Harris",
  "age": 52,
  "gender": "M",
  "join_field": {
    "name": "employee",
    "parent": 1
  }
}
PUT /department/_doc/7?routing=2
{
  "name": "Jane Park",
  "age": 23,
  "gender": "F",
  "join_field": {
    "name": "employee",
    "parent": 2
  }
}
PUT /department/_doc/8?routing=1
{
  "name": "Christina Parker",
  "age": 29,
  "gender": "F",
  "join_field": {
    "name": "employee",
    "parent": 1
  }
}

Querying by parent 
GET /department/_search
{
  "query": {
    "parent_id": {
      "type": "employee",
      "id": 1
    }
  }
}


Querying child documents by parent : Matching child documents by parent criteria
GET /department/_search
{
  "query": {
    "has_parent": {
      "parent_type": "department",
      "query": {
        "term": {
          "name.keyword": "Development"
        }
      }
    }
  }
}

Querying child documents by parent : Incorporating the parent documents' relevance scores
GET /department/_search
{
  "query": {
    "has_parent": {
      "parent_type": "department",
      "score": true,
      "query": {
        "term": {
          "name.keyword": "Development"
        }
      }
    }
  }
}


Querying parent by child documents - Finding parents with child documents matching a bool query
GET /department/_search
{
  "query": {
    "has_child": {
      "type": "employee",
      "query": {
        "bool": {
          "must": [
            {
              "range": {
                "age": {
                  "gte": 50
                }
              }
            }
          ],
          "should": [
            {
              "term": {
                "gender.keyword": "M"
              }
            }
          ]
        }
      }
    }
  }
}

Querying parent by child documents - Taking relevance scores into account with score_mode
GET /department/_search
{
  "query": {
    "has_child": {
      "type": "employee",
      "score_mode": "sum",
      "query": {
        "bool": {
          "must": [
            {
              "range": {
                "age": {
                  "gte": 50
                }
              }
            }
          ],
          "should": [
            {
              "term": {
                "gender.keyword": "M"
              }
            }
          ]
        }
      }
    }
  }
}

Here "score_mode": has the following values:
sum - matching relevance score of all the children is summed up and mapped to the parent
avg - matching relevance score of all the children is averaged up and mapped to the parent 
min - matching minimum relevance score of all the children is found and mapped to the parent
max - matching naximum relevance score of all the children is found and mapped to the parent
none - default behaviour and no relevance score is considered 

Querying parent by child documents - Specifying the minimum and maximum number of children
GET /department/_search
{
  "query": {
    "has_child": {
      "type": "employee",
      "score_mode": "sum",
      "min_children": 1,
      "max_children": 5,
      "query": {
        "bool": {
          "must": [
            {
              "range": {
                "age": {
                  "gte": 50
                }
              }
            }
          ],
          "should": [
            {
              "term": {
                "gender.keyword": "M"
              }
            }
          ]
        }
      }
    }
  }
}

Multi-level relations
Creating the index with mapping
PUT /company
{
  "mappings": {
    "properties": {
      "join_field": { 
        "type": "join",
        "relations": {
          "company": ["department", "supplier"],
          "department": "employee"
        }
      }
    }
  }
}

Adding a company
PUT /company/_doc/1
{
  "name": "My Company Inc.",
  "join_field": "company"
}

Adding a department
PUT /company/_doc/2?routing=1
{
  "name": "Development",
  "join_field": {
    "name": "department",
    "parent": 1
  }
}

Adding an employee
PUT /company/_doc/3?routing=1
{
  "name": "Bo Andersen",
  "join_field": {
    "name": "employee",
    "parent": 2
  }
}

Adding some more test data
PUT /company/_doc/4
{
  "name": "Another Company, Inc.",
  "join_field": "company"
}
PUT /company/_doc/5?routing=4
{
  "name": "Marketing",
  "join_field": {
    "name": "department",
    "parent": 4
  }
}
PUT /company/_doc/6?routing=4
{
  "name": "John Doe",
  "join_field": {
    "name": "employee",
    "parent": 5
  }
}

Example of querying multi-level relations
GET /company/_search
{
  "query": {
    "has_child": {
      "type": "department",
      "query": {
        "has_child": {
          "type": "employee",
          "query": {
            "term": {
              "name.keyword": "John Doe"
            }
          }
        }
      }
    }
  }
}


Parent/child inner hits - Including inner hits for the has_child query
GET /department/_search
{
  "query": {
    "has_child": {
      "type": "employee",
      "inner_hits": {},
      "query": {
        "bool": {
          "must": [
            {
              "range": {
                "age": {
                  "gte": 50
                }
              }
            }
          ],
          "should": [
            {
              "term": {
                "gender.keyword": "M"
              }
            }
          ]
        }
      }
    }
  }
}

Parent/child inner hits - Including inner hits for the has_parent query
GET /department/_search
{
  "query": {
    "has_parent": {
      "inner_hits": {},
      "parent_type": "department",
      "query": {
        "term": {
          "name.keyword": "Development"
        }
      }
    }
  }
}

Terms lookup mechanism 
Adding the following test data first 
PUT /users/_doc/1
{
  "name": "John Roberts",
  "following" : [2, 3]
}
PUT /users/_doc/2
{
  "name": "Elizabeth Ross",
  "following" : []
}
PUT /users/_doc/3
{
  "name": "Jeremy Brooks",
  "following" : [1, 2]
}
PUT /users/_doc/4
{
  "name": "Diana Moore",
  "following" : [3, 1]
}
PUT /stories/_doc/1
{
  "user": 3,
  "content": "Wow look, a penguin!"
}
PUT /stories/_doc/2
{
  "user": 1,
  "content": "Just another day at the office... #coffee"
}
PUT /stories/_doc/3
{
  "user": 1,
  "content": "Making search great again! #elasticsearch #elk"
}
PUT /stories/_doc/4
{
  "user": 4,
  "content": "Had a blast today! #rollercoaster #amusementpark"
}
PUT /stories/_doc/5
{
  "user": 4,
  "content": "Yay, I just got hired as an Elasticsearch consultant - so excited!"
}
PUT /stories/_doc/6
{
  "user": 2,
  "content": "Chilling at the beach @ Greece #vacation #goodtimes"
}

Querying stories from a user's followers
GET /stories/_search
{
  "query": {
    "terms": {
      "user": {
        "index": "users",
        "id": "1",
        "path": "following"
      }
    }
  }
}

Limitation of joins 
-------------------
Join documents must be stored within the same index 
Parent and child documents must be on the same shard
There can be only one join field per index 
A join field can have as many relations as we want 
New relations can be added after creating an index 
Child relationships can be only added to existing parents 
A document can have only one parent but multiple children are possible 

```

## Controlling query results 
```xml
GET /products/_search?format=yaml 
-> this will diplay the output in yaml format

GET /products/_search?format=yaml
{
  "query": {
    "match": {
      "name": "Wine" 
    }
  }
}
-> this will display results for matching name = "wine"

GET /products/_search?pretty
{
  "query": {
    "match": {
      "name": "Wine" 
    }
  }
}
-> this will display result in a pretty format

Excluding the _source field altogether
GET /recipe/_search
{
  "_source": false,
  "query": {
    "match": { "title": "pasta" }
  }
}
-> Useful if we need to retrieve fields like id to be sent to another data source

Only returning the created field
GET /recipe/_search
{
  "_source": "created",
  "query": {
    "match": { "title": "pasta" }
  }
}
-> Displays only the created field from the source 

Only returning an object's key
GET /recipe/_search
{
  "_source": "ingredients.name",
  "query": {
    "match": { "title": "pasta" }
  }
}
-> will return the name propety of the ingredients field

Returning all of an object's keys
GET /recipe/_search
{
  "_source": "ingredients.*",
  "query": {
    "match": { "title": "pasta" }
  }
}
-> will return all the propeties of the ingredients field

Returning the ingredients object with all keys, and the servings field
GET /recipe/_search
{
  "_source": [ "ingredients.*", "servings" ],
  "query": {
    "match": { "title": "pasta" }
  }
}
-> will return all the properties of the ingredents field along with the servings field

Including all of the ingredients object's keys, except the name key
GET /recipe/_search
{
  "_source": {
    "includes": "ingredients.*",
    "excludes": "ingredients.name"
  },
  "query": {
    "match": { "title": "pasta" }
  }
}
-> Will return all the properties of the inludes field while exculding anything that is specified in the excludes field 


Specifying the result size (default result size is 10) - Using a query parameter
GET /recipe/_search?size=2
{
  "_source": false,
  "query": {
    "match": {
      "title": "pasta"
    }
  }
}


Specifying the result size (default result size is 10) - Using a parameter within the request body
GET /recipe/_search
{
  "_source": false,
  "size": 2,
  "query": {
    "match": {
      "title": "pasta"
    }
  }
}

Specifying an offset with the from parameter - Useful for pagination
GET /recipe/_search
{
  "_source": false,
  "size": 2,
  "from": 2,
  "query": {
    "match": {
      "title": "pasta"
    }
  }
}
-> This will start from offset 2 and display 2 documents

Pagination concept 
------------------
total_pages = ceil (total_hits/page_size)
from = page_size * (page_no-1)) 
https://www.elastic.co/guide/en/elasticsearch/reference/current/paginate-search-results.html


Sorting by ascending order (implicitly)
GET /recipe/_search
{
  "_source": false,
  "query": {
    "match_all": {}
  },
  "sort": [
    "preparation_time_minutes"
  ]
}
-> Default sort is ascending 

Sorting by descending order
GET /recipe/_search
{
  "_source": "created",
  "query": {
    "match_all": {}
  },
  "sort": [
    { "created": "desc" }
  ]
}

Sorting by multiple fields
GET /recipe/_search
{
  "_source": [ "preparation_time_minutes", "created" ],
  "query": {
    "match_all": {}
  },
  "sort": [
    { "preparation_time_minutes": "asc" },
    { "created": "desc" }
  ]
}

Sorting by multi-value fields - Sorting by the average rating (descending)
GET /recipe/_search
{
  "_source": "ratings",
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "ratings": {
        "order": "desc",
        "mode": "avg"
      }
    }
  ]
}

Adding a filter clause to the bool query
GET /recipe/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "pasta"
          }
        }
      ],
      "filter": [
        {
          "range": {
            "preparation_time_minutes": {
              "lte": 15
            }
          }
        }
      ]
    }
  }
}

```

## Aggregations
```xml
Data set for this section 
Adding order index and mappings
PUT /order
{
  "mappings": {
    "properties": {
      "purchased_at": {
        "type": "date"
      },
      "lines": {
        "type": "nested",
        "properties": {
          "product_id": {
            "type": "integer"
          },
          "amount": {
            "type": "double"
          },
          "quantity": {
            "type": "short"
          }
        }
      },
      "total_amount": {
        "type": "double"
      },
      "status": {
        "type": "keyword"
      },
      "sales_channel": {
        "type": "keyword"
      },
      "salesman": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer"
          },
          "name": {
            "type": "text"
          }
        }
      }
    }
  }
}

Populating the order index with test data
curl -H "Content-Type: application/json" -XPOST 'http://localhost:9200/order/_doc/_bulk?pretty' --data-binary "@orders-bulk.json"

Metric aggregations
Calculating statistics with sum, avg, min, and max aggregations
GET /order/_search
{
  "size": 0,
  "aggs": {
    "total_sales": {
      "sum": {
        "field": "total_amount"
      }
    },
    "avg_sale": {
      "avg": {
        "field": "total_amount"
      }
    },
    "min_sale": {
      "min": {
        "field": "total_amount"
      }
    },
    "max_sale": {
      "max": {
        "field": "total_amount"
      }
    }
  }
}

Retrieving the number of distinct values
GET /order/_search
{
  "size": 0,
  "aggs": {
    "total_salesmen": {
      "cardinality": {
        "field": "salesman.id"
      }
    }
  }
}

Retrieving the number of values
GET /order/_search
{
  "size": 0,
  "aggs": {
    "values_count": {
      "value_count": {
        "field": "total_amount"
      }
    }
  }
}

Using stats aggregation for common statistics
GET /order/_search
{
  "size": 0,
  "aggs": {
    "amount_stats": {
      "stats": {
        "field": "total_amount"
      }
    }
  }
}

```

## Springboot with Elasticsearch
```xml
Sample project - spring-data-jpa-sample

Also a good example can be found in the below url:
https://mkyong.com/spring-boot/spring-boot-spring-data-elasticsearch-example/

```

```xml
References:
https://github.com/okmich/rdbms_2_nosql/tree/master/elasticsearch
https://www.elastic.co/guide/en/elasticsearch/reference/current
https://reflectoring.io/spring-boot-elasticsearch/
https://www.elastic.co/blog/
https://reflectoring.io/spring-boot-elasticsearch/
https://mkyong.com/spring-boot/spring-boot-spring-data-elasticsearch-example/
https://github.com/codingexplained/complete-guide-to-elasticsearch
https://www.udemy.com/course/elasticsearch-complete-guide/
https://github.com/codingexplained/complete-guide-to-elasticsearch
```








