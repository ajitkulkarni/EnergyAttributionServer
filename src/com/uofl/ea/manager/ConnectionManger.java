package com.uofl.ea.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * This is a class which manages connection with DB.
 */
public class ConnectionManger {
	
	private static BasicDataSource source = new BasicDataSource();
	
	static {
		
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUsername("root");
		source.setPassword("admin");
		source.setUrl("jdbc:mysql://localhost:3306/eadb");
		source.setInitialSize(5);
		try {
			source.getConnection();
		} catch (SQLException e) {
			System.err.println("Error while getting connection from DS due to: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return source.getConnection();
	}
	
	
	

}
