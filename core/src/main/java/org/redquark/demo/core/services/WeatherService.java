package org.redquark.demo.core.services;

import java.io.IOException;

/**
 * @author Anirudh Sharma
 */
public interface WeatherService {

	/**
	 * Get Weather Feed using the given endpoint
	 * 
	 * @param apiEndpoint
	 * @return Weather Feed
	 * @throws IOException
	 */
	String getWeatherFeed(String apiEndpoint) throws IOException;
}
