package org.redquark.demo.core.services;

/**
 * @author Anirudh Sharma
 * 
 * This interface exposes the functionality of calling a JSON Web Service
 */
public interface HttpService {

	/**
	 * This method makes the HTTP call on the given URL
	 * 
	 * @param url
	 * @return {@link String}
	 */
	public String makeHttpCall();
}
