package org.cooling.tower.deployment.agents;

import java.io.File;
import java.util.HashMap;

import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;

public class PostgresDDLAgent implements Agent
{

	@Override
	public AgentResponse deploy(AgentRequest request) 
	{
		// TODO Auto-generated method stub
		return new AgentResponse(DeploymentState.SUCCESS);
	}

	@Override
	public AgentResponse undeploy(AgentRequest request) 
	{
		// TODO Auto-generated method stub
		return new AgentResponse(DeploymentState.SUCCESS);
	}


}
