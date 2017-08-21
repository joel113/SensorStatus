package com.joel.iot.sensorstatus.ssi;

import java.util.Date;

public class SensorStatus {
	
	private float value;
	private String unit;
	private Date timestamp;
	
	public SensorStatus(float value, String unit, Date timestamp) {
		super();
		this.value = value;
		this.unit = unit;
		this.timestamp = timestamp;
	}

	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
