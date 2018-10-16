package org.redquark.demo.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.redquark.demo.core.services.SlingSchedulerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 * 
 * A Sling Scheduler demo using OSGi R6 annotations
 *
 */
@Component(immediate = true, service = CustomScheduler.class)
@Designate(ocd = SlingSchedulerConfiguration.class)
public class CustomScheduler implements Runnable {
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CustomScheduler.class);
	
	/**
	 * Custom parameter that is to be read from the configuration
	 */
	private String customParameter;
	
	/**
	 * Id of the scheduler based on its name
	 */
	private int schedulerId;
	
	/**
	 * Scheduler instance injected
	 */
	@Reference
	private Scheduler scheduler;

	/**
	 * Activate method to initialize stuff
	 * 
	 * @param config
	 */
	@Activate
	protected void activate(SlingSchedulerConfiguration config) {
		
		/**
		 * Getting the scheduler id
		 */
		schedulerId = config.schdulerName().hashCode();
		
		/**
		 * Getting the custom parameter
		 */
		customParameter = config.customParameter();
	}
	
	/**
	 * Modifies the scheduler id on modification
	 * 
	 * @param config
	 */
	@Modified
	protected void modified(SlingSchedulerConfiguration config) {
		
		/**
		 * Removing the scheduler
		 */
		removeScheduler();
		
		/**
		 * Updating the scheduler id
		 */
		schedulerId = config.schdulerName().hashCode();
		
		/**
		 * Again adding the scheduler
		 */
		addScheduler(config);
	}
	
	/**
	 * This method deactivates the scheduler and removes it
	 * @param config
	 */
	@Deactivate
	protected void deactivate(SlingSchedulerConfiguration config) {
		
		/**
		 * Removing the scheduler
		 */
		removeScheduler();
	}
	
	/**
	 * This method removes the scheduler
	 */
	private void removeScheduler() {
		
		log.info("Removing scheduler: {}", schedulerId);
		
		/**
		 * Unscheduling/removing the scheduler
		 */
		scheduler.unschedule(String.valueOf(schedulerId));
	}
	
	/**
	 * This method adds the scheduler
	 * 
	 * @param config
	 */
	private void addScheduler(SlingSchedulerConfiguration config) {
		
		/**
		 * Check if the scheduler is enabled
		 */
		if(config.enabled()) {
			
			/**
			 * Scheduler option takes the cron expression as a parameter and run accordingly
			 */
			ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());
			
			/**
			 * Adding some parameters
			 */
			scheduleOptions.name(config.schdulerName());
			scheduleOptions.canRunConcurrently(false);
			
			/**
			 * Scheduling the job
			 */
			scheduler.schedule(this, scheduleOptions);
			
			log.info("Scheduler added");
			
		} else {
			
			log.info("Scheduler is disabled");
			
		}
	}
	
	/**
	 * Overridden run method to execute Job
	 */
	@Override
	public void run() {

		log.info("Custom Scheduler is now running using the passed custom paratmeter, customParameter {}", customParameter);
		
	}

}
