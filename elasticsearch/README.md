# ElasticSearch

```xml
Download and install elasticsearch.

From the installation folder start the server. 

To start Elasticserch after installation
./bin/elasticsearch

Note: 9200 is the default port for elasticsearch which can be changed in elasticsearch.yml file located in the config folder.
In all the below example I have used port 9400 by changing this defult port.  

Query the server status
GET localhost:9400/ 

```

### Node, Cluster, Shard & Replica -> Same concept as other databases 
```xml
Node -> Single machine running elastic search 
Cluster -> Set of mulitple nodes running togther
Shard -> Index horizontally partitioned across nmulitple nodes
Replica -> Data in a shared being replicated across nodes for fault tolerance  
```

## Index
### An index is like a ‘database' in a relational database. 
```xml
RDBMS => Databases => Tables => Columns/Rows
Elasticsearch => Indices => Types => Documents with Properties
Eg. http://localhost:9400/review/doc 
In the above url the index (database) is 'review' and type (table) is 'doc'

Note: Type concept is now deprecated and we can use the following url instead 
http://localhost:9400/review/_doc
 ```

## Display all the index in elasticsearch
```xml
GET http://localhost:9400/_cat/indices
```

## Mapping
### Mapping is the process of defining how a document, and the fields it contains, are stored and indexed, like DDL in RDBMS
```xml
GET http://localhost:9400/review 
 -> This will get the mapping of the given index (review in our case)
```

## Documents 
### These are the actual records that are stored inside an index, they are like rows inside a table in RDBMS

## Elasticsearch field data types
```xml
https://www.elastic.co/guide/en/elasticsearch/reference/7.1/mapping-types.html#_core_datatypes
```

## Create a index from a mapping file (structure of a database table in RDBMS)
```xml
PUT http://localhost:9400/ml-ratings
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

PUT http://localhost:9400/ml-tags
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

PUT http://localhost:9400/ml-users
BODY
{
	"mappings" : {
		"properties" : {
			"id" : {"type": "integer"},
			"age_group" : {"type": "keyword"},
			"gender" : {"type": "keyword"},
			"occupation" : {"type" : "keyword"},
			"zip_code" : {"type" : "keyword"}
		}
	},
	"settings" : {
		"number_of_shards" : 3,
		"number_of_replicas" : 1
	}
}

PUT http://localhost:9400/ml-movies
BODY 
{
	"mappings" : {
		"dynamic" : false,
		"properties" : {
			"id" : {"type": "long", "store": true},
			"title" : {"type" : "text", "store" : true, "index" : true, "analyzer" : "snowball", "copy_to" : "title_genres"},
			"year" : {"type" : "short", "store" : true},
			"genres" : {"type" : "keyword", "store": true, "copy_to" : "title_genres"},
			"title_genres" : {"type" : "text", "index" : true, "store": false, "analyzer": "standard"}
		}

	},
	"settings" : {
		"number_of_shards" : 1,
		"number_of_replicas" : 1
	}
}

```


### Insert data 
```xml
POST http://localhost:9400/review/_doc
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

### Search Data
```xml
GET http://localhost:9400/review/_search 
 -> Will result in all records being returned

GET http://localhost:9400/review/_search
BODY 
{
  "query": {
    "match": {
      "description": "tropical fruit"
    }
  }
}

 	-> Will result in matching description field containing "tropical fruit"  

Sort data based on a field
GET http://localhost:9400/review/_search?sort=price
	-> Will sort retreived data based on the field price 

Search based on id
GET http://localhost:9400/review/_doc/<id>
	-> Will retreive the data based on the id field 

Count no. of records in the index
GET http://localhost:9400/review/_count 
	-> Will count the records in the index 'review'



```

## Springboot with Elasticsearch
```xml
Sample project - spring-data-jpa-sample


```

References:
https://github.com/okmich/rdbms_2_nosql/tree/master/elasticsearch
https://www.elastic.co/guide/en/elasticsearch/reference/current
https://reflectoring.io/spring-boot-elasticsearch/
https://www.elastic.co/blog/
https://reflectoring.io/spring-boot-elasticsearch/









