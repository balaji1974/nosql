# NoSQL

```xml
Database classifications: 

RDBMS - Vertical scaling 
ACID - Atomicity, Consistency, Isolation, and Durability (applies to transaction)
Distributed Databases - Horizontal scaling 
CAP Theorem - Consistency, Availability, Partition Tolerance - Can have only 2 at a time

BASE - Basically available soft-state Eventually consistent (totally opposite to ACID) 

Availability + Consistency -> Oracle / MySQL / PostgresQL, DB2, SQL Server etc 
Consistency + Partition Tolerance -> HBase, MongoDB, Redis BigTable, ZooKeeper etc 
Availability + Partition Tolerance -> Cassandra, CouchBase, DynamoDB, DNS Server etc 

Consistency  <---------------------------------------------------------------------------------------> Availability
ACID												BASE

Modern DBs want to be somewhere in-between the above 2 ends 
```

## Types of noSQL database: 
### Key-Value Store - Redis, DynamoDB, Riak
### Column Orientied - Google BigTable, Casandra, SimpleDB
### Graph - OrientDB, Neo4J, Tital
### Document Orientied - MongoDB, CouchDB, ElasticSearch

## Key-Value store - Redis (check the redis folder)

## Document Oriented Database - MongoDB (check the mongodb folder)

