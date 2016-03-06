package com.uofl.ea.model;

import java.sql.Timestamp;

public class EventVO {
	
	private String userName;
	private String eventType;
	private String deviceName;
	private Timestamp eventTime;
	private Double distance;
	
	public EventVO() {}

	public EventVO(String userName, String eventType, String deviceName, Timestamp eventTime, Double distance) {
		super();
		this.userName = userName;
		this.eventType = eventType;
		this.deviceName = deviceName;
		this.eventTime = eventTime;
		this.distance = distance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Timestamp getEventTime() {
		return eventTime;
	}

	public void setEventTime(Timestamp eventTime) {
		this.eventTime = eventTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Event [userName=" + userName + ", eventType=" + eventType + ", deviceName="
				+ deviceName + ", eventTime=" + eventTime + ", distance=" + distance + "]";
	}
}