package org.cooling.tower.deployment.manager;

import java.io.File;
import java.util.List;

public interface DeploymentInterface 
{
	public String deploy(List<File> artifacts, File config);
	
	public String undeploy(String deploymentId);
}
