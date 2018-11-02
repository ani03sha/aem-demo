package org.redquark.demo.core.servlets;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anirudh Sharma
 *
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Servlet to read the configuration values",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/configValues" })
public class ConfigurationServlet extends SlingSafeMethodsServlet {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3393167579736572904L;
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ConfigurationServlet.class);
	
	@Reference
	private ConfigurationAdmin admin;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		
		try {
		
		String pid = request.getParameter("pid");
		
		Configuration config = admin.getConfiguration(pid);
		
		Dictionary<String, Object> properties = config.getProperties();
		
		if(properties != null) {
			
			response.getWriter().println(properties.toString());
		}
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
