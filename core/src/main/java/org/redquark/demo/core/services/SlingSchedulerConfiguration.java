package org.redquark.demo.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * @author Anirudh Sharma
 * 
 * This is the configuration class that takes properties for a scheduler to run
 */
@ObjectClassDefinition(name = "SlingSchedulerConfiguration", description = "Sling scheduler configuration")
public @interface SlingSchedulerConfiguration {

	/**
	 * This method will return the name of the Scheduler
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Scheduler name", 
			description = "Name of the scheduler", 
			type = AttributeType.STRING)
	public String schdulerName() default "Custom Sling Scheduler Configuration";

	/**
	 * This method will check if the scheduler is concurrent or not
	 * 
	 * @return {@link Boolean}
	 */
	@AttributeDefinition(
			name = "Enabled", 
			description = "True, if scheduler service is enabled", 
			type = AttributeType.BOOLEAN)
	public boolean enabled() default false;
	
	/**
	 * This method returns the Cron expression which will decide how the scheduler will run
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Cron Expression", 
			description = "Cron expression used by the scheduler", 
			type = AttributeType.STRING)
	public String cronExpression() default "0 * * * * ?";

	/**
	 * This method returns a custom parameter just to show case the functionality
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Custom Parameter", 
			description = "Custom parameter to be displayed by the scheduler", 
			type = AttributeType.STRING)
	public String customParameter() default "AEM Scheduler Demo";
}
