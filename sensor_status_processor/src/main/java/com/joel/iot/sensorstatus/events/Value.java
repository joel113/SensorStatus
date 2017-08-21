package com.joel.iot.sensorstatus.events;

public class Value {

	private String temperature;
	private String humidity;
	
	public Value(String subject, String value) {
		super();
		this.temperature = subject;
		this.humidity = value;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String subject) {
		this.temperature = subject;
	}
	
	public String getHumiditiy() {
		return humidity;
	}
	
	public void setHumidity(String value) {
		this.humidity = value;
	}

}
