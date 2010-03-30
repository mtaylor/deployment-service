package org.cooling.tower.deployment.agents.messages;

import java.util.HashMap;
import java.util.Map;

public class AgentRequest 
{
	private Map<String, String> properties;
	
	public AgentRequest()
	{
		properties = new HashMap<String, String>();
	}
	
	public AgentRequest(Map<String, String> properties)
	{
		this.properties = properties;
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
}
