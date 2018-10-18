package org.redquark.demo.core.jmx;

import javax.management.DynamicMBean;
import javax.management.NotCompliantMBeanException;

import org.osgi.service.component.annotations.Component;

import com.adobe.granite.jmx.annotation.AnnotatedStandardMBean;

/**
 * @author Anirudh Sharma
 *
 */
@Component(immediate = true, service = DynamicMBean.class, property = {"jmx.objectname = org.redquark.demo.core.jmx:type=Custom MBean"}) 
public class CustomMBeanImpl extends AnnotatedStandardMBean implements CustomMBean {

	public CustomMBeanImpl() throws NotCompliantMBeanException {
		super(CustomMBean.class);
	}

	
	@Override
	public String getCustomParameter(String customParameter) {
		
		return customParameter;
	}

}
