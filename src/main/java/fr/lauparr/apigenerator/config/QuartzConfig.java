package fr.lauparr.apigenerator.config;

import fr.lauparr.apigenerator.timers.AdminMetricTimer;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Value("${info.admin.refresh-time-seconds:15}")
	private int adminRefreshTimeSeconds;

	@PostConstruct
	public void postContruct() {
		addSchedule(AdminMetricTimer.class, SimpleScheduleBuilder.repeatSecondlyForever(adminRefreshTimeSeconds));
	}

	@Bean
	@ConditionalOnMissingBean
	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, Trigger trigger) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setJobFactory(jobFactory);
		factory.setWaitForJobsToCompleteOnShutdown(true);
		return factory;
	}

	@Bean
	@ConditionalOnMissingBean
	public JobFactory jobFactory() {
		return new SimpleJobFactory();
	}

	@Bean
	@ConditionalOnMissingBean
	public Trigger trigger() {
		return new CronTriggerImpl();
	}

	public Scheduler getScheduler() {
		return this.schedulerFactoryBean.getScheduler();
	}

	@SneakyThrows
	public void addSchedule(Class<AdminMetricTimer> jobClass, ScheduleBuilder<?> schedBuilder) {
		JobDetail job = JobBuilder.newJob(jobClass)
			.withIdentity(jobClass.getSimpleName())
			.withDescription(jobClass.getName())
			.storeDurably()
			.build();

		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity(jobClass.getSimpleName())
			.startNow()
			.withSchedule(schedBuilder)
			.build();

		this.getScheduler().scheduleJob(job, trigger);
	}

}
