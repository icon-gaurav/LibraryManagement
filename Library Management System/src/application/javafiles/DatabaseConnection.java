package application.javafiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.concurrent.Task;

public class DatabaseConnection extends Task<Connection> {
	private static Connection conn;
	private String driverpath="com.mysql.jdbc.Driver";
	private String url="jdbc:mysql://localhost/library?useSSL=false";
	private String username;
	private String password;
	
	public DatabaseConnection(String username,String password) {
		this.username=username;
		this.password=password;
	}
	
	public static Connection getDataBaseConnection(String username,String password) {
		String driverpath="com.mysql.jdbc.Driver";
		String url="jdbc:mysql://localhost/library?useSSL=false";
		try {
			Class.forName(driverpath).newInstance();
			conn=DriverManager.getConnection(url,username,password);
			return conn;
		}
		catch(Exception e) {
			return null;
		}
	}
	public void closeConnection() {
		try {
			conn.close();
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	protected Connection call() throws Exception {
		try {
			Class.forName(driverpath).newInstance();
			conn=DriverManager.getConnection(url,username,password);
			updateMessage("Connection is Established");
			return conn;
		}
		catch(Exception e) {
			return null;
		}
	}
}
