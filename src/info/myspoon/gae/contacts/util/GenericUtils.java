package info.myspoon.gae.contacts.util;

public class GenericUtils {
	@SuppressWarnings("unchecked")
	public static <T> T autoCast(Object src) {
		return (T) src;
	}
}
