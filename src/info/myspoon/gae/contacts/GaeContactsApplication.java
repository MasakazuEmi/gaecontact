package info.myspoon.gae.contacts;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

public class GaeContactsApplication extends Application {
	private static Logger logger = Logger.getLogger(GaeContactsApplication.class.getName());

	public Set<Class<?>> getClasses() {
		logger.log(Level.INFO, "GaeContactsApplication#getClasses start.");

		HashSet<Class<?>> set = new HashSet<Class<?>>();
		set.add(GaeContactsService.class);
		return set;
	}
}
