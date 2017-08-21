package com.joel.iot.sensorstatus.cassandra;

import java.util.Calendar;

import com.datastax.driver.core.Session;
import com.joel.iot.sensorstatus.events.Value;

public class CassandraSink {
	
	private String host;
	private int port;
	private String keyspace;
	private Session session;
	
	public CassandraSink(String host, int port, String keyspace) {
		this.host = host;
		this.port = port;
		this.keyspace = keyspace;
		connect();
	}
	
	public void connect() {
	    CassandraConnector client = new CassandraConnector();
	    client.connect(host, port, keyspace);
	    this.session = client.getSession();
	}
	
	public void insert(Value value) {
		StringBuilder stringBuilder = new StringBuilder("INSERT INTO ")
				.append("sensor_data (deviceid, timestamp, values) ")
				.append("VALUES (1,'")
				.append(Calendar.getInstance().getTimeInMillis())
				.append("',{'temperature':'")
				.append(value.getTemperature())
				.append("','humidity':'")
				.append(value.getHumiditiy())
				.append("'});");
		String query = stringBuilder.toString();
		try {
			session.execute(query);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
