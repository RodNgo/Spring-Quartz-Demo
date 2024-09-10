package com.rodngo.Spring_Quartz_Demo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UpperCaseJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String input = (String) context.getJobDetail().getJobDataMap().get("inputString");
        String uppercased = input.toUpperCase();
        System.out.println("Uppercased String: " + uppercased);
    }
}
