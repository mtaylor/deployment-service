package org.cooling.tower.deployment.agents;

import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;

public interface Agent 
{
	public enum DeploymentState { SUCCESS, FAILED, PENDING };
	
	public AgentResponse deploy(AgentRequest request);
	
	public AgentResponse undeploy(AgentRequest request);
}
