Sensor status processor for reading sensor values from a MQTT broker and inserting values into a Cassandra database.

Configuration for a cluster mode.

Commands:
=========

(1) Create a user defined network

https://docs.docker.com/engine/userguide/networking/

docker network ls
docker network create --driver bridge cassandra-isolated-nw
docker network inspect cassandra-isolated-nw

(2) Run a the cassandra image in a cluster

docker build -t cassandra .
docker run --net=cassandra-isolated-nw -d --name cassandra-1 -p 9042:9042 -e "CASSANDRA_SEEDS=172.18.0.2,172.18.0.3" -e "CASSANDRA_CLUSTER_NAME=cluster" cassandra
docker run --net=cassandra-isolated-nw -d --name cassandra-2 -p 9043:9042 -e "CASSANDRA_SEEDS=172.18.0.2,172.18.0.3" -e "CASSANDRA_CLUSTER_NAME=cluster" cassandra
docker exec -it cassandra-1 bash
docker logs cassandra

https://docs.datastax.com/en/cassandra/2.1/cassandra/initialize/initializeSingleDS.html
https://docs.docker.com/engine/reference/run/#env-environment-variables

The docker command sets the environment variable for setting the cassandra seeds. Cassandra seeds are IP adresses where new nodes announces that they joining the cluster.

https://stackoverflow.com/questions/10407072/cassandra-seed-nodes-and-clients-connecting-to-nodes
https://stackoverflow.com/questions/26852413/cassandra-java-driver-how-many-contact-points-is-reasonable

(3) Create a keyspace with a replication strategy

create keyspace test with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 2 }

https://dzone.com/articles/introduction-apache-cassandras

https://10kloc.wordpress.com/2012/12/27/cassandra-chapter-5-data-replication-strategies/

(4) Inspecting the cassandra cluster

nodetool status

https://docs.datastax.com/en/cassandra/2.1/cassandra/tools/toolsStatus.html


