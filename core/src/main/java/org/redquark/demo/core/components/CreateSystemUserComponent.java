package org.redquark.demo.core.components;

import static org.redquark.demo.core.constants.AppConstants.INTERMEDIATE_PATH;
import static org.redquark.demo.core.constants.AppConstants.SUBSERVICE_NAME;
import static org.redquark.demo.core.constants.AppConstants.SERVICE_USER_ID;
import static org.redquark.demo.core.constants.AppConstants.SERVICE_USER_GROUP_NAME;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.redquark.demo.core.services.ManipulateUserMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 */
@Component/*(immediate = true)*/
public class CreateSystemUserComponent {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private ManipulateUserMapperService manipulateUserMapperService;

	@Activate
	protected void activate(BundleContext bundleContext) {
		String bundleId = bundleContext.getBundle().getSymbolicName();
		// Create system user
		createSystemUser(bundleId);
	}

	/**
	 * This method creates a system user programmatically using the given
	 * configuration
	 * 
	 * @param bundleId
	 */
	private void createSystemUser(String bundleId) {
		try (ResourceResolver resourceResolver = resourceResolverFactory.getThreadResourceResolver()) {
			// Adapt resource resolver to get the Session
			Session session = resourceResolver.adaptTo(Session.class);
			// This provides access to and means to maintain authorizable objects i.e. users
			// and groups
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);
			// Get the reference of the User object
			User user = (User) userManager.getAuthorizable(SERVICE_USER_ID);
			// If the user does not exist
			if (user == null) {
				// Create system user
				user = userManager.createSystemUser(SERVICE_USER_ID, INTERMEDIATE_PATH);
				// Save the session
				session.save();
			}
			// Adding this user to the specified group
			Group group = (Group) userManager.getAuthorizable(SERVICE_USER_GROUP_NAME);
			group.addMember(userManager.getAuthorizable(SERVICE_USER_ID));
			setBundleProperty(bundleId, SERVICE_USER_ID, SUBSERVICE_NAME);
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void setBundleProperty(String bundleId, String systemUserId, String subserviceName) {
		manipulateUserMapperService.setProperty(bundleId, systemUserId, subserviceName);
	}
}
