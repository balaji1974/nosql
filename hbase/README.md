# HBase 

## Wide column database - Eg. HBase, Cassandra, BigTable 


```xml
It consist of Key and a column family (which is a collection of columns)
Key is unique and used to indentify individual rows in a table 
Each column family consist of one or many columns
Columns can vary across rows 
Columns can also store multiple versions of each value marked with timestamp 
This timestamp is used to find the older version of values
The most recent version is selected if no timestamp is provided 
Each value is stored as a new version with timestamp rather than updating an older value 

For data access we need to have table name, key, column family, column and optional timestamp (or else most recent version is fetched)


```


References: 
https://www.udemy.com/course/sql-nosql-big-data-hadoop/    
https://www.youtube.com/watch?v=B5eq0fsMXGk





