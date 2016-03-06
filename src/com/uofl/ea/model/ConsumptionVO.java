package com.uofl.ea.model;

import java.sql.Timestamp;

public class ConsumptionVO {
	
	private String deviceName;
	private Timestamp startTime;
	private Timestamp endTime;
	private Double energyConsumption;
	
	public ConsumptionVO() {}
	
	public ConsumptionVO(String deviceName, Timestamp startTime, Timestamp endTime, Double energyConsumption) {
		super();
		this.deviceName = deviceName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.energyConsumption = energyConsumption;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Double getEnergyConsumption() {
		return energyConsumption;
	}
	public void setEnergyConsumption(Double energyConsumption) {
		this.energyConsumption = energyConsumption;
	}
	
	

}
