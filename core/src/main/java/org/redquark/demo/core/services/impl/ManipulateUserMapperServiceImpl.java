package org.redquark.demo.core.services.impl;

import static org.redquark.demo.core.constants.AppConstants.USER_MAPPER_SERVICE_REFERENCE;
import static org.redquark.demo.core.constants.AppConstants.USER_MAPPING;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.redquark.demo.core.services.ManipulateUserMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 *
 */
@Component(service = ManipulateUserMapperService.class, property = {
		Constants.SERVICE_ID + "= OSGi Configuration Service",
		Constants.SERVICE_DESCRIPTION + "= This service gets and sets the OSGi configurations" })
public class ManipulateUserMapperServiceImpl implements ManipulateUserMapperService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	// This service gets the OSGi services
	@Reference
	private ConfigurationAdmin configurationAdmin;

	@Override
	public void setProperty(String bundleId, String systemUserId, String subserviceName) {

		try {
			Configuration configuration = configurationAdmin.getConfiguration(USER_MAPPER_SERVICE_REFERENCE);
			// Get the properties of the configuration
			Dictionary<String, Object> props = configuration.getProperties();
			// Check if the properties are present
			if (props == null) {
				props = new Hashtable<>();
			}
			String propertyValue = bundleId + ":" + subserviceName + "=" + systemUserId;
			props.put(USER_MAPPING, propertyValue);
			configuration.update(props);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
