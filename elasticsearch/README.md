# ElasticSearch

## Installation option 1
```xml
Download and install elasticsearch from the below URL: 
https://www.elastic.co/downloads/elasticsearch

Uzip the zip file and move it to the location of your choice. 
Now navigate to the root of the extracted elasticsearch folder. 
From this folder start the server by issuing the following command: 

bin/elasticsearch

Note: 9200 is the default port for elasticsearch which can be changed in elasticsearch.yml file located in the config folder.

We can configure many more things like apart from port settings in this yaml file like: 
cluster name
node name
data file storage path
log file storage path 
host ip 

Next we can set the JVM options in the file called jvm.options located in the config directory. 

For logging propeties setting we can configure the file called log4j2.properties file located under the config directory. 

Once the server is started we can query the server status with the following  url: 
GET localhost:9200/ 
or curl http://localhost:9200

```

## Installation option 2
```xml

Directly sign up for a 14 day free trial of elastic cloud and start using it. 

The sign-up url is given below: 
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


For production grade installation (multinode) follow the instructions given in the below url:
https://www.elastic.co/guide/en/elasticsearch/reference/7.5/docker.html 

```



### Node, Cluster, Shard & Replica -> Same concept as other databases 
```xml
Node -> Single machine running elastic search 
Cluster -> Set of mulitple nodes running togther
Shard -> Index horizontally partitioned across nmulitple nodes
Replica -> Data in a shared being replicated across nodes for fault tolerance  
Documents -> Data is stored in documents as json objects
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

Since all the below commands are run from the Kibana dev tools console I will be omiting the http://localhost:9200/ and Kibana will add it to all my requests
But if we use the same commands from postman/curl please be sure to append http://localhost:9200/ to all the below commands 
If we copy the below command from dev tools as a curl command, the full URL is appended with the curl request automatically. 

Here all underscore (_) referes to the APIs that we are calling and the other part refers to the command we are running. 
For eg. In the below request cluster is the API that we are calling and health is the command we are running against this API. 

GET _cluster/health -> This will display the clusters health status 
GET _cat/nodes -> This will list the information of the nodes 
GET _cat/nodes?v -> Same command as above with descriptive header information 
GET _nodes -> Retreive more complete node information 
GET _cat/indices -> Display all the indices in elasticsearch

Leading dot (.) in indices will help us to hide the index within the node (eg. .kibana_7.15.1_001)

```
## Sharding 
```xml
This is a process by which an index is split into mulitple indices 
The reason for this is to run the search query in parallel - for optimization 
and data storage to be managed in chunks on mulitple nodes

A default shard for an index is 1 and a decent number for millions of documents in an index is 5 
A shard can be split or shrink and we have elasticsearch apis for each of this operation. 

```

## Replication
```xml
Replication works by creating copies of shards
A shard that has been replicated is called a primary shard
Primary and secondary shards are together called as replication groups
No. of replicas can be configured at index creating time
One is the default value for a replica 

```


## Mapping
### Mapping is the process of defining how a document, and the fields it contains, are stored and indexed, like DDL in RDBMS
```xml
GET http://localhost:9200/review 
 -> This will get the mapping of the given index (review in our case)
```

## Create Index without mapping
```xml
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

### Search Query DSL 
```xml
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
	-> This ia a range query that matchs price between 12 and 17 both incluive, 
	-> if we need to exclued 12 and 17 use gt and lt instead 

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
	-> This will match the query expression where fields are sepeated by + 

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

```

### Aggregation Query - Used on numeric fields
```xml
Syntax
{
	"query": { … },
	"aggregations" : { … }
}

Types of aggreation are: Bucketing, Metrics, Matrix & Pipeline

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
	-> This will group on "points" and perfom std stats operation like count, min, max, avg and sum on "price" 

```

## ELK - Elastic, Logstash, Kibana
### Elastic - Database engine built on top of Apache Lucene
### Logstash - Tool for data collection, transformation and transportation pipeline
### Kibana - Data visulization and analytics platform 

## Kibana
```xml
Download and unzip Kibana from https://www.elastic.co/downloads/kibana or https://www.elastic.co/start
Move it to a folder of your choice

Start Kibana from the root installation folder of kibana by executing the following command:
bin/kibana

This will autoconnect to Elasticsearch provide it started in the default port of 9200

Now use the browser to connect to Kibana using the url http://localhost:5601/

Next click on "Add Data" and enter
```

## Logstash
```xml
Download and install logstash from https://www.elastic.co/downloads/logstash

Start Logstash from the folder by running the following sample pipeline:
./bin/logstash -e 'input { stdin { } } output { stdout {} }'

```

##Sample data to work with for practise
```xml
Download the sample file "complete.es.json"

Create a mapping
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

Upload the file into elasticsearch using the following command:
curl -XPOST 'localhost:9200/_bulk?pretty' -H "Content-Type: application/json" --data-binary @complete.es.json

Count the records that were upload:
GET http://localhost:9200/ufo/_count

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
https://www.udemy.com/course/elasticsearch-complete-guide/learn/lecture/7373340
```








