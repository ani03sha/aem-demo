package org.redquark.demo.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

/**
 * @author Anirudh Sharma
 * 
 * This interface represents an OSGi configuration which can be found at - 
 * ./system/console/configMgr
 */
@ObjectClassDefinition(
		name = "Http Configuration", 
		description = "This configuration reads the values to make an HTTP call to a JSON webservice")
public @interface HttpConfiguration {

	/**
	 * This is a checkbox property which will indicate of the configuration is
	 * executed or not
	 * 
	 * @return {@link Boolean}
	 */
	@AttributeDefinition(
			name = "Enable config", 
			description = "This property indicates whether the configuration values will taken into account or not", 
			type = AttributeType.BOOLEAN)
	public boolean enableConfig();

	/**
	 * This method returns the protocol that is being used
	 * 
	 * @return Protocol
	 */
	@AttributeDefinition(
			name = "Protocol", 
			description = "Choose Protocol", 
			options = {
			@Option(label = "HTTP", value = "http"), @Option(label = "HTTPS", value = "https") })
	public String getProtocol();

	/**
	 * Returns the server
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Server", 
			description = "Enter the server name")
	public String getServer();

	/**
	 * Returns the endpoint
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Endpoint", 
			description = "Enter the endpoint")
	public String getEndpoint();
}
