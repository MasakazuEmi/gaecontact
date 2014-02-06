package info.myspoon.gae.contacts.dao;

import java.sql.SQLException;

public abstract class DaoFactory {

	public enum DaoType {
		Sql,
	}

	public static DaoFactory createDaoFactory(DaoType type) {
		switch (type) {
		case Sql:
			return new SQLDaoFactory();

		default:
			throw new IllegalArgumentException("not support type.");
		}
	}

	public abstract <T, PK> IDao<T, PK> createDao(T defaultEntity, PK defaultKey) throws SQLException;
	public abstract void setAutoCommit(boolean autoCommit) throws SQLException;
	public abstract void commit() throws SQLException;
	public abstract void rollback() throws SQLException;
	public abstract void close() throws SQLException;
}
