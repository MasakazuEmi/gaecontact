package info.myspoon.gae.contacts.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class SQLDaoFactory extends DaoFactory {
	private static Logger logger = Logger.getLogger(SQLDaoFactory.class.getName());

	static {
		logger.log(Level.INFO, "SQLDaoFactory#.static start.");

		try {
			DriverManager.registerDriver(new AppEngineDriver());
		} catch (SQLException e) {
			logger.log(Level.ALL, "registerDriver fail.", e);
		}
	}

	private Connection conn;

	protected SQLDaoFactory() {
		logger.log(Level.INFO, "SQLDaoFactory#SQLDaoFactory start.");

		try {
			final String instanceName = "nantoka-sql:nantoka1";
			final String dbName = "gaecontacts";
			conn = DriverManager.getConnection("jdbc:google:rdbms://" + instanceName + "/" + dbName);
		} catch(SQLException e) {
			logger.log(Level.ALL, "setup connection fail.", e);
		}

		logger.log(Level.INFO, "SQLDaoFactory#SQLDaoFactory end.");
	}

	@Override
	public <T, PK> IDao<T, PK> createDao(T defaultEntity, PK defaultKey) throws SQLException {
		logger.log(Level.INFO, "SQLDaoFactory#createDao start.");

		DaoSQLImpl<T, PK> dao = new DaoSQLImpl<T, PK>(conn);
		dao.initializeEntityInfo(defaultEntity, defaultKey);

		logger.log(Level.INFO, "SQLDaoFactory#createDao end.");
		return dao;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		logger.log(Level.INFO, "SQLDaoFactory#setAutoCommit start.");

		conn.setAutoCommit(autoCommit);
	}

	@Override
	public void commit() throws SQLException {
		logger.log(Level.INFO, "SQLDaoFactory#commit start.");

		conn.commit();
	}

	@Override
	public void rollback() throws SQLException {
		logger.log(Level.INFO, "SQLDaoFactory#rollback start.");

		conn.rollback();
	}
	@Override
	public void close() throws SQLException {
		logger.log(Level.INFO, "SQLDaoFactory#close start.");

		if(conn != null && !conn.isClosed())
			conn.close();

		logger.log(Level.INFO, "SQLDaoFactory#close end.");
	}
}
