package com.tablefootbal.server.events;

import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.readings.SensorReadings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:readings.properties")
@Slf4j
public class SensorDataManager implements ApplicationListener<SensorUpdateEvent>
{
	@Value("${readings.threshold}")
	private int THRESHOLD;
	
	@Value("${readings.max_readings}")
	private int MAX_READINGS;
	
	private final SensorTrackingScheduler scheduler;
	
	private Map<String, SensorReadings> readingsMap;
	
	
	@Autowired
	public SensorDataManager(SensorTrackingScheduler scheduler)
	{
		this.readingsMap = new HashMap<>();
		this.scheduler = scheduler;
	}
	
	@Override
	public void onApplicationEvent(SensorUpdateEvent sensorUpdateEvent)
	{
		
		Sensor sensor = (Sensor) sensorUpdateEvent.getSource();
		SensorReadings.Reading reading = sensorUpdateEvent.getReading();
		
		log.debug("-------> RECEIVED UPDATE EVENT WITH PARAMETERS:\n" +
				"ID: " + sensor.getId() +
				"READINGS: " + reading);
		
		log.debug("-------> STARTING TRACKING FOR SENSOR WITH ID: " + sensor.getId());
		
		scheduler.startTracking(sensor.getId());
		
		SensorReadings storedReadings = readingsMap.get(sensor.getId());
		
		if(storedReadings == null)
		{
			storedReadings = new SensorReadings(MAX_READINGS);
		}
		
		storedReadings.addReading(reading);
		readingsMap.put(sensor.getId(), storedReadings);
		
		int average = storedReadings.getAverage();
		
		sensor.setActive(average > THRESHOLD);
	}
}
