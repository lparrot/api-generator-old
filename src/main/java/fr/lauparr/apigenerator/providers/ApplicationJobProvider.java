package fr.lauparr.apigenerator.providers;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationJobProvider extends SpringBeanJobFactory {

	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		// Calling methods of parent classes
		Object jobInstance = super.createJobInstance(bundle);
		// Injection
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}

}
