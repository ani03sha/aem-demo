package org.redquark.demo.core.workflows;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

/**
 * @author Anirudh Sharma
 * 
 * Custom Workflow Step
 */
@Component(service = WorkflowProcess.class, property = {"process.label = Custom Workflow Demo"})
public class CustomWorkflow implements WorkflowProcess {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CustomWorkflow.class);
	
	/**
	 * Overridden method which executes when the workflow is invoked
	 */
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
		
		log.info("Executing the workflow");
		
		try {
			
		String textValue = metaDataMap.get("textValue", "empty");
		
		String dateValue = metaDataMap.get("dateValue", "empty");
		
		log.info("Text: {}", textValue);
		log.info("Date: {}", dateValue);
		
		} catch (Exception e) {
			
			log.error(e.getMessage(), e);
		}
	}

}
