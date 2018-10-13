package org.redquark.demo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

/**
 * @author Anirudh Sharma
 * 
 * User Component model class
 */
@Model(adaptables = Resource.class)
public class UserModel {

	@Inject 
	private String firstName;
	
	@Inject
	private String lastName;
	
	@Inject
	private String gender;
	
	@Inject
	private String country;

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
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
}
