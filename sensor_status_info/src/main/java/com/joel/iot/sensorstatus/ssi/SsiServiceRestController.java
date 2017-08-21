package com.joel.iot.sensorstatus.ssi;

import java.io.IOException;
import java.util.Date;

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
	
	final private String GET_SENSOR_DATA_PER_SENSORID = "SELECT value, unit, timestamp FROM Sensor.Sensor_Data WHERE deviceid = ? AND month = ?"; 

	@RequestMapping(value = "/sensorstatus/v1/{sensorid}", method = RequestMethod.GET, produces = "application/octet-stream")
	public String getSensorStatus(@PathVariable String sensorId, @RequestParam(value="parameters", defaultValue="none") String parameters) throws IOException {
		Session session = connect();
		PreparedStatement pstmt = session.prepare(GET_SENSOR_DATA_PER_SENSORID);
		BoundStatement bstmt = new BoundStatement(pstmt);
		ResultSet resultSet = session.execute(bstmt.bind(1, "April"));
		Gson gson = new Gson();
		StringBuffer jsonResult = new StringBuffer();
		for(Row row : resultSet) {
			float value = row.getFloat("value");
			String unit = row.getString("unit");
			Date timestamp = row.getTimestamp("timestamp");
			SensorStatus sensorStatus = new SensorStatus(value, unit, timestamp);
			jsonResult.append(gson.toJson(sensorStatus));
		}	
		disconnect(session);
		return jsonResult.toString();
	}
	
	@RequestMapping(value = "/sensorstatus/v1/{sensorid}", method = RequestMethod.DELETE, produces = "application/octet-stream")
	public String deleteSensorStatus(@PathVariable String sensorId) throws IOException {
		// TODO: implement access to cassandra db	
		return "";
	}
	
	@RequestMapping(value = "/sensorstatus/v1/{sensorid}", method = RequestMethod.PUT, produces = "application/octet-stream")
	public String putSensorStatus(@PathVariable String sensorId, @RequestParam(value="parameters", defaultValue="none") String values) throws IOException {
		// TODO: implement access to cassandra db	
		return "";
	}

	@RequestMapping(value = "/sensorstatus/v1/status", method = RequestMethod.GET)
	public String status() {
		return "HTTP 200";
	}
	
	public Session connect() {
		String serverIp = "";
		String keyspace = "sensor";
		Cluster cluster = Cluster.builder().addContactPoint(serverIp).build();
		Session session = cluster.connect(keyspace);
		return session;
	}
	
	public void disconnect(Session session) {
		session.close();
	}

}
