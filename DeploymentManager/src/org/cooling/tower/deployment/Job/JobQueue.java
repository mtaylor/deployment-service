package org.cooling.tower.deployment.Job;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.cooling.tower.deployment.util.Utils;

public class JobQueue
{
	private ConcurrentLinkedQueue<CoolingTowerJob> queue;
	
	private static JobQueue jobQueue;

	public static JobQueue getInstance()
	{
		if(jobQueue == null)
		{
			jobQueue = new JobQueue();
		}
		return jobQueue;
	}
	
	private JobQueue()
	{
		queue = new ConcurrentLinkedQueue<CoolingTowerJob>();
	}
	
	public void addJob(CoolingTowerJob job)
	{
		queue.offer(job);
	}
	
	public CoolingTowerJob getJob()
	{
		return queue.poll();
	}
}
