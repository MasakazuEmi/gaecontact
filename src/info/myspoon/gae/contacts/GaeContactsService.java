package info.myspoon.gae.contacts;

import info.myspoon.gae.contacts.dao.DaoFactory;
import info.myspoon.gae.contacts.dao.DaoFactory.DaoType;
import info.myspoon.gae.contacts.dao.IDao;
import info.myspoon.gae.contacts.entity.ContactEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/contact")
public class GaeContactsService {
	private static Logger logger = Logger.getLogger(GaeContactsService.class.getName());

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private void setHeader() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
	}

	@OPTIONS
	public void doOptions() {
		logger.log(Level.INFO, "GaeContactsService#doOptions start.");
		setHeader();
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Collection<ContactEntity> getAllContact() throws SQLException {
		logger.log(Level.INFO, "GaeContactsService#getAllContact start.");

		setHeader();

		DaoFactory factory = DaoFactory.createDaoFactory(DaoType.Sql);
		try {
			IDao<ContactEntity, Integer> dao = factory.createDao(new ContactEntity(), new Integer(0));
			return dao.selectAll();
		} finally {
			factory.close();
		}
	}

	@OPTIONS
	@Path("{id}")
	public void doIdOptions(@PathParam("id") int id) throws SQLException {
		logger.log(Level.INFO, "GaeContactsService#doIdOptions start.");
		setHeader();
	}

	@Path("{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public ContactEntity getContact(@PathParam("id") int id) throws SQLException {
		logger.log(Level.INFO, "GaeContactsService#getContact start.");

		setHeader();

		DaoFactory factory = DaoFactory.createDaoFactory(DaoType.Sql);
		try {
			IDao<ContactEntity, Integer> dao = factory.createDao(new ContactEntity(), new Integer(0));
			return dao.select(id);
		} finally {
			factory.close();
		}
	}

	@Path("{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public void updateContact(@PathParam("id") int id, ContactEntity entity) throws SQLException {
		logger.log(Level.INFO, "GaeContactsService#updateContact start.");

		setHeader();

		DaoFactory factory = DaoFactory.createDaoFactory(DaoType.Sql);
		try {
			IDao<ContactEntity, Integer> dao = factory.createDao(new ContactEntity(), new Integer(0));
			dao.update(entity);
		} finally {
			factory.close();
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void insertContact(ContactEntity entity) throws SQLException {
		logger.log(Level.INFO, "GaeContactsService#insertContact start.");

		setHeader();

		DaoFactory factory = DaoFactory.createDaoFactory(DaoType.Sql);
		try {
			IDao<ContactEntity, Integer> dao = factory.createDao(new ContactEntity(), new Integer(0));
			dao.insert(entity);
		} finally {
			factory.close();
		}
	}

	@Path("{id}")
	@DELETE
	public void deleteContact(@PathParam("id") int id) throws SQLException {
		logger.log(Level.INFO, "GaeContactsService#deleteContact start.");

		setHeader();

		DaoFactory factory = DaoFactory.createDaoFactory(DaoType.Sql);
		try {
			IDao<ContactEntity, Integer> dao = factory.createDao(new ContactEntity(), new Integer(0));
			dao.delete(id);
		} finally {
			factory.close();
		}
	}
}
