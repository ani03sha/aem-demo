package org.redquark.demo.core.datasource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Activate;
import org.redquark.demo.core.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

/**
 * @author Anirudh Sharma
 *
 */
public class Datasource extends WCMUsePojo {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(Datasource.class);
	
	@Activate
	public void activate() throws Exception {
		
		try {
			
			final ResourceResolver resolver = getResourceResolver();
			
			String dataPath = ResourceUtil.getValueMap(getResource().getChild("datasource")).get("data_path", String.class);
			
			log.info("Data path is: {}", dataPath);
			
			Resource resource = resolver.getResource(AppConstants.DATASOURCE_PATH + dataPath);
			
			log.info("Resource: {}", resource);
			
			Map<String, String> data = new LinkedHashMap<>();
			
			Node currentNode = resource.adaptTo(Node.class);
			
			NodeIterator nodeIterator = currentNode.getNodes();
			
			while(nodeIterator.hasNext()) {
				
				Node node = nodeIterator.nextNode();
				
				if (!node.hasProperty("value")) {
					data.put(node.getName(), node.getProperty("name").getString());
				} else if (node.hasProperty("name")) {
					data.put(node.getProperty("value").getValue().getString(),
							node.getProperty("name").getValue().getString());
				} else {
					data.put(node.getProperty("value").getValue().getString(), node.getName());
				}
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			DataSource ds = new SimpleDataSource(new TransformIterator<>(data.keySet().iterator(), new Transformer() {

				@Override
				public Object transform(Object o) {

					String dropValue = (String) o;

					ValueMap vm = new ValueMapDecorator(new HashMap<>());

					vm.put("value", dropValue);
					vm.put("text", data.get(dropValue));

					return new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm);
				};
			}));
			
			getRequest().setAttribute(DataSource.class.getName(), ds);
			
		} catch (Exception e) {
			
			log.error(e.getMessage(), e);
		}

	}

}
