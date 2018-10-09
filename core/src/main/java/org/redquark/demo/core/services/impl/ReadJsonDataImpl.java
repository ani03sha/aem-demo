package org.redquark.demo.core.services.impl;

import org.osgi.service.component.annotations.Component;
import static org.redquark.demo.core.constants.AppConstants.URL;
import org.redquark.demo.core.services.ReadJsonService;
import org.redquark.demo.core.utils.Network;

/**
 * @author Anirudh Sharma
 * 
 * Implementation of ReadJsonService
 */
@Component(immediate = true, service = ReadJsonService.class)
public class ReadJsonDataImpl implements ReadJsonService {


	/**
	 * Overridden method which will read the JSON data via an HTTP GET call
	 */
	@Override
	public String getData() {
		
		String response = Network.readJson(URL);
		
		return response;
	}

}
