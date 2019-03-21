package com.x.generater.core;

public class MysqlDBInfo {
	private String driver = "com.mysql.jdbc.Driver";
	private String dbHost = "127.0.0.1";
	private String dbPort = "3306";
	private String dbName = "mydb";
	private String userName = "root";
	private String password = "123456";
	
	public MysqlDBInfo() {}
	
	public MysqlDBInfo(String dbHost, String dbPort, String dbName, String userName, String password) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
	}
	
	public String getConnUrl() {
		StringBuffer connUrl = new StringBuffer();
		connUrl.append("jdbc:mysql://");
		connUrl.append(this.dbHost);
		connUrl.append(":");
		connUrl.append(this.dbPort);
		connUrl.append("/");
		connUrl.append(this.dbName);
		connUrl.append("?characterEncoding=utf8&useSSL=true");
		return connUrl.toString();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
