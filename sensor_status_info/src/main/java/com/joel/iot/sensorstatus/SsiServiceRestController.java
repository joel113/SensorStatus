package com.joel.iot.sensorstatus;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;

@RestController
public class SsiServiceRestController {
	
	final private String GET_SENSOR_DATA_PER_DEVICEID = "SELECT values, timestamp FROM sensor.sensor_data WHERE deviceid = ? ORDER BY timestamp DESC";
	final private String GET_SENSOR_DATA_PER_DEVICEID_SINCE = "SELECT values, timestamp FROM sensor.sensor_data WHERE deviceid = ? AND timestamp >= ? ORDER BY timestamp DESC";
	final private String GET_SENSOR_DATA_PER_DEVICEID_TO = "SELECT values, timestamp FROM sensor.sensor_data WHERE deviceid = ? AND timestamp <= ? ORDER BY timestamp DESC";
	final private String GET_SENSOR_DATA_PER_DEVICEID_SINCE_TO = "SELECT values, timestamp FROM sensor.sensor_data WHERE deviceid = ? AND timestamp => ? AND timestamp <= ? ORDER BY timestamp DESC";
	final private String DELETE_SENSOR_DATA_PER_DEVICEID = "DELETE FROM sensor.sensor_data WHERE deviceid = ?";

	@RequestMapping(value = "/sensordata/v1/{deviceId}", method = RequestMethod.GET, produces = "application/octet-stream")
	public String getSensorStatus(@PathVariable int deviceId, @RequestParam(required=false) String since, @RequestParam(required=false) String to, @RequestParam(required=false) Integer rows) throws IOException {
		Session session = connect();
		PreparedStatement pstmt;
		if(since != null && to != null) {
			pstmt = session.prepare(GET_SENSOR_DATA_PER_DEVICEID_SINCE_TO + ((rows != null && rows > 0) ? " LIMIT " + rows : ""));
		}
		else if(since != null && to == null) {
			pstmt = session.prepare(GET_SENSOR_DATA_PER_DEVICEID_SINCE + ((rows != null && rows > 0) ? " LIMIT " + rows : ""));
		}
		else if(since == null && to != null) {
			pstmt = session.prepare(GET_SENSOR_DATA_PER_DEVICEID_TO + ((rows != null && rows > 0) ? " LIMIT " + rows : ""));
		}
		else {		
			pstmt = session.prepare(GET_SENSOR_DATA_PER_DEVICEID + ((rows != null && rows > 0) ? " LIMIT " + rows : ""));
		}
		System.out.println(pstmt.getQueryString());
		BoundStatement bstmt = new BoundStatement(pstmt);
		bstmt.setInt(0, deviceId);
		ResultSet resultSet = session.execute(bstmt);
		Gson gson = new Gson();
		StringBuffer jsonResult = new StringBuffer();
		for(Row row : resultSet) {
			Map<String,String> values = row.getMap("values", String.class, String.class);
			Date timestamp = row.getTimestamp("timestamp");
			SensorStatus sensorStatus = new SensorStatus(values, timestamp);
			jsonResult.append(gson.toJson(sensorStatus));
		}	
		disconnect(session);
		return jsonResult.toString();
	}
	
	@RequestMapping(value = "/sensordata/v1/{deviceid}", method = RequestMethod.DELETE, produces = "application/octet-stream")
	public void deleteSensorStatus(@PathVariable int deviceId) throws IOException {
		Session session = connect();
		session.execute(DELETE_SENSOR_DATA_PER_DEVICEID);
		disconnect(session);
	}

	@RequestMapping(value = "/rest/v1/status", method = RequestMethod.GET)
	public String status() {
		return "HTTP 200";
	}
	
	public Session connect() {
		String serverIp = "localhost";
		String keyspace = "sensor";
		Cluster cluster = Cluster.builder().addContactPoint(serverIp).build();
		Session session = cluster.connect(keyspace);
		return session;
	}
	
	public void disconnect(Session session) {
		session.close();
	}

}
