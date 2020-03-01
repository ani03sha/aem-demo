package org.redquark.demo.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.event.WorkflowEvent;
import com.day.cq.workflow.exec.Workflow;
import com.day.cq.workflow.exec.WorkflowData;

/**
 * @author Anirudh Sharma
 *
 */
@Component(service = EventHandler.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION
				+ "= This event handler listens to the events that occur in the life cycle of a Workflow",
		EventConstants.EVENT_TOPIC + "=" + WorkflowEvent.EVENT_TOPIC })
public class WorkflowEventsHandler implements EventHandler {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private WorkflowService workflowService;

	/**
	 * This method returns the workflow session
	 * 
	 * @return {@link WorkflowSession}
	 */
	private WorkflowSession getWorkflowSession() {
		try {
			// Creating and getting the service user map
			Map<String, Object> serviceUserMap = new HashMap<>();
			serviceUserMap.put(ResourceResolverFactory.SUBSERVICE, "eventingService");
			// Get the reference of ResourceResolver from the ResourceResolverFactory and
			// the Service User map
			ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(serviceUserMap);
			// Adapting ResourceResolver to the Session object
			Session session = resourceResolver.adaptTo(Session.class);
			WorkflowSession workflowSession = workflowService.getWorkflowSession(session);
			return workflowSession;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		LOG.info("Received event of topic: {}", event.getTopic());
		WorkflowSession workflowSession = null;
		try {
			// Get the workflow session
			workflowSession = getWorkflowSession();
			// Get the received topic
			String topic = event.getTopic();
			// Check if the received topic is of type Workflow event
			if (topic.equals(WorkflowEvent.EVENT_TOPIC)) {
				// Get the type of event
				Object eventType = event.getProperty(WorkflowEvent.EVENT_TYPE);
				// Check for each types of events in a workflow
				if (eventType.equals(WorkflowEvent.WORKFLOW_STARTED_EVENT)) {
					LOG.info("Workflow has started");
					String instanceId = (String) event.getProperty(WorkflowEvent.WORKFLOW_INSTANCE_ID);
					try {
						// Get the reference of the Workflow object
						Workflow workflow = workflowSession.getWorkflow(instanceId);
						// Get the workflow data
						WorkflowData workflowData = workflow.getWorkflowData();
						LOG.info("Data in workflow: {}", workflowData.getPayload().toString());
					} catch (WorkflowException e) {
						LOG.error(e.getMessage(), e);
					}
				} else if (eventType.equals(WorkflowEvent.WORKFLOW_COMPLETED_EVENT)) {
					LOG.info("Workflow has completed");
				} else if (eventType.equals(WorkflowEvent.WORKFLOW_RESUMED_EVENT)) {
					LOG.info("Workflow is resumed");
				} else if (eventType.equals(WorkflowEvent.WORKFLOW_ABORTED_EVENT)) {
					LOG.info("Workflow is aborted");
				} else if (eventType.equals(WorkflowEvent.WORKFLOW_SUSPENDED_EVENT)) {
					LOG.info("Workflow is suspended");
				} else if (eventType.equals(WorkflowEvent.WORKITEM_DELEGATION_EVENT)) {
					LOG.info("Workflow is delegated");
				} else {
					LOG.warn("Something is wrong with the workflow");
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (workflowSession != null) {
				workflowSession.getSession().logout();
			}
		}
	}

}
