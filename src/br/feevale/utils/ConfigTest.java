package br.feevale.utils;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlException;

public class ConfigTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConfig() {
		Config config;
		try {
			config = new Config();
		} catch (FileNotFoundException | YamlException e) {
			fail("Load Config without args should work.");
		}
		
		try {
			config = new Config("inexistentfile.yaml");
		} catch (FileNotFoundException | YamlException e) {
			// Success		
		} 
	}

	@Test
	public void testSingleton() {
		try {
			Config config = Config.getInstance();
		} catch (Exception e) {
			fail("Load Config without args should work.");
		} 
	}
	
	@Test
	public void testGet() {
		Config config;
		try {
			config = new Config();
			Map db = (Map) config.get("db");
			assertEquals("127.0.0.1", db.get("host"));
		} catch (FileNotFoundException | YamlException e) {
			fail("Sorry");
			e.printStackTrace();
		}
	}

}
