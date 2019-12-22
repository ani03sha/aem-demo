package org.redquark.demo.core.models;

import java.nio.charset.Charset;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class generates a random id for the main div tag of any component
 * 
 * @author Anirudh Sharma
 */
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class IdGeneratorModel {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Inject
	private String prefix;

	@SlingObject
	private SlingHttpServletRequest request;

	@PostConstruct
	protected void init() {

		// Getting the reference of the current node
		Node currentNode = request.getResource().adaptTo(Node.class);

		// Stored id, if any
		String storedId = null;

		try {
			// Getting the stored id from the node
			storedId = currentNode.getProperty("uniqueId").getString();
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
		}

		try {
			if (storedId == null || storedId.isEmpty()) {
				currentNode.setProperty("uniqueId", prefix + "_" + generateId(8));
				request.getResourceResolver().adaptTo(Session.class).save();
			}
		} catch (RepositoryException e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * This method generates the random id
	 * 
	 * @return {@link String}
	 */
	private String generateId(int n) {

		// Length is bounded by 256 Character
		byte[] array = new byte[256];
		new Random().nextBytes(array);

		String randomString = new String(array, Charset.forName("UTF-8"));
		// Create a StringBuffer to store the result
		StringBuffer r = new StringBuffer();

		// Append first 20 alphanumeric characters
		// from the generated random String into the result
		for (int k = 0; k < randomString.length(); k++) {
			char ch = randomString.charAt(k);
			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {
				r.append(ch);
				n--;
			}
		}
		// return the resultant string
		return r.toString();
	}
}
