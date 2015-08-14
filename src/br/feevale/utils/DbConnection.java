package br.feevale.utils;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;

public class DbConnection {
	
	private Connection connection;
	private boolean isBusy;

	public DbConnection() throws Exception {
		Config config = Config.getInstance();
		Map<String, String> configDb = (Map<String, String>) config.get("db");
		Class.forName(configDb.get("driver"));
		this.connection = DriverManager.getConnection(configDb.get("host") + configDb.get("dbname"), configDb.get("user"), configDb.get("password"));
	}

	public Connection getConnection() {
		return connection;
	}
	
	public boolean isBusy() {
		return this.isBusy;
	}
	
	public void reserve() {
		this.isBusy = true;
	}

	public void setFree() {
		this.isBusy = false;
	}
	
}
