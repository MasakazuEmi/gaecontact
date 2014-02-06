package info.myspoon.gae.contacts.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T, PK> {
	public T select(PK pk) throws SQLException;
	public List<T> selectAll() throws SQLException;
	public PK insert(T entity) throws SQLException;
	public void update(T entity) throws SQLException;
	public void delete(PK pk) throws SQLException;
}
