package com.uofl.ea.model;

import java.sql.Timestamp;

public class CurrentEventVO {

	private Long id;
	private Long eventId;
	private Timestamp eventTime;
	private String deviceName;

	public CurrentEventVO() {}
	
	public CurrentEventVO(Long id, Long eventId, Timestamp eventTime, String deviceName) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.eventTime = eventTime;
		this.deviceName = deviceName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Timestamp getEventTime() {
		return eventTime;
	}

	public void setEventTime(Timestamp eventTime) {
		this.eventTime = eventTime;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public String toString() {
		return "CurrentEventVO [id=" + id + ", eventId=" + eventId + ", eventTime=" + eventTime + ", deviceName="
				+ deviceName + "]";
	}
}
