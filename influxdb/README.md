# InfluxDB

## It is a timeseries database 
## It is optimized for storing and serving time series through associated pairs of time(s) and value(s)

## Regular timeseries -> data comes at a fixed interval of time
## Irregular timeseries -> data comes at a irregular interval of time -> burst of data (banking transaction during day time)
## A univariate time series has only one variable, a bivariate has two variables, and a multivariate has more than two variables.

## It is schema-less that does not support foreign keys, joins and transactions


## Installation & Running steps
```xml
InfluxDB can be downloaded from https://portal.influxdata.com/downloads/ 
or directly for MAC from https://dl.influxdata.com/influxdb/releases/influxdb2-2.4.0-darwin-amd64.tar.gz
Next unzip and move to a folder
Change to the folder where the app is moved
Run ./influxd (on mac remove the security settings to run it)

Next download influx CLI to connect 
https://docs.influxdata.com/influxdb/cloud/tools/influx-cli/#manually-download-and-install

Dashboard
Point your browser to http://localhost:8086/ and enter the relevant details and enter 

Next to get into the shell of InflxDB run the following command:
./influx v1 shell

Telegraph - It is an agent written in Go for collecting metrics and writing them into InfluxDB or other possible outputs.
https://dl.influxdata.com/telegraf/releases/telegraf-1.24.2_darwin_amd64.dmg

Telegraph Plugins -> https://docs.influxdata.com/telegraf/v1.24/plugins/ 


Chronograph -> It is used for visulaization of data in telegraph 
Kapacitor -> It is a streaming data processing engine 




```

## Concepts 
```xml
Database - Its a logical container in InfluxDB
Measurement -> Its like a table in a regular RDBMS world 
Retention Policy -> It contains rules for a period that data is kept before being discarded - Default is AUTOGEN which is infinite  
Timestamp -> It holds the data and time value of every measurement (table)
Field -> They are key-value pairs eg. key is the column name and value is the actual value of the column in RDBMS  - They are not indexed 
Field Set -> Collection of fields in a  point 
Tag -> They are like Fields but optional (key tag - value tag pair) - They are indexed 
Tag Set -> Collection of tags in a  point 
Series -> Way to classify a point that share a measurement, tag set and field  
Point -> It is uniquily identified by a series and a timestamp 
Continous Query -> They are like scheduled Stored procedures run periodically by InfluxDB 
Data Types -> Sepecific value types (boolean, int, float, timestamp, string)

```



References:
```xml
https://www.udemy.com/course/sql-nosql-big-data-hadoop/ 
https://grouplens.org/datasets/movielens/
```