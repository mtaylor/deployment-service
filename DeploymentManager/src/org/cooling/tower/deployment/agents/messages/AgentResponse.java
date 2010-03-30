package org.cooling.tower.deployment.agents.messages;

import java.util.HashMap;
import java.util.Map;

import org.cooling.tower.deployment.agents.Agent;
import org.cooling.tower.deployment.agents.Agent.DeploymentState;

public class AgentResponse
{
	private DeploymentState state;
	
	private Map<String, String> properties;
	
	private Throwable exception;
	
	public AgentResponse(DeploymentState state)
	{
		properties = new HashMap<String, String>();
		this.state = state;
	}
	
	public AgentResponse(DeploymentState state, Map<String, String> properties)
	{
		this.properties = properties;
		this.state = state;
	}
	
	public void addProperty(String key, String value)
	{
		properties.put(key, value);
	}
	
	public void removeProperty(String key)
	{
		properties.remove(key);
	}
	
	public String getProperty(String key)
	{
		return properties.get(key);
	}
	
	public Map<String, String> getProperties()
	{
		return properties;
	}

	public DeploymentState getState() 
	{
		return state;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	
}
