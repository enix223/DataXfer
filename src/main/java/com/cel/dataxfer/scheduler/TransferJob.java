package com.cel.dataxfer.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cel.dataxfer.jython.Transfer;

@DisallowConcurrentExecution
public class TransferJob implements Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {		
		System.out.println("Transfer job is started...");
		Transfer.batch();
		System.out.println("Transfer job is ended...");
	}
}
