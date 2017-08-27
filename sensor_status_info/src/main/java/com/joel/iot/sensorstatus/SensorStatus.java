package com.joel.iot.sensorstatus;

import java.util.Date;
import java.util.Map;

public class SensorStatus {
	
	private Map<String, String> values;
	private Date timestamp;
	
	public SensorStatus(Map<String, String> values, Date timestamp) {
		super();
		this.values = values;
		this.timestamp = timestamp;
	}

	public Map<String, String> getValue() {
		return values;
	}
	
	public void setValue(Map<String, String> values) {
		this.values = values;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
