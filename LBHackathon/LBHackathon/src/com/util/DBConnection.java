package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public Connection getConnection() {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "lb_hacks", "lb_hacks");

		} catch (SQLException E) {
			System.out.println("exception");
		}
		return conn;
	}

}
