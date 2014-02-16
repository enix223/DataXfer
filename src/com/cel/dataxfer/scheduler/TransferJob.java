package com.cel.dataxfer.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cel.dataxfer.jython.Transfer;

public class TransferJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {		
		Transfer.batch();
	}
}
