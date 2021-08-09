package fr.lauparr.apigenerator.services;

import fr.lauparr.apigenerator.pojo.dto.JvmInfoDTO;
import fr.lauparr.apigenerator.pojo.dto.ThreadInfoDTO;
import io.micrometer.core.instrument.Statistic;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationService {

	@Getter
	private final List<ThreadInfoDTO> threads = new ArrayList<>();
	@Getter
	private final List<JvmInfoDTO> jvmInfos = new ArrayList<>();

	@Autowired
	private MetricsEndpoint metricsEndpoint;

	@Value("${info.admin.max-thread}")
	private long adminMaxThread;
	@Value("${info.admin.max-jvm-info}")
	private long adminMaxJvmInfo;

	public void addThread() {
		if (threads.size() >= adminMaxThread) {
			this.threads.remove(0);
		}
		this.threads.add(new ThreadInfoDTO(
			getValueFromMetric("jvm.threads.live", Statistic.VALUE),
			getValueFromMetric("jvm.threads.daemon", Statistic.VALUE)
		));
	}

	public void addJvmInfo() {
		if (jvmInfos.size() >= adminMaxJvmInfo) {
			this.jvmInfos.remove(0);
		}
		this.jvmInfos.add(new JvmInfoDTO(
			getValueFromMetric("jvm.memory.used", Statistic.VALUE),
			getValueFromMetric("jvm.memory.committed", Statistic.VALUE),
			getValueFromMetric("jvm.memory.max", Statistic.VALUE)
		));
	}

	private double getValueFromMetric(String metricName, Statistic statistic) {
		MetricsEndpoint.Sample metric = metricsEndpoint.metric(metricName, new ArrayList<>()).getMeasurements()
			.stream().filter(measure -> measure.getStatistic().equals(statistic))
			.findFirst().orElse(null);
		return metric != null ? metric.getValue() : 0;
	}
}
