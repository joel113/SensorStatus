package com.joel.iot.sensorstatus;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.google.gson.Gson;
import com.joel.iot.sensorstatus.cassandra.CassandraSink;
import com.joel.iot.sensorstatus.events.Value;
import com.joel.iot.sensorstatus.mqttstreaming.MqttSubject;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class SensorProcessor {
	
	private String broker;
	private static Logger log = Logger.getRootLogger();
	
	public SensorProcessor(String broker) {
		this.broker = broker;
		log.setLevel(Level.TRACE);
	}
	
	public void startProcessing(final String clientId, final String topic) {
		
		log.info("Connecting to MQTT and Cassandra database.");
		final MqttSubject valueSubject = new MqttSubject(broker, clientId, topic);
		final CassandraSink cassandraSink = new CassandraSink("localhost", 9042, "SENSOR");
		
		final Observable<Value> values = valueSubject.observe().doOnSubscribe(new Action0() {
			@Override
			public void call() {
				log.info(String.format("Subscribe to %s to process values.", topic));
			}
		}).doOnUnsubscribe(new Action0() {
			@Override
			public void call() {
				log.info(String.format("Unsubscribe from %s.", topic));		
			}
		}).map(new Func1<String, Value>() {
			@Override
			public Value call(String json) {
				Gson gson = new Gson();
				return gson.fromJson(json, Value.class);
			}	
		});
		
		values.forEach(new Action1<Value>() {
			@Override
			public void call(Value value) {
				log.trace(String.format("Processing value %s.", value.getTemperature()));	
				cassandraSink.insert(value);
			}
		});
		
	}
	
    public static void main(String[] args) {
    	
    	try {
	        SimpleLayout layout = new SimpleLayout();
	        ConsoleAppender consoleAppender = new ConsoleAppender( layout );
	        log.addAppender(consoleAppender);
	        FileAppender fileAppender = new FileAppender( layout, "logs/sensorprocessor.log", false );
	        log.addAppender(fileAppender);
    	}
    	catch(Exception ex) {
    		System.out.println(ex);
    	}
    	
    	SensorProcessor processor = new SensorProcessor("tcp://joel-flocke:1883");
    	processor.startProcessing("event-processor-joel-weatherman-sensor-status","/joel-weatherman/sensor");
    }
}
