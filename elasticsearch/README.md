# ElasticSearch

## ELK - Elastic, Logstash, Kibana
### Elastic - Database engine built on top of Apache Lucene
### Logstash - Tool for data collection, transformation and transportation pipeline
### Kibana - Data visualization and analytics platform 

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
-> if size is not set to 0, then the first 10 results are displayed after which the final output for the 1000 records are displayed. 

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
-> This displays the total no. of distinct sales men based on their ids 

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
-> This displays the total number of values that the total_amount field has. 

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
-> This gives the statistics like count, max, min, avg & sum of the field total amount

Creating a bucket for each status value
GET /order/_search
{
  "size": 0,
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status"
      }
    }
  }
}
-> The status field value is grouped and is counted against each distinct value and displayed 

Including 20 terms instead of the default 10
GET /order/_search
{
  "size": 0,
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status",
        "size": 20
      }
    }
  }
}
-> Without the "size": 20 we will get 10 unique values if they are more than 10 values and all other will be grouped under the sum_other_doc_count parameter 

Aggregating documents with missing field (or NULL)
GET /order/_search
{
  "size": 0,
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status",
        "size": 20,
        "missing": "N/A"
      }
    }
  }
}
-> By adding "missing" parameter we can have the null values also grouped into one bucket 

Changing the minimum document count for a bucket to be created
GET /order/_search
{
  "size": 0,
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status",
        "size": 20,
        "missing": "N/A",
        "min_doc_count": 200
      }
    }
  }
}
-> min_doc_count will specify the minimum no. of documents that need to be present in result to be considered for the count, default value is 0, 
this is like a having clause in SQL 

Ordering the buckets
GET /order/_search
{
  "size": 0,
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status",
        "size": 20,
        "missing": "N/A",
        "min_doc_count": 0,
        "order": {
          "_key": "asc"
        }
      }
    }
  }
}
-> This is like the order by clause in SQL. If you need to order by value instead of the key then use "_count": "asc"

Note: Please note that terms query document aggregation counts are approximate and not accurate (because of the distruted nature of the shards)


Nested aggregations - Retrieving statistics for each status
GET /order/_search
{
  "size": 0,
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status"
      },
      "aggs": {
        "status_stats": {
          "stats": {
            "field": "total_amount"
          }
        }
      }
    }
  }
}
-> this will display the count of aggration of status field along with the stats(min, max ett) of total_amount field within this aggregation 

Nested aggregations - Narrowing down the aggregation context
GET /order/_search
{
  "size": 0,
  "query": {
    "range": {
      "total_amount": {
        "gte": 100
      }
    }
  },
  "aggs": {
    "status_terms": {
      "terms": {
        "field": "status"
      },
      "aggs": {
        "status_stats": {
          "stats": {
            "field": "total_amount"
          }
        }
      }
    }
  }
}
-> This will narrow down by filtering and including only documents having total_amount greater than 100 

Filtering out documents - Filtering out documents with low total_amount
GET /order/_search
{
  "size": 0,
  "aggs": {
    "low_value": {
      "filter": {
        "range": {
          "total_amount": {
            "lt": 50
          }
        }
      }
    }
  }
}
-> This is an example of running a filter before applying any aggregation 

Filtering out documents - Aggregating on the bucket of remaining documents
GET /order/_search
{
  "size": 0,
  "aggs": {
    "low_value": {
      "filter": {
        "range": {
          "total_amount": {
            "lt": 50
          }
        }
      },
      "aggs": {
        "avg_amount": {
          "avg": {
            "field": "total_amount"
          }
        }
      }
    }
  }
}
-> From the previous query we apply aggration 'avg' to just the filtered results 

Defining bucket rules with filters - Placing documents into buckets based on criteria
GET /recipe/_search
{
  "size": 0,
  "aggs": {
    "my_filter": {
      "filters": {
        "filters": {
          "pasta": {
            "match": {
              "title": "pasta"
            }
          },
          "spaghetti": {
            "match": {
              "title": "spaghetti"
            }
          }
        }
      }
    }
  }
}
-> This will filter documents into bucket by matching fiter criteria that we set 

Defining bucket rules with filters - Calculate average ratings for buckets
GET /recipe/_search
{
  "size": 0,
  "aggs": {
    "my_filter": {
      "filters": {
        "filters": {
          "pasta": {
            "match": {
              "title": "pasta"
            }
          },
          "spaghetti": {
            "match": {
              "title": "spaghetti"
            }
          }
        }
      },
      "aggs": {
        "avg_rating": {
          "avg": {
            "field": "ratings"
          }
        }
      }
    }
  }
}
-> This will aggregate the results from the previous filter criteria that we set 

Range aggregation
GET /order/_search
{
  "size": 0,
  "aggs": {
    "amount_distribution": {
      "range": {
        "field": "total_amount",
        "ranges": [
          {
            "to": 50
          },
          {
            "from": 50,
            "to": 100
          },
          {
            "from": 100
          }
        ]
      }
    }
  }
}
-> This will create buckets of range that has been specified 

Date_range aggregation
GET /order/_search
{
  "size": 0,
  "aggs": {
    "purchased_ranges": {
      "date_range": {
        "field": "purchased_at",
        "ranges": [
          {
            "from": "2016-01-01",
            "to": "2016-01-01||+6M"
          },
          {
            "from": "2016-01-01||+6M",
            "to": "2016-01-01||+1y"
          }
        ]
      }
    }
  }
}
-> Date range aggregation is same as before but applied to date fields. This is || (pipe) +6M (plus 6 months)

Specifying the date format
GET /order/_search
{
  "size": 0,
  "aggs": {
    "purchased_ranges": {
      "date_range": {
        "field": "purchased_at",
        "format": "yyyy-MM-dd",
        "ranges": [
          {
            "from": "2016-01-01",
            "to": "2016-01-01||+6M"
          },
          {
            "from": "2016-01-01||+6M",
            "to": "2016-01-01||+1y"
          }
        ]
      }
    }
  }
}
-> This specifies the date format to display and this should match the format that we use in the from and to ranges

Enabling keys for the buckets
GET /order/_search
{
  "size": 0,
  "aggs": {
    "purchased_ranges": {
      "date_range": {
        "field": "purchased_at",
        "format": "yyyy-MM-dd",
        "keyed": true,
        "ranges": [
          {
            "from": "2016-01-01",
            "to": "2016-01-01||+6M"
          },
          {
            "from": "2016-01-01||+6M",
            "to": "2016-01-01||+1y"
          }
        ]
      }
    }
  }
}
-> This will enable the date range as the key for each bucket which makes the display better

Defining the bucket keys
GET /order/_search
{
  "size": 0,
  "aggs": {
    "purchased_ranges": {
      "date_range": {
        "field": "purchased_at",
        "format": "yyyy-MM-dd",
        "keyed": true,
        "ranges": [
          {
            "from": "2016-01-01",
            "to": "2016-01-01||+6M",
            "key": "first_half"
          },
          {
            "from": "2016-01-01||+6M",
            "to": "2016-01-01||+1y",
            "key": "second_half"
          }
        ]
      }
    }
  }
}
-> Ths defines our own custom keys for each of the bucket 

Adding a sub-aggregation
GET /order/_search
{
  "size": 0,
  "aggs": {
    "purchased_ranges": {
      "date_range": {
        "field": "purchased_at",
        "format": "yyyy-MM-dd",
        "keyed": true,
        "ranges": [
          {
            "from": "2016-01-01",
            "to": "2016-01-01||+6M",
            "key": "first_half"
          },
          {
            "from": "2016-01-01||+6M",
            "to": "2016-01-01||+1y",
            "key": "second_half"
          }
        ]
      },
      "aggs": {
        "bucket_stats": {
          "stats": {
            "field": "total_amount"
          }
        }
      }
    }
  }
}
-> This adds aggeration to each of the bucket based on the field specified 

Histograms - Distribution of total_amount with interval 25
GET /order/_search
{
  "size": 0,
  "aggs": {
    "amount_distribution": {
      "histogram": {
        "field": "total_amount",
        "interval": 25
      }
    }
  }
}
-> This will automatically create buckets in a specified interval on a specified numeric field, in our case the total_amount

Requiring minimum 1 document per bucket
GET /order/_search
{
  "size": 0,
  "aggs": {
    "amount_distribution": {
      "histogram": {
        "field": "total_amount",
        "interval": 25,
        "min_doc_count": 1
      }
    }
  }
}
-> This will make sure that each bucket has a minimum of at least 1 document. Empty buckets will not be displayed in the histogram results 

Specifying fixed bucket boundaries
GET /order/_search
{
  "size": 0,
  "query": {
    "range": {
      "total_amount": {
        "gte": 100
      }
    }
  },
  "aggs": {
    "amount_distribution": {
      "histogram": {
        "field": "total_amount",
        "interval": 25,
        "min_doc_count": 0,
        "extended_bounds": {
          "min": 0,
          "max": 500
        }
      }
    }
  }
}
-> Since the range query has already filted the values below 100 and the maximum value of total_amount is between 275 to 300 bucket 
but by using the extended_bounds we force display ranges between 0 to 500 with intervals of 25 but the count for these extended ranges will be zero.  

Aggregating by month with the date_histogram aggregation
GET /order/_search
{
  "size": 0,
  "aggs": {
    "orders_over_time": {
      "date_histogram": {
        "field": "purchased_at",
        "calendar_interval": "month"
      }
    }
  }
}
-> This histogram query will aggeregate the date field values by each month. Other parameters like year, day etc can also be used 

Aggregating by month and within it by day with the date_histogram aggregation
GET /order/_search
{
  "size": 0,
  "aggs": {
    "orders_over_month": {
      "date_histogram": {
        "field": "purchased_at",
        "calendar_interval": "month"
      },
      "aggs": {
        "orders_over_day": {
            "date_histogram": {
              "field": "purchased_at",
              "calendar_interval": "day"
          }
        }
      }
    }
  }
} 
-> This will aggregate by month and within each month by day

Global aggregation - Break out of the aggregation context
GET /order/_search
{
  "query": {
    "range": {
      "total_amount": {
        "gte": 100
      }
    }
  },
  "size": 0,
  "aggs": {
    "all_orders": {
      "global": { },
      "aggs": {
        "stats_amount": {
          "stats": {
            "field": "total_amount"
          }
        }
      }
    }
  }
}
-> By specifing global it will berak out of the query range filter and aggregate all documents of the index 

Global aggregation - Adding aggregation without global context
GET /order/_search
{
  "query": {
    "range": {
      "total_amount": {
        "gte": 100
      }
    }
  },
  "size": 0,
  "aggs": {
    "all_orders": {
      "global": { },
      "aggs": {
        "stats_amount": {
          "stats": {
            "field": "total_amount"
          }
        }
      }
    },
    "stats_expensive": {
      "stats": {
        "field": "total_amount"
      }
    }
  }
}
-> By using stats we will once again break out of the global context and consider only the range query results 

Missing field values - Adding test documents
POST /order/_doc/1001
{
  "total_amount": 100
}
POST /order/_doc/1002
{
  "total_amount": 200,
  "status": null
}

Missing field values - Aggregating documents with missing field value
GET /order/_doc/_search
{
  "size": 0,
  "aggs": {
    "orders_without_status": {
      "missing": {
        "field": "status"
      }
    }
  }
}
-> This will aggreate the document with missing field and display its count 

Missing field values - Combining missing aggregation with other aggregations
GET /order/_doc/_search
{
  "size": 0,
  "aggs": {
    "orders_without_status": {
      "missing": {
        "field": "status"
      },
      "aggs": {
        "missing_sum": {
          "sum": {
            "field": "total_amount"
          }
        }
      }
    }
  }
}
-> This will find the sum of the total_amount for the missing field status 

Missing field values - Deleting test documents
DELETE /order/_doc/1001
DELETE /order/_doc/1002

Aggregating nested objects
GET /department/_search
{
  "size": 0,
  "aggs": {
    "employees": {
      "nested": {
        "path": "employees"
      }
    }
  }
}
-> This is a general aggregation query for nested objects where the path is important 

GET /department/_search
{
  "size": 0,
  "aggs": {
    "employees": {
      "nested": {
        "path": "employees"
      },
      "aggs": {
        "minimum_age": {
          "min": {
            "field": "employees.age"
          }
        }
      }
    }
  }
}
-> This is a sub aggregation to  the employee object 

```


## Improving the search results 
```xml
Proximity searches - Adding test documents
PUT /proximity/_doc/1
{
  "title": "Spicy Sauce"
}
PUT /proximity/_doc/2
{
  "title": "Spicy Tomato Sauce"
}
PUT /proximity/_doc/3
{
  "title": "Spicy Tomato and Garlic Sauce"
}
PUT /proximity/_doc/4
{
  "title": "Tomato Sauce (spicy)"
}
PUT /proximity/_doc/5
{
  "title": "Spicy and very delicious Tomato Sauce"
}


Proximity searches - Adding the slop parameter to a match_phrase query
GET /proximity/_search
{
  "query": {
    "match_phrase": {
      "title": {
        "query": "spicy sauce",
        "slop": 1
      }
    }
  }
}
-> the slop field will help us determine how far the two search terms can be sepearted from each other. Value 1 means by 1 word between them 

GET /proximity/_search
{
  "query": {
    "match_phrase": {
      "title": {
        "query": "spicy sauce",
        "slop": 2
      }
    }
  }
}
-> Value 2 in slop field means that these two search terms can be 2 words apart (edit distance by moving the terms around)


Affecting relevance scoring with proximity
------------------------------------------
A simple match query within a bool query
GET /proximity/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": {
              "query": "spicy sauce"
            }
          }
        }
      ]
    }
  }
}
-> This ia general match query that will fetch all matches of spicy and sauce terms 

Boosting relevance based on proximity
GET /proximity/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": {
              "query": "spicy sauce"
            }
          }
        }
      ],
      "should": [
        {
          "match_phrase": {
            "title": {
              "query": "spicy sauce"
            }
          }
        }
      ]
    }
  }
}
-> This will boost the relavance score for best maching phrases 

Adding the slop parameter
GET /proximity/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": {
              "query": "spicy sauce"
            }
          }
        }
      ],
      "should": [
        {
          "match_phrase": {
            "title": {
              "query": "spicy sauce",
              "slop": 5
            }
          }
        }
      ]
    }
  }
}
-> We can also add a slop field to the match_phrase which will futher boost the relevance score for the lesser matching phrases 

Fuzzy match query
-----------------
Searching with fuzziness set to auto
GET /products/_search
{
  "query": {
    "match": {
      "name": {
        "query": "l0bster",
        "fuzziness": "auto"
      }
    }
  }
}
-> Typos can be matched by adding "fuzziness" to the search query 

GET /products/_search
{
  "query": {
    "match": {
      "name": {
        "query": "lobster",
        "fuzziness": "auto"
      }
    }
  }
}
-> Even terms like oyster will be matched here 

Fuzziness is per term (and specifying an integer)
GET /products/_search
{
  "query": {
    "match": {
      "name": {
        "query": "l0bster love",
        "operator": "and",
        "fuzziness": 1
      }
    }
  }
}
-> Here edit distance is just 1 and applied per term and so 'lobster live' will be matached

Switching letters around with transpositions
GET /products/_search
{
  "query": {
    "match": {
      "name": {
        "query": "lvie",
        "fuzziness": 1
      }
    }
  }
}
-> Transpositions (where AB can be switched as BA) is also matached as edit distance of 1 and so 'lvie' is matched for 'live'

Disabling transpositions
GET /products/_search
{
  "query": {
    "match": {
      "name": {
        "query": "lvie",
        "fuzziness": 1,
        "fuzzy_transpositions": false
      }
    }
  }
}
-> "fuzzy_transpositions": false will disable transpositions 

Fuzzy query
GET /products/_search
{
  "query": {
    "fuzzy": {
      "name": {
        "value": "LOBSTER",
        "fuzziness": "auto"
      }
    }
  }
}
-> fuzzy query will not match LOBSTER since it is not equal to a match query.
A fuzzy query is a term level query and it will not match as all terms are stored in lower case. 

GET /products/_search
{
  "query": {
    "fuzzy": {
      "name": {
        "value": "lobster",
        "fuzziness": "auto"
      }
    }
  }
}
-> Here the fuzzy query will act like the previous match query because we used lower case in our term level search 
Best practise is to avoid fuzzy query and always use match query with fuziness parameter. 


Adding synonyms
---------------
Creating index with custom analyzer
PUT /synonyms
{
  "settings": {
    "analysis": {
      "filter": {
        "synonym_test": {
          "type": "synonym", 
          "synonyms": [
            "awful => terrible",
            "awesome => great, super",
            "elasticsearch, logstash, kibana => elk",
            "weird, strange"
          ]
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "synonym_test"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "description": {
        "type": "text",
        "analyzer": "my_analyzer"
      }
    }
  }
}

Testing the analyzer (with synonyms)
POST /synonyms/_analyze
{
  "analyzer": "my_analyzer",
  "text": "awesome"
}

POST /synonyms/_analyze
{
  "analyzer": "my_analyzer",
  "text": "Elasticsearch"
}

POST /synonyms/_analyze
{
  "analyzer": "my_analyzer",
  "text": "weird"
}

POST /synonyms/_analyze
{
  "analyzer": "my_analyzer",
  "text": "Elasticsearch is awesome, but can also seem weird sometimes."
}

Adding a test document
POST /synonyms/_doc
{
  "description": "Elasticsearch is awesome, but can also seem weird sometimes."
}

Searching the index for synonyms
GET /synonyms/_search
{
  "query": {
    "match": {
      "description": "great"
    }
  }
}
-> Term awesome is replaced with great and hence this query will be matched

GET /synonyms/_search
{
  "query": {
    "match": {
      "description": "awesome"
    }
  }
}
-> This query will still match the document as this will be converted into an inverted index of great before sending it for matching with terms

Adding synonyms from file
-------------------------
Copy the file synonyms.txt into the config folder elastic search under the analysis directory. 

Adding index with custom analyzer
PUT /synonyms
{
  "settings": {
    "analysis": {
      "filter": {
        "synonym_test": {
          "type": "synonym",
          "synonyms_path": "analysis/synonyms.txt"
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "synonym_test"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "description": {
        "type": "text",
        "analyzer": "my_analyzer"
      }
    }
  }
}


POST /synonyms/_analyze
{
  "analyzer": "my_analyzer",
  "text": "Elasticsearch"
}
-> Since we used our custom analyser the Elasticsearch is replaced by the term elk 

Highlighting matches in fields
------------------------------
Adding a test document
PUT /highlighting/_doc/1
{
  "description": "Let me tell you a story about Elasticsearch. It's a full-text search engine that is built on Apache Lucene. It's really easy to use, but also packs lots of advanced features that you can use to tweak its searching capabilities. Lots of well-known and established companies use Elasticsearch, and so should you!"
}

Highlighting matches within the description field
GET /highlighting/_search
{
  "_source": false,
  "query": {
    "match": { "description": "Elasticsearch story" }
  },
  "highlight": {
    "fields": {
      "description" : {}
    }
  }
}
-> Here the matching text is highlighed within the <em> tag in the result 

Specifying a custom tag
GET /highlighting/_search
{
  "_source": false,
  "query": {
    "match": { "description": "Elasticsearch story" }
  },
  "highlight": {
    "pre_tags": [ "<strong>" ],
    "post_tags": [ "</strong>" ],
    "fields": {
      "description" : {}
    }
  }
}
-> This will match the text with our own custom tag as highligher 

Stemming
--------
Creating a test index with custom analyser 
PUT /stemming_test
{
  "settings": {
    "analysis": {
      "filter": {
        "synonym_test": {
          "type": "synonym",
          "synonyms": [
            "firm => company",
            "love, enjoy"
          ]
        },
        "stemmer_test" : {
          "type" : "stemmer",
          "name" : "english"
        }
      },
      "analyzer": {
        "my_analyzer": {
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "synonym_test",
            "stemmer_test"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "description": {
        "type": "text",
        "analyzer": "my_analyzer"
      }
    }
  }
}

Adding a test document
PUT /stemming_test/_doc/1
{
  "description": "I love working for my firm!"
}


Matching the document with the base word (work)
GET /stemming_test/_search
{
  "query": {
    "match": {
      "description": "enjoy work"
    }
  }
}
-> this will matach our source document because of synonymn and stemming

The query is stemmed, so the document still matches
GET /stemming_test/_search
{
  "query": {
    "match": {
      "description": "love working"
    }
  }
}
-> this will matach our source document because of synonymn and stemming 

Synonyms and stemmed words are still highlighted
GET /stemming_test/_search
{
  "query": {
    "match": {
      "description": "enjoy work"
    }
  },
  "highlight": {
    "fields": {
      "description": {}
    }
  }
}
-> Highlighting the match even incase of stemming/synonyms

```

## Download and Import data into Elasticsearch 
```xml
curl -H "Content-Type: application/x-ndjson" -XPOST http://localhost:9200/movies/_bulk --data-binary "@movies.json"

curl http://media.sundog-soft.com/es7/shakes-mapping.json -o shakes-mapping.json -> This downloads the data 
curl -H "Content-Type: application/json" -XPUT 127.0.0.1:9200/shakespeare --data-binary @shakes-mapping.json -> This imports the mapping to elasticsearch 


curl http://media.sundog-soft.com/es7/shakespeare_7.0.json -o shakespeare_7.0.json -> This downloads the data 
curl -H "Content-Type: application/json" -XPOST 127.0.0.1:9200/shakespeare/_bulk --data-binary @shakespeare_7.0.json -> This imports the data to elasticsearch 
```


## Kibana
```xml
Download and unzip Kibana from https://www.elastic.co/downloads/kibana or https://www.elastic.co/start
Move it to a folder of your choice

Start Kibana from the root installation folder of kibana by executing the following command:
bin/kibana

This will auto connect to Elasticsearch provide it started in the default port of 9200

Now use the browser to connect to Kibana using the url http://localhost:5601/


To create a simple dashboard, do the following: 
1. Menu -> Management -> Stack Management -> Kibana -> Index Patterns -> Create Index Patterns -> <Enter any index name that you need> -> Create Index Pattern 
2. All the fields of the matched index will be displayed.
3. Next go to Discover -> <Select your index from drop down> -> You can fiter, search and vizualize the date from here 


Kibana Lens -> Create index patterns and associate them with charts dynamically. 

Kibana Management
-----------------
Spaces -> Create spaces for users and customize features that needs to be given to each space. 
Saved Objects -> Standard dashboards can be imported or exported.
Advance Settings -> All global settings for the given space can be set here

Kibana Canvas
-------------
This is a live visualization tool for our elasticsearch data -> Useful for creating infographics. 

Check here:
http://media.sundog-soft.com/es/canvas.txt


```

## Metricbeat 
```xml
Download and install metricbeat from https://www.elastic.co/downloads/beats/metricbeat
Move it to a folder of your choice

Start Metricbeat from the root installation folder of metricbeat by executing the following command:
./metricbeat 

This will collect the default system related metrics and send it out to elasticsearch 

If we need to collect other application related metrics then we need to enable them in metricbeat. 
List of available modules can be found by running the command: 
./metricbeat modules list

Combine this with Kibana to get powerful metrics on visualization and dashboards. 

Refer to the various system metrics avaiable in metricbeat from the following url:
https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-module-system.html

Eg. 
system.filesystem.free = available free space
system.cpu.idle.pct = % of CPU that is idle 
system.memory.used.pct = % of memory used 
```

## Logstash
```xml

Install logstash
----------------
Download and install logstash from https://www.elastic.co/downloads/logstash

Start Logstash from the folder by running the following sample pipeline:
./bin/logstash -e 'input { stdin { } } output { stdout {} }'

```

### Send logfiles to Elasticsearch
```xml
Lets download a sample access log file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/access_log -o access_log  -> This will download the file called access_log into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-accesslog.conf)
input {
  file {
    path => "<fullpath>/access_log"
    start_position => "beginning"
  }
}

filter {
  grok {
    match => {"message" => "%{COMBINEDAPACHELOG}"}
  }
  date {
    match => ["timestamp", "dd/MMM/yyyy:HH:mm:ss Z"]
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
  }
  stdout {
    codec => rubydebug 
  }
}

Now from the downloaded logstash folder run the following command (assuming that logstash-accesslog.conf file is in the config folder)
bin/logstash -f config/logstash-accesslog.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index (will vary depending on the current date of the system): 
GET /logstash-2021.11.25-000001/_search

```

### Send Database table to Elasticsearch
```xml
Lets try to connect to a database table and extract data and move it to elasticsearch 
In my case the database is test and the table name is employee

Download the mysql jdbc driver from the url https://dev.mysql.com/downloads/connector/j/
In my case the driver is mysql-connector-java-8.0.27.zip. 
Extract and place the mysql-connector-java-8.0.27.jar in a convenient location. 

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-mysql.conf)
input {
  jdbc {
    jdbc_connection_string => "jdbc:mysql://localhost:3306/test"
    jdbc_user => "<userid>"
    jdbc_password => "<password>"
    jdbc_driver_library => "/<full_path_to_driver>/mysql-connector-java-8.0.27.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    statement => "SELECT * FROM employee;"
  }
}


output {
  elasticsearch {
    hosts => ["localhost:9200"]
    index => "employee-sql"
  }
  stdout {
    codec => json_lines 
  }
}

Now from the downloaded logstash folder run the following command (assuming that logstash-mysql.conf file is in the config folder)
bin/logstash -f config/logstash-mysql.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /employee-sql/_search

```

### Send CSV file data to Elasticsearch
```xml 
Lets download a sample csv file that we will use to import into elasticsearch using logstash 
https://raw.githubusercontent.com/coralogix-resources/elk-course-samples/master/csv-schema-short-numerical.csv -o csv-schema-short-numerical.csv 
-> This will download the file called csv-schema-short-numerical.csv into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-csv.conf)
input {
  file {
    path => "<fullpath>/csv-schema-short-numerical.csv"
    start_position => "beginning"
    sincedb_path => "/tmp/null" 
  }
}
filter {
  csv {
      separator => ","
      skip_header => "true"
      columns => ["id","timestamp","paymentType","name","gender","ip_address","purpose","country","age"]
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "csv-read-demo"
  }
stdout {}

}

Now from the downloaded logstash folder run the following command (assuming that logstash-csv.conf file is in the config folder)
bin/logstash -f config/logstash-csv.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /csv-read-demo/_search

```

### Send CSV file data to Elasticsearch with modification in the data 
```xml 
Lets download a sample csv file that we will use to import into elasticsearch using logstash 
https://raw.githubusercontent.com/coralogix-resources/elk-course-samples/master/csv-schema-short-numerical.csv -o csv-schema-short-numerical.csv 
-> This will download the file called csv-schema-short-numerical.csv into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-csv-mod.conf)
input {
  file {
    path => "<filepath>/csv-schema-short-numerical.csv"
    start_position => "beginning"
    sincedb_path => "/tmp/null"
  }
}
filter {
  csv {
      separator => ","
      skip_header => "true"
      columns => ["id","timestamp","paymentType","name","gender","ip_address","purpose","country","age"]
  }
  mutate {
      convert => {
          age => "integer"
      }
          remove_field => ["message","@timestamp","path","host","@version"]
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "csv-read-mod"
  }

stdout {}

}

Now from the downloaded logstash folder run the following command (assuming that logstash-csv-mod.conf file is in the config folder)
bin/logstash -f config/logstash-csv-mod.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /csv-read-mod/_search

```

### Send Json data to Elasticsearch 
```xml
Lets download a sample json file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/sample-json.log -o sample-json.log
-> This will download the file called sample-json.log into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-json.conf)
input {
  file {
    path => "<full file path>/sample-json.log"
    start_position => "beginning"
    sincedb_path => "/tmp/null"
  }
}
filter {
  json {
    source => "message"
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "json-demo"
  }
  stdout {}

}

Now from the downloaded logstash folder run the following command (assuming that logstash-json.conf file is in the config folder)
bin/logstash -f config/logstash-json.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /json-demo/_search

```

### Send modified Json data to Elasticsearch 
```xml
Lets download a sample json file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/sample-json.log -o sample-json.log
-> This will download the file called sample-json.log into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-json-mod.conf)
input {
  file {
    path => "<full file path>/sample-json.log"
    start_position => "beginning"
    sincedb_clean_after => 0
    sincedb_path => "/tmp/null"
  }
}
filter {
  json {
    source => "message"
  }
  if [paymentType] == "Mastercard" {
    drop {}
  }
  mutate {
      remove_field => ["message","@timestamp","path","host","@version"]
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "json-mod-demo"
  }
  stdout {}

}

Now from the downloaded logstash folder run the following command (assuming that logstash-json-mod.conf file is in the config folder)
bin/logstash -f config/logstash-json-mod.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /json-demo-mod/_search

```

### Split Json data and send to Elasticsearch 
```xml
Lets download a sample json file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/sample-json-split.log -o sample-json-spilt.log
-> This will download the file called sample-json-split.log into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-json-split.conf)
input {
  file {
    #type => "json"
    path => "<full file path>/sample-json-split.log"
    start_position => "beginning"
    sincedb_path => "/tmp/null"
  }
}
filter {
  json {
    source => "message"
  }
  split {
    field => "[pastEvents]"
  }
  mutate {
      remove_field => ["message","@timestamp","path","host","@version"]
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "json-split-demo"
  }
  stdout {}

}

Now from the downloaded logstash folder run the following command (assuming that logstash-json-split.conf file is in the config folder)
bin/logstash -f config/logstash-json-split.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /json-split-demo/_search

```

### Split Json data, structure it and send to Elasticsearch 
```xml
Lets download a sample json file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/sample-json-split.log -o sample-json-spilt.log
-> This will download the file called sample-json-split.log into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-json-split-structured.conf)
input {
  file {
    #type => "json"
    path => "<full file path>/sample-json-split.log"
    start_position => "beginning"
    sincedb_clean_after => 0
    sincedb_path => "/tmp/null"
  }
}
filter {
  json {
    source => "message"
  }
  split {
    field => "[pastEvents]"
  }
  mutate {
      add_field => {
        "eventId" => "%{[pastEvents][eventId]}"
        "transactionId" => "%{[pastEvents][transactionId]}"
      }
      remove_field => ["message","@timestamp","path","host","@version","pastEvents"]
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "json-split-structured-demo"
  }
  stdout {}

}

Now from the downloaded logstash folder run the following command (assuming that logstash-json-split-structured.conf file is in the config folder)
bin/logstash -f config/logstash-json-splits-structured.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /json-split-structured-demo/_search

```

### Send files from Amazon S3 bucket to Elasticsearch
```xml
For this I have created an S3 bucket named logstash-storage and given it public access

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-s3.conf)
input {
  s3 {
    bucket => "logstash-storage"
    access_key_id => "<access-key>"
    secret_access_key => "<secret-key>"
  }
}

filter {
  grok {
    match => {"message" => "%{COMBINEDAPACHELOG}"}
  }
  date {
    match => ["timestamp", "dd/MMM/yyyy:HH:mm:ss Z"]
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "s3-logs"
  }
  stdout {
    codec => rubydebug 
  }
}

Run the following command (assuming that logstash-s3.conf file is in the config folder)
bin/logstash -f config/logstash-s3.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /s3-logs/_search

```

### Using grok filter and send data to Elasticsearch
```xml
Lets download a sample file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/sample.log -o sample.log
-> This will download the file called sample.log into your local folder. 
or I have provided the file under the logstash-config directory

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-grok.conf)
input {
  file {
    path => "<full file path>/sample.log"
    start_position => "beginning"
    sincedb_path => "/tmp/null"
  }
}
filter {
  grok {
    match => {"message" => ['%{TIMESTAMP_ISO8601:time} %{LOGLEVEL:logLevel} %{GREEDYDATA:logMessage}'] }
  }
}
output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "grok-demo"
  }
  stdout {}
}

Now from the downloaded logstash folder run the following command (assuming that logstash-grok.conf file is in the config folder)
bin/logstash -f config/logstash-grok.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /grok-demo/_search

```

### Using grok filter for multiformat log file and send data to Elasticsearch
```xml
Lets download a sample file that we will use to import into elasticsearch using logstash 
curl http://media.sundog-soft.com/es/sample.log -o sample.log
-> This will download the file called sample.log into your local folder. 
or I have provided the file under the logstash-config directory.

Add additional line to this file as below and also rename it as sample-multiformat.log
192.168.0.1 GET /usr/id/properties

Now configure a logstash configuration file as follows: (file is given under the logstash folder - logstash-grok-multiformat.conf)
input {
  file {
    path => "<full file path>/sample-multiformat.log"
    start_position => "beginning"
    sincedb_path => "/tmp/null"
  }
}

filter {
  grok {
    match => {"message" => [
        '%{TIMESTAMP_ISO8601:time} %{LOGLEVEL:logLevel} %{GREEDYDATA:logMessage}',
        '%{IP:clientIP} %{WORD:httpMethod} %{URIPATH:url}'
     ] }
  }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "grok-mutliformat-demo"
  }
  stdout {}
}

Now from the downloaded logstash folder run the following command (assuming that logstash-grok-multiformat.conf file is in the config folder)
bin/logstash -f config/logstash-grok-multiformat.conf

You will be able to see the data being populated into elasticsearch. In my case it was pouplated to the following index: 
GET /grok-mutliformat-demo/_search

```

### Reading standard common log files and send them to Elasticsearch
```xml
(see previous sections on how to run samples from the below table)
----------------------------------------------------------------------------------------------------------------------------------------------------------------------
| File format          | Log File Name        | Configuration File Name | Run Command                                         | View Index                           |
----------------------------------------------------------------------------------------------------------------------------------------------------------------------
| Nginx log files      | nginx-access.log     | logstash-nginx.conf     | bin/logstash -f config/logstash-nginx.conf          | GET /nginx-access-logs/_search       |
| IIS log files        | iis-sample.log       | logstash-iis.conf       | bin/logstash -f config/logstash-iis.conf            | GET /iis-log/_search                 |
| MongoDB log files    | mongodb.log          | logstash-mongodb.comf   | bin/logstash -f config/logstash-mongodb.conf        | GET /mongo-logs/_search              |
| Apache log files     | access_log           | logstash-apache.conf    | bin/logstash -f config/logstash-apache.conf         | GET /apache-log/_search              |
| Elastic log files    | elasticsearch.log    | logstash-es.conf        | bin/logstash -f config/logstash-es.conf             | GET /elasticsearch-logs/_search      |
| Elastic slow logs    | es_slowlog.log       | logstash-es-sl.conf     | bin/logstash -f config/logstash-es-sl.conf          | GET /elastic-slow-logs/_search       | 
| MySQL slow logs      | mysql-slow.log       | logstash-mysql-sl.conf  | bin/logstash -f config/logstash-mysql-sl.conf       | GET /mysql-slowlogs/_search          | 
| AWS Elastic LB logs  | elb_logs.log         | logstash-aws-elb.conf   | bin/logstash -f config/logstash-aws-elb.conf        | GET /aws-elb-logs/_search            | 
| AWS App LB logs      | alb_logs.log         | logstash-aws-alb.conf   | bin/logstash -f config/logstash-aws-alb.conf        | GET /aws-alb-logs/_search            | 
| AWS CloudFront logs  | cloudfront_logs.log  | logstash-aws-cf.conf    | bin/logstash -f config/logstash-aws-cf.conf         | GET /aws-cloudfront-logs/_search     |
----------------------------------------------------------------------------------------------------------------------------------------------------------------------

```

### Input plugins for Logstash
```xml
Heartbeat plugin (useful for health checks to periodically check on something) 
----------------
Create a config called heartbeat.conf (contents below) and run is using the comnmad bin/logstash -f config/heartbeat.conf.
It runs every 5 seconds and sends the output to elasticsearch. message can be "sequence" or "epoch"
input {
  heartbeat {
    message => "sequence"
    #message => "epoch"
    interval => 5
    type => "heartbeat"
  }
}

output {
  if [type] == "heartbeat" {
     elasticsearch {
     hosts => "http://localhost:9200"
     index => "heartbeat"
   }
  }
 stdout {
  codec => "rubydebug"
  }
}

Generator plugin (useful for testing by production steam of continous data)
----------------
Create a config called generator.conf (contents below) and run is using the comnmad bin/logstash -f config/generator.conf.
input {
  generator {
    lines => [
          '{"id": 1,"first_name": "Ford","last_name": "Tarn","email": "ftarn0@go.com","gender": "Male","ip_address": "112.29.200.6"}', 
          '{"id": 2,"first_name": "Kalila","last_name": "Whitham","email": "kwhitham1@wufoo.com","gender": "Female","ip_address": "98.98.248.37"}'
    ]
    count => 0
    codec =>  "json"
  }
}
output {
  elasticsearch {
     hosts => "http://localhost:9200"
     index => "generator"
  } 
  stdout {
    codec => "rubydebug"
  }
}

DeadLetter Queue (Events that cannot be processed are collected in the dead letter queue if enabled)
----------------
For this to work please set the following (uncomment and change) in the logstash.yml file under the config folder
dead_letter_queue.enable: true
path.dead_letter_queue: <path>

Now create a conf file to read the json content which has some data that will fall into dead letter queue
input {
  file {
    start_position => "beginning"
    path => "<full file path>/sample-data-dlq.json"
    sincedb_path => "/tmp/null"
  }
}
filter {
    json {
        source => "message"
    }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "dql-injest-data"
  }

stdout {}
}

The rejected data will fall into a dead letter queue which can be read by running the following config: 
input {
  dead_letter_queue {
    path => "<file path>/dlq"
    # We can also add "commit_offsets => true" here if we want Logstash to continue
    # where it left off, instead of re-processing all events in DLQ at subsequent runs
  }
}

output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "dlq-reject-data"
  }
  stdout {
    codec => "rubydebug"
  }
}


Http Poller (to check http end points continously and store the result into a datastore)
-----------
Create a config file called http-poller.conf as follows:
input {
 http_poller {
      urls => {
       external_api => {
          method => post
          url => "https://jsonplaceholder.typicode.com/posts"
          body => '{ "title": "foo", "body": "bar", "userId": "1"}'
          headers => {
           "content-type" => "application/json"
          }
       }
      }
      tags => "external-api"
      request_timeout => 100
      schedule => {"every" => "5s"}
      codec => "json"
      metadata_target => "http_poller_metadata"
 }
 http_poller  {
     urls => {
     es_health_status => {
        method => get
        url => "http://localhost:9200/_cluster/health"
        headers => {
          Accept => "application/json"
          }
        }
     }
     tags => "es_health"
     request_timeout => 60
     schedule => { cron => "* * * * * UTC"}
     codec => "json"
     metadata_target => "http_poller_metadata"
 }

}


output {
    if "es_health" in [tags] {  
      elasticsearch{
      hosts => ["localhost:9200"] 
      index => "http-poller-es-health" 
    }      
  }
   if "external-api" in [tags] {  
      elasticsearch{
      hosts => ["localhost:9200"] 
      index => "http-poller-api" 
    }      
  }
      stdout { 
      codec => "rubydebug"
    } 
}

Run this with the following command: 
bin/logstash -f config/http-poller.conf

Check the result in the following URL: 
GET /http-poller-es-health/_search
GET /http-poller-api/_search

Twitter Plugin (This allows elasticsearch to directly import twitter data based on keywords)
--------------
Create a config file called twitter.conf with its content as follows: 
input {
  twitter {
      consumer_key => "REPLACE THIS WITH YOUR API KEY"
      consumer_secret => "REPLACE THIS WITH YOUR API KEY SECRET"
      oauth_token => "REPLACE THIS WITH YOUR ACCESS TOKEN"
      oauth_token_secret => "REPLACE THIS WITH YOUR ACCESS TOKEN SECRET"
      keywords => ["money","bank"]
      full_tweet => true
  }
}
output {
   elasticsearch {
     hosts => "http://localhost:9200"
     index => "twitter"
  }
stdout {
  codec => "rubydebug"
  }
}

Run this with the following command: 
bin/logstash -f config/twitter.conf

Check the result in the following URL: 
GET /twitter/_search

```

## Reading System Log using Logstash
```xml
On Unix it is located at /var/log/syslog
On my Mac it is located at the below location: 
sudo head -10 /private/var/log/system.log


Please set the following syslog config file on mac to route all log enteries to the TCP IP port 10514. 
We can later configure Logstash to listen to this port for messages. 
open file -> /etc/syslog.conf 
Add the following line
*.*         @@127.0.0.1:10514
Save and restart syslog service by the following method:
Restart the ‘syslogd’ service by the below way: But before doing so, check if it’s running by typing:
$ ps -e | grep syslogd
5070 ??         2:33.75 /usr/sbin/syslogd
The following commands restart the service. Enter your password one more time if necessary.
$ sudo launchctl unload /System/Library/LaunchDaemons/com.apple.syslogd.plist
$ sudo launchctl load /System/Library/LaunchDaemons/com.apple.syslogd.plist
Check if the service was really shut down and restarted by typing the same command again. The counter should have been reset and the PID (5070 in the example above) should be a different one.
$ ps -e | grep syslogd
18597 ??         0:00.01 /usr/sbin/syslogd
With this syslog is ready and sending messages to the TCP IP port 10514.

Create a config file called syslog.conf with its content as follows: 
input {
  tcp {
    host => "127.0.0.1"
    port => 10514
    type => "rsyslog"
  }
}
filter {
       grok {
        match => { "message" => ["%{SYSLOGTIMESTAMP:[system][syslog][timestamp]} %{SYSLOGHOST:[system][syslog][hostname]} %{DATA:[system][syslog][program]}(?:\[%{POSINT:[system][syslog][pid]}\])?: %{GREEDYMULTILINE:[system][syslog][message]}"] }
        pattern_definitions => { "GREEDYMULTILINE" => "(.|\n)*" }
        remove_field => "message"
      }
      date {
        match => [ "[system][syslog][timestamp]", "MMM  d HH:mm:ss", "MMM dd HH:mm:ss" ]
      }
}
output {
  if [type] == "rsyslog" {
    elasticsearch {
      hosts => [ "localhost:9200" ]
      index => "syslog-data" 
    }
  }
  stdout { 
    codec => "rubydebug"
  }
}

Run this with the following command: 
bin/logstash -f config/syslog.conf

Check the result in the following URL: 
GET /syslog-data/_search

```


## Useful grok debugger tool 
```xml
(it is also availble in kibana console - use below only if you are not using kibana console)
https://grokdebug.herokuapp.com/
```

## Apache Kafka with Elasticsearch/Logstash - (can rather use Kafka connectors for this to send data directly to elastic search but this is another alternative option)
```xml
Note: Detail information on Kafka can be found under my Kafka repository. 
First download and install Kafka. 

Start Zookeeper: 
bin/zookeeper-server-start.sh config/zookeeper.properties

Start Kafka: 
bin/kafka-server-start.sh config/server.properties

Create a Kafka Topic called kafka-logs: 
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic kafka-logs --partitions 1 --replication-factor 1

Send streaming messages from our access_log file into Kafka topic that was created: 
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic kafka-logs < "full file path"\access_log 

Create a logstash config file called logstash-kafka.conf with its content as follows: 
input {
  kafka {
    bootstrap_servers => "localhost:9092"
    topics => ["kafka-logs"]
  }
}
filter {
  grok {
    match => {"message" => "%{COMBINEDAPACHELOG}"}
  }
  date {
    match => ["timestamp", "dd/MMM/yyyy:HH:mm:ss Z"]
  }
}
output {
  elasticsearch {
    hosts => [ "localhost:9200" ]
    index => "kafka-logs" 
  }
  stdout { 
    codec => "rubydebug"
  }
}

Run this with the following command: 
bin/logstash -f config/logstash-kafka.conf

Check the result in the following URL: 
GET /kafka-logs/_search

```

## Hadoop with Elasticsearch/Logstash 
```xml
To be completed later 
```

## Apache Spark with Elasticsearch/Logstash 
```xml
To be completed later 
```

## Apache Flink with Elasticsearch/Logstash 
```xml
To be completed later 
```

## Sample SQL queries in Elasticsearch
```xml
POST /_sql 
{
  "query" : "describe movies"
}

POST /_sql 
{
  "query" : "select * from review"
}

POST /_sql?format=txt
{
  "query" : "select * from review"
}
-> this is better formatted for reading

POST /_sql?format=txt
{
  "query" : "select title, year from movies where year>1960 order by title"
}

POST /_sql/translate?pretty
{
  "query" : "select title, year from movies where year>1960 order by title"
}
-> this will return the json query that will be executed under the hood  

elasticsearch-sql-cli is the command line interface for SQL queries in elasticsearch 

*** Please read the SQL query limitations here: 
https://www.elastic.co/guide/en/elasticsearch/reference/current/sql-limitations.html

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
https://wiki.splunk.com/Community:HowTo_Configure_Mac_OS_X_Syslog_To_Forward_Data
```








