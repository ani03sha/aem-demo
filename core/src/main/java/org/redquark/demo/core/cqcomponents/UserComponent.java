package org.redquark.demo.core.cqcomponents;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * @author Anirudh Sharma
 * 
 * This component lists all the users and groups present in the JCR
 */
public class UserComponent extends WCMUsePojo {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(UserComponent.class);

	private List<String> users, groups;

	private Session session;

	@Override
	public void activate() throws Exception {

		try {

			log.info("----------< Processing starts >----------");

			ResourceResolver resourceResolver = getResourceResolver();

			session = resourceResolver.adaptTo(Session.class);

			UserManager userManager = ((JackrabbitSession) session).getUserManager();

			Iterator<Authorizable> userIterator = userManager.findAuthorizables("jcr:primaryType", "rep:User");
			Iterator<Authorizable> groupIterator = userManager.findAuthorizables("jcr:primaryType", "rep:Group");

			users = new LinkedList<>();
			groups = new LinkedList<>();

			while (userIterator.hasNext()) {

				log.info("Getting user");

				Authorizable user = userIterator.next();

				if (!user.isGroup()) {
					log.info("User found: {}", user.getID());
					users.add(user.getID());
				}
			}

			while (groupIterator.hasNext()) {

				log.info("Getting group");

				Authorizable group = groupIterator.next();

				if (group.isGroup()) {
					log.info("Group found {}", group.getID());
					groups.add(group.getID());
				}
			}
		} catch (Exception e) {

			log.error(e.getMessage(), e);
		}
	}


	/**
	 * @return the users
	 */
	public List<String> getUsers() {
		return users;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

}
