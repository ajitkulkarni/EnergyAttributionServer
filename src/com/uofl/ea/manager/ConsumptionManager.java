package com.uofl.ea.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.uofl.ea.model.ConsumptionVO;

public class ConsumptionManager {
	
	private static ConsumptionManager consumptionManager;
	
	public static synchronized ConsumptionManager getConsumptionManager() {
		if(consumptionManager == null) {
			consumptionManager = new ConsumptionManager();
		}
		return consumptionManager;
	}

	public void saveUserConsumption(String userName, String deviceName, Timestamp eventStartTime, Timestamp eventEndTime,
			double consumption) {
		Connection conn = null;
		try {
			conn = ConnectionManger.getConnection();
			String insertTableSQL = "INSERT INTO user_consumptions"
					+ "(user_name, device_name, start_time, end_time, energy_consumption) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, deviceName);
			preparedStatement.setTimestamp(3, eventStartTime);
			preparedStatement.setTimestamp(4, eventEndTime);
			preparedStatement.setDouble(5, consumption);
			// execute insert SQL statement
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Error while saving energy consumption due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after saving energy consumption due to: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method returns the consumption of user
	 * @param userName
	 * @return
	 */
	public List<ConsumptionVO> fetchConsumptionByUser(String userName) {
		Connection conn = null;
		List<ConsumptionVO> resultList = new ArrayList<>();
		try {
			conn = ConnectionManger.getConnection();
			String countQuery = "select device_name, start_time, end_time, energy_consumption from user_consumptions where user_name = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(countQuery);
			preparedStatement.setString(1, userName);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String deviceName = rs.getString(1);
				Timestamp startTime = rs.getTimestamp(2);
				Timestamp endtTime = rs.getTimestamp(3);
				Double consumption = rs.getDouble(4);
				resultList.add(new ConsumptionVO(deviceName, startTime, endtTime, consumption));
			}
		} catch (SQLException ex) {
			System.err.println("Error while fetching users consumptions due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after fetching users consumptions due to: "
							+ ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		return resultList;
	}

}
