package org.redquark.demo.core.components;

import static org.redquark.demo.core.constants.AppConstants.WEATHER_API_ENDPOINT;

import java.io.IOException;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.redquark.demo.core.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 */
@Component(immediate = true, name = "Weather Details Component")
public class WeatherDetailsComponent {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	// Injecting the reference of WeatherService
	@Reference
	private WeatherService weatherService;

	@Activate
	protected void activate(Map<String, String> config) {
		log.info("Weather details component - Activated");

		// Set up a thread which wakes up every 5s and makes a call to the Weather API
		// to fetch the details
		Runnable task = () -> {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					Thread.sleep(5000);
					try {
						log.info(weatherService.getWeatherFeed(WEATHER_API_ENDPOINT));
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		};

		Thread weatherThread = new Thread(task);
		weatherThread.setName("Weather Details");
		weatherThread.start();
	}
}
