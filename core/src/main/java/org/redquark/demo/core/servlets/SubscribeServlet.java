package org.redquark.demo.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 */
@Component(
		service = Servlet.class, 
		property = { 
				"sling.servlet.methods=" + HttpConstants.METHOD_POST,
				"sling.servlet.paths=" + "/bin/register" 
			}
		)
public class SubscribeServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -5219940127094912938L;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		// Getting parameters from the request
		String email = request.getParameter("email");
		Session session = null;
		try {
			if (email != null && !email.isEmpty()) {
				log.debug("Adding [{}] to subscription list", email);
				session = request.getResourceResolver().adaptTo(Session.class);

				// Get the root node
				Node root = session.getRootNode();

				Node subscriberNode = null;

				if (!root.hasNode("content/subscribers")) {
					subscriberNode = root.addNode("content/subscribers", "nt:unstructured");
				} else {
					subscriberNode = root.getNode("content/subscribers");
				}

				// Adding a new subscriber to the list
				Node subscriber = subscriberNode.addNode(session.getUserID());
				subscriber.setProperty("email", email);

				// Saving the session
				session.save();

				response.sendRedirect(StringUtils.replace(request.getResource().getPath(), "/jcr:content", ".html"));
			}
		} catch (RepositoryException | IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (session != null && !session.isLive()) {
				session.logout();
			}
		}
	}
}
