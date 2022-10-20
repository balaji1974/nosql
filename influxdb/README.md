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


```


References:
```xml
https://www.udemy.com/course/sql-nosql-big-data-hadoop/ 
https://grouplens.org/datasets/movielens/
```