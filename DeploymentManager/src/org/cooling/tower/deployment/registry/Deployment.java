package org.cooling.tower.deployment.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cooling.tower.deployment.Job.CoolingTowerJob;
import org.cooling.tower.deployment.agents.Agent.DeploymentState;


public class Deployment
{
	private String userId;
	
	private String deploymentId;
	
	private String agentType;
	
	private DeploymentState state;
	
	private List<String> children;
		
	private Map<String, String> properties;
	
	public Deployment(String userId, String deploymentId, String agentType, List<String> children)
	{
		this.userId = userId;
		
		this.state = DeploymentState.PENDING;
		
		this.deploymentId = deploymentId;
		
		this.children = children;
		
		this.agentType = agentType;
		
		properties = new HashMap<String, String>();
	}
	
	public void setProperties(Map<String, String> properties)
	{
		this.properties = properties;
	}
	
	public Map<String, String> getProperties()
	{
		return properties;
	}

	public String getDeploymentId() 
	{
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) 
	{
		this.deploymentId = deploymentId;
	}

	public DeploymentState getState() 
	{
		return state;
	}

	public void setState(DeploymentState state) 
	{
		this.state = state;
	}

	public List<String> getChildren() 
	{
		return children;
	}

	public void setChildren(List<String> children) 
	{
		this.children = children;
	}

	public String getUserId() 
	{
		return userId;
	}

	public void setUserId(String userId) 
	{
		this.userId = userId;
	}

	public String getAgentType() 
	{
		return agentType;
	}

	public void setAgentType(String agentType) 
	{
		this.agentType = agentType;
	}	
	
	public void addChild(String deploymentId)
	{
		children.add(deploymentId);
	}
	
	/*
	 * Creates a 2D array containing this deployments child deployments, where each entry in the list is a list of deployments at each level
	 * in the deployment dependency.  This allows each list to be sorted in order of dependency.  For example, A deployment with 2 children, where each
	 * child has 2 children.  The 1st entry in the 2D list will contain a list with one value (the parent), the 2nd will contain a list with 2 deployments (the children), the
	 * third list will have 4 deployments, the grandchildren.  This allows the deployment service to figure out which deployments can be executed in parrallel 
	 * and which sequentially. 
	 */
	public List<ArrayList<Deployment>> createParrellelExecutionList()
	{
		return createParrallelChildDependencyList(this, new ArrayList<ArrayList<Deployment>>(), 0);
	}
	
	private List<ArrayList<Deployment>> createParrallelChildDependencyList(Deployment currentDeployment, List<ArrayList<Deployment>> deploymentDependencies, int level)
	{
		if(deploymentDependencies.size() <= level)
		{
			deploymentDependencies.add(level, new ArrayList<Deployment>());
		}
		
		deploymentDependencies.get(level).add(currentDeployment);
		
		level++;
		for(String childId : currentDeployment.getChildren())
		{
			Deployment child = DeploymentRegistry.getInstance().getDeployment(childId);
			createParrallelChildDependencyList(child, deploymentDependencies, level);
		}	
		return deploymentDependencies;
	}
}
