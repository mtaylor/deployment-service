package org.cooling.tower.deployment.artifact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtifactManager 
{	
	private static String baseUrl = "repository";
	
	private ArtifactManager()
	{
	}
	
	private static ArtifactManager repository;
	
	public static ArtifactManager getInstance()
	{
		if(repository == null)
		{
			repository = new ArtifactManager();
		}
		
		return repository;
	}
	
	public File getArtifact(String userId, String deploymentId, String artifactId)
	{
		return new File(baseUrl + File.separator + userId + File.separator + deploymentId + File.separator + artifactId);
	}
	
	public String storeArtifact(String userId, String deploymentId, File file)
	{
		File deploymentDir = new File(baseUrl + File.separator + userId + File.separator + deploymentId);
		deploymentDir.mkdirs();
		
		String artifactId = UUID.randomUUID().toString();
		file.renameTo(new File(deploymentDir.getAbsolutePath() + File.separator + artifactId));
		return artifactId;
	}
	
	public List<String> storeArtifacts(String userId, String deploymentId, List<File> artifacts)
	{
		ArrayList<String> artifactIds = new ArrayList<String>();
		for(File file : artifacts)
		{
			artifactIds.add(ArtifactManager.getInstance().storeArtifact(userId, deploymentId, file));
		}
		return artifactIds;
	}
}
