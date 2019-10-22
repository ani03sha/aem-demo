package org.redquark.demo.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.designer.Style;

/**
 * @author Anirudh Sharma
 */
@Model(adaptables = SlingHttpServletRequest.class, resourceType = "demoproject/components/content/info")
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "true") })
public class InfoModel {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String message;
	private String logoImage;
	private String logoAltText;

	@SlingObject
	private SlingHttpServletRequest request;

	@Inject
	@Source("script-bindings")
	private Style currentStyle;

	@Inject
	@Via("resource")
	private String firstName;

	@Inject
	@Via("resource")
	private String lastName;

	@Inject
	@Via("resource")
	private String email;

	@PostConstruct
	protected void init() {
		log.info("Inside post construct");
		message = "This component demonstrate the use of AEM Sling Model Exporter";
		if (request != null) {
			this.message = "Request path: " + request.getRequestPathInfo().getResourcePath() + "\n";
		}
		message += "First Name: " + firstName + " \n";
		message += "Last Name: " + lastName + "\n";
		message += "Email Id: " + email + "\n";
		logoImage = currentStyle.get("logoImage", String.class);
		logoAltText = currentStyle.get("logoAltText", String.class);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the logoImage
	 */
	public String getLogoImage() {
		return logoImage;
	}

	/**
	 * @return the logoAltText
	 */
	public String getLogoAltText() {
		return logoAltText;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

}
