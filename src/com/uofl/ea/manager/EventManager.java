package com.uofl.ea.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.uofl.ea.model.CurrentEventVO;
import com.uofl.ea.model.EventVO;
import com.uofl.ea.util.DeviceConsumptionUtil;

/**
 * This class manages event processing.
 */
public class EventManager {

	// ~ Static Variables ----------------------------------------------------------------------------------------------

	/** The Singleton instance. */
	private static EventManager eventManager;

	//~ Static Methods -------------------------------------------------------------------------------------------------

	/**
	 * The singleton implementation.
	 * @return instance of event manager.
	 */
	public static synchronized EventManager getEventManager() {
		if(eventManager == null) {
			eventManager = new EventManager();
		}
		return eventManager;
	}

	//~ Instance Methods -----------------------------------------------------------------------------------------------

	/**
	 * This method processes a new event.
	 * @param event
	 */
	public void processNewEvent(EventVO event) {
		if ("ON".equalsIgnoreCase(event.getEventType())) {
			processOnEvent(event);
		} else if ("OFF".equalsIgnoreCase(event.getEventType())) {
			processOffEvent(event);
		}
	}

	/**
	 * This method process and new OFF event.
	 * @param event event details.
	 */
	private void processOffEvent(EventVO event) {

		// Save the event in events.
		Long eventId = saveEvent(event);

		if (eventId != null && eventId > 0) {
			
			// Ignore OFF event if there no current ON event
			CurrentEventVO currentOnEvent = fetchOnEventIfExist(event);

			if (currentOnEvent != null && currentOnEvent.getId() != null && currentOnEvent.getEventId() != null
					&& currentOnEvent.getEventTime() != null) {

				// If ON event is present, record the user_consumption && delete current ON event.
				long onSince = currentOnEvent.getEventTime().getTime();
				long offSince = event.getEventTime().getTime();
				long onFor = offSince - onSince;
				ConsumptionManager.getConsumptionManager().saveUserConsumption(event.getUserName(), event.getDeviceName(), currentOnEvent.getEventTime(), event.getEventTime(), ((onFor/1000.0) * DeviceConsumptionUtil.DEVICE_CONSUMPTION.get(event.getDeviceName())));
				deleteCurrntOn(currentOnEvent.getId());
			}

		} else {
			throw new RuntimeException("Error while creation of OFF event.");
		}

	}

	/**
	 * This method process and new ON event.
	 * @param event event details.
	 */
	private void processOnEvent(EventVO event) {

		// Save the event in events.
		Long eventId = saveEvent(event);
		
		if (eventId != null && eventId > 0) {

			// Ignore event if there is already an ON request for the device and user.
			boolean isOnEventExist = checkOnEventExist(event);

			// If ON event of not there, save the event in current_on_events.
			if (!isOnEventExist) {
				saveCurrentOnEvent(event, eventId);
			}
		} else {
			throw new RuntimeException("Error while creation of ON event.");
		}
	}

	/**
	 * This method saves event in event table.
	 * 
	 * @param event
	 *            event details.
	 */
	private Long saveEvent(EventVO event) {
		Connection conn = null;
		try {
			conn = ConnectionManger.getConnection();
			String insertTableSQL = "INSERT INTO events"
					+ "(user_name, device_name, event_type, event_time, distance) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, event.getUserName());
			preparedStatement.setString(2, event.getDeviceName());
			preparedStatement.setString(3, event.getEventType());
			preparedStatement.setTimestamp(4, event.getEventTime());
			preparedStatement.setDouble(5, event.getDistance());
			// execute insert SQL statement
			preparedStatement.executeUpdate();
			
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
            	return -1L;
            }
		} catch (SQLException ex) {
			System.err.println("Error while saving event due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after saving event due to: " + ex.getMessage());
					ex.printStackTrace();
					
				}
			}
		}
		return -1L;
	}

	/**
	 * This method returns true if ON event is recorded for user and device.
	 * 
	 * @param event
	 *            the event details.
	 * @return true if ON event is recorded for user and device.
	 */
	private boolean checkOnEventExist(EventVO event) {
		Connection conn = null;
		int count = 0;
		try {
			conn = ConnectionManger.getConnection();
			String countQuery = "select count(*) as exist from current_on_events where user_name = ? and device_name = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(countQuery);
			preparedStatement.setString(1, event.getUserName());
			preparedStatement.setString(2, event.getDeviceName());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("exist");
			}
		} catch (SQLException ex) {
			System.err.println("Error while checking ON request present due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after checking ON request present due to: "
							+ ex.getMessage());
					ex.printStackTrace();
				}
			}
		}

		return count > 0;
	}

	/**
	 * This method saves a current ON event.
	 * 
	 * @param event
	 *            the event details.
	 * @param eventId
	 *            parent event ID.
	 */
	private void saveCurrentOnEvent(EventVO event, Long eventId) {
		Connection conn = null;
		try {
			conn = ConnectionManger.getConnection();
			String insertTableSQL = "INSERT INTO current_on_events"
					+ "(event_id, user_name, device_name, event_time, distance) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, eventId);
			preparedStatement.setString(2, event.getUserName());
			preparedStatement.setString(3, event.getDeviceName());
			preparedStatement.setTimestamp(4, event.getEventTime());
			preparedStatement.setDouble(5, event.getDistance());
			// execute insert SQL statement
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Error while saving event due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after saving event due to: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method returns current event data if ON event exist.
	 * 
	 * @param event
	 *            event data.
	 * @return current event data if ON event exist.
	 */
	private CurrentEventVO fetchOnEventIfExist(EventVO event) {
		Connection conn = null;
		CurrentEventVO currentEventVO = null;
		try {
			conn = ConnectionManger.getConnection();
			String countQuery = "select id, event_id, event_time, device_name from current_on_events where user_name = ? and device_name = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(countQuery);
			preparedStatement.setString(1, event.getUserName());
			preparedStatement.setString(2, event.getDeviceName());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong(1);
				Long eventId = rs.getLong(2);
				Timestamp eventTime = rs.getTimestamp(3);
				String deviceName = rs.getString(4);
				currentEventVO = new CurrentEventVO(id, eventId, eventTime, deviceName);
			}
		} catch (SQLException ex) {
			System.err.println("Error while checking ON event present due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after checking ON event present due to: "
							+ ex.getMessage());
					ex.printStackTrace();
				}
			}
		}

		return currentEventVO;
	}

	/**
	 * This method deletes a current ON event.
	 * 
	 * @param id
	 *            id of the current ON event to delete.
	 */
	private void deleteCurrntOn(Long id) {
		Connection conn = null;
		try {
			conn = ConnectionManger.getConnection();
			String deleteSQL = "delete from current_on_events where id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
			preparedStatement.setLong(1, id);
			// execute delete SQL statement 
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Error while deleteing current ON event due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after eleteing current ON event due to: "
							+ ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method returns the list of ON events of user.
	 * 
	 * @param userName
	 *            user name
	 * @return list of ON events of user.
	 */
	public List<CurrentEventVO> fetchOnEventsForUser(String userName) {
		Connection conn = null;
		List<CurrentEventVO> resultList = new ArrayList<>();
		try {
			conn = ConnectionManger.getConnection();
			String countQuery = "select id, event_id, event_time, device_name from current_on_events where user_name = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(countQuery);
			preparedStatement.setString(1, userName);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong(1);
				Long eventId = rs.getLong(2);
				Timestamp eventTime = rs.getTimestamp(3);
				String deviceName = rs.getString(4);
				resultList.add(new CurrentEventVO(id, eventId, eventTime, deviceName));
			}
		} catch (SQLException ex) {
			System.err.println("Error while fetching users ON events due to: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					System.err.println("Error while closing connection after fetching users ON events due to: "
							+ ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		return resultList;
	}
}
