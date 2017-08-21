Sensor status processor for reading sensor values from a MQTT broker and inserting values into a Cassandra database.

Commands:
=========

(1) Run a Docker container with cassandra

docker build -t cassandra .
docker run -d --name cassandra -p 9042:9042 cassandra
docker exec -it cassandra bash
docker logs cassandra

The [official Cassandra Image](https://hub.docker.com/_/cassandra/) from Docker Hub is used for this image.

(2) Create and access the tables within the cassandra database

In order to access the cassandra command line you have to use the following commands.

cqlsh
help

With the following commands you can create a keyspace as well as a table.

CREATE KEYSPACE Sensor WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE SENSOR;
CREATE TABLE temperature_data (timestamp timestamp, deviceid int, value decimal, PRIMARY KEY(deviceid, timestamp));
SELECT * FROM temperature_data;

More information about [Cassandra keyspaces](http://docs.datastax.com/en/cql/3.1/cql/cql_reference/create_keyspace_r.html).

Data Modeling
=============

More information about the data modeling with cassandra can be found [here](http://docs.datastax.com/en/cql/3.1/cql/ddl/ddl_intro_c.html). This page shows some examples including collection columns. The collection columns consists of a set,
a list and a map. For our use case the [map type](http://docs.datastax.com/en/cql/3.1/cql/cql_using/use_map_t.html) is the most interesing one. A map is a pair of typed values. In order to use maps for our case we can modify the table designed
above:

CREATE TABLE sensor_data (deviceid int, timestamp timestamp, values map<text, text>, PRIMARY KEY(deviceid, timestamp));

But before we can create this table we have to drop the old table.


Background information:
=======================

Following links were used as background information.

https://stackoverflow.com/questions/16870502/how-to-connect-cassandra-using-java-class/16871984#16871984

http://gettingstartedwithcassandra.blogspot.de/2011/06/cassandra-keyspaces-what-are-they.html

https://docs.datastax.com/en/cql/3.1/cql/cql_reference/create_keyspace_r.html

CREATE KEYSPACE Sensor WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

https://docs.datastax.com/en/cql/3.1/cql/cql_reference/create_table_r.html

CREATE TABLE Sensor.Sensor_Data (deviceid int, value decimal, unit text, month text, timestamp timestamp, PRIMARY KEY ((deviceid,month),timestamp));

Based this design on following posts:

--> https://academy.datastax.com/use-cases/internet-of-things-time-series

CREATE TABLE "ObjectsOverTime"(
  type_id ascii,
  node_id uuid,
  channel ascii,
  when timestamp,
  object_state blob,
  PRIMARY KEY ((node_id, type_id), when)
) WITH CLUSTERING ORDER BY (when DESC);

--> http://stackoverflow.com/questions/37523894/cassandra-how-to-model-time-series-of-sensor-data

CREATE TABLE sensor_data (
  sensor_id timeuuid,
  partitioning_time_range bigint,
  insertion_time_in_timeuuid timeuuid,
  float_value float
  int_value int,
  bool_value bool,
  text_value text,
  PRIMARY KEY( (sensor_id, hour), insertion_time_in_timeuuid)
) WITH CLUSTERING ORDER BY (insertion_time_in_timeuuid DESC);

--> http://stackoverflow.com/questions/36048660/cassandra-partition-key-for-time-series-data?noredirect=1&lq=1

query-based moedeling
time bucketing

CREATE TABLE newdata_by_deviceid_and_time (
  timestamp timestamp,
  deviceid int,
  tagid int,
  decvalue decimal,
  alphavalue text,
  PRIMARY KEY (deviceid,timestamp));

CREATE TABLE newdata_by_deviceid_and_time (
  timestamp timestamp,
  deviceid int,
  tagid int,
  decvalue decimal,
  alphavalue text,
  monthbucket text,
  PRIMARY KEY ((deviceid,monthbucket),timestamp));

--> http://stackoverflow.com/questions/24949676/difference-between-partition-key-composite-key-and-clustering-key-in-cassandra?rq=1

Difference between primary key, partition key, composite key and clustering key

--> https://en.wikipedia.org/wiki/Webhook

--> http://resthooks.org/docs/performance/

--> http://stackoverflow.com/questions/16870502/how-to-connect-cassandra-using-java-class

select value, unit, timestamp from Sensor.Sensor_Data where deviceid = 1 and month = 'April';
