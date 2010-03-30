package org.cooling.tower.deployment.agents;

import org.cooling.tower.deployment.agents.messages.AgentRequest;
import org.cooling.tower.deployment.agents.messages.AgentResponse;

public class MockAgent implements Agent
{
	@Override
	public AgentResponse deploy(AgentRequest request) 
	{
		System.out.println("------------- Mock Agent: Deploy --------------");
		for(String key : request.getProperties().keySet())
		{
			System.out.println(key + ":" + request.getProperty(key));
		}
		System.out.println("-----------------------------------------------");
		return new AgentResponse(DeploymentState.SUCCESS);
	}

	@Override
	public AgentResponse undeploy(AgentRequest request) 
	{
		System.out.println("------------- Mock Agent: Undeploy --------------");
		
		for(String key : request.getProperties().keySet())	
		{
			System.out.println(key + ":" + request.getProperty(key));
		}
		System.out.println("-------------------------------------------------");
		return new AgentResponse(DeploymentState.SUCCESS);
	}
	
}
