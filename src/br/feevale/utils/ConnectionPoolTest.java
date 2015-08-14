package br.feevale.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConnectionPoolTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetConnection() {
		try {
			DbConnection connection = ConnectionPool.getConnection();
			assertTrue(connection.isBusy());
			connection.setFree();
			connection.getConnection().close();
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetSameConnection() {
		try {
			DbConnection connection1 = ConnectionPool.getConnection();
			connection1.setFree();
			DbConnection connection2 = ConnectionPool.getConnection();
			assertEquals(connection1, connection2);
			connection1.getConnection().close();
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetDifferentConnection() {
		try {
			DbConnection connection1 = ConnectionPool.getConnection();
			DbConnection connection2 = ConnectionPool.getConnection();
			assertNotEquals(connection1, connection2);
			connection1.getConnection().close();
			connection2.getConnection().close();
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

}
