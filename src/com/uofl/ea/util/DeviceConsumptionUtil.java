package com.uofl.ea.util;

import java.util.HashMap;
import java.util.Map;

public class DeviceConsumptionUtil {
	
	public static final Map<String, Double> DEVICE_CONSUMPTION = new HashMap<>();
	
	static {
		DEVICE_CONSUMPTION.put("FAN", 50.0);
		DEVICE_CONSUMPTION.put("Hair Dryer", 100.0);
		DEVICE_CONSUMPTION.put("CFL", 10.0);
	}
}
