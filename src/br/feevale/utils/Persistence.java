package br.feevale.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

/**
 * @question Pq não fazer essa classe como superclasse de Product e Client?
 * @author thiago
 *
 */
public class Persistence {
	
	protected Object obj;

	public Persistence(Object obj) {
		this.obj = obj;
	}
	
	public ArrayList<Field> getFields() {
		ArrayList<Field> result = new ArrayList<Field>();
		Field[] fields = this.obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(IgnoreField.class) == null) {
				result.add(field);
			}
		}
		return result;
	}

	public String[] getFieldNames() {
		ArrayList<Field> fields = this.getFields();
		String[] names = new String[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			names[i] = fields.get(i).getName();
		}
		return names;
	}
	
	public String[] getFieldNames(ArrayList<Field> fields) {
		String[] names = new String[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			names[i] = fields.get(i).getName();
		}
		return names;
	}
	
	public Object getFieldValue(Field field) {
		Object result = null;
		String fieldName = field.getName();
		String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			Method theMethod = this.obj.getClass().getMethod(methodName);
			if (theMethod != null) {
				result = theMethod.invoke(this.obj);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public String getTableName() {
		String tableName;
		if (this.obj.getClass().getAnnotation(TableName.class) != null) {
			tableName = this.obj.getClass().getAnnotation(TableName.class).value();
		} else {
			tableName = this.obj.getClass().getSimpleName();
		}
		return tableName.toLowerCase();
	}
	
	private void runPSUpdate(String sql, ArrayList<Field> fields) throws Exception {
		DbConnection connection = ConnectionPool.getConnection();
		try {
			PreparedStatement ps = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < fields.size(); i++) {
				Object value = this.getFieldValue(fields.get(i));
				ps.setObject(i+1, value);
			}
			String query = ps.toString();
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			
			if (keys.next()) {
				Integer id = keys.getInt(1);
				Method setId = this.obj.getClass().getMethod("setId", new Class[]{Integer.class});
				if (setId != null) {
					setId.invoke(this.obj, id);
				}
			}
			
		}
		finally {
			connection.setFree();
		}
	}
	
	public void create() throws Exception {
		String sql = this.getInsertSQL(this.getFieldNames());
		this.runPSUpdate(sql, this.getFields());
	}
	
	public String getInsertSQL(String[] fields) throws InvalidAttributesException {
		String tableName = this.getTableName();
		if (fields.length == 0) {
			throw new InvalidAttributesException("It should have at least one field");
		}
		
		String field_names = "";
		String field_values = "";
		for (int i = 0; i < fields.length; i++) {
			field_names += fields[i] + ", ";
			field_values += "?, ";
		}
		field_names = field_names.substring(0, (field_names.length()-2));
		field_values = field_values.substring(0, field_values.length()-2);
		
		String sql = "INSERT INTO " + tableName + " (" + field_names + ") VALUES (" + field_values + ");";
		return sql;
	}
	
	public String getUpdateSQL(ArrayList<Field> fields) throws InvalidAttributesException {
		String tableName = this.getTableName();
		
		if (fields.size() == 0) {
			throw new InvalidAttributesException("It should have at least one field");
		}
		
		String field_setting = "";
		for (Field field : fields) {
			if (field.getAnnotation(DontUpdate.class) == null) {
				field_setting += field.getName() + " = ?, ";
			}
		}
		field_setting = field_setting.substring(0, (field_setting.length()-2));
		
		String sql = "UPDATE " + tableName + " SET " + field_setting + " WHERE id = ?;";
		return sql;
	}
	
	public void save() throws Exception {
		String sql = this.getUpdateSQL(this.getFields());
		ArrayList<Field> fields = this.getFields();
		Field idField = fields.get(0);
		fields.remove(0);
		fields.add(idField);
		this.runPSUpdate(sql, fields);
	}

	public String getDeleteSQL() {
		String tableName = this.getTableName();
		String sql = "DELETE FROM " + tableName + " WHERE id = ?;";
		return sql;
	}
	
	public void destroy() throws Exception {
		String sql = this.getDeleteSQL();
		ArrayList<Field> fields = this.getFields();
		Field idField = fields.get(0);
		fields = new ArrayList<Field>();
		fields.add(idField);
		this.runPSUpdate(sql, fields);
	}
	
}
