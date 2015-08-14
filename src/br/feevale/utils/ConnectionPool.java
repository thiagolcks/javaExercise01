package br.feevale.utils;

import java.util.ArrayList;

/**
 * @question Porque trabalhar com ela instanciada e não apenas com ele estática?
 */
public class ConnectionPool {
	
	private static ConnectionPool self = null;
	private ArrayList<DbConnection> connections;
	
	private ConnectionPool() {
		this.connections = new ArrayList<DbConnection>();
	}
	
	public static DbConnection getConnection() throws Exception {
		if (ConnectionPool.self == null) {
			ConnectionPool.self = new ConnectionPool();
		}
		return ConnectionPool.self.getDbConnection();
	}

	private synchronized DbConnection getDbConnection() throws Exception {
		for (DbConnection c : this.connections) {
			if (! c.isBusy()) {
				c.reserve();
				return c;
			}
		}
		
		DbConnection c = new DbConnection();
		c.reserve();
		this.connections.add(c);
		return c;
	}

}
