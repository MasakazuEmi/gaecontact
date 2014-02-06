package info.myspoon.gae.contacts.dao;

import info.myspoon.gae.contacts.dao.annotaion.Column;
import info.myspoon.gae.contacts.dao.annotaion.Entity;
import info.myspoon.gae.contacts.dao.annotaion.Id;
import info.myspoon.gae.contacts.util.GenericUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PrePersist;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DaoSQLImpl<T, PK> implements IDao<T, PK>{
	private static Logger logger = Logger.getLogger(DaoSQLImpl.class.getName());

	private Connection conn = null;
	private T defaultEntity;
	private PK defaultKey;
	private String tableName = null;
	private String keyName = null;
	private Field keyField = null;
	private Map<Field, String> fieldToSqlName = new HashMap<Field, String>();

	protected DaoSQLImpl(Connection conn) {
		this.conn = conn;
	}

	protected void initializeEntityInfo(T defaultEntity, PK defaultKey) {
		logger.log(Level.INFO, "DaoSQLImpl#initializeEntityInfo start.");

		this.defaultEntity = defaultEntity;
		this.defaultKey = defaultKey;

		Entity entityAnnotation = defaultEntity.getClass().getAnnotation(Entity.class);
		if(entityAnnotation == null) {
			throw new IllegalArgumentException(defaultEntity.getClass().getName() + " has no Entity Annotation.");
		}
		tableName = entityAnnotation.value();
		if(tableName == null || tableName.length() == 0) {
			throw new IllegalArgumentException(defaultEntity.getClass().getName() + " has no table name.");
		}
		for(Field f : defaultEntity.getClass().getDeclaredFields()) {
			for(Annotation annotaion : f.getAnnotations()) {
				if(annotaion instanceof Id && keyName == null) {
					keyName = ((Id)annotaion).value();
					keyField = f;
					fieldToSqlName.put(f, ((Id)annotaion).value());
				}
				if(annotaion instanceof Column) {
					fieldToSqlName.put(f, ((Column)annotaion).value());
				}
			}
		}
		logger.log(Level.INFO, "initializeEntityInfo.");
	}

	@SuppressWarnings("unchecked")
	protected T createInstance() {
		logger.log(Level.INFO, "DaoSQLImpl#createInstance start.");

		try {
			return (T)defaultEntity.getClass().newInstance();
		} catch (Exception e) {
			logger.log(Level.ALL, "new instance fail.", e);
			return null;
		}
	}

	@Override
	public T select(PK pk) throws SQLException {
		logger.log(Level.INFO, "DaoSQLImpl#select start.");

		String sql = "SELECT * FROM " + tableName + " WHERE " + keyName + " = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setObject(1, pk);
		ResultSet rs = ps.executeQuery();
		List<T> result = createEntity(rs);
		if(result != null && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<T> selectAll() throws SQLException {
		logger.log(Level.INFO, "DaoSQLImpl#selectAll start.");

		String sql = "SELECT * FROM " + tableName;
		ResultSet rs = conn.createStatement().executeQuery(sql);
		return createEntity(rs);
	}

	private List<T> createEntity(ResultSet rs) throws SQLException {
		logger.log(Level.INFO, "DaoSQLImpl#createEntity start.");

		List<T> result = new ArrayList<T>();
		while(rs.next()) {
			T record = createInstance();
			for(Field field : fieldToSqlName.keySet()) {
				try {
					field.setAccessible(true);
					field.set(record, rs.getObject(fieldToSqlName.get(field)));
				} catch (Exception e) {
					logger.log(Level.ALL, "selectAll fail.", e);
					throw new SQLException(e);
				}
			}
			result.add(record);
		}
		return result;
	}

	@Override
	public PK insert(T entity) throws SQLException {
		logger.log(Level.INFO, "DaoSQLImpl#insert start.");

		return insertUpdateImpl(entity, true);
	}

	@Override
	public void update(T entity) throws SQLException {
		logger.log(Level.INFO, "DaoSQLImpl#insert start.");

		insertUpdateImpl(entity, false);
	}

	private PK insertUpdateImpl(T entity, boolean insert) throws SQLException {
		HashMap<String, Object> fieldValues;
		try {
			fieldValues = createEntityValues(entity);
		} catch (Exception e) {
			logger.log(Level.ALL, "createEntityValues fail.", e);
			throw new SQLException(e);
		}

		String sql = "";
		if(insert) {
			fieldValues.remove(keyName);
			String val = StringUtils.repeat("?,", fieldValues.size());
			val = val.substring(0, val.length() - 1);
			sql = "INSERT INTO " + tableName + " "
					+ " (" + StringUtils.join(fieldValues.keySet(), ',') + ")"
					+ " VALUES (" + val + ")";
		} else {
			sql = "UPDATE " + tableName + " SET " + StringUtils.join(fieldValues.keySet(), "=?,") + "=?"
					+ " WHERE " + keyName + "=?";

		}

		PreparedStatement pstm = conn.prepareStatement(sql);
		int index = 1;
		for(String field : fieldValues.keySet()) {
			pstm.setObject(index++, fieldValues.get(field));
		}
		if(!insert) {
			pstm.setObject(index++, fieldValues.get(keyName));
		}
		int num = pstm.executeUpdate();
		if(num == 1) {
			try {
				return GenericUtils.autoCast(keyField.get(entity));
			} catch (IllegalArgumentException e) {
				throw new SQLException(e);
			} catch (IllegalAccessException e) {
				throw new SQLException(e);
			}
		}
		throw new SQLException("result false.");
	}

	private HashMap<String, Object> createEntityValues(T entity) throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		for(Field field : fieldToSqlName.keySet()) {
			String fieldName = fieldToSqlName.get(field);
			field.setAccessible(true);
			Object val = field.get(entity);
			values.put(fieldName, val);
		}
		return values;
	}

	@Override
	public void delete(PK pk) throws SQLException {
		String sql = "DELETE FROM " + tableName + " WHERE " + keyName + "=?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setObject(1, pk);
		int num = pstm.executeUpdate();
		if(num != 1) {
			throw new SQLException("delete failed.");
		}
	}
}
