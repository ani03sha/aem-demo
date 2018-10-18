package org.redquark.demo.core.servlets;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/**
 * @author Anirudh Sharma
 * 
 * This servlet uses the QueryBuilder API to fetch the results from the JCR
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/querybuilder" })
public class QueryBuilderServlet extends SlingSafeMethodsServlet {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2610051404257637265L;
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(QueryBuilderServlet.class);
	
	/**
	 * Injecting the QueryBuilder dependency
	 */
	@Reference
	private QueryBuilder builder;
	
	/**
	 * Session object
	 */
	private Session session;
	
	/**
	 * Overridden doGet() method which executes on HTTP GET request
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		try {

			log.info("----------< Executing Query Builder Servlet >----------");

			/**
			 * This parameter is passed in the HTTP call
			 */
			String param = request.getParameter("param");

			log.info("Search term is: {}", param);
			
			/**
			 * Get resource resolver instance
			 */
			ResourceResolver resourceResolver = request.getResourceResolver();
			
			/**
			 * Adapting the resource resolver to the session object
			 */
			session = resourceResolver.adaptTo(Session.class);
			
			/**
			 * Map for the predicates
			 */
			Map<String, String> predicate = new HashMap<>();

			/**
			 * Configuring the Map for the predicate
			 */
			predicate.put("path", "/content/dam");
			predicate.put("type", "dam:Asset");
			predicate.put("group.p.or", "true");
			predicate.put("group.1_fulltext", param);
			predicate.put("group.1_fulltext.relPath", "jcr:content");
			
			/**
			 * Creating the Query instance
			 */
			Query query = builder.createQuery(PredicateGroup.create(predicate), session);
			
			query.setStart(0);
			query.setHitsPerPage(20);
			
			/**
			 * Getting the search results
			 */
			SearchResult searchResult = query.getResult();
			
			int count = 0;
			
			for(Hit hit : searchResult.getHits()) {
				
				String path = hit.getPath();
				
				response.getWriter().println(++count+". " + path);
			}
		} catch (Exception e) {

			log.error(e.getMessage(), e);
		} finally {
			
			if(session != null) {
				
				session.logout();
			}
		}
	}

}
