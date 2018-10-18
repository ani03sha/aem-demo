package org.redquark.demo.core.jmx;

import com.adobe.granite.jmx.annotation.Description;

/**
 * @author Anirudh Sharma
 * 
 * This interface exposes the input parameter for the MBean
 */
@Description("Input parameters for getting the System information")
public interface SystemInfo {

	@Description("Enter the protocol, hostname and port of the server")
	String getBundles(String protocol, String hostName, String port);
	
	@Description("Enter the protocol, hostname and port of the server")
	String getComponents(String protocol, String hostName, String port);
	
	@Description("Enter the protocol, hostname and port of the server")
	String getServices(String protocol, String hostName, String port);
}
