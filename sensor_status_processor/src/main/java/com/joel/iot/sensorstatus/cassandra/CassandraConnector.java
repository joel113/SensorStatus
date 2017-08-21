package com.joel.iot.sensorstatus.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnector {

	private Cluster cluster;
	private Session session;

	public void connect(String node, Integer port, String keyspace) {
		cluster = Cluster.builder().addContactPoint(node).build();
		session = cluster.connect(keyspace);
	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

}
