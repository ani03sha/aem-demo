package org.redquark.demo.core.workflows;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
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
 */
@Component(service = WorkflowProcess.class, property = { "process.label=" + "Process Step Example" })
public class ProcessStepExample implements WorkflowProcess {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		// Getting payload from Workflow - workItem -> workflowData -> payload
		String payloadType = workItem.getWorkflowData().getPayloadType();

		// Check type of payload; there are two - JCR_PATH and JCR_UUID
		if (StringUtils.equals(payloadType, "JCR_PATH")) {
			log.info("Payload type: {}", payloadType);
			// Get the JCR path from the payload
			String path = workItem.getWorkflowData().getPayload().toString();
			log.info("Payload path: {}", path);
		}

		// Get workflow process arguments
		String[] processArguments = metaDataMap.get("PROCESS_ARGS", "Default").split(",");
		log.info("Process args: {}", Arrays.toString(processArguments));
	}

}
