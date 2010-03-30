package org.cooling.tower.deployment.agents.registry;

import java.util.HashMap;

import org.cooling.tower.deployment.agents.Agent;

public class AgentRegistry
{	
	private HashMap<String, Class<? extends Agent>> agents;
		
	private static AgentRegistry agentRegistry = null;
	
	private AgentRegistry()
	{
		agents = new HashMap<String, Class<? extends Agent>>();
	}
	
	public static AgentRegistry getInstance()
	{
		if(agentRegistry == null)
		{
			agentRegistry = new AgentRegistry();
		}
		return agentRegistry;
	}
	
	public void registerAgent(String agentType, Class<? extends Agent> agentClass)
	{
		agents.put(agentType.toUpperCase(), agentClass);
	}
	
	public Agent getAgentInstance(String agentType) throws Exception
	{
		System.out.println("Looking for: " + agentType);
		
		for(String key : agents.keySet())
		{
			System.out.println(key);
		}
		return agents.get(agentType.toUpperCase()).newInstance();
	}
}
