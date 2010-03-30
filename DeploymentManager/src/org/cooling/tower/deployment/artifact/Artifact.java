package org.cooling.tower.deployment.artifact;

import java.util.UUID;

public class Artifact 
{
	private String ownerId;
	
	private String rootDeploymentId;
	
	private String id;
	
	private String name;
	
	private String version;
		
	private String artifactType;
	
	private boolean canCoExist;
	
	public Artifact(String ownerId, String rootDeploymentId, String name, String artifactType)
	{
		this.id = UUID.randomUUID().toString();
		
		this.ownerId = ownerId;
		
		this.rootDeploymentId = rootDeploymentId;
		
		this.name = name;
		
		this.artifactType = artifactType;
	}

	public String getOwnerId() 
	{
		return ownerId;
	}

	public void setOwnerId(String ownerId) 
	{
		this.ownerId = ownerId;
	}

	public String getRootDeploymentId() 
	{
		return rootDeploymentId;
	}

	public void setRootDeploymentId(String rootDeploymentId) 
	{
		this.rootDeploymentId = rootDeploymentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getVersion() 
	{
		return version;
	}

	public void setVersion(String version) 
	{
		this.version = version;
	}

	public String getArtifactType() 
	{
		return artifactType;
	}

	public void setArtifactType(String artifactType) 
	{
		this.artifactType = artifactType;
	}

	public boolean isCanCoExist() 
	{
		return canCoExist;
	}

	public void setCanCoExist(boolean canCoExist) 
	{
		this.canCoExist = canCoExist;
	}
	
	
}
