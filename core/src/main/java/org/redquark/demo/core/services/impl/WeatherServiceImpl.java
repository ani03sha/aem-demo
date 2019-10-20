package org.redquark.demo.core.services.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.redquark.demo.core.services.WeatherService;
import org.redquark.demo.core.utils.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 */
@Component(service = WeatherService.class, name = "Weather Service")
public class WeatherServiceImpl implements WeatherService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Activate
	protected void activate(Map<String, String> config) {
		log.info("Weather Service - Activate");
	}

	@Override
	public String getWeatherFeed(String apiEndpoint) throws IOException {

		// Check if the endpoint is not null
		if (StringUtils.isBlank(apiEndpoint)) {
			return StringUtils.EMPTY;
		}

		// Create and HTTP client and get the data from the API
		String weatherFeed = Network.readJson(apiEndpoint);

		return weatherFeed;
	}

}
