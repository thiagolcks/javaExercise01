package br.feevale.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DbConnectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetConnection() {
		try {
			DbConnection db = new DbConnection();
			db.getConnection().close();
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testIsBusy() {
		try {
			DbConnection db = new DbConnection();
			db.reserve();
			assertTrue(db.isBusy());
			db.getConnection().close();
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetFree() {
		try {
			DbConnection db = new DbConnection();
			db.reserve();
			db.setFree();
			assertFalse(db.isBusy());
			db.getConnection().close();
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
