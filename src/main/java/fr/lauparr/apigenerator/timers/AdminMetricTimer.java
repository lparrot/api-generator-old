package fr.lauparr.apigenerator.timers;

import fr.lauparr.apigenerator.services.ApplicationService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class AdminMetricTimer implements Job {

	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		applicationService.addCpuUsage();
		applicationService.addThread();
		applicationService.addJvmInfo();
		applicationService.addJvmNonHeapInfo();
		simpMessagingTemplate.convertAndSend("/topic/metrics", true);
	}
}
