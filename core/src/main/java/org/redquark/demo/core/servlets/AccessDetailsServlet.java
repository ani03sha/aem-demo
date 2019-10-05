package org.redquark.demo.core.servlets;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 */
@Component(
		service = Servlet.class, 
		property = { 
				"sling.servlet.methods=" + HttpConstants.METHOD_GET,
				"sling.servlet.resourceTypes=" + "demoproject/components/structure/page",
				"sling.servlet.extensions=" + "html" 
				}
		)
public class AccessDetailsServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -3690971782079217260L;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		Session session = null;

		try {

			// Getting the current resource reference
			Resource currentResource = request.getResource();
			
			// Getting the resource resolver
			ResourceResolver resolver = request.getResourceResolver();

			// Getting JCR node equivalent to resource
			Node currentNode = currentResource.adaptTo(Node.class);

			if (!currentNode.hasNode("jcr:data")) {
				// Adding a node
				Node dataNode = currentNode.addNode("jcr:data");
				session = resolver.adaptTo(Session.class);
				String currentUser = session.getUserID();
				log.info("{}/jcr:data is created by {}", currentResource.getPath(), currentUser);
				dataNode.setProperty("creator", currentUser);

				session.save();

				response.sendRedirect(StringUtils.replace(currentResource.getPath(), "/jcr:content", ".html"));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}
		}
	}
}
