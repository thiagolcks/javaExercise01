package br.feevale.utils;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import org.junit.Before;
import org.junit.Test;

import br.feevale.client.Client;
import br.feevale.product.Product;

public class PersistanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetFields() {
		Client client1 = new Client();
		client1.setName("John Doe");
		Persistence persistence = new Persistence(client1);
		ArrayList<Field> fields = persistence.getFields();
		assertEquals(fields.size(), 8);
		assertEquals(fields.get(0).getName(), "id");
	}
	

	@Test
	public void testGetFieldValue() {
		Client client1 = new Client();
		client1.setName("John Doe");
		client1.setId(667);
		Persistence persistence = new Persistence(client1);
		ArrayList<Field> fields = persistence.getFields();
		assertEquals(persistence.getFieldValue(fields.get(1)), "John Doe");
		assertEquals(persistence.getFieldValue(fields.get(0)), 667);
	}
	

	@Test
	public void testGetFieldNames() {
		Client client1 = new Client();
		Persistence persistence = new Persistence(client1);
		ArrayList<Field> fields = persistence.getFields();
		String[] names = persistence.getFieldNames(fields);
		assertEquals(fields.size(), names.length);
		for (int i = 0; i < names.length; i++) {
			assertEquals(names[i], fields.get(i).getName());
		}
	}
	
	@Test
	public void testGetTableName() {
		// With Annotation		
		Client client1 = new Client();
		Persistence persistence1 = new Persistence(client1);
		assertEquals(persistence1.getTableName(), "clients");
		
		// Without Annotation		
		Product product1 = new Product();
		Persistence persistence2 = new Persistence(product1);
		assertEquals(persistence2.getTableName(), "product");
	}
	
	@Test
	public void testGetInsertSQL() {
		Product product1 = new Product();
		Persistence persistence = new Persistence(product1);
		String[] names = persistence.getFieldNames();
		String sql;
		try {
			sql = persistence.getInsertSQL(names);
			assertEquals(sql, "INSERT INTO product (id, name, price) VALUES (?, ?, ?);");
		} catch (InvalidAttributesException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetInsertSQLWithoutFields() {
		Product product1 = new Product();
		Persistence persistence = new Persistence(product1);
		String[] names = new String[0];
		try {
			persistence.getInsertSQL(names);
			fail("It should have throwed an exception.");
		} catch (InvalidAttributesException e) {
			// Success			
		}
	}

	@Test
	public void testCreate() {
		Product product = this.makeAProduct();
		Persistence persistence = new Persistence(product);
		try {
			assertEquals(product.getId(), 0);
			persistence.create();
			assertTrue(product.getId() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testUpdateSQL() {
		Product product1 = new Product();
		Persistence persistence = new Persistence(product1);
		ArrayList<Field> fields = persistence.getFields();
		String sql;
		try {
			sql = persistence.getUpdateSQL(fields);
			assertEquals(sql, "UPDATE product SET name = ?, price = ? WHERE id = ?;");
		} catch (InvalidAttributesException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetUpdateSQLWithoutFields() {
		Product product1 = new Product();
		Persistence persistence = new Persistence(product1);
		ArrayList<Field> fields= new ArrayList<Field>();
		try {
			persistence.getUpdateSQL(fields);
			fail("It should have throwed an exception.");
		} catch (InvalidAttributesException e) {
			// Success			
		}
	}
	
	@Test
	public void testUpdate() {
		Product product = this.makeAProduct();
		Persistence persistence = new Persistence(product);
		try {
			persistence.create();
			String oldName = product.getName();
			String newName = oldName + " - UPDATED";
			product.setName(newName);
			persistence.save();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeleteSQL() {
		Product product1 = new Product();
		Persistence persistence = new Persistence(product1);
		String sql;
		sql = persistence.getDeleteSQL();
		assertEquals(sql, "DELETE FROM product WHERE id = ?;");
	}
	
	@Test
	public void testDestroy() {
		Product product = this.makeAProduct();
		Persistence persistence = new Persistence(product);
		try {
			persistence.create();
			persistence.destroy();
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private Product makeAProduct() {
		Product product = new Product();
		Random random = new Random();
		Integer rnd = random.nextInt(10 - 1 + 1) * random.nextInt(10 - 1 + 1) + random.nextInt(10 - 1 + 1);
		product.setName("Product " + rnd.toString());
		product.setPrice(random.nextInt(10 - 1 + 1));
		return product;
	}

}
