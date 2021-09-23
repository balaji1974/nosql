# ElasticSearch

```xml
Download and install elasticsearch.

From the installation folder start the server. 

To start Elasticserch after installation
./bin/elasticsearch

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

## Mapping
### Mapping is the process of defining how a document, and the fields it contains, are stored and indexed, like DDL in RDBMS
```xml
GET http://localhost:9400/review 
 -> This will get the mapping of the given index (review in our case)
```

## Documents 
### These are the actual records that are stored inside an index, they are like rows inside a table in RDBMS


### Insert data 
```xml
POST http://localhost:9400/review/_doc
<sample data - in the  body of the request>
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

<sample data - in the  body of the request>
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
{
  "query": {
    "match": {
      "description": "tropical fruit"
    }
  }
}

 -> Will result in matching description field containing "tropical fruit"  


```

## Springboot with Elasticsearch



References:
https://reflectoring.io/spring-boot-elasticsearch/
https://www.elastic.co/blog/









