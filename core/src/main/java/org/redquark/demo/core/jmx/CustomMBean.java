package org.redquark.demo.core.jmx;

import com.adobe.granite.jmx.annotation.Description;

/**
 * @author Anirudh Sharma
 *
 */
@Description("Sample MBean that exposes custom parameter")
public interface CustomMBean {

	@Description("Custom Parameter")
	String getCustomParameter(String parmeter);
}
