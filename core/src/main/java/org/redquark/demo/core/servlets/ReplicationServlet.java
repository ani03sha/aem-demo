package org.redquark.demo.core.servlets;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * @author Anirudh Sharma
 *
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Servlet to replicate the passed contents",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/replication" })
public class ReplicationServlet extends SlingSafeMethodsServlet {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1860103923125331408L;

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ReplicationServlet.class);

	@Reference
	private Replicator replicator;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		log.info("----------< Processing starts >----------");

		try {

			String path = request.getParameter("contentPath");

			ResourceResolver resolver = request.getResourceResolver();

			Session session = resolver.adaptTo(Session.class);

			replicateContent(session, path);

			activatePageAssets(resolver, path);
			
			log.info("----------< Processing ends >----------");
			
			response.getWriter().println("Replicated content");

		} catch (Exception e) {

			log.error(e.getMessage(), e);
		}
	}

	private void activatePageAssets(ResourceResolver resolver, String path) {

		Set<String> pageAssetPaths = getPageAssetsPaths(resolver, path);

		if (pageAssetPaths == null) {

			return;
		}

		Session session = resolver.adaptTo(Session.class);

		for (String assetPath : pageAssetPaths) {
			replicateContent(session, assetPath);
		}
	}

	private Set<String> getPageAssetsPaths(ResourceResolver resolver, String pagePath) {

		PageManager pageManager = resolver.adaptTo(PageManager.class);

		Page page = pageManager.getPage(pagePath);

		if (page == null) {
			return new LinkedHashSet<>();
		}

		Resource resource = page.getContentResource();
		AssetReferenceSearch assetReferenceSearch = new AssetReferenceSearch(resource.adaptTo(Node.class),
				DamConstants.MOUNTPOINT_ASSETS, resolver);
		Map<String, Asset> assetMap = assetReferenceSearch.search();

		return assetMap.keySet();
	}

	private void replicateContent(Session session, String path) {

		try {
			replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
			log.info("Replicated: {}", path);
		} catch (ReplicationException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

}
