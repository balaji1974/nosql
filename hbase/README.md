# Apache HBase 

## Wide column database - Eg. HBase, Cassandra, BigTable 
## HBase runs on Hadoop file system 
## HBase is an open-source wide column store distributed database that is based on Google's Bigtable. 

## About HBase

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

### HMaster manages the regional server (HRegionServer) 
### HRegionServer are just distribution of table data 
### Each region consist of store and each store consist of one or more HFiles
### HBase uses bloom filter to check if a row contains a named column 
### Region splitting and compactness is managed by the HRegionServer if the HFile size grows and HMaster is notified 



## Installing HBase 

## HBase requires installing Hadoop cluster 

### Option 1  - Install from docker - Easiest method 
```xml
docker pull dajobe/hbase 
mkdir -p hdata/data -> create a local directory anywhere 
cd hdata -> Change to the hdata directory 
docker run --name=hbase-docker -h hbase-docker -d -p 16010:16010 -p 9095:9095 -p 8085:8085 -v $PWD/data:/data dajobe/hbase -> run the container 
docker ps -> Check if the container is running 
docker exec -it hbase-docker bash -> For getting into the running container 
hbase shell -> Once inside the container, run this command to get into the Hbase shell 

http://localhost:16010/master-status -> for the Master Server
http://localhost:9095/thrift.jsp -> for the thrift UI
http://localhost:8085/rest.jsp -> for the REST server UI
http://localhost:16010/zk.jsp -> for the embedded Zookeeper

```


### Option 2 - Install from OVA sandbos using Virtual box - Will consume lot of resources from host 
```xml
https://www.cloudera.com/downloads/hortonworks-sandbox/hdp.html
Steps: 
Import the OVA file into your VM (my case virtualbox) and start the vm 
Open browser and go to localhost:4000
Login with credentials root/hadoop and change password 
Then execute ambari-admin-password-reset to change the admin login password 
Then open the browser and go to localhost:8080 and login using admin/with the pwd you just set 
If you use ssh you can connect to your vm using localhost:2222 with credentials maria_dev/maria_dev 

```

### Option 3 - Use hadoop on the cloud - Use IBM free tier or else it will cost money - Note: This is no more available
```xml
You can only use this service for 50 hours/per month 
https://www.ibm.com/cloud/free
Steps: 
Create your cloud account 
Login to this account
View Catalog
Search for "open spark hadoop"
Filter will find "Analytics Engine"
Click it and create a free tier server (this is no more available - as it requires CC and VAT reg number)

```

## Key use cases of HBase
```xml

https://data-flair.training/blogs/hbase-use-cases/

```


References: 
```xml
https://www.udemy.com/course/sql-nosql-big-data-hadoop/    
https://www.youtube.com/watch?v=B5eq0fsMXGk
```




