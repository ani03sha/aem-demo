package org.redquark.demo.core.jmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.management.DynamicMBean;
import javax.management.NotCompliantMBeanException;

import org.apache.commons.codec.binary.Base64;
import org.osgi.service.component.annotations.Component;
import org.redquark.demo.core.utils.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.jmx.annotation.AnnotatedStandardMBean;

/**
 * @author Anirudh Sharma
 * 
 * Implementation class for the MBean
 */
@Component(immediate = true, service = DynamicMBean.class, property = {"jmx.objectname = org.redquark.demo.core.jmx:type=System Info MBean"})
public class SystemInfoImpl extends AnnotatedStandardMBean implements SystemInfo {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SystemInfoImpl.class);
	
	/**
	 * Parameters to read user input
	 */
	
	public SystemInfoImpl() throws NotCompliantMBeanException {
		super(SystemInfo.class);
	}

	@Override
	public String getBundles(String protocol, String hostname, String port) {
		
		log.info("Logging the bundles in JSON");
		
		String url = protocol + "://" + hostname + ":" + port + "/system/console/bundles/.json";
		
		String result;

		if (protocol.equalsIgnoreCase("http")) {
			result = makeHttpCall(url);
		} else {
			result = Network.readJson(url);
		}
		return result;
	}
	
	@Override
	public String getComponents(String protocol, String hostname, String port) {
		
		log.info("Logging the components in JSON");
		
		String url = protocol + "://" + hostname + ":" + port + "/system/console/components/.json";
		
		String result;

		if (protocol.equalsIgnoreCase("http")) {
			result = makeHttpCall(url);
		} else {
			result = Network.readJson(url);
		}
		return result;
	}
	
	@Override
	public String getServices(String protocol, String hostname, String port) {

		log.info("Logging the services in JSON");

		String url = protocol + "://" + hostname + ":" + port + "/system/console/services/.json";

		String result;

		if (protocol.equalsIgnoreCase("http")) {
			result = makeHttpCall(url);
		} else {
			result = Network.readJson(url);
		}
		return result;
	}

	private String makeHttpCall(String requestURL) {
		
		URL url;
        try {
               url = new URL(requestURL);
               URLConnection uc;
               uc = url.openConnection();

               uc.setRequestProperty("X-Requested-With", "Curl");

               String userpass = "admin" + ":" + "admin";
               String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
               uc.setRequestProperty("Authorization", basicAuth);

               BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
               StringBuilder builder = new StringBuilder();
               String line = null;
               while ((line = reader.readLine()) != null) {
                     builder.append(line);
                     builder.append(System.getProperty("line.separator"));

               }
               String result = builder.toString();
               
               return result;
               
        } catch (IOException e) {
               e.printStackTrace();
        }
		
        return "";
	}
}
